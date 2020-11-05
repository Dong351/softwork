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
     * @param teamid
     * @param user
     * @return
     */
    Object JoinTeamRequest(Integer teamid, User user);
}
