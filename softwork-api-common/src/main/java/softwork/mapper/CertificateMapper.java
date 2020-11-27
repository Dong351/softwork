package softwork.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import softwork.pojo.entities.Certificate;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

public interface CertificateMapper extends BaseMapper<Certificate> {

    @Update("update certificate set watched = watched + 1 where id = #{id}")
    void addWatched(String id);

    @Select("select id,name,office_web,enroll_start,enroll_end,contest_start,contest_end,collected,watched from certificate")
    List<Certificate> baseInfo();

    @Select("select id,name,office_web,enroll_start,enroll_end,contest_start,contest_end,collected,watched from certificate where id = #{id}")
    Certificate GetListVOById(Integer id);
}
