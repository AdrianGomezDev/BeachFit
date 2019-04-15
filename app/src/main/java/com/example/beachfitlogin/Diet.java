package com.example.beachfitlogin;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Diet extends Fragment implements FoodAdapter.OnFoodClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String INSTANT_SEARCH_URL = "https://trackapi.nutritionix.com/v2/search/instant?";
    private static final String NUTRITION_INFO_URL = "https://trackapi.nutritionix.com/v2/natural/nutrients?";
//    private static final String NUTRITIONIX_APP_ID = "31017349";
//    private static final String NUTRITIONIX_API_KEY = "24958b5962bf0cf6010622eefb73504a";
    private static final String NUTRITIONIX_APP_ID = "81c57972";
    private static final String NUTRITIONIX_API_KEY = "ed2c50519010c1b21a2207e7559890e1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText searchBarView;
    private ProgressBar progressBar;
    private NestedScrollView scrollView;
    private RecyclerView suggestionsRecycler;

    private FoodAdapter foodAdapter;
    private List<FoodModel> foodList;
    private OnFragmentInteractionListener mListener;

    public Diet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Diet.
     */
    // TODO: Rename and change types and number of parameters
    public static Diet newInstance(String param1, String param2) {
        Diet fragment = new Diet();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Diet");

        View layout = inflater.inflate(R.layout.fragment_diet, container, false);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());

        scrollView = layout.findViewById(R.id.suggestionsScroll);

        searchBarView = layout.findViewById(R.id.foodSearchBar);
        searchBarView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                new RetrieveSearchSuggestionsTask().execute();
            }
        });

        progressBar = layout.findViewById(R.id.searchProgressBar);

        suggestionsRecycler = layout.findViewById(R.id.searchSuggestionsRecyclerView);
        suggestionsRecycler.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(suggestionsRecycler.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.line_divider)));
        suggestionsRecycler.addItemDecoration(dividerItemDecoration);
        suggestionsRecycler.setLayoutManager(layoutManager);

        foodList = new ArrayList<>();

        ImageButton searchButton = layout.findViewById(R.id.foodSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveSearchSuggestionsTask().execute();
            }
        });

        return layout; // Inflate the layout for this fragment
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentMessage("Diet", uri);
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
    public void onFoodClick(int position) {
        String foodName = foodList.get(position).getFoodName();
        new RetrieveNutritionInfoTask().execute(foodName);
    }

    //TODO: incomplete
    private class RetrieveNutritionInfoTask extends AsyncTask<String, Void, String> {

        protected  void onPreExecute() {
            suggestionsRecycler.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(NUTRITION_INFO_URL + "query=" + params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("x-app-id", NUTRITIONIX_APP_ID);
                urlConnection.setRequestProperty("x-app-key", NUTRITIONIX_API_KEY);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {

        }
    }


    private class RetrieveSearchSuggestionsTask extends AsyncTask<Void, Void, String> {

        private String query;

        protected void onPreExecute() {
            if(searchBarView.getText().toString().isEmpty()){
                query = "nothing";
            }
            else{
                query = searchBarView.getText().toString();
            }
            progressBar.setVisibility(View.VISIBLE);
            suggestionsRecycler.setVisibility(View.GONE);
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(INSTANT_SEARCH_URL + "query=" + query);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("x-app-id", NUTRITIONIX_APP_ID);
                urlConnection.setRequestProperty("x-app-key", NUTRITIONIX_API_KEY);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR!";
                Log.d("Error", "onPostExecute: response = null");
            }
            progressBar.setVisibility(View.GONE);
            suggestionsRecycler.setVisibility(View.VISIBLE);
            Log.i("INFO", response);

            try {
                foodList.clear();
                List<String> tagNameList = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("common");
                for(int i = 0; i < array.length(); i++){
                    JSONObject jo = array.getJSONObject(i);
                    if(tagNameList.isEmpty()){
                        tagNameList.add(jo.getString("tag_name"));
                        FoodModel foodModel = new FoodModel(jo.getString("food_name"),
                                Uri.parse(jo.getJSONObject("photo").getString("thumb")));
                        foodList.add(foodModel);
                    }
                    else if(!tagNameList.contains(jo.getString("tag_name"))){
                        tagNameList.add(jo.getString("tag_name"));
                        FoodModel foodModel = new FoodModel(jo.getString("food_name"),
                                Uri.parse(jo.getJSONObject("photo").getString("thumb")));
                        foodList.add(foodModel);
                    }
                }
                foodAdapter = new FoodAdapter(foodList);
                suggestionsRecycler.setAdapter(foodAdapter);
                scrollView.scrollTo(0,0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}