package softwork.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import softwork.pojo.entities.ChatMessage;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Repository
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    @Select("SELECT * FROM chatMessage WHERE room_id='#{roomid}' ORDER BY create_time")
    List<ChatMessage> findListOrderByTime(String roomid);

    @Select("select * from (select * from chatMessage order by create_time desc)tmp  " +
            "WHERE send_id=#{send_id} group by receive_id order by create_time desc")
    List<ChatMessage> findListGroupByRecId(Integer send_id);
}
