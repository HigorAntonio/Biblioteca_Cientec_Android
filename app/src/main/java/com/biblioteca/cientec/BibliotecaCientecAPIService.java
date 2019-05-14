package com.biblioteca.cientec;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BibliotecaCientecAPIService {
    public static final String BASE_URL = "http://10.0.2.2:3000/";

    @FormUrlEncoded
    @POST("auth/authenticate")
    Call<String> postAuthentication(@Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/register")
    Call<String> postRegister(@Field("name") String name,
                              @Field("email") String email,
                              @Field("password") String password);

    @GET("projects")
    Call<String> getProjects(@Header("Authorization") String authorization);
}
