package softwork.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ChatMessagePreVO {
    private Integer tid;

    private Integer uid;

    private String uname;

    private String tname;

    private String avatar_url;

    private Integer type;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:ss")
    private Date last_time;

//    private Integer unread;
}
