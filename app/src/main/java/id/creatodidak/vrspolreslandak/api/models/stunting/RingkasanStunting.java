package id.creatodidak.vrspolreslandak.api.models.stunting;

import com.google.gson.annotations.SerializedName;

public class RingkasanStunting {
    @SerializedName("jgiziburuk")
    private int jumGiziBuruk;

    @SerializedName("tgiziburuk")
    private int totalGiziBuruk;

    @SerializedName("jgizikurang")
    private int jumGiziKurang;

    @SerializedName("tgizikurang")
    private int totalGiziKurang;

    @SerializedName("jgizibaik")
    private int jumGiziBaik;

    @SerializedName("tgizibaik")
    private int totalGiziBaik;

    @SerializedName("jgizirlebih")
    private int jumGiziLebih;

    @SerializedName("tgizirlebih")
    private int totalGiziLebih;

    @SerializedName("jgizilebih")
    private int jumGiziLebihLebih;

    @SerializedName("tgizilebih")
    private int totalGiziLebihLebih;

    @SerializedName("jobesitas")
    private int jumObesitas;

    @SerializedName("tobesitas")
    private int totalObesitas;

    @SerializedName("total")
    private TotalGizi totalGizi;

    public int getJumGiziBuruk() {
        return jumGiziBuruk;
    }

    public int getTotalGiziBuruk() {
        return totalGiziBuruk;
    }

    public int getJumGiziKurang() {
        return jumGiziKurang;
    }

    public int getTotalGiziKurang() {
        return totalGiziKurang;
    }

    public int getJumGiziBaik() {
        return jumGiziBaik;
    }

    public int getTotalGiziBaik() {
        return totalGiziBaik;
    }

    public int getJumGiziLebih() {
        return jumGiziLebih;
    }

    public int getTotalGiziLebih() {
        return totalGiziLebih;
    }

    public int getJumGiziLebihLebih() {
        return jumGiziLebihLebih;
    }

    public int getTotalGiziLebihLebih() {
        return totalGiziLebihLebih;
    }

    public int getJumObesitas() {
        return jumObesitas;
    }

    public int getTotalObesitas() {
        return totalObesitas;
    }

    public TotalGizi getTotalGizi() {
        return totalGizi;
    }

    public class TotalGizi {
        @SerializedName("GIZI BURUK")
        private int giziBuruk;

        @SerializedName("GIZI KURANG")
        private int giziKurang;

        @SerializedName("GIZI BAIK")
        private int giziBaik;

        @SerializedName("RESIKO GIZI LEBIH")
        private int resikoGiziLebih;

        @SerializedName("GIZI LEBIH")
        private int giziLebih;

        @SerializedName("OBESITAS")
        private int obesitas;

        public int getGiziBuruk() {
            return giziBuruk;
        }

        public int getGiziKurang() {
            return giziKurang;
        }

        public int getGiziBaik() {
            return giziBaik;
        }

        public int getResikoGiziLebih() {
            return resikoGiziLebih;
        }

        public int getGiziLebih() {
            return giziLebih;
        }

        public int getObesitas() {
            return obesitas;
        }
    }
}
