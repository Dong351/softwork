package softwork.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import softwork.pojo.entities.Contest;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Repository
public interface ContestMapper extends BaseMapper<Contest> {
    @Select("SELECT name,enroll_start,enroll_end,pic_url,level,watched FROM contest where id=#{id} ")
    Contest GetListVOById(Integer id);

    List<Contest> getBaseInfoByCondition(Contest contest);

    @Select("select DISTINCT type from contest")
    List<String> getContestType();

    @Select("select DISTINCT level from contest")
    List<String> getContestLevel();

    @Update("update contest set watched = watched + 1 where id = #{id}")
    void addWatched(String id);
}
