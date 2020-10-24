package softwork.pojo.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static softwork.utils.Constants.REGEX_PHONE_NUMBER;

@Data
public class UserUpdateDTO {
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式有误")
    private String email;

    @NotBlank(message = "电话号码不能为空")
    @Pattern(regexp = REGEX_PHONE_NUMBER, message = "没有此号码")
    private String phone;

    private String sno;
}
