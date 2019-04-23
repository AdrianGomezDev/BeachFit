package com.example.beachfitlogin;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StepCounter extends Fragment implements SensorEventListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TRACKER_COUNT = "trackerCount";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private float trackCount;

    //////////////////////////////////

    private SensorManager mSensorManager;
    private Sensor mSensorStep;
    private TextView mCount;
    boolean activityRunning;
    private Button mResetButton;
    private float stepCounter;
    private float tracker;
    //private static final String BUNDLE_TRACKER = "BUNDLE_TRACKER";
    //private ImageView runImage;

    //////////////////////////////////

    private OnFragmentInteractionListener mListener;

    public StepCounter() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Analytics.
     */
    // TODO: Rename and change types and number of parameters
    public static StepCounter newInstance(String param1, String param2) {
        StepCounter fragment = new StepCounter();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        //args.putFloat(TRACKER_COUNT,trackCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if(savedInstanceState != null){
        //    tracker = savedInstanceState.getFloat(TRACKER_COUNT);
        //}
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        tracker = sp.getFloat(TRACKER_COUNT,0.0f);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            //trackCount = getArguments().getFloat(TRACKER_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //if(savedInstanceState != null){
        //    tracker = savedInstanceState.getFloat(TRACKER_COUNT);
        //}

        getActivity().setTitle("Step Counter");
        // try to lock the fragment to portrait mode.
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_counter, container, false);

        mCount = view.findViewById(R.id.count);
        //runImage = getActivity().findViewById(R.id.stickrun);
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mResetButton = view.findViewById(R.id.reset);
        mResetButton.setOnClickListener(this);


        return view;
    }

//    public void onSaveInstanceState(Bundle outState){
//        super.onSaveInstanceState(outState);
//        outState.putFloat(TRACKER_COUNT,tracker);
//    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentMessage("Step Counter", uri);
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

    @Override
    public void onResume(){
        super.onResume();
        activityRunning = true;
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            mSensorManager.registerListener(this,countSensor,mSensorManager.SENSOR_DELAY_UI);
        }
        else{
            Toast.makeText(this.getActivity(),"Count sensor not available!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        activityRunning = false;
    }

    @Override
    public void onStop(){
        super.onStop();
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(TRACKER_COUNT,tracker);
        editor.commit();
        // Unregister all sensor listeners in this callback so they don't
        // continue to use resources when the app is paused.
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(activityRunning){
            stepCounter = event.values[0] - tracker;
            //mCount.setText(String.valueOf(Math.round(event.values[0])));
            mCount.setText(String.valueOf(Math.round(stepCounter)));

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.reset) {
            tracker = tracker + stepCounter;
            mCount.setText(String.valueOf(0));
        }

    }
}
