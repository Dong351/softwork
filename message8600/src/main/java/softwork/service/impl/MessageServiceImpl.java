package softwork.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwork.mapper.ChatMessageMapper;
import softwork.mapper.MessageMapper;
import softwork.mapper.TeamMapper;
import softwork.mapper.UserMapper;
import softwork.pojo.entities.ChatMessage;
import softwork.pojo.entities.Message;
import softwork.pojo.entities.User;
import softwork.pojo.vo.MessageListVO;
import softwork.pojo.vo.MessageVO;
import softwork.service.MessageService;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    TeamMapper teamMapper;
    @Autowired
    ChatMessageMapper chatMessageMapper;

    @Override
    public Object GetList(User user) {
        Message findByReceiveId = new Message();
        findByReceiveId.setReceive_uid(user.getId());
        List<MessageListVO> messageListVOS = new ArrayList<>();
        List<Message> messages = messageMapper.select(findByReceiveId);
        for(Message message:messages){
            MessageListVO messageListVO = new MessageListVO();
            BeanUtils.copyProperties(message,messageListVO);

            if(message.getType() == 1){
                messageListVO.setTname(teamMapper.selectByPrimaryKey(message.getTid()).getName());
            }
            messageListVO.setSend_userName(userMapper.selectByPrimaryKey(message.getSend_uid()).getUsername());
            messageListVOS.add(messageListVO);
        }
        return messageListVOS;
    }

    @Override
    public Object GetMessage(Integer read, Integer mid, User user) {
        Message message = messageMapper.selectByPrimaryKey(mid);
        MessageVO messageVO = new MessageVO();
        BeanUtils.copyProperties(message,messageVO);
        messageVO.setSend_userName(userMapper.selectByPrimaryKey(message.getSend_uid()).getUsername());
        messageVO.setTeam_name(teamMapper.selectByPrimaryKey(message.getTid()).getName());

        //根据read参数来判断是否已读
        if(read == 2){
            System.out.println("预览");
        }
        else if(read == 1){
            message.setReaded(1);
        }

        //更新message的type
        messageMapper.updateByPrimaryKeySelective(message);
        return messageVO;
    }

    @Override
    public Object GetChatList(User user) {
        List<ChatMessage> groupByRecId = chatMessageMapper.findListGroupByRecId(user.getId());
        for (ChatMessage chatMessage:groupByRecId){

        }
        return groupByRecId;
    }
}
