package softwork.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import softwork.exception.CommonException;
import softwork.mapper.ResetMailCodeMapper;
import softwork.mapper.UserMapper;
import softwork.pojo.entities.ResetMailCode;
import softwork.pojo.entities.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Service
public class MailServiceImpl {


    private final Random random;

    private final UserMapper userMapper;

    private final ResetMailCodeMapper resetMailCodeMapper;


    private final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    private final String from = "190368071@qq.com";

    @Autowired
    private JavaMailSender mailSender;



    public MailServiceImpl(Random random, UserMapper userMapper, ResetMailCodeMapper resetMailCodeMapper) {
        this.random = random;
        this.userMapper = userMapper;
        this.resetMailCodeMapper = resetMailCodeMapper;
    }

    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);//收信人
        message.setSubject(subject);//主题
        message.setText(content);//内容
        message.setFrom(from);//发信人
        mailSender.send(message);
    }

    public void sendResetEmail(String email){


        System.out.println(2);
        List<User> userList = userMapper.findUserByEmail(email);
        if (userList.isEmpty()){
            throw new CommonException("该邮箱未注册");
        }
        else {
            //遍历数据库删除时间大于5min的条目
            System.out.println(3);
            resetMailCodeMapper.deleteOutOfTime();

        //判断数据库中是否存在该邮箱的申请记录 如果有且在5min内则返回（） 否则发送邮件
        ResetMailCode resetMailCode = resetMailCodeMapper.findByEmail(email);
        if(resetMailCode!=null){
            throw new CommonException("已发送至您邮箱，请10分钟后重试");
        }
            System.out.println(4);
        String code = generateCode(6)+"";
        //向数据库中插入新的记录
        ResetMailCode mailCodeExample = new ResetMailCode();
        mailCodeExample.setCode(code);
        mailCodeExample.setDate(new Date());
        mailCodeExample.setEmail(email);
        User user = userList.get(0);
        mailCodeExample.setUid(user.getId());

            System.out.println(5);
        resetMailCodeMapper.insert(mailCodeExample);
            System.out.println(6);
            Date t = new Date();
//            t.setTime(t.getTime() + 8*60*60*1000);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(7);
        sendSimpleMail(email, "竞枢重置密码", "亲爱的用户:" +
                "\n\n"+
                "欢迎使用竞枢邮箱重置密码功能。请复制以下验证码填入网页"+"\n\n"
                +code+   "\n\n" +
                "请在5分钟内完成密码修改"+"\n\n"
                +"（本邮件由系统自动发出，请勿回复。）"+"\n"
                +df.format(t)
        );
            System.out.println(8);
    }
}


    private String generateCode(Integer codeSize) {

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < codeSize; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
