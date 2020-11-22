package softwork.service;

import softwork.pojo.entities.User;

public interface CollectService {
    /**
     * 收藏指定id的竞赛
     * @param contestId
     * @param user
     * @return
     */
    Object CollectContest(Integer contestId, User user);

    /**
     * 收藏指定id的证书
     * @param certificateId
     * @param user
     * @return
     */
    Object CollectCertificate(Integer certificateId, User user);

    /**
     * 查看自己收藏的所有比赛
     * @param user
     * @return
     */
    Object GetCollectContest(User user);
}
