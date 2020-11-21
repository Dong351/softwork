package softwork.pojo.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import static softwork.utils.Constants.REGEX_PHONE_NUMBER;

@Data
public class UserUpdateDTO {
    private String username;

    @Email(message = "邮箱格式有误")
    private String email;

    @Pattern(regexp = REGEX_PHONE_NUMBER, message = "没有此号码")
    private String phone;

    private String name;

    private Integer sex;

    private String description;
}
