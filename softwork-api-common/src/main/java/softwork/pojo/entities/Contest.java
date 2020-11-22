package softwork.pojo.entities;

import lombok.Data;

import java.util.Date;

@Data
public class Contest {
    private Integer id;
    private String name;
    private String originator;
    private String url;
    private String type;
    private String level;
    private Date enroll_start;
    private Date enroll_end;
    private Date contest_start;
    private Date contest_end;
    private String info;
    private String pic_url;
}
