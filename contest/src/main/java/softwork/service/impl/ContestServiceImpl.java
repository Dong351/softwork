package softwork.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import softwork.mapper.ContestMapper;
import softwork.pojo.dto.PageDTO;
import softwork.pojo.entities.Contest;
import softwork.service.ContestService;

import java.util.List;

@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    ContestMapper contestMapper;

    @Override
    public PageInfo getBaseInfo(PageDTO pageDTO,Contest contest) {
        PageHelper.startPage(pageDTO);
        List<Contest> list= contestMapper.getBaseInfoByCondition(contest);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public Contest getInfoByID(String id) {
        return contestMapper.selectByPrimaryKey(id);
    }

    @Override
    @Cacheable(value = "ContestType")
    public List<String> getAllType() {
        return contestMapper.getContestType();
    }

    @Override
    @Cacheable(value = "ContestLevel")
    public List<String> getAllLevel() {
        return contestMapper.getContestLevel();
    }
}
