package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("msg")
    private String msg;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
