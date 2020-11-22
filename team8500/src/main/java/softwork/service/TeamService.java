package softwork.service;

import softwork.pojo.dto.TeamCreateDTO;
import softwork.pojo.entities.User;

public interface TeamService {
    /**
     * 创建队伍
     *
     * @param contestId
     * @param dto
     * @param user
     * @return
     */
    Object Create(Integer contestId, TeamCreateDTO dto, User user);

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
     * @param user
     * @return
     */
    Object GetTeamInfo(Integer teamid, User user);

    /**
     * 通过竞赛id查看该竞赛下所有队伍
     * @param contestId
     * @return
     */
    Object GetListByContestId(Integer contestId);

    /**
     * 解散队伍
     * @param teamid
     * @param user
     * @return
     */
    Object BreakTeam(Integer teamid, User user);
}
