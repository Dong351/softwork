package softwork.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softwork.exception.CommonException;
import softwork.mapper.MessageMapper;
import softwork.mapper.TeamMapper;
import softwork.mapper.TeamPartnerMapper;
import softwork.pojo.dto.TeamCreateDTO;
import softwork.pojo.entities.Message;
import softwork.pojo.entities.Team;
import softwork.pojo.entities.TeamPartner;
import softwork.pojo.entities.User;
import softwork.service.TeamService;

import java.util.Date;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    TeamMapper teamMapper;
    @Autowired
    TeamPartnerMapper teamPartnerMapper;
    @Autowired
    MessageMapper messageMapper;

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
    public Object JoinTeamRequest(Integer teamid, User user) {
        Message message = new Message();
        message.setTid(teamid);
        message.setCreate_time(new Date());
        message.setSend_uid(user.getId());
        Team team = teamMapper.selectByPrimaryKey(teamid);
        message.setReceive_uid(team.getOwnerid());
        message.setContain(user.getUsername() + "申请加入您的队伍 " + team.getName());
        message.setType(2);
        messageMapper.insert(message);
        return null;
    }
}
