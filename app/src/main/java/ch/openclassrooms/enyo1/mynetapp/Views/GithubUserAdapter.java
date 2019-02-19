package ch.openclassrooms.enyo1.mynetapp.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;

import java.util.List;

import ch.openclassrooms.enyo1.mynetapp.Models.GithubUser;
import ch.openclassrooms.enyo1.mynetapp.R;

public class GithubUserAdapter extends RecyclerView.Adapter<GithubUserViewHolder> {

    // Create an interface for call back.
    public interface Listener{
        void onClickDeleteButton(int position);
    }

    // 2 - Declaring callback
    private final Listener callback;


    // FOR DATA
    private List<GithubUser> githubUsers;
    // 1 - Declaring a Glide object
    private RequestManager glide;


    // CONSTRUCTOR
    // 3 - Passing an instance of callback through constructor
    public GithubUserAdapter(List<GithubUser> githubUsers, RequestManager glide,Listener callback) {
        this.githubUsers = githubUsers;
        this.glide=glide;
        this.callback=callback;
    }

    @Override
    public GithubUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_main_item, parent, false);

        return new GithubUserViewHolder(view);
    }

    // UPDATE VIEW HOLDER WITH A GITHUBUSER
    // 4 - Passing an instance of callback through each ViewHolder
    @Override
    public void onBindViewHolder(GithubUserViewHolder viewHolder, int position) {
        viewHolder.updateWithGithubUser(this.githubUsers.get(position),this.glide,this.callback);
    }

    // RETURN THE TOTAL COUNT OF ITEMS IN THE LIST
    @Override
    public int getItemCount() {
        return this.githubUsers.size();
    }


    public GithubUser getGithubUser(int position){
        return this.githubUsers.get(position);

    }




}
