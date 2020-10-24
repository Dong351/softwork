package softwork.exception.handler;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import softwork.exception.CommonException;
import softwork.utils.JsonResult;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order
public class GlobalExceptionHandler {

    private static final String ERROR_SEPARATOR = ".";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(ERROR_SEPARATOR + "\n")) + ERROR_SEPARATOR;
        return JsonResult.badRequest(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleConstraintViolationException(ConstraintViolationException e) {
        String errors = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(ERROR_SEPARATOR + "\n")) + ERROR_SEPARATOR;
        return JsonResult.badRequest(errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return JsonResult.badRequest('"' + e.getParameterName() + "\"字段不存在.");
    }

    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handlerCommonException(CommonException e) {
        return JsonResult.errorMsg(e.getMessage());
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return JsonResult.badRequest(e.getMessage());
    }


}