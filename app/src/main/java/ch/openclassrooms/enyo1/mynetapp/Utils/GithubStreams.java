package ch.openclassrooms.enyo1.mynetapp.Utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ch.openclassrooms.enyo1.mynetapp.Models.GithubUser;
import ch.openclassrooms.enyo1.mynetapp.Models.GithubUserInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * This class to list all Streams relative to Github, that will return all an Observable, so that
 * our Fragment/Activity will subscribe on.
 */
public class GithubStreams {

    /**
     *
     * @param username,
     *       the given username.
     * @return Observable, on that our fragment/activity would subscribe on.
     */

    public static Observable<List<GithubUser>> streamFetchUserFollowing(String username){
        GithubService githubService=GithubService.retrofit.create(GithubService.class);
        return githubService.getFollowing(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);

    }

    // 1 - Create a stream that will get user infos on Github API
    public static Observable<GithubUserInfo> streamFetchUserInfo(String username){
        GithubService gitHubService = GithubService.retrofit.create(GithubService.class);
        return gitHubService.getUserInfos(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    // 2 - Create a stream that will :
    //     A. Fetch all users followed by "username"
    //     B. Return the first user of the list
    //     C. Fetch details of the first user
    public static Observable<GithubUserInfo> streamFetchUserFollowingAndFetchFirstUserInfos(String username){
        return streamFetchUserFollowing(username) // A.
                .map(new Function<List<GithubUser>, GithubUser>() {
                    @Override
                    public GithubUser apply(List<GithubUser> users) throws Exception {
                        return users.get(0); // B.
                    }
                })
                .flatMap(new Function<GithubUser, Observable<GithubUserInfo>>() {
                    @Override
                    public Observable<GithubUserInfo> apply(GithubUser user) throws Exception {
                        // C.
                        return streamFetchUserInfo(user.getLogin());
                    }
                });
    }


}
