package softwork.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwork.mapper.ChatMessageMapper;
import softwork.pojo.entities.ChatMessage;
import softwork.pojo.entities.User;
import softwork.pojo.vo.ChatMessageVO;
import softwork.service.ChatMessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    ChatMessageMapper chatMessageMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void save(ChatMessage message) {
        chatMessageMapper.insert(message);
    }

    public Object Single(Integer uid, User user) {
        System.out.println(user);
        Integer uid1 = user.getId();
        System.out.println(uid1+" "+uid);
        String tid;
        if(uid1 < uid){
             tid = uid1.toString() + "-" + uid.toString();
        }
        else tid = uid.toString() + "-" + uid1.toString();
        ChatMessage findSingleMessage = new ChatMessage();
        findSingleMessage.setRoom_id(tid);
        List<ChatMessage> singleMessages = chatMessageMapper.select(findSingleMessage);
//        List<ChatMessage> singleMessages = chatMessageMapper.findListOrderByTime(tid);
        List<ChatMessageVO> messageVOS = new ArrayList<>();
        for(ChatMessage chatMessage:singleMessages){
            ChatMessageVO chatMessageVO = new ChatMessageVO();
            BeanUtils.copyProperties(chatMessage,chatMessageVO);
            chatMessageVO.setId(chatMessage.getMid());
            chatMessageVO.setTime(chatMessage.getCreate_time());

            messageVOS.add(chatMessageVO);
        }
        return messageVOS;
    }

    public void SendQueue(Map<String,String> map){
        rabbitTemplate.convertAndSend("ChatMessageQueue", "ChatMessageRoute", map);
    }

}
