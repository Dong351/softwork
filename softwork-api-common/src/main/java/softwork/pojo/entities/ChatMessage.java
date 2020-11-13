package softwork.pojo.entities;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "chatMessage")
@Data
public class ChatMessage {
    private Integer id;
    private Integer send_id;
    private Integer receive_id;
    private Integer room_id;
    private Integer type;
    private String content;
    private Date create_time;

}
