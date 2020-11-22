package softwork.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class TeamJoinedVO {
    private Integer tid;
    private Integer contestid;
    private String tName;
    private String leaderAvatarUrl;
    private String[] teamPartnerUrls;
    private Integer tCount;
    private Date create_time;
    private Integer click;

}
