package softwork.mapper;

import org.springframework.stereotype.Repository;
import softwork.pojo.entities.ChatMessage;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
}
