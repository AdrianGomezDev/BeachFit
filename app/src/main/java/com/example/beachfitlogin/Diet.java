package com.example.beachfitlogin;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Diet extends Fragment {

    private static final String INSTANT_SEARCH_URL = "https://trackapi.nutritionix.com/v2/search/instant?";
    private static final String NUTRITION_INFO_URL = "https://trackapi.nutritionix.com/v2/natural/nutrients";
//    private static final String NUTRITIONIX_APP_ID = "31017349";
//    private static final String NUTRITIONIX_API_KEY = "24958b5962bf0cf6010622eefb73504a";
    private static final String NUTRITIONIX_APP_ID = "81c57972";
    private static final String NUTRITIONIX_API_KEY = "ed2c50519010c1b21a2207e7559890e1";

    private EditText searchBarView;
    private ProgressBar progressBar;
    private NestedScrollView suggestionsScrollView;
    private RecyclerView suggestionsRecycler;

    private ConstraintLayout nutrientsView;
    private TextView nutrientsText;
    private TextView nutrientsTitleText;
    private ImageView nutrientsImage;
    private Button addToFoodLog;

    private OnFragmentInteractionListener mListener;

    public Diet() {
        // Required empty public constructor
    }

    public static Diet newInstance() {
        return new Diet();
    }

    //*********************************** Lifecycle Methods **************************************//

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Diet");

        View layout = inflater.inflate(R.layout.fragment_diet, container, false);
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());

        // Foods search bar related views
        searchBarView = layout.findViewById(R.id.foodSearchBar);
        searchBarView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                //TODO: Uncomment to update search suggestions after every letter inputted from user.
                //TODO: This will waste a lot of api calls. We only get 1000 calls on the free Nutritionix developer plan.
                //new RetrieveSearchSuggestionsTask().execute();
            }
        });
        ImageButton searchButton = layout.findViewById(R.id.foodSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBarView.onEditorAction(EditorInfo.IME_ACTION_DONE);
                new RetrieveSearchSuggestionsTask().execute();
            }
        });
        progressBar = layout.findViewById(R.id.searchProgressBar);
        suggestionsScrollView = layout.findViewById(R.id.suggestionsScroll);
        suggestionsRecycler = layout.findViewById(R.id.searchSuggestionsRecyclerView);
        suggestionsRecycler.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(suggestionsRecycler.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.line_divider)));
        suggestionsRecycler.addItemDecoration(dividerItemDecoration);
        suggestionsRecycler.setLayoutManager(layoutManager);

        // Nutrient related views
        addToFoodLog = layout.findViewById(R.id.addToMyFoodLogButton);
        nutrientsView = layout.findViewById(R.id.nutrientsLayout);
        nutrientsText = layout.findViewById(R.id.nutrientInfoView);
        nutrientsTitleText = layout.findViewById(R.id.foodTitleView);
        nutrientsImage = layout.findViewById(R.id.foodImageNutrientView);

        return layout; // Inflate the layout for this fragment
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //**************************************** API Calls *****************************************//

    private class RetrieveNutritionInfoTask extends AsyncTask<Object, Void, String> {

        private String foodName;
        private Uri foodImage;

        protected  void onPreExecute() {
            suggestionsScrollView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Object... params) {
            foodName = (String) params[0];
            foodImage = Uri.parse((String)params[1]);
            try {
                URL url = new URL(NUTRITION_INFO_URL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("x-app-id", NUTRITIONIX_APP_ID);
                urlConnection.setRequestProperty("x-app-key", NUTRITIONIX_API_KEY);
                urlConnection.setRequestProperty("x-remote-user-id", "0");
                String query =  "{\"query\": \"" + params[0] + "\"}";
                byte[] outputInBytes = query.getBytes(StandardCharsets.UTF_8);
                OutputStream os = urlConnection.getOutputStream();
                os.write(outputInBytes);
                os.close();
                return readURLConnectionBuffer(urlConnection);
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "An error has occurred.";
                Log.d("DEBUG", "onPostExecute: response = null");
            }

            progressBar.setVisibility(View.GONE);
            nutrientsView.setVisibility(View.VISIBLE);

            try {
                JSONObject jo = new JSONObject(response).getJSONArray("foods").getJSONObject(0);
                Uri foodImage = Uri.parse(jo.getJSONObject("photo").getString("highres"));
                if(foodImage.equals(Uri.parse("null"))){
                    foodImage = Uri.parse(jo.getJSONObject("photo").getString("thumb"));
                }
                Picasso.get().load(foodImage).into(nutrientsImage);

                final FoodModel foodModel = jsonToFoodModel(jo);

                nutrientsTitleText.setText(capitalizeString(foodModel.getFoodName()));
                nutrientsText.setText(foodModel.toString());

                addToFoodLog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //TODO: Open dialog, get servings and date of eating and write to FireStore for that user
                        DialogFragment dialogFragment = AddFoodLog.newInstance(foodModel);
                        dialogFragment.show(getChildFragmentManager(), "Food Log");
                    }
                });
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    private class RetrieveSearchSuggestionsTask extends AsyncTask<Void, Void, String> implements FoodAdapter.OnFoodClickListener {

        private String query;
        private List<FoodModel> foodList = new ArrayList<>();

        protected void onPreExecute() {
            if(searchBarView.getText().toString().isEmpty()){
                query = "nothing";
            }
            else{
                query = searchBarView.getText().toString();
            }
            nutrientsView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            suggestionsScrollView.setVisibility(View.GONE);
        }

        protected String doInBackground(Void... urls) {
            try {
                URL url = new URL(INSTANT_SEARCH_URL + "query=" + query);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("x-app-id", NUTRITIONIX_APP_ID);
                urlConnection.setRequestProperty("x-app-key", NUTRITIONIX_API_KEY);
                return readURLConnectionBuffer(urlConnection);
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR!";
                Log.d("Error", "onPostExecute: response = null");
            }

            progressBar.setVisibility(View.GONE);
            suggestionsScrollView.setVisibility(View.VISIBLE);

            try {
                foodList.clear();
                List<String> tagNameList = new ArrayList<>();
                JSONArray array = new JSONObject(response).getJSONArray("common");
                for(int i = 0; i < array.length(); i++){
                    JSONObject jo = array.getJSONObject(i);
                    if(!tagNameList.contains(jo.getString("tag_name"))){
                        tagNameList.add(jo.getString("tag_name"));
                        FoodModel foodModel = new FoodModel(
                                capitalizeString(jo.getString("tag_name")),
                                Uri.parse(jo.getJSONObject("photo").getString("thumb"))
                        );
                        foodList.add(foodModel);
                    }
                }
                suggestionsRecycler.setAdapter(new FoodAdapter(foodList, this));
                suggestionsScrollView.scrollTo(0,0);
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage(), e);
                e.printStackTrace();
            }
        }

        @Override
        public void onFoodClick(int position) {
            searchBarView.onEditorAction(EditorInfo.IME_ACTION_DONE);
            String foodName = foodList.get(position).getFoodName();
            String foodImage = foodList.get(position).getPhotoThumb().toString();
            new RetrieveNutritionInfoTask().execute(foodName, foodImage);
        }
    }

    //************************************** Helper Methods **************************************//

    // Capitalize first letters of all words in a string
    private String capitalizeString(String str){
        String words[]=str.split("\\s");
        StringBuilder capitalizedString= new StringBuilder();
        for(String w:words){
            String first = w.substring(0,1);
            String afterFirst = w.substring(1);
            capitalizedString.append(first.toUpperCase()).append(afterFirst).append(" ");
        }
        return capitalizedString.toString().trim();
    }

    // Creates a food model from Nutritionix /natural/nutrients endpoint API response
    private FoodModel jsonToFoodModel(JSONObject jo) throws JSONException{
        return new FoodModel(
                capitalizeString(jo.getString("food_name")),
                Uri.parse(jo.getJSONObject("photo").getString("thumb")),
                Double.parseDouble(jo.getString("serving_qty")),
                jo.getString("serving_unit"),
                Double.parseDouble(jo.getString("serving_weight_grams")),
                Double.parseDouble(jo.getString("nf_calories")),
                Double.parseDouble(jo.getString("nf_total_fat")),
                Double.parseDouble(jo.getString("nf_saturated_fat")),
                Double.parseDouble(jo.getString("nf_cholesterol")),
                Double.parseDouble(jo.getString("nf_sodium")),
                Double.parseDouble(jo.getString("nf_total_carbohydrate")),
                Double.parseDouble(jo.getString("nf_dietary_fiber")),
                Double.parseDouble(jo.getString("nf_sugars")),
                Double.parseDouble(jo.getString("nf_protein"))
        );
    }

    // Reads the response from the api call from a buffer and returns response as a string
    private String readURLConnectionBuffer(HttpURLConnection urlConnection) throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                response.append(line).append("\n");
            }
            bufferedReader.close();
            return response.toString();
        } finally{
            urlConnection.disconnect();
        }
    }
}