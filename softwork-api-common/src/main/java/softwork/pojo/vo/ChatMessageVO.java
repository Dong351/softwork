package softwork.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ChatMessageVO {
    private Integer id;
    private String content;
    private Integer send_id;
    private Integer receive_id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
}
