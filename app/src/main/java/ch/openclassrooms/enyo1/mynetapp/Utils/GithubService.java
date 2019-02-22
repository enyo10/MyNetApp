package ch.openclassrooms.enyo1.mynetapp.Utils;

import java.util.List;


import ch.openclassrooms.enyo1.mynetapp.Models.GithubUser;
import ch.openclassrooms.enyo1.mynetapp.Models.GithubUserInfo;
import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubService {

    @GET("users/{username}/following")
    Observable<List<GithubUser>> getFollowing(@Path("username") String username);

    @GET("/users/{username}")
    Observable<GithubUserInfo> getUserInfos(@Path("username") String username);



    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();




}
