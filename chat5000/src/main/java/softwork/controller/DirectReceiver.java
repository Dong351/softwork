package softwork.controller;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import softwork.mapper.ChatMessageMapper;
import softwork.pojo.entities.ChatMessage;

import java.util.Date;
import java.util.Map;

@Component
@RabbitListener(queues = "ChatMessageQueue")//监听的队列名称 TestDirectQueue
public class DirectReceiver {
    @Autowired
    ChatMessageMapper chatMessageMapper;

    @RabbitHandler
    public void process(Map<String,Object> message) {
        System.out.println("DirectReceiver消费者收到消息  : " + message.toString());
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setReceive_id((Integer) message.get("receive_id"));
        chatMessage.setContent((String) message.get("message"));
        chatMessage.setRoom_id(message.get("tid").toString());
        chatMessage.setCreate_time(new Date());
        chatMessage.setSend_id((Integer) message.get("send_id"));
        chatMessage.setReaded(0);
        chatMessage.setType(1);
        chatMessageMapper.insert(chatMessage);
    }

}