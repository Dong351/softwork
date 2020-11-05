package softwork.pojo.entities;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;

@Data
public class Team {
    @Id
    private Integer tid;

    private Integer ownerid;

    private Date create_time;

    private String name;

    private String description;
}
