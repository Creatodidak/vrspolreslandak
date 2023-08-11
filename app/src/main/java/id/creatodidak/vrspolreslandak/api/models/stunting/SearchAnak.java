package id.creatodidak.vrspolreslandak.api.models.stunting;

public class SearchAnak {
    private boolean found;
    private AnakData data;

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public AnakData getData() {
        return data;
    }

    public void setData(AnakData data) {
        this.data = data;
    }

    public static class AnakData {
        private int id;
        private String nik;
        private String nama;
        private String tanggal_lahir;
        private String jeniskelamin;
        private String nama_ibu;
        private String rt;
        private String rw;
        private String dusun;
        private String desa;
        private String kecamatan;
        private String kabupaten;
        private String provinsi;
        private String created_at;
        private String updated_at;

        // Getter and Setter methods for all the fields

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNik() {
            return nik;
        }

        public void setNik(String nik) {
            this.nik = nik;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getTanggalLahir() {
            return tanggal_lahir;
        }

        public void setTanggalLahir(String tanggal_lahir) {
            this.tanggal_lahir = tanggal_lahir;
        }

        public String getJenisKelamin() {
            return jeniskelamin;
        }

        public void setJenisKelamin(String jeniskelamin) {
            this.jeniskelamin = jeniskelamin;
        }

        public String getNamaIbu() {
            return nama_ibu;
        }

        public void setNamaIbu(String nama_ibu) {
            this.nama_ibu = nama_ibu;
        }

        public String getRt() {
            return rt;
        }

        public void setRt(String rt) {
            this.rt = rt;
        }

        public String getRw() {
            return rw;
        }

        public void setRw(String rw) {
            this.rw = rw;
        }

        public String getDusun() {
            return dusun;
        }

        public void setDusun(String dusun) {
            this.dusun = dusun;
        }

        public String getDesa() {
            return desa;
        }

        public void setDesa(String desa) {
            this.desa = desa;
        }

        public String getKecamatan() {
            return kecamatan;
        }

        public void setKecamatan(String kecamatan) {
            this.kecamatan = kecamatan;
        }

        public String getKabupaten() {
            return kabupaten;
        }

        public void setKabupaten(String kabupaten) {
            this.kabupaten = kabupaten;
        }

        public String getProvinsi() {
            return provinsi;
        }

        public void setProvinsi(String provinsi) {
            this.provinsi = provinsi;
        }

        public String getCreatedAt() {
            return created_at;
        }

        public void setCreatedAt(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdatedAt() {
            return updated_at;
        }

        public void setUpdatedAt(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
