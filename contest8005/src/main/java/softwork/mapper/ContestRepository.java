package softwork.mapper;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import softwork.pojo.entities.Contest;

import java.util.List;

public interface ContestRepository extends ElasticsearchRepository<Contest,Integer> {

    List<Contest> findByNameOrInfoLike(String keyword1, String keyword2, Pageable pageable);
}