package com.biblioteca.cientec;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface BibliotecaCientecAPIService {
    public static final String BASE_URL = "http://10.0.2.2:3000/"; // Executa com API no servidor local
    //public static final String BASE_URL = "https://bbt-cientec-api.herokuapp.com"; // Executa com API no servidor remoto

    @FormUrlEncoded
    @POST("auth/register")
    Call<String> postRegister(@Field("name") String name,
                              @Field("email") String email,
                              @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/authenticate")
    Call<String> postAuthentication(@Field("email") String email,
                                    @Field("password") String password);

    @GET("projects")
    Call<String> getProjects(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("authors")
    Call<String> postNewAuthor(@Header("Authorization") String authorization,
                               @Field("name") String name,
                               @Field("about") String about);

    @GET("authors")
    Call<String> getAuthors(@Header("Authorization") String authorization);

    @GET("authors/{id}")
    Call<String> getAuthor(@Header("Authorization") String authorization, @Path("id") String id);

    @FormUrlEncoded
    @POST("genres")
    Call<String> postNewGenre(@Header("Authorization") String authorization,
                               @Field("name") String name);

    @GET("genres")
    Call<String> getGenres(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("bookGenre/multi")
    Call<String> postBookGenre(@Header("Authorization") String authorization,
                               @Field("bookId") int bookId,
                               @Field("genresId") int[] genresId);

    @Multipart
    @POST("/books")
    Call<String> postNewBook(@Header("Authorization") String authorization,
                             @Part MultipartBody.Part cover,
                             @Part("isbn") String isbn,
                             @Part("title") String title,
                             @Part("edition") String edition,
                             @Part("publisher") String publisher,
                             @Part("authorId") int authorId,
                             @Part("originalTitle") String originalTitle,
                             @Part("description") String description,
                             @Part("numberOfPages") String numberOfPages,
                             @Part("language") String language);

    @GET("books")
    Call<String> getBooks(@Header("Authorization") String authorization);

    @GET("reviews/user/{bookId}")
    Call<String> getMyReview(@Header("Authorization") String authorization, @Path("bookId") int bookId);
}
