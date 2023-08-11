package id.creatodidak.vrspolreslandak.api.models.stunting;

public class ListDus {
    private long id;
    private int prov;
    private int kab;
    private int kec;
    private long des;
    private String nama;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getProv() {
        return prov;
    }

    public void setProv(int prov) {
        this.prov = prov;
    }

    public int getKab() {
        return kab;
    }

    public void setKab(int kab) {
        this.kab = kab;
    }

    public int getKec() {
        return kec;
    }

    public void setKec(int kec) {
        this.kec = kec;
    }

    public long getDes() {
        return des;
    }

    public void setDes(long des) {
        this.des = des;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
