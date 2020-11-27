package softwork.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import softwork.mapper.ContestMapper;
import softwork.mapper.ContestRepository;
import softwork.pojo.dto.PageDTO;
import softwork.pojo.entities.Contest;
import softwork.service.ContestService;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
public class ContestServiceImpl implements ContestService {

    @Autowired
    ContestMapper contestMapper;

    @Autowired
    ContestRepository contestRepository;

    @Override
    public PageInfo getBaseInfo(PageDTO pageDTO,Contest contest) {
        pageDTO.setOrderBy(camelToUnderline(pageDTO.getOrderBy())+ " " +pageDTO.getOrderRule());
        pageDTO.setPageNum(pageDTO.getPageNum());
        PageHelper.startPage(pageDTO);
        List<Contest> list= contestMapper.getBaseInfoByCondition(contest);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    private String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Override
    public Contest getInfoByID(String id) {
        contestMapper.addWatched(id);
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

    @Override
    public Page<Contest> searchByKeyWord(String keyword, PageDTO dto, Contest contest) {
        //设置sort与分页
        Sort sort = Sort.by(Sort.Order.desc(dto.getOrderBy()));
        if(dto.getOrderRule().toLowerCase().equals("asc")){
            sort = Sort.by(Sort.Order.asc(dto.getOrderBy()));
        }
        PageRequest page = PageRequest.of(dto.getPageNum()-1,dto.getPageSize(),sort);
        //query设置
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(queryStringQuery("\""+keyword+"\""))
                .withFields("id","name","originator","type","level","enrollStart","enrollEnd","contestStart","contestEnd","picUrl")
                .withPageable(page);
        //对应level与Type
        if(contest.getLevel()!=null && contest.getType()!=null){
            queryBuilder.withFilter(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("level",contest.getLevel()))
                    .must(QueryBuilders.termQuery("type",contest.getType())));
        }else if(contest.getLevel()!=null){
            queryBuilder.withFilter(QueryBuilders.termQuery("level",contest.getLevel()));
        }else if(contest.getType()!=null){
            queryBuilder.withFilter(QueryBuilders.termQuery("type",contest.getType()));
        }

        SearchQuery searchQuery = queryBuilder.build();

        Page<Contest> contestPage = contestRepository.search(searchQuery);

        return contestPage;
    }
}
