package softwork.mapper;

import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Repository;
import softwork.pojo.entities.ClickLimit;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface ClickLimitMapper extends BaseMapper<ClickLimit> {

    @Delete("DELETE FROM click_limit where ((UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(time))/3600) > 6")
    void deleteOutTime();
}
