package softwork.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import softwork.pojo.entities.User;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username=#{username}")
    User findByName(String username);

    @Select("select * from user where username=#{username}")
    public User selectByName(String name);

    @Select("SELECT * FROM user where email = #{email}")
    List<User> findUserByEmail(String email);
}
