package com.bruceewu;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

/**
 * Created by mac on 2017/5/16.
 */

public interface RestApiEx {
    @GET("/mobile/index.php")
    Call<String> get(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/mobile/index.php")
    Call<String> post(@QueryMap Map<String, String> query, @FieldMap Map<String, String> params);

    @GET("/sns/oauth2/access_token")
    Call<String> wxAccessToken(@QueryMap Map<String, String> params);

    @GET("/sns/userinfo")
    Call<String> wxUserinfo(@QueryMap Map<String, String> params);

    @Multipart
    @POST("/upfile.php")
    Call<String> uploadImage(@Part("submit") String description, @Part("file\"; filename=\"image.png\"") RequestBody img);
}
