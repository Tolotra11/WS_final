package cloud.model;

import java.util.HashMap;

public class Error {
    public int code ;
    public String message;
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public HashMap<String,Object> getError(String e){
            this.setCode(500);
            this.setMessage(e);
            HashMap<String,Object> errmap = new HashMap<>();
            errmap.put("error",this);
            return errmap;
    }
}
