package softwork.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessagePreVO {
    private Integer receive_id;

    private Integer type;

    private String content;

    private Date last_time;

    private Integer unread;
}
