package id.creatodidak.vrspolreslandak.api.models.stunting;

import com.google.gson.annotations.SerializedName;

public class HapusItem {
    @SerializedName("success")
    private boolean success;

    @SerializedName("msg")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
