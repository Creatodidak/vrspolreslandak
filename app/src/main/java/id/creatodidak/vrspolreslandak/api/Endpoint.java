package id.creatodidak.vrspolreslandak.api;

import id.creatodidak.vrspolreslandak.api.models.LoginResponse;
import id.creatodidak.vrspolreslandak.api.models.ResponseChecker;
import id.creatodidak.vrspolreslandak.api.models.ServerResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Endpoint {

    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginResponse> login(
            @Field("nrp") String nrp,
            @Field("pass") String pass
    );


    @FormUrlEncoded
    @POST("cekhotspot")
    Call<ResponseChecker> cekhotspot(
            @Field("wilayah") String wilayah
    );

    Call<ServerResponse> updKampanyeKarhutla(String nrp, String koord, String namatarget, String lokasikampanye, MultipartBody.Part image);

    @FormUrlEncoded
    @POST("auth/tokenfcm")
    Call<ServerResponse> savetoken(
            @Field("nrp") String nrp,
            @Field("wilayah") String wilayah,
            @Field("token") String token);
}
