package softwork.mapper;

import org.springframework.stereotype.Repository;
import softwork.pojo.entities.Route;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface RouteMapper extends BaseMapper<Route> {
}
