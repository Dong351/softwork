package softwork.mapper;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import softwork.pojo.entities.Certificate;

public interface CertificateRepository extends ElasticsearchRepository<Certificate,Integer> {
}