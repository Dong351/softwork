package softwork.mapper;

import org.springframework.stereotype.Repository;
import softwork.pojo.entities.Message;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface MessageMapper extends BaseMapper<Message> {
}
