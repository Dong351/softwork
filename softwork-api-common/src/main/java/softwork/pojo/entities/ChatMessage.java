package softwork.pojo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "chatMessage")
@Data
public class ChatMessage {
    @Id
    @Column(name = "id")
    private Integer mid;

    private String room_id;

    private Integer send_id;

    private Integer receive_id;

    private Integer type;   //0已处理 1已读 2队伍申请 3队伍申请回复

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date create_time;

}
