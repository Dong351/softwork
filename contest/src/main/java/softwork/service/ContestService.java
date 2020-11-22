package softwork.service;

import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import softwork.pojo.dto.PageDTO;
import softwork.pojo.entities.Contest;

import java.util.List;

public interface ContestService {

    PageInfo getBaseInfo(PageDTO pageDTO,Contest contest);

    Contest getInfoByID(String id);

    List<String> getAllType();

    List<String> getAllLevel();

    Page<Contest> searchByKeyWord(String keyword, PageDTO dto, Contest contest);
}
