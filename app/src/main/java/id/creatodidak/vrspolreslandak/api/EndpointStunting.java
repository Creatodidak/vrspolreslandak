package id.creatodidak.vrspolreslandak.api;

import java.util.List;

import id.creatodidak.vrspolreslandak.api.models.stunting.AmbilToken;
import id.creatodidak.vrspolreslandak.api.models.stunting.DataKms;
import id.creatodidak.vrspolreslandak.api.models.stunting.Dokumentasi;
import id.creatodidak.vrspolreslandak.api.models.stunting.HapusItem;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListDes;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListDus;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListKab;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListKec;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListProv;
import id.creatodidak.vrspolreslandak.api.models.stunting.ModelItem;
import id.creatodidak.vrspolreslandak.api.models.stunting.Respstunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.RingkasanStunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.SearchByNIK;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface EndpointStunting {

    @GET("stunting/ringkasan")
    Call<RingkasanStunting> ringkasanStunting();

    @GET("basic/prov")
    Call<List<ListProv>> listProv();

    @FormUrlEncoded
    @POST("basic/kab")
    Call<List<ListKab>> listKab(
            @Field("prov") int id
    );

    @FormUrlEncoded
    @POST("basic/kec")
    Call<List<ListKec>> listKec(
            @Field("kab") int id
    );

    @FormUrlEncoded
    @POST("basic/desa")
    Call<List<ListDes>> listDes(
            @Field("kec") int id
    );

    @FormUrlEncoded
    @POST("basic/dusun")
    Call<List<ListDus>> listDus(
            @Field("des") long id
    );

    @FormUrlEncoded
    @POST("anak/register")
    Call<Respstunting> sendRegistrasiData(
            @Field("nik") String dnik,
            @Field("nama") String dnama,
            @Field("tanggallahir") String dtanggallahir,
            @Field("jeniskelamin") String djeniskelamin,
            @Field("namaibu") String dnamaibu,
            @Field("rt") String drt,
            @Field("rw") String drw,
            @Field("dusun") String ddusun,
            @Field("desa") String ddesa,
            @Field("kecamatan") String dkecamatan,
            @Field("kabupaten") String dkabupaten,
            @Field("provinsi") String dprovinsi,
            @Field("bb") String dbb,
            @Field("tb") String dtb,
            @Field("lk") String dlk,
            @Field("satker") String satker,
            @Field("nikibu") String dnikibu);

    @FormUrlEncoded
    @POST("anak/search")
    Call<SearchByNIK> searchAnak(
            @Field("nik") String nik
    );

    @FormUrlEncoded
    @POST("ibu/search")
    Call<SearchByNIK> searchIbu(
            @Field("nik") String nik
    );

    @FormUrlEncoded
    @POST("anak/tambahdatakembanganak")
    Call<Respstunting> sendKembangAnak(
            @Field("nik") String nik,
            @Field("bb") String bb,
            @Field("tb") String tb,
            @Field("lk") String lk,
            @Field("satker") String satker
    );

    @FormUrlEncoded
    @POST("ibu/register")
    Call<Respstunting> sendRegistrasiDataIbu(
            @Field("nama") String nama,
            @Field("nik") String nik,
            @Field("tanggallahir") String tanggallahir,
            @Field("pekerjaan") String pekerjaan,
            @Field("rt") String rt,
            @Field("rw") String rw,
            @Field("bb") String bb,
            @Field("lp") String lp,
            @Field("usia") String usia,
            @Field("denyut") String denyut,
            @Field("sistolik") String sistolik,
            @Field("diastolik") String diastolik,
            @Field("prov") String prov,
            @Field("kab") String kab,
            @Field("kec") String kec,
            @Field("des") String des,
            @Field("dus") String dus,
            @Field("valcb1") String valcb1,
            @Field("valcb2") String valcb2,
            @Field("valcb3") String valcb3,
            @Field("valcb4") String valcb4,
            @Field("satker") String satker
    );

    @FormUrlEncoded
    @POST("ibu/tambahdatakembangibu")
    Call<Respstunting> sendDataKembangIbu(
            @Field("nik") String nik,
            @Field("bb") String bb,
            @Field("lp") String lp,
            @Field("usia") String usia,
            @Field("denyut") String denyut,
            @Field("sistolik") String sistolik,
            @Field("diastolik") String diastolik,
            @Field("valcb1") String valcb1,
            @Field("valcb2") String valcb2,
            @Field("valcb3") String valcb3,
            @Field("valcb4") String valcb4,
            @Field("satker") String satker
    );

    @FormUrlEncoded
    @POST("ibumenyusui/register")
    Call<Respstunting> sendRegistrasiDataibumenyusui(
            @Field("nama") String nama,
            @Field("nik") String nik,
            @Field("tanggallahir") String tanggallahir,
            @Field("pekerjaan") String pekerjaan,
            @Field("rt") String rt,
            @Field("rw") String rw,
            @Field("bb") String bb,
            @Field("asi") String asi,
            @Field("sistolik") String sistolik,
            @Field("diastolik") String diastolik,
            @Field("prov") String prov,
            @Field("kab") String kab,
            @Field("kec") String kec,
            @Field("des") String des,
            @Field("dus") String dus,
            @Field("valcb1") String valcb1,
            @Field("valcb2") String valcb2,
            @Field("valcb3") String valcb3,
            @Field("valcb4") String valcb4,
            @Field("satker") String satker
    );

    @FormUrlEncoded
    @POST("ibumenyusui/tambahdatakembangibumenyusui")
    Call<Respstunting> sendDataKembangibumenyusui(
            @Field("nik") String nik,
            @Field("bb") String bb,
            @Field("asi") String asi,
            @Field("sistolik") String sistolik,
            @Field("diastolik") String diastolik,
            @Field("valcb1") String valcb1,
            @Field("valcb2") String valcb2,
            @Field("valcb3") String valcb3,
            @Field("valcb4") String valcb4,
            @Field("satker") String satker
    );

    @FormUrlEncoded
    @POST("item/adddata")
    Call<HapusItem> sendItem(
            @Field("data") String data,
            @Field("satker") String satker,
            @Field("type") String type);

    @FormUrlEncoded
    @POST("item/getdata")
    Call<ModelItem> getItem(
            @Field("satker") String satker,
            @Field("type") String type);

    @FormUrlEncoded
    @POST("item/deldata")
    Call<HapusItem> delItem(
            @Field("satker") String satker,
            @Field("type") String type,
            @Field("id") int id);

    @FormUrlEncoded
    @POST("kms/getdata")
    Call<DataKms> getKms(
            @Field("nik") String nik
    );

    @GET("report/wa/{token}")
    Call<ResponseBody> getLaporanWa(
            @Path("token") String token
    );

    @FormUrlEncoded
    @POST("report/stunting/token/list")
    Call<AmbilToken> getToken(
            @Field("nama") String nama
    );

    @Multipart
    @POST("report/adddokumentasi")
    Call<HapusItem> uploadDokumentasi(
            @Part("satker") String satker,
            @Part("jenis") String jenis,
            @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("report/getdokumentasi")
    Call<Dokumentasi> getDokumentasi(
            @Field("token") String token,
            @Field("satker") String satker
    );

    @FormUrlEncoded
    @POST("report/getdokumentasi")
    Call<Dokumentasi> getAllDokumentasi(
            @Field("token") String token
    );
}
