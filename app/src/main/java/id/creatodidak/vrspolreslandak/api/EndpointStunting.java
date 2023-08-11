package id.creatodidak.vrspolreslandak.api;

import java.util.List;

import id.creatodidak.vrspolreslandak.api.models.stunting.ListDes;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListDus;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListKab;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListKec;
import id.creatodidak.vrspolreslandak.api.models.stunting.ListProv;
import id.creatodidak.vrspolreslandak.api.models.stunting.Respstunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.RingkasanStunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.SearchAnak;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EndpointStunting {

    @GET("anak/ringkasan")
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
            @Field("lk") String dlk
    );

    @FormUrlEncoded
    @POST("anak/search")
    Call<SearchAnak> searchAnak(
            @Field("nik") String nik
    );

    @FormUrlEncoded
    @POST("anak/tambahdatakembanganak")
    Call<Respstunting> sendKembangAnak(
            @Field("nik") String nik,
            @Field("bb") String bb,
            @Field("tb") String tb,
            @Field("lk") String lk
    );
}
