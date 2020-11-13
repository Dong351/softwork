package softwork.service;

import org.springframework.web.multipart.MultipartFile;
import softwork.pojo.dto.*;
import softwork.pojo.entities.User;
import softwork.pojo.vo.UserInfoVO;

import java.io.IOException;

public interface UserService {

    /**
     * 通过token查找用户，用于接口验证
     * @param token
     * @return
     */
    public User queryUserByToken(String token);

    /**
     * 根据phone来查询是否存在该用户
     * @param phone
     * @return
     */
    public boolean userExistsByPhone(String phone);

    /**
     * 更新user的token
     * @param user
     */
    public void updateToken(User user);

    /**
     * 用户注册
     * @param dto
     */
    public void register(UserRegisterDTO dto);

    /**
     * 用户登录
     * @param dto
     * @return
     */
    public Object login(UserLoginDTO dto);

    /**
     * 根据dto修改指定user的信息
     * @param dto
     * @param user
     * @return
     */
    public Object updateUserInfo(UserUpdateDTO dto, User user);

    /**
     * 修改密码
     * @param dto
     * @param user
     */
    public void modifyPassword(UserModifyPasswordDTO dto, User user);

    /**
     * 重置密码
     * @param dto
     */
    public void resetPassword(UserResetPasswordDTO dto);

    UserInfoVO getInfo(User user);

    /**
     * 上传用户头像
     * @param avatar
     * @param user
     */
    void UploadAvatar(MultipartFile avatar, User user) throws IOException;
}
