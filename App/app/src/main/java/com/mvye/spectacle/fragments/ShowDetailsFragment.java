package com.mvye.spectacle.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.mvye.spectacle.R;
import com.mvye.spectacle.models.Show;

import java.util.Objects;

public class ShowDetailsFragment extends Fragment {

    ImageView imageViewPosterImage;
    TextView textViewShowName;
    TextView textViewShowDescription;
    TextView textViewScore;
    Button buttonFollow;
    RecyclerView recyclerViewEpisodes;
    Show show;

    public ShowDetailsFragment() {}

    public static ShowDetailsFragment newInstance(Show show) {
        ShowDetailsFragment showDetailsFragment = new ShowDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("show", show);
        showDetailsFragment.setArguments(args);
        return showDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        show = getArguments().getParcelable("show");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setupVariables(view);
        setPosterImageAndName();
        // TODO: Call API to populate details
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupVariables(View view) {
        imageViewPosterImage = view.findViewById(R.id.imageViewPosterImage);
        textViewShowName = view.findViewById(R.id.textViewShowName);
        textViewShowDescription = view.findViewById(R.id.textViewShowDescription);
        textViewScore = view.findViewById(R.id.textViewScore);
        buttonFollow = view.findViewById(R.id.buttonFollow);
        recyclerViewEpisodes = view.findViewById(R.id.recyclerViewEpisodes);
    }

    private void setPosterImageAndName() {
        Glide.with(requireContext()).load(show.getPosterImage().getUrl())
                .override(Target.SIZE_ORIGINAL)
                .into(imageViewPosterImage);
        textViewShowName.setText(show.getShowName());
    }
}