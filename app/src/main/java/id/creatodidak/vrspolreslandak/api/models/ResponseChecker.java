package id.creatodidak.vrspolreslandak.api.models;

import java.util.List;

public class ResponseChecker {
    private boolean status;
    private List<DataKarhutla> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<DataKarhutla> getData() {
        return data;
    }

    public void setData(List<DataKarhutla> data) {
        this.data = data;
    }
}

