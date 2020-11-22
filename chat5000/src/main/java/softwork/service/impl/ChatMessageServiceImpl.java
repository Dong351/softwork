package softwork.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwork.mapper.ChatMessageMapper;
import softwork.pojo.entities.ChatMessage;
import softwork.pojo.entities.User;
import softwork.service.ChatMessageService;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {
    @Autowired
    ChatMessageMapper chatMessageMapper;
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
        return singleMessages;
    }
}
