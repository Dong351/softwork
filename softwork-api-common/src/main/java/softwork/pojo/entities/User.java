package softwork.pojo.entities;

import lombok.Data;

import javax.persistence.Id;

@Data
public class User {

    @Id
    private Integer id;
    private String username;
    private String password;
    private String phone;
    private Integer sex;
    private String name;
    private String email;
    private Integer type;
    private String accesstoken;
    private String avatar_url;
    private String description;
}
