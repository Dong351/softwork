package softwork.pojo.vo;

import lombok.Data;

@Data
public class CollectContestListVO {
    private Integer status;
    private String restTime;
    private String contestName;
    private String pic_url;
    private Integer collections;
    private String level;
}
