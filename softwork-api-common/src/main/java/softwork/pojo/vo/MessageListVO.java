package softwork.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MessageListVO {
    private Integer mid;

    private Integer send_uid;

    private Integer type;

    private String send_userName;

    private Date create_time;
}
