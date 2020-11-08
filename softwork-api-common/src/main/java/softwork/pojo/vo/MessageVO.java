package softwork.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MessageVO {
    private Integer mid;
    private Integer uid;
    private Integer send_uid;
    private String send_userName;
    private Integer tid;
    private String team_name;
    private Integer type;
    private String contain;
    private Date create_time;
}
