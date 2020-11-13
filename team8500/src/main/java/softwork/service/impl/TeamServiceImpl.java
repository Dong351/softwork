package softwork.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwork.exception.CommonException;
import softwork.mapper.MessageMapper;
import softwork.mapper.TeamMapper;
import softwork.mapper.TeamPartnerMapper;
import softwork.mapper.UserMapper;
import softwork.pojo.dto.TeamCreateDTO;
import softwork.pojo.entities.Message;
import softwork.pojo.entities.Team;
import softwork.pojo.entities.TeamPartner;
import softwork.pojo.entities.User;
import softwork.pojo.vo.TeamInfoVO;
import softwork.pojo.vo.TeamJoinedVO;
import softwork.pojo.vo.TeamPartnerVO;
import softwork.service.TeamService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    TeamMapper teamMapper;
    @Autowired
    TeamPartnerMapper teamPartnerMapper;
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object Create(TeamCreateDTO dto, User user) {
        if(teamMapper.SelectCountByOnwerId(user.getId()) > 10){
            throw new CommonException("创建队伍不可超过10个！");
        }

        //插入team表
        Team team = new Team();
        BeanUtils.copyProperties(dto,team);
        team.setName(dto.getTeamName());
        team.setCreate_time(new Date());
        team.setOwnerid(user.getId());
        Integer tid = teamMapper.InsertRTId(team);
//        System.out.println(team.getTid());

        //将队长插入teamPartner表
        TeamPartner teamPartner = new TeamPartner();
        teamPartner.setTid(team.getTid());
        teamPartner.setUid(user.getId());
        teamPartner.setCreate_time(new Date());
        teamPartnerMapper.insert(teamPartner);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object JoinTeamRequest(String requestContain, Integer teamid, User user) {
        //查找是否已经申请
        Message findExist = new Message();
        findExist.setTid(teamid);
        findExist.setSend_uid(user.getId());
        if(messageMapper.select(findExist) != null){
            throw new CommonException("您已申请！");
        }

        Message message = new Message();
        message.setTid(teamid);
        message.setCreate_time(new Date());
        message.setSend_uid(user.getId());
        Team team = teamMapper.selectByPrimaryKey(teamid);
        message.setReceive_uid(team.getOwnerid());
        message.setContain(requestContain);
        message.setType(2);
        messageMapper.insert(message);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object DealTeamRequest(Integer messageid, Integer deal, User user) {
//        if(messageid != 2 && messageid != 1){
//            throw new CommonException("不是队伍申请信息");
//        }
        Message message = messageMapper.selectByPrimaryKey(messageid);
        Integer tid = message.getTid();
        Team team = teamMapper.selectByPrimaryKey(tid);

        //权限判断
        if(user.getId() != team.getOwnerid()){
            throw new CommonException("您不是队长，无权限审批");
        }


        TeamPartner teamPartner = new TeamPartner();
        Message returnMessage = new Message();
        returnMessage.setType(3);
        returnMessage.setSend_uid(user.getId());
        returnMessage.setTid(tid);
        returnMessage.setReceive_uid(message.getSend_uid());
        returnMessage.setCreate_time(new Date());
        //处理结果1为同意 0为拒绝,并发送回信
        if(deal == 1){
            teamPartner.setTid(tid);
            teamPartner.setUid(message.getSend_uid());
            teamPartner.setCreate_time(new Date());
            teamPartnerMapper.insert(teamPartner);
            returnMessage.setContain("队伍："+team.getName()+" 已同意你的入队申请");
        }
        else if(deal == 0){
            returnMessage.setContain("队伍："+team.getName()+" 拒绝你的入队申请");
        }
        messageMapper.insert(returnMessage);

        //更新messgae的type
        message.setType(0);
        messageMapper.updateByPrimaryKeySelective(message);
        return null;
    }

    @Override
    public Object ShowJoinedTeam(User user) {
        TeamPartner findJoined = new TeamPartner();
        findJoined.setUid(user.getId());
        List<TeamPartner> joined = teamPartnerMapper.select(findJoined);
        List<TeamJoinedVO> joinedVOS = new ArrayList<>();
        for(TeamPartner teamPartner:joined){
            TeamJoinedVO teamJoinedVO = new TeamJoinedVO();
            teamJoinedVO.setTid(teamPartner.getTid());
            Team team = teamMapper.selectByPrimaryKey(teamPartner.getTid());
            teamJoinedVO.setTName(team.getName());
            teamJoinedVO.setLeaderAvatarUrl(userMapper.selectByPrimaryKey(team.getOwnerid()).getAvatar_url());
            teamJoinedVO.setCreate_time(team.getCreate_time());


            //依次将队友的头像url插入string[]中
            TeamPartner findAllPartner = new TeamPartner();
            findAllPartner.setTid(team.getTid());
            List<TeamPartner> partners = teamPartnerMapper.select(findAllPartner);
            teamJoinedVO.setTCount(partners.size());
            String[] partnerAvatarUrl = new String[partners.size()-1];
            int i = 0;
            for(TeamPartner teamPartner1:partners){
                if(teamPartner1.getUid() == team.getOwnerid())
                    continue;
                String partnerUrl = userMapper.selectByPrimaryKey(teamPartner1.getUid()).getAvatar_url();
                System.out.println(partnerUrl);
                partnerAvatarUrl[i++] = partnerUrl;
            }
            teamJoinedVO.setTeamPartnerUrls(partnerAvatarUrl);
            joinedVOS.add(teamJoinedVO);
        }


        return joinedVOS;
    }

    @Override
    public Object GetTeamInfo(Integer teamid) {
        Team team = teamMapper.selectByPrimaryKey(teamid);
        TeamInfoVO teamInfoVO = new TeamInfoVO();
        List<TeamPartnerVO> teamPartnerVOS= new ArrayList<TeamPartnerVO>();
        BeanUtils.copyProperties(team,teamInfoVO);
        teamInfoVO.setTeam_description(team.getDescription());
        TeamPartner findAllPartner = new TeamPartner();
        findAllPartner.setTid(teamid);

        List<TeamPartner> teamPartners = teamPartnerMapper.select(findAllPartner);
        for(TeamPartner teamPartner:teamPartners){
            User user = userMapper.selectByPrimaryKey(teamPartner.getUid());
            TeamPartnerVO teamPartnerVO = new TeamPartnerVO();
            teamPartnerVO.setPosition(0);
            if(user.getId() == team.getOwnerid())
                teamPartnerVO.setPosition(1);
            teamPartnerVO.setDescription(user.getDescription());
            teamPartnerVO.setUname(user.getUsername());
            teamPartnerVO.setUid(user.getId());
            teamPartnerVOS.add(teamPartnerVO);
        }
        teamInfoVO.setTeamPartners(teamPartnerVOS);
        return teamInfoVO;
    }
}
