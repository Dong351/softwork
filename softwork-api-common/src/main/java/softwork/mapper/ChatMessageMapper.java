package softwork.mapper;

import org.apache.ibatis.annotations.Param;
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
            "WHERE (send_id=#{uid} or receive_id=#{uid}) and type != 2 group by send_id order by create_time desc")
    List<ChatMessage> findListGroupBySendId(Integer uid);

    @Select("select * from (select * from chatMessage order by create_time desc)tmp  " +
            "WHERE (send_id=#{uid} or receive_id=#{uid}) and type != 2 group by receive_id order by create_time desc")
    List<ChatMessage> findListGroupByRecId(Integer uid);

    @Select("select * from (select * from chatMessage order by create_time desc)tmp " +
            "WHERE room_id=#{tid} group by room_id order by create_time desc")
    ChatMessage findTeamMessageByTid(Integer tid);

//    @Select("")
//    ChatMessage findTeamMessage(Integer tid);

    @Select("select * from chatMessage " +
            "WHERE (send_id=#{uid} and receive_id=#{uid1}) or (receive_id=#{uid} and send_id=#{uid1}) order by create_time")
    List<ChatMessage> findSingleMessages(@Param("uid") Integer uid, @Param("uid1") Integer uid1);
}
