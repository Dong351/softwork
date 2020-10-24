package softwork.exception;

public class TokenAuthFailedException extends RuntimeException {

    public TokenAuthFailedException() {
        this("Token验证失败");
    }

    public TokenAuthFailedException(String message) {
        super(message);
    }

    public TokenAuthFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
