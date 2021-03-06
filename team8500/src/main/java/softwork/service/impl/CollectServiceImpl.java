package softwork.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwork.mapper.CertificateMapper;
import softwork.mapper.CollectMapper;
import softwork.mapper.ContestMapper;
import softwork.pojo.entities.Certificate;
import softwork.pojo.entities.Collect;
import softwork.pojo.entities.Contest;
import softwork.pojo.entities.User;
import softwork.pojo.vo.CollectContestListVO;
import softwork.pojo.vo.CollectCertificateListVO;
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
    @Autowired
    CertificateMapper certificateMapper;

    @Override
    public Object CollectContest(Integer contestId, User user) {
        Collect findExist = new Collect();
        Contest contest = contestMapper.selectByPrimaryKey(contestId);

        findExist.setData_id(contestId);
        findExist.setUid(user.getId());
        findExist.setType(1);
        Collect exist = collectMapper.selectOne(findExist);
        //判断是否收藏，没收藏插入collect表，contest表收藏+1，反之
        if (exist == null) {
            Collect collect = new Collect();
            collect.setUid(user.getId());
            collect.setType(1);
            collect.setData_id(contestId);
            collectMapper.insert(collect);

            contest.setCollected(contest.getCollected()+1);
            contestMapper.updateByPrimaryKeySelective(contest);
        }
        else {
            collectMapper.delete(exist);
            contest.setCollected(contest.getCollected()-1);
            contestMapper.updateByPrimaryKeySelective(contest);
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
            collectContestListVO.setContestId(collect.getData_id());
            collectContestListVO.setContestName(contest.getName());
            collectContestListVO.setViews(contest.getWatched());

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

            collectContestListVO.setCollections(collectMapper.getContestCollections(collect.getData_id()));
            collectContestListVOS.add(collectContestListVO);
        }
        return collectContestListVOS;
    }

    @Override
    public Object GetCollectCertificate(User user) {
        Collect findJoinedCertificate = new Collect();
        List<CollectCertificateListVO> collectCertificateListVOS = new ArrayList<>();
        findJoinedCertificate.setUid(user.getId());
        findJoinedCertificate.setType(2);
        List<Collect> certificateCollects = collectMapper.select(findJoinedCertificate);
        for(Collect collect:certificateCollects){
            Certificate certificate = certificateMapper.GetListVOById(collect.getData_id());
            System.out.println(certificate);
            CollectCertificateListVO collectcertificateListVO = new CollectCertificateListVO();
            BeanUtils.copyProperties(certificate,collectcertificateListVO);
            collectcertificateListVO.setCertificateId(collect.getData_id());
            collectcertificateListVO.setCertificateName(certificate.getName());
            collectcertificateListVO.setViews(certificate.getWatched());

            //计算时间差
            long nowTime = new Date().getTime();
            long startTime = certificate.getEnroll_start().getTime();
            long endTime = certificate.getEnroll_end().getTime();
            long nd = 1000 * 24 * 60 * 60;
            if(startTime > nowTime){
                long diff = startTime - nowTime;
                long days = diff / nd;
                collectcertificateListVO.setStatus(1);
                collectcertificateListVO.setRestTime("离报名开始还有"+days+"天");
            }
            else if(startTime < nowTime && nowTime < endTime){
                long diff = endTime - nowTime;
                long days = diff / nd;
                collectcertificateListVO.setStatus(2);
                collectcertificateListVO.setRestTime("离报名结束还有"+days+"天");
            }
            else {
                collectcertificateListVO.setStatus(0);
                collectcertificateListVO.setRestTime("报名已截止");
            }

            collectcertificateListVO.setCollections(collectMapper.getContestCollections(collect.getData_id()));
            collectCertificateListVOS.add(collectcertificateListVO);
        }
        return collectCertificateListVOS;
    }

    @Override
    public Object ContestCollectStatus(Integer contestId, User user) {
        Collect findExist = new Collect();
        findExist.setType(1);
        findExist.setData_id(contestId);
        findExist.setUid(user.getId());
        Collect collect = collectMapper.selectOne(findExist);
        if(collect == null){
            return 0;
        }
        else return 1;
    }

    @Override
    public Object CertificateCollectStatus(Integer certificateId, User user) {
        Collect findExist = new Collect();
        findExist.setType(2);
        findExist.setData_id(certificateId);
        Collect collect = collectMapper.selectOne(findExist);
        if(collect == null){
            return 0;
        }
        else return 1;
    }
}
