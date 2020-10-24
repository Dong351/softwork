package softwork.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static softwork.utils.Constants.REGEX_USER_LOGIN_PASSWORD;

@Data
public class UserResetPasswordDTO {
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = REGEX_USER_LOGIN_PASSWORD, message = "密码格式有误")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String code;
}