package softwork.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.util.List;

public class JsonResult {
    /**
     * 定义jackson对象
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 响应业务状态
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应中的数据
     */
    @JsonInclude(Include.NON_NULL)
    private Object data;

    private static JsonResult build(Integer status, String msg, Object data) {
        return new JsonResult(status, msg, data);
    }

    public static JsonResult ok(Object data) {
        return new JsonResult(data);
    }

    public static JsonResult ok(Object data, String message) {
        return new JsonResult(0,message,data);
    }

    public static JsonResult ok() {
        return new JsonResult(null);
    }

    public static JsonResult errorMsg(String msg) {
        return new JsonResult(1001, msg, null);
    }

    public static JsonResult errorObject(int status, String msg, Object data) {
        return new JsonResult(status, msg, data);
    }

    public static JsonResult errorMsg(int status, String msg) {
        return new JsonResult(status, msg, null);
    }
    public static JsonResult errorMap(Object data) {
        return new JsonResult(1002, "error", data);
    }

    public static JsonResult badRequest(String data) {
        return new JsonResult(HttpStatus.BAD_REQUEST.value(), data, null);
    }

    public static JsonResult notFound(String msg) {
        return new JsonResult(HttpStatus.NOT_FOUND.value(), msg, null);
    }

    public static JsonResult errorForbidden(String msg) {
        return new JsonResult(HttpStatus.FORBIDDEN.value(), msg, null);
    }

    public static JsonResult errorException(String msg) {
        return new JsonResult(500, msg, null);
    }

    private JsonResult(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    private JsonResult(Object data) {
        this.status = 0;
        this.message = "成功";
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * @param jsonData
     * @param clazz
     * @return {@link JsonResult}实例
     * @description 将json结果集转化为JSONResult对象
     * 需要转换的对象是一个类
     */
    public static JsonResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, JsonResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isObject()) {
                obj = MAPPER.readValue(data.traverse(), clazz);
            } else if (data.isTextual()) {
                obj = MAPPER.readValue(data.asText(), clazz);
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("message").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param json
     * @return {@link JsonResult}实例
     * @Description: 没有object对象的转化
     */
    public static JsonResult format(String json) {
        try {
            return MAPPER.readValue(json, JsonResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param jsonData
     * @param clazz
     * @return {@link JsonResult}实例
     * @Description: Object是集合转化
     * 需要转换的对象是一个list
     */
    public static JsonResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("message").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return JsonUtil.objectToJson(this);
    }
}
