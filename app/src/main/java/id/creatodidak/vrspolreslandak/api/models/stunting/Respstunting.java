package id.creatodidak.vrspolreslandak.api.models.stunting;

import com.google.gson.annotations.SerializedName;

public class Respstunting {
    @SerializedName("ops")
    private boolean ops;

    @SerializedName("msg")
    private String message;

    public boolean isOps() {
        return ops;
    }

    public String getMessage() {
        return message;
    }
}