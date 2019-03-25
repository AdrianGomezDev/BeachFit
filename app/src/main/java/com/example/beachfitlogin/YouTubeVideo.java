package com.example.beachfitlogin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YouTubeVideo extends YouTubePlayerSupportFragment {
    private static final String ARG_VIDEO_ID = "VideoID";
    private static final String TAG = "YouTubeVideo";

    // TODO: Remove this hardcoded API key later
    private static final String API_KEY = "AIzaSyBGNEH01L6J5cthzDW3L44VDBDaaznlKfk";

    private OnFragmentInteractionListener mListener;

    private YouTubePlayer activePlayer;

    public YouTubeVideo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param videoID Video id needed to play the youtube video
     * @return A new instance of fragment YouTubeVideo.
     */
    public static YouTubeVideo newInstance(String videoID) {
        YouTubeVideo fragment = new YouTubeVideo();
        Bundle args = new Bundle();
        args.putString(ARG_VIDEO_ID, videoID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                    Log.d(TAG, "onInitializationFailure: ");
                }

                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                    activePlayer = player;
                    activePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    if (!wasRestored) {
                        activePlayer.loadVideo(getArguments().getString(ARG_VIDEO_ID));
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
