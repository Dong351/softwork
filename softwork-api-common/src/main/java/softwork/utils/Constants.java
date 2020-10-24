package softwork.utils;


/**
 * 常量
 */
public class Constants {

    /**
     * 手机号正则表达式
     */
    public static final String REGEX_PHONE_NUMBER = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    /**
     * 用户登录密码的正则表达式
     */
    public static final String REGEX_USER_LOGIN_PASSWORD = "^(?=.*[a-zA-Z0-9].*)(?=.*[a-zA-Z\\W].*)(?=.*[0-9\\W].*).{6,20}$";


    /**
     * 用户登录密码的正则表达式
     */
    public static final String REGEX_USER_LOGIN_EMAIL = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

    /**
     * 用户类型
     */
    public static  final Integer USER_NORMAL = 1;

    public static final Integer USER_STUDENT = 2;

    public static final Integer OLD_STUDENT = 3;

    public static final Integer TEACHER= 4;

    public static final Integer MULTI_MANAGER  = 8;

    public static final Integer MULTI_MEMBER = 9;

    public static final Integer USER_MANAGER = 10;
}
