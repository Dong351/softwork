package softwork.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static softwork.utils.Constants.REGEX_USER_LOGIN_PASSWORD;

@Data
public class UserModifyPasswordDTO {
    @NotBlank(message = "电话号码不能为空")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = REGEX_USER_LOGIN_PASSWORD, message = "密码格式有误")
    private String password;

    @NotBlank(message = "修改密码不能为空")
    private String prePassword;
}
