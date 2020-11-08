package softwork.pojo.entities;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "teamPartner")
public class TeamPartner {
    private Integer tid;
    private Integer uid;
    private Date create_time;
}
