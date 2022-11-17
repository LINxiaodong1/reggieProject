package riggie.common;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class R<T> {
    private Integer code;
    private T Data;
    private String msg;
    private Map map=new HashMap<>();

    public static <T>R<T> success(T data){
        R<T> r=new R<T>();
        r.code=1;
        r.Data=data;
        return r;
    }
    public static <T> R<T> error(String msg){
        R<T> r=new R<T>();
        r.code=0;
        r.msg=msg;
        return r;
    }
    public R<T> add(String key,Object value){
        this.map.put(key,value);

        return this;
    }

}
