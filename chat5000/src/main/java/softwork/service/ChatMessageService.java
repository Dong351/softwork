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


    /**
     * 查看消息预览
      * @param user
     * @return
     */
    Object GetChatMessagePreview(User user);

    /**
     * 查看指定tid的队伍聊天记录
     * @param tid
     * @param user
     * @return
     */
    Object TeamGetHistoryMessage(Integer tid, User user);
}
