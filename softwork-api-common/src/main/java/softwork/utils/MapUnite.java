package softwork.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述:
 * 实体类与Map转换
 *
 * @author songxinyong
 * @ClassName MapUnite
 * @date 2019-11-22 10:59
 */
public class MapUnite {
    /**
     * 实体类转Map
     * @param object 实体对象
     * @return
     */
    public static Map getMap(Object object){
        Map map = new HashMap();
        if (object == null) {
            return map;
        }
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(object));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
          * Map转成实体对象
          * @param map map实体对象包含属性
          * @param clazz 实体对象类型
          * @return
          */
    public static Object getEntity(Map<String, Object> map, Class<?> clazz) {
         if (map == null) {
            return null;
         }
         Object obj = null;
         try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
        return obj;
    }
}
