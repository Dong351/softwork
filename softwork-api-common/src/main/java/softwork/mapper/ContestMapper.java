package softwork.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import softwork.pojo.entities.Contest;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface ContestMapper extends BaseMapper<Contest> {
    @Select("SELECT name,enroll_start,enroll_end,pic_url,level FROM contest where id={id} ")
    public Contest GetListVOById(Integer id);
}
