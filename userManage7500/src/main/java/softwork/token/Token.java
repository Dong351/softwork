package softwork.token;




import java.lang.annotation.*;

/**
 * 可以注解controller方法参数中
 * <p>
 * Supported types are:
 * <ul>
 * <li>{@code {@link User } 获得token对应的user实例</li>
 * <li>{@code {@link Integer }}获得token对应User的uid</li>
 * </ul>
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Token {
    String value() default "Authorization";

    boolean required() default true;
}
