package softwork.pojo.vo;


import lombok.Data;

@Data
public class UserInfoVO {

    private Integer id;
    private String username;
    private String name;
    private String phone;
    private String email;
    private String accesstoken;
    private Integer type;
    private String avatar_url;
    private String description;

    private Integer sex;

}
