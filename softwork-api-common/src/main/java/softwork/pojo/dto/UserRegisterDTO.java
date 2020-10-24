package softwork.pojo.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static softwork.utils.Constants.*;

@Data
public class UserRegisterDTO {

    @NotBlank(message = "姓名不能为空")
    private String username;


    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = REGEX_USER_LOGIN_PASSWORD, message = "密码格式有误")
    private String password;

    @NotBlank(message = "电话号码不能为空")
    @Pattern(regexp = REGEX_PHONE_NUMBER, message = "没有此号码")
    private String phone;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式有误")
    private String email;

    @NotBlank(message = "学号不能为空")
    private String sno;
}
