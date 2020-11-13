package softwork.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class TeamInfoVO {
    private Integer tid;
    private String name;
    private String team_description;
    private List<TeamPartnerVO> teamPartners;

}
