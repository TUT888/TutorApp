package com.example.tutorapp.api;

import com.example.tutorapp.MainActivity;
import com.example.tutorapp.model.ClassObject;
import com.example.tutorapp.model.Post;
import com.example.tutorapp.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APIService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create();
    APIService apiService = new Retrofit.Builder()
            .baseUrl(MainActivity.URL +"/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);

    @GET("post/get_search_post.php")
    Call<ResultObjectAPI> getSearchPost(@Query("key") String search, @Query("user_id") String user_id, @Query("role") int role);

    @GET("user/get_user.php")
    Call<ResultObjectAPI> getUser(@Query("phoneNumber") String id);

    @GET("class/get_classes_from_tutor.php")
    Call<ResultAPI> getClassesFromTutor(@Query("tutorPhone") String tutorPhone);

    @GET("class/get_class_rated_from_tutor.php")
    Call<ResultAPI> getClassRatedFromTutor(@Query("tutorPhone") String tutorPhone);

    @GET("class/get_class_by_id.php")
    Call<ResultObjectAPI> getClassById(@Query("classID") String classID);

    @GET("class/get_active_classes_from_tutor.php")
    Call<ResultAPI> getActiveClassFromTutor(@Query("tutorPhone") String tutorPhone);

    @Headers({"Content-Type: application/json"})
    @PUT("class/update_status.php")
    Call<ResultStringAPI> updateStatus(@Body Map<String,String> body);

    @GET("rate/get_rating_by_classID.php")
    Call<ResultObjectAPI> getRatingByClassID(@Query("classID") String classID);

    @FormUrlEncoded
    @POST("user/login_tutor.php")
    Call<ResultObjectAPI> userLogin(@Field("phoneNumber") String phoneNumber,
                                    @Field("password") String password);

    @GET("student/get_search_student.php")
    Call<ResultAPI> getSearchStudent(@Query("key") String search);

    @Headers({"Content-Type: application/json"})
    @POST("post/add_new_post.php")
    Call<ResultStringAPI> addNewPost(@Body Post newPost);

    @Headers({"Content-Type: application/json"})
    @POST("post/update_post.php")
    Call<ResultStringAPI> updatePost(@Body Post updatePost);

    @Headers({"Content-Type: application/json"})
    @POST("class/add_new_class.php")
    Call<ResultStringAPI> addNewClass(@Body ClassObject newClass);

    @GET("post/get_my_posts.php")
    Call<ResultAPI> getMyPosts(@Query("phoneNumber") String phoneNumber);

    @GET("class/get_pending_classes_from_tutor.php")
    Call<ResultAPI> getPendingClassFromTutor(@Query("tutorPhone") String tutorPhone);

    @GET("post/remove_my_post.php")
    Call<ResultStringAPI> removeMyPost(@Query("postID") String postID);

    @FormUrlEncoded
    @POST("user/change_password.php")
    Call<ResultStringAPI> changePassword(@Field("phoneNumber") String phoneNumber,
                                         @Field("newPassword") String newPassword);

    @Headers({"Content-Type: application/json"})
    @POST("user/tutor_register.php")
    Call<ResultStringAPI> register(@Body User newUser);

    @Headers({"Content-Type: application/json"})
    @POST("post/update_hideFrom.php")
    Call<ResultStringAPI> updateHideFrom(@Body Map<String,String> body);
}