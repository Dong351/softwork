package softwork.mapper;

import org.apache.ibatis.annotations.Select;
import softwork.pojo.entities.Contest;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

public interface ContestMapper extends BaseMapper<Contest> {

    List<Contest> getBaseInfoByCondition(Contest contest);

    @Select("select DISTINCT type from contest")
    List<String> getContestType();

    @Select("select DISTINCT level from contest")
    List<String> getContestLevel();
}
