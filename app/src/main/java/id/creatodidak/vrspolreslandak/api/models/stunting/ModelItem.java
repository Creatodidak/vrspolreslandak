package id.creatodidak.vrspolreslandak.api.models.stunting;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelItem {
    @SerializedName("ada")
    private boolean ada;

    @SerializedName("data")
    private List<Item> itemList;

    public boolean isAda() {
        return ada;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public static class Item {
        @SerializedName("id")
        private int id;

        @SerializedName("nama")
        private String nama;

        @SerializedName("satker")
        private String satker;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        public int getId() {
            return id;
        }

        public String getNama() {
            return nama;
        }

        public String getSatker() {
            return satker;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}
