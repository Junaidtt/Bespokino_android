package com.app.bespokino.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.bespokino.R;
import com.app.bespokino.adapter.HowItWorksAdapter;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class How_it_worksFragment extends Fragment {

    // API キー
    private static final String API_KEY = "AIzaSyD_OtiVlTAvEUpXFyxVm0YFC8ayXulOAUM";

   // fXSkM28erD0
    private static String VIDEO_ID = "fXSkM28erD0";
    // private static String VIDEO_ID = "FIQNyJY-m3k";



    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String[] dataArray;
    //{String.valueOf(R.string.app_name),"Item 2","Item 3","Item 4","Item 5","Item 6","Item 7","Item 8","Item 9","Item 10","Item 11","Item 12","Item 13","Item 14","Item 15","Item 16","Item 17","Item 18","Item 19","Item 20","Item 21","Item 22","Item 23","Item 24"};


    public How_it_worksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_how_it_works, container, false);



        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();


        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();


        youTubePlayerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {


            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    player.loadVideo(VIDEO_ID);
                    player.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                String errorMessage = youTubeInitializationResult.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);
            }


        });
        dataArray = getActivity().getResources().getStringArray(R.array.howitwroks);
        recyclerView = (RecyclerView)view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HowItWorksAdapter(dataArray);
        recyclerView.setAdapter(adapter);

        return  view;
    }

}
