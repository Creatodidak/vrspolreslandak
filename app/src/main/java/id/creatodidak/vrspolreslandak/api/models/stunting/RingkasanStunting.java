package id.creatodidak.vrspolreslandak.api.models.stunting;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RingkasanStunting {
    @SerializedName("jgiziburuk")
    private int jumGiziBuruk;

    @SerializedName("tgiziburuk")
    private int totalGiziBuruk;

    @SerializedName("jumlahibut")
    private int jumlahIbuT;

    @SerializedName("jumlahibuy")
    private int jumlahIbuY;

    @SerializedName("totalibu")
    private int totalIbu;

    @SerializedName("totalanak")
    private int totalAnak;

    @SerializedName("jumlahibumenyusuit")
    private int jumlahibumenyusuiT;

    @SerializedName("jumlahibumenyusuiy")
    private int jumlahibumenyusuiY;

    @SerializedName("totalibumenyusui")
    private int totalibumenyusui;

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

    @SerializedName("totalbingkisan")
    private int totalBingkisan;

    @SerializedName("dataposyandu")
    private List<Dataposyandu> dataposyanduList;

    public int getJumGiziBuruk() {
        return jumGiziBuruk;
    }

    public int getTotalGiziBuruk() {
        return totalGiziBuruk;
    }

    public int getJumIbuToday() {
        return jumlahIbuT;
    }

    public int getJumIbuYesterday() {
        return jumlahIbuY;
    }

    public int getTotIbu() {
        return totalIbu;
    }

    public int getTotAnak() {
        return totalAnak;
    }

    public int getJumibumenyusuiToday() {
        return jumlahibumenyusuiT;
    }

    public int getJumibumenyusuiYesterday() {
        return jumlahibumenyusuiY;
    }

    public int getTotibumenyusui() {
        return totalibumenyusui;
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

    public int getTotalBingkisan() {
        return totalBingkisan;
    }

    public TotalGizi getTotalGizi() {
        return totalGizi;
    }

    public List<Dataposyandu> getDataposyanduList() {
        return dataposyanduList;
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

    public class Dataposyandu {

        @SerializedName("giat")
        private  boolean giat;
        @SerializedName("satker")
        private String satker;

        @SerializedName("jumanak")
        private int jumanak;

        @SerializedName("jumibuhamil")
        private int jumibuhamil;

        @SerializedName("jumibumenyusui")
        private int jumibumenyusui;

        @SerializedName("jumvitamin")
        private int jumvitamin;

        @SerializedName("jummakanan")
        private int jummakanan;

        @SerializedName("jumbingkisan")
        private int jumbingkisan;

        @SerializedName("daftarmakanan")
        private List<Daftarmakanan> daftarmakananList;

        @SerializedName("daftarbingkisan")
        private List<Daftarbingkisan> daftarbingkisanList;

        public boolean isAda(){
            return giat;
        }
        public String getSatker() {
            return satker;
        }

        public int getJumanak() {
            return jumanak;
        }

        public int getJumibuhamil() {
            return jumibuhamil;
        }

        public int getJumibumenyusui() {
            return jumibumenyusui;
        }

        public int getJumvitamin() {
            return jumvitamin;
        }

        public int getJummakanan() {
            return jummakanan;
        }

        public int getJumbingkisan() {
            return jumbingkisan;
        }

        public List<Daftarmakanan> getDaftarmakananList() {
            return daftarmakananList;
        }

        public List<Daftarbingkisan> getDaftarbingkisanList() {
            return daftarbingkisanList;
        }
    }

    public class Daftarmakanan {
        @SerializedName("nama")
        private String nama;

        public String getNama() {
            return nama;
        }
    }

    public class Daftarbingkisan {
        @SerializedName("nama")
        private String nama;

        public String getNama() {
            return nama;
        }
    }
}
