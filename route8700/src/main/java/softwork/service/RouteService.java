package softwork.service;

import softwork.pojo.dto.RouteEditDTO;
import softwork.pojo.entities.User;

public interface RouteService {
    /**
     * 将指定contestId的竞赛加入路线
     * @param contestId
     * @param user
     * @return
     */
    Object AddRoute(Integer contestId, User user);

    /**
     * 获取所有路线
     * @param user
     * @return
     */
    Object GetRoutes(User user);

    /**
     * 编辑route表中的remark
     * @param routeId
     * @param content
     * @param user
     * @return
     */
    Object EditRemarks(Integer routeId, RouteEditDTO content, User user);

    /**
     * 路线提醒
     * @param user
     * @return
     */
    Object RouteAlert(User user);
}
