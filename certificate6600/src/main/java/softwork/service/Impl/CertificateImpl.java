package softwork.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import softwork.mapper.CertificateMapper;
import softwork.mapper.CertificateRepository;
import softwork.pojo.dto.PageDTO;
import softwork.pojo.entities.Certificate;
import softwork.service.CertificateService;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
public class CertificateImpl implements CertificateService {

    @Autowired
    CertificateMapper certificateMapper;

    @Autowired
    CertificateRepository certificateRepository;

    @Override
    public PageInfo getBaseInfo(PageDTO dto) {
        dto.setOrderBy(camelToUnderline(dto.getOrderBy())+ " " +dto.getOrderRule());
        dto.setPageNum(dto.getPageNum());
        PageHelper.startPage(dto);
        List<Certificate> list= certificateMapper.baseInfo();
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
    public Certificate getInfoByID(String id) {
        certificateMapper.addWatched(id);
        return certificateMapper.selectByPrimaryKey(id);
    }

    @Override
    public Page<Certificate> searchByKeyWord(String keyword, PageDTO dto) {
        Sort sort = Sort.by(Sort.Order.desc(camelToUnderline(dto.getOrderBy())));
        if(dto.getOrderRule().toLowerCase().equals("asc")){
            sort = Sort.by(Sort.Order.asc(dto.getOrderBy()));
        }
        PageRequest page = PageRequest.of(dto.getPageNum()-1,dto.getPageSize(),sort);
        //query设置
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(queryStringQuery("\""+keyword+"\""))
                .withFields("id","name","office_web","enroll_start","enroll_end","contest_start","contest_end")
                .withPageable(page);
        SearchQuery searchQuery = queryBuilder.build();

        Page<Certificate> certificatePage = certificateRepository.search(searchQuery);

        return certificatePage;
    }
}
