package softwork.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwork.mapper.UserMapper;
import softwork.pojo.entities.User;
import softwork.service.UserService;

import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    Random random;
    @Autowired
    UserMapper userMapper;


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

    public User SelectByKey(Integer id){
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

}
