package softwork.pojo.entities;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Table(name = "contest")
public class Contest{

    @Id
    private Integer id;

    private String name;

    private String originator;

    private String type;

    private String level;

    private String info;

    private Timestamp enrollStart;

    private Timestamp enrollEnd;

    private Timestamp contestStart;

    private Timestamp contestEnd;
    
    private String picUrl;

}
