package softwork.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RoutesInfoVO {
    private Integer id;
    private Integer data_id;
    private Integer type;
    private Date time;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String name;
    private String remarks;
}
