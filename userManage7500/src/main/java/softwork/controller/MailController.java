package softwork.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;
import softwork.mapper.ResetMailCodeMapper;
import softwork.mapper.UserMapper;
import softwork.service.UserService;
import softwork.service.impl.MailServiceImpl;
import softwork.utils.JsonResult;

import java.util.Random;

@RestController
@RequestMapping(value = "/mail")
public class MailController {


    private final MailServiceImpl mailService;


    public MailController(UserService userService, Random random, MailServiceImpl mailService, UserMapper userMapper, ResetMailCodeMapper resetMailCodeMapper) {
        this.mailService = mailService;
    }

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送忘记密码邮件请求，每日申请次数不超过5次，每次申请间隔不低于1分钟
     *
     * @param email
     */
    @GetMapping("/sendResetEmail/{email}")
    public WebAsyncTask<Object> sendResetEmail(@PathVariable String email) {
        System.out.println(1);
        return new WebAsyncTask<Object>(()->{
            mailService.sendResetEmail(email);
            return JsonResult.ok();
        });
    }
}

