package softwork.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwork.mapper.ChatMessageMapper;
import softwork.mapper.TeamMapper;
import softwork.mapper.TeamPartnerMapper;
import softwork.mapper.UserMapper;
import softwork.pojo.entities.ChatMessage;
import softwork.pojo.entities.Team;
import softwork.pojo.entities.TeamPartner;
import softwork.pojo.entities.User;
import softwork.pojo.vo.ChatMessagePreVO;
import softwork.pojo.vo.ChatMessageVO;
import softwork.pojo.vo.ChatTeamMessageVO;
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
    @Autowired
    TeamMapper teamMapper;
    @Autowired
    TeamPartnerMapper teamPartnerMapper;
    @Autowired
    UserMapper userMapper;

    public void save(ChatMessage message) {
        chatMessageMapper.insert(message);
    }

    public Object Single(Integer uid, User user) {
        System.out.println(user);
        Integer uid1 = user.getId();
        System.out.println(uid1+" "+uid);

        List<ChatMessage> singleMessages = chatMessageMapper.findSingleMessages(uid,uid1);
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

    @Override
    public Object GetChatMessagePreview(User user) {
        ChatMessage findByUid = new ChatMessage();
        findByUid.setSend_id(user.getId());

        List<ChatMessage> chatMessages1 = chatMessageMapper.findListGroupByRecId(user.getId());
        List<ChatMessage> chatMessages2 = chatMessageMapper.findListGroupBySendId(user.getId());
        List<ChatMessage> chatMessages = new ArrayList<>();
        int mark = -1;
        if(chatMessages1.size() > chatMessages2.size()){
            chatMessages = chatMessages1;
            mark = 1;
        }
        else {
            chatMessages = chatMessages2;
            mark = 2;
        }

        List<ChatMessagePreVO> chatMessagePreVOS = new ArrayList<>();
        for(ChatMessage chatMessage:chatMessages){
            if(mark == 1 && chatMessage.getReceive_id() == user.getId())    continue;
            if(mark == 2 && chatMessage.getSend_id() == user.getId())       continue;

            ChatMessagePreVO chatMessagePreVO = new ChatMessagePreVO();
            BeanUtils.copyProperties(chatMessage,chatMessagePreVO);
            chatMessagePreVO.setLast_time(chatMessage.getCreate_time());
            Team team = teamMapper.selectByPrimaryKey(chatMessage.getRoom_id());
            chatMessagePreVO.setTname(team.getName());
            Integer avatar_id;
            if(user.getId() == chatMessage.getSend_id())    avatar_id = chatMessage.getReceive_id();
            else avatar_id = chatMessage.getSend_id();
            User user1 = userMapper.selectByPrimaryKey(avatar_id);
            chatMessagePreVO.setAvatar_url(user1.getAvatar_url());
//            if(user1.getAvatar_url() == null)
            chatMessagePreVO.setUname(user1.getUsername());
            chatMessagePreVO.setUid(user1.getId());
            chatMessagePreVO.setTid(team.getTid());


            chatMessagePreVOS.add(chatMessagePreVO);
        }

        //将群聊记录插入
        TeamPartner findTeamByUid = new TeamPartner();
        findTeamByUid.setUid(user.getId());
        List<TeamPartner> teams = teamPartnerMapper.select(findTeamByUid);
        for (TeamPartner teamPartner:teams){
            ChatMessage teamMessage = chatMessageMapper.findTeamMessageByTid(teamPartner.getTid());
            if(teamMessage == null) continue;
            ChatMessagePreVO chatMessageVO = new ChatMessagePreVO();
            BeanUtils.copyProperties(teamMessage,chatMessageVO);
            chatMessageVO.setTname(teamMapper.selectByPrimaryKey(teamPartner.getTid()).getName());
            chatMessageVO.setLast_time(teamMessage.getCreate_time());
            chatMessageVO.setTid(teamPartner.getTid());
            chatMessageVO.setUid(teamMessage.getSend_id());
            chatMessageVO.setUname(userMapper.selectByPrimaryKey(teamMessage.getSend_id()).getUsername());

            chatMessagePreVOS.add(chatMessageVO);
        }

        return chatMessagePreVOS;
    }

    public List<Integer> GetTeamUidList(Integer tid){
        List<Integer> uids = new ArrayList<>();
        Team team = teamMapper.selectByPrimaryKey(tid);
        Integer leaderId = team.getOwnerid();
        uids.add(leaderId);

        //找出所有队友
        TeamPartner findByTid = new TeamPartner();
        findByTid.setTid(tid);
        List<TeamPartner> teamPartners = teamPartnerMapper.select(findByTid);
        //将他们的uid插入list
        for(TeamPartner teamPartner:teamPartners){
            if(teamPartner.getUid() == leaderId){
                continue; }
            uids.add(teamPartner.getUid());
        }
        return uids;
    }

    public void SendQueue(Map<String, Object> map){
        rabbitTemplate.convertAndSend("ChatMessageQueue", "ChatMessageRoute", map);
    }


    @Override
    public Object TeamGetHistoryMessage(Integer tid, User user) {
        ChatMessage findByTid = new ChatMessage();
        findByTid.setRoom_id(tid.toString());
        findByTid.setType(2);
        List<ChatMessage> chatMessages = chatMessageMapper.select(findByTid);
        List<ChatTeamMessageVO> chatTeamMessageVOS = new ArrayList<>();
        for(ChatMessage chatMessage:chatMessages){
            ChatTeamMessageVO chatTeamMessageVO = new ChatTeamMessageVO();
            chatTeamMessageVO.setUid(chatMessage.getSend_id());
            chatTeamMessageVO.setContent(chatMessage.getContent());
            chatTeamMessageVO.setTime(chatMessage.getCreate_time());

            chatTeamMessageVOS.add(chatTeamMessageVO);
        }
        return chatTeamMessageVOS;
    }
}
