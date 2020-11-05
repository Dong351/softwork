package softwork.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import softwork.pojo.entities.Team;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface TeamMapper extends BaseMapper<Team> {

    @Select("select COUNT(*) from team where ownerid=#{uid}")
    public Integer SelectCountByOnwerId(Integer uid);

    @Insert("insert team(ownerid,create_time,name,description) values(#{ownerid},#{create_time},#{name},#{description})")
    @Options(useGeneratedKeys=true,keyColumn="tid",keyProperty = "tid")
    public Integer InsertRTId(Team team);
}
