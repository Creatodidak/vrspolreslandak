package id.creatodidak.vrspolreslandak.api.models.stunting;

public class SearchByNIK {
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

    }
}
