package softwork.pojo.dto;

import lombok.Data;
import softwork.pojo.entities.Team;

import javax.validation.constraints.NotBlank;

@Data
public class TeamCreateDTO extends Team {
    @NotBlank(message = "队伍名称不能为空")
    private String teamName;
}
