package softwork.pojo.entities;

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

    private Integer type;

    private Integer readed;

    private String content;

    private Date create_time;

}
