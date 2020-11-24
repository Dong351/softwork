package softwork.pojo.entities;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class Message {
    @Id
    private Integer mid;

    private Integer tid;

    private Integer send_uid;

    private Integer receive_uid;

    private Integer type;   //0已处理 1队伍申请 2队伍申请回复

    private Integer readed;   //0未读 1已读

    private String contain;

    private Date create_time;

}
