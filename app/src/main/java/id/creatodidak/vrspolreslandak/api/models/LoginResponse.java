package id.creatodidak.vrspolreslandak.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginResponse {

    @SerializedName("status")
    private boolean status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("logindetail")
    private List<LoginDetail> loginDetails;

    // Konstruktor, Getter, dan Setter

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

    public List<LoginDetail> getLoginDetails() {
        return loginDetails;
    }

    public void setLoginDetails(List<LoginDetail> loginDetails) {
        this.loginDetails = loginDetails;
    }

    public static class LoginDetail {
        @SerializedName("nrp")
        private String nrp;

        @SerializedName("nama")
        private String nama;

        @SerializedName("pangkat")
        private String pangkat;

        @SerializedName("satker")
        private String satker;

        @SerializedName("satfung")
        private String satfung;

        @SerializedName("jabatan")
        private String jabatan;

        @SerializedName("tanggal_lahir")
        private String tanggalLahir;

        @SerializedName("foto")
        private String foto;

        @SerializedName("wa")
        private String wa;

        @SerializedName("wilayah")
        private String wilayah;

        public String getNrp() {
            return nrp;
        }

        public void setNrp(String nrp) {
            this.nrp = nrp;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getPangkat() {
            return pangkat;
        }

        public void setPangkat(String pangkat) {
            this.pangkat = pangkat;
        }

        public String getSatker() {
            return satker;
        }

        public void setSatker(String satker) {
            this.satker = satker;
        }

        public String getSatfung() {
            return satfung;
        }

        public void setSatfung(String satfung) {
            this.satfung = satfung;
        }

        public String getJabatan() {
            return jabatan;
        }

        public void setJabatan(String jabatan) {
            this.jabatan = jabatan;
        }

        public String getTanggalLahir() {
            return tanggalLahir;
        }

        public void setTanggalLahir(String tanggalLahir) {
            this.tanggalLahir = tanggalLahir;
        }

        public String getFoto() {
            return foto;
        }

        public void setFoto(String foto) {
            this.foto = foto;
        }

        public String getWa() {
            return wa;
        }

        public void setWa(String wa) {
            this.wa = wa;
        }

        public String getWilayah() {
            return wilayah;
        }

        public void setWilayah(String wilayah) {
            this.wilayah = wilayah;
        }
    }
}
