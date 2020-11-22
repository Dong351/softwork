package softwork.pojo.entities;

import lombok.Data;

import java.util.Date;

@Data
public class ClickLimit {
    private Integer uid;
    private Integer teamid;
    private Date time;
}
