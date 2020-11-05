package softwork.pojo.entities;

import lombok.Data;

import java.util.Date;

@Data
public class Message {
    private Integer tid;

    private Integer send_uid;

    private Integer receive_uid;

    private Integer type;   //0已处理 1已读 2队伍申请 3队伍申请回复

    private String contain;

    private Date create_time;
}
