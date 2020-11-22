package softwork.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwork.mapper.CollectMapper;
import softwork.mapper.ContestMapper;
import softwork.pojo.entities.Collect;
import softwork.pojo.entities.Contest;
import softwork.pojo.entities.User;
import softwork.pojo.vo.CollectContestListVO;
import softwork.service.CollectService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    CollectMapper collectMapper;
    @Autowired
    ContestMapper contestMapper;

    @Override
    public Object CollectContest(Integer contestId, User user) {
        Collect findExist = new Collect();
        findExist.setData_id(contestId);
        findExist.setUid(user.getId());
        findExist.setType(1);
        Collect exist = collectMapper.selectOne(findExist);
        if (exist == null) {
            Collect collect = new Collect();
            collect.setUid(user.getId());
            collect.setType(1);
            collect.setData_id(contestId);
            collectMapper.insert(collect);
        }
        else {
            collectMapper.delete(exist);
        }
        return null;
    }

    @Override
    public Object CollectCertificate(Integer certificateId, User user) {
        Collect findExist = new Collect();
        findExist.setData_id(certificateId);
        findExist.setUid(user.getId());
        findExist.setType(2);
        Collect exist = collectMapper.selectOne(findExist);
        if (exist == null) {
            Collect collect = new Collect();
            collect.setUid(user.getId());
            collect.setType(2);
            collect.setData_id(certificateId);
            collectMapper.insert(collect);
        }
        else {
            collectMapper.delete(exist);
        }
        return null;
    }

    @Override
    public Object GetCollectContest(User user) {
        Collect findJoinedContest = new Collect();
        List<CollectContestListVO> collectContestListVOS = new ArrayList<>();
        findJoinedContest.setUid(user.getId());
        findJoinedContest.setType(1);
        List<Collect> contestCollects = collectMapper.select(findJoinedContest);
        for(Collect collect:contestCollects){
            Contest contest = contestMapper.GetListVOById(collect.getData_id());
            System.out.println(contest);
            CollectContestListVO collectContestListVO = new CollectContestListVO();
            BeanUtils.copyProperties(contest,collectContestListVO);
            collectContestListVO.setContestName(contest.getName());

            //计算时间差
            long nowTime = new Date().getTime();
            long startTime = contest.getEnroll_start().getTime();
            long endTime = contest.getEnroll_end().getTime();
            long nd = 1000 * 24 * 60 * 60;
            if(startTime > nowTime){
                long diff = startTime - nowTime;
                long days = diff / nd;
                collectContestListVO.setStatus(1);
                collectContestListVO.setRestTime("离报名开始还有"+days+"天");
            }
            else if(startTime < nowTime && nowTime < endTime){
                long diff = endTime - nowTime;
                long days = diff / nd;
                collectContestListVO.setStatus(2);
                collectContestListVO.setRestTime("离报名结束还有"+days+"天");
            }
            else {
                collectContestListVO.setStatus(0);
                collectContestListVO.setRestTime("报名已截止");
            }

//            collectContestListVO.

            collectContestListVOS.add(collectContestListVO);
        }
        return null;
    }
}
