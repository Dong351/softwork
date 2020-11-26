package softwork.pojo.entities;

import lombok.Data;

import javax.persistence.Id;

@Data
public class ShowCertificate {
    @Id
    private Integer id;
    private Integer uid;
    private String name;
    private String url;
}
