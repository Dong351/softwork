package softwork.service;

import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import softwork.pojo.dto.PageDTO;
import softwork.pojo.entities.Certificate;

public interface CertificateService {
    PageInfo getBaseInfo(PageDTO dto);

    Certificate getInfoByID(String id);

    Page<Certificate> searchByKeyWord(String keyword, PageDTO dto);
}
