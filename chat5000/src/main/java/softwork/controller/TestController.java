package softwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import softwork.mapper.UserMapper;
import softwork.pojo.entities.User;

@RestController
@RequestMapping("/ttt")
public class TestController {
    @Autowired
    UserMapper userMapper;

    @GetMapping("/test")
    public String SqlTest(){
        String sendId = "3";
        User user = userMapper.selectByPrimaryKey(Integer.valueOf(sendId));
        System.out.println(user);
        return "success";
    }
}
