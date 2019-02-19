package ch.openclassrooms.enyo1.mynetapp.Controllers.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.openclassrooms.enyo1.mynetapp.Models.GithubUser;
import ch.openclassrooms.enyo1.mynetapp.Models.GithubUserInfo;
import ch.openclassrooms.enyo1.mynetapp.R;
import ch.openclassrooms.enyo1.mynetapp.Utils.GithubStreams;
import ch.openclassrooms.enyo1.mynetapp.Utils.ItemClickSupport;
import ch.openclassrooms.enyo1.mynetapp.Views.GithubUserAdapter;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * to handle interaction events.
 *
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements GithubUserAdapter.Listener {


    // FOR DESIGN
    // 1 - Declare the SwipeRefreshLayout
    @BindView(R.id.fragment_main_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.fragment_main_recycler_view)
    RecyclerView recyclerView; // 1 - Declare RecyclerView

   /* @BindView(R.id.main_fragment_progressBar)
    ProgressBar progressBar;*/

    //FOR DATA
    private Disposable disposable;
    // 2 - Declare list of users (GithubUser) & Adapter
    private List<GithubUser> githubUsers;
    private GithubUserAdapter adapter;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        this.configureRecyclerView(); // - 4 Call during UI creation
        // 2 - Calling the method that configuring click on RecyclerView
        this.configureOnClickRecyclerView();
        // 4 - Configure the SwipeRefreshLayout
        this.configureSwipeRefreshLayout();
         this.executeHttpRequestWithRetrofit(); // 5 - Execute stream after UI creation


        return view;
    }

    // 2 - Because of implementing the interface, we have to override its method

    @Override
    public void onClickDeleteButton(int position) {
        GithubUser user = adapter.getGithubUser(position);
        Toast.makeText(getContext(), "You are trying to delete user : "+user.getLogin(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    // 3 - Configure RecyclerView, Adapter, LayoutManager & glue it together
    private void configureRecyclerView() {
        // 3.1 - Reset list
        this.githubUsers = new ArrayList<>();
        // 3.2 - Create adapter passing the list of users and passing reference to callback
        this.adapter = new GithubUserAdapter(this.githubUsers, Glide.with(this), this);

        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    // 2 - Configure the SwipeRefreshLayout
    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHttpRequestWithRetrofit();
            }
        });
    }


    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    private void executeHttpRequestWithRetrofit() {
       // this.updateUIWhenStartingHTTPRequest();

        this.disposable = GithubStreams.streamFetchUserFollowing("JakeWharton").subscribeWith(new DisposableObserver<List<GithubUser>>() {
            @Override
            public void onNext(List<GithubUser> users) {
                // 6 - Update RecyclerView after getting results from Github API
                updateUI(users);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void updateUI(List<GithubUser> users) {
       // this.progressBar.setVisibility(View.GONE);

        // 3 - Stop refreshing and clear actual list of users
        swipeRefreshLayout.setRefreshing(false);
        githubUsers.clear();
        githubUsers.addAll(users);
        adapter.notifyDataSetChanged();

    }

    // -----------------
    // ACTION
    // -----------------

    // 1 - Configure item click on RecyclerView
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_main_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);

                        // 1 - Get user from adapter
                        GithubUser user = adapter.getGithubUser(position);
                        // 2 - Show result in a Toast
                        Toast.makeText(getContext(), "You clicked on user : "+user.getLogin(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    /*@OnClick(R.id.fragment_main_button)
    public void submit(View view) {
        this.executeHttpRequestWithRetrofit();

    }

    private void updateUIWhenStartingHTTPRequest() {
        // this.textView.setText("Downloading...");
        this.progressBar.setVisibility(View.VISIBLE);

    }*/






/*

    // FOR DESIGN

   // @BindView(R.id.fragment_main_text_view) TextView textView;
    @BindView(R.id.main_fragment_progressBar) ProgressBar progressBar;

    public MainFragment() { }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    // 4 - Declare Subscription
    private Disposable disposable;


    // -----------------
    // ACTIONS
    // -----------------

    @OnClick(R.id.fragment_main_button)
    public void submit(View view) {
       // this.executeHttpRequestWithRetrofit();
        this.executeSecondHttpRequestWithRetrofit();
      //  this.streamShowString();


    }

    // ------------------------------
    //  HTTP REQUEST (Retrofit Way)
    // ------------------------------

   // 1- Execute our Stream.
    private void executeHttpRequestWithRetrofit(){
        // 1.1 - Update UI
        this.updateUIWhenStartingHTTPRequest();
        // 1.2 - Execute the stream subscribing to Observer defined inside GithubStream
        this.disposable= GithubStreams.streamFetchUserFollowing("JakeWharton")
                .subscribeWith(new DisposableObserver<List<GithubUser>>() {
                    @Override
                    public void onNext(List<GithubUser> githubUsers) {
                        Log.e("TAG","On Next ");
                        // 1.3 - Update UI with list of users
                        updateUIWithListOfUsers(githubUsers);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG","On Error"+Log.getStackTraceString(e));


                    }

                    @Override
                    public void onComplete() {
                        Log.e("Tag", "On Completed !!");

                    }
                });

    }

    private void executeSecondHttpRequestWithRetrofit(){
        this.updateUIWhenStartingHTTPRequest();
        this.disposable = GithubStreams.streamFetchUserFollowingAndFetchFirstUserInfos("JakeWharton")
                .subscribeWith(new DisposableObserver<GithubUserInfo>() {
            @Override
            public void onNext(GithubUserInfo users) {
                Log.e("TAG","On Next");
                updateUIWithUserInfo(users);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));
            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");
            }
        });
    }







    // ------------------
    //  UPDATE UI
    // ------------------

    private void updateUIWhenStartingHTTPRequest(){
       // this.textView.setText("Downloading...");
        this.progressBar.setVisibility(View.VISIBLE);

    }

    private void updateUIWhenStoppingHTTPRequest(String response){
       this.progressBar.setVisibility(View.GONE);
        this.textView.setText(response);

    }


    private void updateUIWithListOfUsers(List<GithubUser> users){
        StringBuilder stringBuilder = new StringBuilder();
        for (GithubUser user : users){
            stringBuilder.append("-"+user.getLogin()+"\n\n");

        }
        updateUIWhenStoppingHTTPRequest(stringBuilder.toString());

    }

    private void updateUIWithUserInfo(GithubUserInfo userInfo){
        this.updateUIWhenStoppingHTTPRequest("The first Following of Jake Wharthon is "+userInfo.getName()+" with "+userInfo.getFollowers()+" followers.");

    }


   */
/* // +++++++++++++++++++++++++++++1
    //  REACTIVE x
    // ++++++++++++++++++++++++++++++

    // 1. Create Observable
    private Observable<String> getObservable(){
        return Observable.just("Cool !");
    }

    // 2. Create Subscriber
    private DisposableObserver<String>getSubscriber(){
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                textView.setText("Observable emits : "+s);


            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","On Error"+Log.getStackTraceString(e));


            }

            @Override
            public void onComplete() {
                Log.e("TAG","On Complete !!");


            }
        };



    }

    // 3. Create Stream and execute it.
    private void streamShowString(){
        this.disposable=this.getObservable()
                .map(getFunctionUpperCase()) // B. - Apply function
                .subscribeWith(this.getSubscriber());

    }*//*


    // 5 . Dispose subscription
    public void disposeWhenDestroy(){
        if(this.disposable!=null && !this.disposable.isDisposed())
            this.disposable.dispose();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    // A - Create function to Uppercase a string
    private Function<String, String> getFunctionUpperCase(){
        return new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s.toUpperCase();
            }
        };
    }


*/


}
