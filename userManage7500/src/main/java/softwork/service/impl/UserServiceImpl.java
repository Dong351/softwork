package softwork.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import softwork.exception.CommonException;
import softwork.mapper.ResetMailCodeMapper;
import softwork.mapper.ShowCertificateMapper;
import softwork.mapper.UserMapper;
import softwork.pojo.dto.*;
import softwork.pojo.entities.ResetMailCode;
import softwork.pojo.entities.ShowCertificate;
import softwork.pojo.entities.User;
import softwork.pojo.vo.UserInfoVO;
import softwork.service.UserService;
import softwork.utils.HttpUtils;
import softwork.utils.UploadFileStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    Random random;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ResetMailCodeMapper resetMailCodeMapper;
    @Autowired
    ShowCertificateMapper showCertificateMapper;

    public User queryUserByToken(String token) {
        User exampleUser = new User();
        exampleUser.setAccesstoken(token);
        return userMapper.selectOne(exampleUser);
    }

    public boolean userExistsByPhone(String phone) {
        User exampleUser = new User();
        exampleUser.setPhone(phone);

        if (userMapper.selectOne(exampleUser) != null) {
            return true;
        }
        else return false;
    }

    private boolean userExistsByEMail(String Email){
        User exampleUser = new User();
        exampleUser.setEmail(Email);
        if (userMapper.selectOne(exampleUser) != null) {
            return true;
        }
        else return false;
    }

    private String generateToken(Integer uid, long time) {
        return DigestUtils.md5Hex(uid + "-" + time);
    }

    private String encryptPassword(String source) {
        return DigestUtils.sha256Hex(source);
    }

    public void updateToken(User user) {

        User exampleUser = new User();

        String accessToken;
        // 必须保证token的唯一性
        do {
            accessToken = generateToken(user.getId(), System.currentTimeMillis());
            exampleUser.setAccesstoken(accessToken);
        } while (userMapper.selectOne(exampleUser) != null);

        exampleUser.setId(user.getId());
        userMapper.updateByPrimaryKeySelective(exampleUser);
        user.setAccesstoken(accessToken);
    }


    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterDTO dto) {
        // 手机号必须未注册
        if(userExistsByPhone(dto.getPhone())){
            throw new CommonException("手机号已注册");
        }

        //邮箱必须未注册
        if(userExistsByEMail(dto.getEmail())){
            throw new CommonException("该邮箱已注册");
        }

        User user = new User();
        user.setType(1);
        BeanUtils.copyProperties(dto,user);

        
        //密码加密
        user.setPassword(encryptPassword(dto.getPassword()));

        // 生成token，如果不进行这一步会出现多个用户token为空的情况，违反唯一性约束, 这样子还是会有极小的概率出现token不唯一的情况
        user.setAccesstoken(generateToken(random.nextInt(65535), System.currentTimeMillis()));
        //插入user表
        userMapper.insertSelective(user);
    }


    public Object getUserInfoVO(User user) {
        User find = userMapper.selectOne(user);
        UserInfoVO userInfoVO = new UserInfoVO();

        BeanUtils.copyProperties(find, userInfoVO);

        return userInfoVO;
    }

    public Object login(UserLoginDTO dto) {
        // 根据手机号在数据库中查找用户
        User exampleUser = new User();
        exampleUser.setPhone(dto.getPhone());
        User user = userMapper.selectOne(exampleUser);

        //没有查到用户，则说明手机号为注册
        if(user == null){
            throw new CommonException("手机号未注册");
        }

        //比对手机登录密码
        if(!Objects.equals(user.getPassword(),encryptPassword(dto.getPassword()))){
            throw new CommonException("账号密码不正确");
        }

        //登陆成功则返回token
        updateToken(user);

        User returnUser = new User();
        returnUser.setAccesstoken(user.getAccesstoken());
        returnUser.setId(user.getId());

        //返回用户信息
        return returnUser;
    }


    public Object updateUserInfo(UserUpdateDTO dto, User user) {
        System.out.println(dto);
        //判断电话和邮箱是否存在
        User findPhoneExist = new User();
        User findEmailExist = new User();
        findPhoneExist.setPhone(dto.getPhone());
        findEmailExist.setEmail(dto.getEmail());
        User phoneExist = new User();
        User emailExist = new User();

        if(dto.getPhone() != null){
            phoneExist = userMapper.selectOne(findPhoneExist);
            if(phoneExist != null){
                if(phoneExist.getId() != user.getId()){
                    throw new CommonException("该电话已注册！");
                }
            }
        }

        if (dto.getEmail() != null){
            emailExist = userMapper.selectOne(findEmailExist);
            if(emailExist != null){
                if(emailExist.getId() != user.getId()){
                    throw new CommonException("该邮箱已注册！");
                }
            }
        }



        User updateUser = new User();
        BeanUtils.copyProperties(dto,updateUser);


        updateUser.setId(user.getId());
        userMapper.updateByPrimaryKeySelective(updateUser);
        return getUserInfoVO(updateUser);
    }

    public void modifyPassword(UserModifyPasswordDTO dto, User user) {
        if(dto.getPrePassword() == null){
            throw new CommonException("原密码不能为空");
        }

        if(!Objects.equals(user.getPassword(),encryptPassword(dto.getPrePassword()))){
            throw new CommonException("原密码不正确");
        }

        //原密码正确，修改数据库
        User exampleUser = new User();
        exampleUser.setId(user.getId());
        exampleUser.setPassword(encryptPassword(dto.getPassword()));
        userMapper.updateByPrimaryKeySelective(exampleUser);
    }


    public void resetPassword(UserResetPasswordDTO dto) {
        ResetMailCode resetMailCode = resetMailCodeMapper.findByEmail(dto.getEmail());
        if(resetMailCode == null){
            List<User> userList = userMapper.findUserByEmail(dto.getEmail());
            if (userList.isEmpty()){
                throw new CommonException("该邮箱未注册");
            }
            else{
                throw new CommonException("验证码已过期");
            }
        }
        else if (Objects.equals(dto.getCode(),resetMailCode.getCode())){
            User user = new User();
            user.setId(resetMailCode.getUid());
            user.setPassword(encryptPassword(dto.getPassword()));
            userMapper.updateByPrimaryKeySelective(user);
            resetMailCodeMapper.deleteByPrimaryKey(resetMailCode.getId());
        }
        else{
            throw new CommonException("验证码错误");
        }
    }

    @Override
    public UserInfoVO getInfo(Integer uid, User user) {
        User findUser = userMapper.selectByPrimaryKey(uid);
        UserInfoVO info = new UserInfoVO();
        if(uid == 0)
            BeanUtils.copyProperties(user,info);
        else{
            BeanUtils.copyProperties(findUser,info);

            //将电话、邮箱和姓名隐藏
            String str = "******";
            StringBuilder sb = new StringBuilder(info.getPhone());
            sb.replace(3,9,str);

            info.setPhone(sb.toString());
        }
//        info.setAccesstoken(null);
        return info;
    }

    @Override
    public void UploadAvatar(MultipartFile avatar, User user) throws IOException {
        FileInputStream inputStream = (FileInputStream) avatar.getInputStream();
        UploadFileStatus fileStatus = new UploadFileStatus();
        // 上传到服务器后的文件名
        fileStatus.setFileName(user.getId().toString());
        // 上传到服务器的哪个位置
        fileStatus.setFilePath("/root/usr/local/webfile/softwork/");
        // 文件的大小
        fileStatus.setFileSize(inputStream.available());
        // 文件的类型
        fileStatus.setFileType("png");
        fileStatus.setInputStream(inputStream);
        String result = HttpUtils.postFile("http://49.234.239.138:3000/upload/", fileStatus);
        System.out.println(result);

        //将url更新到user表
        user.setAvatar_url("https://soft.leavessoft.cn/avatar/"+user.getId()+".png");
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public void UploadCertifcate(MultipartFile certificate, String name, User user) throws IOException{
        FileInputStream inputStream = (FileInputStream) certificate.getInputStream();
        UploadFileStatus fileStatus = new UploadFileStatus();
        // 上传到服务器后的文件名
        Random random = new Random();
        int i = random.nextInt(10000);
        String s = user.getId().toString() + i;
        fileStatus.setFileName(s);
        System.out.println(s);
        // 上传到服务器的哪个位置
        fileStatus.setFilePath("/root/usr/local/webfile/softwork/");
        // 文件的大小
        fileStatus.setFileSize(inputStream.available());
        // 文件的类型
        fileStatus.setFileType("png");
        fileStatus.setInputStream(inputStream);
        String result = HttpUtils.postFile("http://49.234.239.138:3000/upload/", fileStatus);
        System.out.println(result);

        ShowCertificate showCertificate = new ShowCertificate();
        showCertificate.setUid(user.getId());
        showCertificate.setName(name);
        showCertificate.setUrl("https://soft.leavessoft.cn/avatar/"+s+".png");
        showCertificateMapper.insert(showCertificate);
    }

    @Override
    public Object getCertificates(Integer uid) {
        ShowCertificate findByUid = new ShowCertificate();
        findByUid.setUid(uid);
        List<ShowCertificate> showCertificates = showCertificateMapper.select(findByUid);
        return showCertificates;
    }

    @Override
    public Object DeleteCertificates(Integer id, User user) {
        ShowCertificate find = new ShowCertificate();
        ShowCertificate showCertificate = showCertificateMapper.selectByPrimaryKey(id);
        if(showCertificate.getUid() != user.getId()){
            throw new CommonException("权限不足");
        }

        String[] split = showCertificate.getUrl().split("/");


        String resultInfo = null;
        String filePath = "/root/usr/local/webfile/softwork/"+split[split.length-1];
        //System.out.println(img_path);//输出测试一下文件路径是否正确
        File file = new File(filePath);
        if (file.exists()) {//文件是否存在
            if (file.delete()) {//存在就删了
                showCertificateMapper.delete(showCertificate);
                resultInfo =  "删除成功";
            } else {
                resultInfo =  "删除失败";
            }
        } else {
            resultInfo = "文件不存在！";
        }
        System.out.println(resultInfo);
        return null;
    }
}
