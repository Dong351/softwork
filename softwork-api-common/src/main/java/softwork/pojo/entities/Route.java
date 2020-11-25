package softwork.pojo.entities;

import lombok.Data;

import javax.persistence.Id;

@Data
public class Route {
    @Id
    private Integer id;

    private Integer uid;

    private Integer type;

    private Integer data_id;

    private String remarks;
}
