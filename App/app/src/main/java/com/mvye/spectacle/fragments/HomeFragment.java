package com.mvye.spectacle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.mvye.spectacle.R;
import com.mvye.spectacle.adapters.ShowAdapter;
import com.mvye.spectacle.models.Show;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    Toolbar toolbar;
    SearchView searchView;
    RecyclerView recyclerViewFollowingShows;
    RecyclerView recyclerViewRecommendedShows;
    List<Show> followingShows;
    List<Show> recommendedShows;
    ShowAdapter followingShowsAdapter;
    ShowAdapter recommendedShowsAdapter;
    ImageView ivHomeProfile;

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerViews(view);
        setupToolbar(view);
        queryFollowingShows();
        queryRecommendedShows();
        ivHomeProfile = view.findViewById(R.id.ivHomeProfile);
        setCurrentProfilePicture();
    }

    private void setCurrentProfilePicture() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseFile file = (ParseFile) user.get("profilePicture");
        Glide.with(requireContext()).load(file.getUrl())
                .override(Target.SIZE_ORIGINAL)
                .into(ivHomeProfile);
    }

    private void setupRecyclerViews(View view) {
        recyclerViewFollowingShows = view.findViewById(R.id.recyclerViewFollowingShows);
        recyclerViewRecommendedShows = view.findViewById(R.id.recyclerViewRecommendedShows);
        followingShows = new ArrayList<>();
        recommendedShows = new ArrayList<>();
        ShowAdapter.OnPosterImageClickListener onFollowingPosterImageClickListener = new ShowAdapter.OnPosterImageClickListener() {
            @Override
            public void OnPosterImageClickListener(int postion) {
                openShowDetailsFragment(followingShows.get(postion));
            }
        };
        ShowAdapter.OnPosterImageClickListener onRecommendedPosterImageClickListener = new ShowAdapter.OnPosterImageClickListener() {
            @Override
            public void OnPosterImageClickListener(int postion) {
                openShowDetailsFragment(recommendedShows.get(postion));
            }
        };
        followingShowsAdapter = new ShowAdapter(getContext(), followingShows, onFollowingPosterImageClickListener);
        recommendedShowsAdapter = new ShowAdapter(getContext(), recommendedShows, onRecommendedPosterImageClickListener);
        recyclerViewFollowingShows.setAdapter(followingShowsAdapter);
        recyclerViewRecommendedShows.setAdapter(recommendedShowsAdapter);
        recyclerViewFollowingShows.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerViewRecommendedShows.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
    }

    private void openShowDetailsFragment(Show show) {
        ShowDetailsFragment showDetailsFragment = ShowDetailsFragment.newInstance(show);
        getParentFragmentManager().beginTransaction().replace(R.id.frameLayoutContainer, showDetailsFragment).addToBackStack("").commit();
    }

    private void setupToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        searchView = (SearchView) toolbar.getMenu().findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO: Do something searchy
                return false;
            }
        });
    }

    private void queryFollowingShows() {
        ParseQuery<ParseObject> query = ParseUser.getCurrentUser().getRelation("following").getQuery();
        query.include("Show");
        List<Show> shows = new ArrayList<>();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> showList, ParseException e) {
                for (ParseObject show : showList) {
                    shows.add((Show) show);
                }
                Log.i("HomeFragment", "Size is: " + shows.size());
                followingShowsAdapter.clear();
                followingShowsAdapter.addAll(shows);
            }
        });
    }

    private void queryRecommendedShows() {
        ParseQuery<Show> query = ParseQuery.getQuery(Show.class);
        query.findInBackground(new FindCallback<Show>() {
            @Override
            public void done(List<Show> showList, ParseException e) {
                recommendedShowsAdapter.clear();
                recommendedShowsAdapter.addAll(showList);
            }
        });
    }
}