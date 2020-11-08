package softwork.service;

import softwork.pojo.entities.User;

public interface MessageService {
    /**
     * 获取用户的消息通知列表
     * @param user
     * @return
     */
    Object GetList(User user);

    /**
     * 根据mid获取指定消息
     * @param mid
     * @param user
     * @return
     */
    Object GetMessage(Integer mid, User user);
}
