package softwork.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwork.exception.CommonException;
import softwork.mapper.ResetMailCodeMapper;
import softwork.mapper.UserMapper;
import softwork.pojo.dto.*;
import softwork.pojo.entities.ResetMailCode;
import softwork.pojo.entities.User;
import softwork.pojo.vo.UserInfoVO;
import softwork.service.UserService;

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

        //返回用户信息
        return getUserInfoVO(user);
    }


    public Object updateUserInfo(UserUpdateDTO dto, User user) {
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

}
