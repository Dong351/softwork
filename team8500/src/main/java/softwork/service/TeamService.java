package softwork.service;

import softwork.pojo.dto.TeamCreateDTO;
import softwork.pojo.entities.User;

public interface TeamService {
    /**
     * 创建队伍
     * @param dto
     * @param user
     * @return
     */
    Object Create(TeamCreateDTO dto, User user);

    /**
     * 申请加入队伍
     *
     *
     * @param requestContain
     * @param teamid
     * @param user
     * @return
     */
    Object JoinTeamRequest(String requestContain, Integer teamid, User user);

    /**
     * 处理队伍申请
     * @param messageid
     * @param deal
     * @param user
     * @return
     */
    Object DealTeamRequest(Integer messageid, Integer deal, User user);

    /**
     * 查看自己加入了哪些队伍
     * @param user
     * @return
     */
    Object ShowJoinedTeam(User user);

    /**
     * 查看指定队伍详情
     * @param teamid
     * @return
     */
    Object GetTeamInfo(Integer teamid);
}
