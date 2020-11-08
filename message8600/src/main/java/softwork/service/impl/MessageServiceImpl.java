package softwork.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwork.mapper.MessageMapper;
import softwork.mapper.TeamMapper;
import softwork.mapper.UserMapper;
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

    @Override
    public Object GetList(User user) {
        Message findByReceiveId = new Message();
        findByReceiveId.setReceive_uid(user.getId());
        List<MessageListVO> messageListVOS = new ArrayList<>();
        List<Message> messages = messageMapper.select(findByReceiveId);
        for(Message message:messages){
            MessageListVO messageListVO = new MessageListVO();
            BeanUtils.copyProperties(message,messageListVO);
            messageListVO.setSend_userName(userMapper.selectByPrimaryKey(message.getSend_uid()).getUsername());
            messageListVOS.add(messageListVO);
        }
        return messageListVOS;
    }

    @Override
    public Object GetMessage(Integer mid, User user) {
        Message message = messageMapper.selectByPrimaryKey(mid);
        MessageVO messageVO = new MessageVO();
        BeanUtils.copyProperties(message,messageVO);
        messageVO.setSend_userName(userMapper.selectByPrimaryKey(message.getSend_uid()).getUsername());
        messageVO.setTeam_name(teamMapper.selectByPrimaryKey(message.getTid()).getName());

        //更新message的type
        message.setType(1);
        messageMapper.updateByPrimaryKeySelective(message);
        return messageVO;
    }
}
