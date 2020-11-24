package softwork.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import softwork.pojo.entities.Collect;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface CollectMapper extends BaseMapper<Collect> {

    @Select("select count(*) from collect where type=1 and data_id=#{contestid}")
    Integer getContestCollections(Integer contestid);

}
