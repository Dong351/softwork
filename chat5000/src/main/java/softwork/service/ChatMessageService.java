package softwork.service;

import softwork.pojo.entities.User;

public interface ChatMessageService {
    /**
     * 查看与指定uid用户的私聊记录
     * @param uid
     * @param user
     * @return
     */
    Object Single(Integer uid, User user);


}
