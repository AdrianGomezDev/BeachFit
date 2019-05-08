package com.example.beachfitlogin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;


public class Goals extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FloatingActionButton newGoal;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirestoreRecyclerAdapter<GoalModel, GoalViewHolder> adapter;


    public Goals() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Goals.
     */
    // TODO: Rename and change types and number of parameters
    public static Goals newInstance(String param1, String param2) {
        Goals fragment = new Goals();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Goals");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_goals, container, false);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());

        newGoal = view.findViewById(R.id.fab);


        recyclerView = view.findViewById(R.id.goalsRecyclerView);



        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        Query query = rootRef.collection("users")
                .document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .collection("Goal Logs")
                .orderBy("date", Query.Direction.ASCENDING);

        final GoalModel goalModel = new GoalModel("","",false,0);

        FirestoreRecyclerOptions<GoalModel> options = new FirestoreRecyclerOptions.Builder<GoalModel>()
                .setQuery(query,GoalModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<GoalModel, GoalViewHolder>(options) {


            @NonNull
            @Override
            public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.goal_index, viewGroup, false);

                return new GoalViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull GoalViewHolder holder, int position, @NonNull final GoalModel goalModel) {
                final String goalName = goalModel.getName();
                holder.setGoalName(goalName);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick (View view){
                        onButtonPressed(goalName);
                    }
                });

            }
        };

        newGoal.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                DialogFragment dialogFragment = AddGoalLog.newInstance(goalModel);
                dialogFragment.show(getChildFragmentManager(), "Goal Log");
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String goalName) {
        if (mListener != null) {
            mListener.onFragmentMessage("Goals", goalName);
            //Toast.makeText(this.getActivity(), goalName, Toast.LENGTH_LONG).show();

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
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    private class GoalViewHolder extends RecyclerView.ViewHolder {
        private View view;

        GoalViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        void setGoalName(String goalName) {
            TextView textView = view.findViewById(R.id.goal_index_text_view);
            textView.setText(goalName);
        }
    }


}
