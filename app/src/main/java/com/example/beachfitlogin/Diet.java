package com.example.beachfitlogin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.beachfitlogin.Adapters.DailyDietLogAdapter;
import com.example.beachfitlogin.Adapters.FoodAdapter;
import com.example.beachfitlogin.Models.DailyDietLogModel;
import com.example.beachfitlogin.Models.FoodModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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

import static com.example.beachfitlogin.Util.capitalizeString;
import static com.example.beachfitlogin.Util.hideKeyboardFrom;
import static com.example.beachfitlogin.Util.isEmpty;
import static com.example.beachfitlogin.Util.myDoubleParser;

public class Diet extends Fragment implements DailyDietLogAdapter.OnDailyDietLogClickListener{

    private static final String INSTANT_SEARCH_URL = "https://trackapi.nutritionix.com/v2/search/instant?";
    private static final String NUTRITION_INFO_URL = "https://trackapi.nutritionix.com/v2/natural/nutrients";
//    private static final String NUTRITIONIX_APP_ID = "31017349";
//    private static final String NUTRITIONIX_API_KEY = "24958b5962bf0cf6010622eefb73504a";
    private static final String NUTRITIONIX_APP_ID = "81c57972";
    private static final String NUTRITIONIX_API_KEY = "ed2c50519010c1b21a2207e7559890e1";

    private EditText searchBarView;
    private ProgressBar progressBar;
    private View clickCatcherView;
    private CardView suggestionsCardView;
    private RecyclerView suggestionsRecycler;
    private CardView nutrientsCardView;
    private LinearLayout nutrientsView;
    private TextView nutrientsText;
    private TextView nutrientsTitleText;
    private ImageView nutrientsImage;
    private Button addToFoodLog;
    private Button backToDietLog;
    private DailyDietLogAdapter dailyDietLogAdapter;

    public Diet() { /* Required empty public constructor */ }

    public static Diet newInstance() { return new Diet(); }

    //*********************************** Lifecycle Methods **************************************//

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Diet"); // Set fragment title
        View layout = inflater.inflate(R.layout.fragment_diet, container, false); // Inflate main layout

        searchBarView = layout.findViewById(R.id.foodSearchBar);
        ImageButton searchButton = layout.findViewById(R.id.foodSearchButton);
        progressBar = layout.findViewById(R.id.searchProgressBar);
        suggestionsCardView = layout.findViewById(R.id.suggestionsCardView);
        suggestionsRecycler = layout.findViewById(R.id.searchSuggestionsRecyclerView);
        nutrientsCardView = layout.findViewById(R.id.nutrientsCardView);
        nutrientsView = layout.findViewById(R.id.nutrientsLayout);
        nutrientsText = layout.findViewById(R.id.nutrientInfoView);
        nutrientsTitleText = layout.findViewById(R.id.foodTitleView);
        nutrientsImage = layout.findViewById(R.id.foodImageNutrientView);
        addToFoodLog = layout.findViewById(R.id.addToMyFoodLogButton);
        backToDietLog = layout.findViewById(R.id.backToDietLogsButton);
        clickCatcherView = layout.findViewById(R.id.clickCatcherView);

        // suggestionsRecycler displays the search results for the food that the user entered
        suggestionsRecycler.setHasFixedSize(true);
        suggestionsRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        suggestionsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        // dietLogRecycler displays the daily food logs that are currently in the database
        RecyclerView dietLogRecycler = layout.findViewById(R.id.dietLogRecyclerView);
        dietLogRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Remove the suggestions view if user clicks outside of search suggestions area
        clickCatcherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nutrientsCardView.setVisibility(View.GONE);
                suggestionsCardView.setVisibility(View.GONE);
                clickCatcherView.setVisibility(View.GONE);
            }
        });

        // TODO: Updates search results every time user enters a letter
        // TODO: Uncomment to enable
//        searchBarView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                new RetrieveSearchSuggestionsTask().execute();
//            }
//        });

        // Fetch search results upon click of the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(searchBarView)) {
                    nutrientsCardView.setVisibility(View.GONE);
                    clickCatcherView.setVisibility(View.VISIBLE);
                    hideKeyboardFrom(Objects.requireNonNull(getContext()), searchBarView);
                    new RetrieveSearchSuggestionsTask().execute();
                }
            }
        });

        // User may also fetch search results by pressing the done button in soft keyboard
        searchBarView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(!isEmpty(searchBarView) && actionId == EditorInfo.IME_ACTION_DONE){
                    hideKeyboardFrom(Objects.requireNonNull(getContext()), searchBarView);
                    nutrientsCardView.setVisibility(View.GONE);
                    new RetrieveSearchSuggestionsTask().execute();
                    return true;
                }
                return false;
            }
        });

        // Prepare query to fetch diet logs
        Query query = FirebaseFirestore.getInstance().collection("users")
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .collection("Diet Logs")
                .orderBy("date", Query.Direction.DESCENDING);

        // Use the query above to prepare the options for FireStore adapter
        FirestoreRecyclerOptions<DailyDietLogModel> options = new FirestoreRecyclerOptions.Builder<DailyDietLogModel>()
                .setQuery(query, DailyDietLogModel.class)
                .build();

        dailyDietLogAdapter = new DailyDietLogAdapter(options, this);

        dietLogRecycler.setAdapter(dailyDietLogAdapter);

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        dailyDietLogAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        dailyDietLogAdapter.stopListening();
    }

    @Override
    public void onDailyDietLogClick(int position) {
        // Hide suggestions if user clicks outside of suggestions scroll view
        suggestionsCardView.setVisibility(View.GONE);
        nutrientsCardView.setVisibility(View.GONE);
    }

    //**************************************** API Calls *****************************************//

    @SuppressLint("StaticFieldLeak")
    private class RetrieveNutritionInfoTask extends AsyncTask<Object, Void, String> {
        protected  void onPreExecute() {
            suggestionsCardView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Object... params) {
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

            // Load hi-res image if one exists, otherwise load the thumbnail
            try {

                JSONObject jo = new JSONObject(response).getJSONArray("foods").getJSONObject(0);
                Uri foodImage = Uri.parse(jo.getJSONObject("photo").getString("highres"));
                final FoodModel foodModel = jsonToFoodModel(jo);

                if(foodImage.equals(Uri.parse("null"))){
                    foodImage = Uri.parse(jo.getJSONObject("photo").getString("thumb"));
                }

                Picasso.get().load(foodImage).into(nutrientsImage);
                nutrientsTitleText.setText(capitalizeString(foodModel.getFoodName()));
                nutrientsText.setText(foodModel.toString());

                addToFoodLog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogFragment dialogFragment = AddFoodLog.newInstance(foodModel);
                        dialogFragment.show(getChildFragmentManager(), "Food Log");
                    }
                });

                backToDietLog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nutrientsCardView.setVisibility(View.GONE);
                    }
                });
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
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
            suggestionsCardView.setVisibility(View.GONE);
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
            suggestionsCardView.setVisibility(View.VISIBLE);

            try {
                foodList.clear();
                List<String> tagIdList = new ArrayList<>();
                JSONArray array = new JSONObject(response).getJSONArray("common");
                for(int i = 0; i < array.length(); i++){
                    JSONObject jo = array.getJSONObject(i);
                    if(!tagIdList.contains(jo.getString("tag_id"))){
                        tagIdList.add(jo.getString("tag_id"));
                        FoodModel foodModel = new FoodModel(
                                capitalizeString(jo.getString("food_name")),
                                Uri.parse(jo.getJSONObject("photo").getString("thumb"))
                        );
                        foodList.add(foodModel);
                    }
                }
                suggestionsRecycler.setAdapter(new FoodAdapter(foodList, this));
                suggestionsCardView.scrollTo(0,0);
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage(), e);
                e.printStackTrace();
            }
        }

        @Override
        public void onFoodClick(int position) {
            suggestionsCardView.setVisibility(View.GONE);
            nutrientsCardView.setVisibility(View.VISIBLE);
            String foodName = foodList.get(position).getFoodName();
            String foodImage = foodList.get(position).getPhotoThumb().toString();
            new RetrieveNutritionInfoTask().execute(foodName, foodImage);
        }
    }

    //************************************** Helper Methods **************************************//

    // Creates a food model from Nutritionix /natural/nutrients endpoint API response
    private FoodModel jsonToFoodModel(JSONObject jo) throws JSONException{
        return new FoodModel(
                capitalizeString(jo.getString("food_name")),
                Uri.parse(jo.getJSONObject("photo").getString("thumb")),
                myDoubleParser(jo.getString("serving_qty")),
                jo.getString("serving_unit"),
                myDoubleParser(jo.getString("serving_weight_grams")),
                myDoubleParser(jo.getString("nf_calories")),
                myDoubleParser(jo.getString("nf_total_fat")),
                myDoubleParser(jo.getString("nf_saturated_fat")),
                myDoubleParser(jo.getString("nf_cholesterol")),
                myDoubleParser(jo.getString("nf_sodium")),
                myDoubleParser(jo.getString("nf_total_carbohydrate")),
                myDoubleParser(jo.getString("nf_dietary_fiber")),
                myDoubleParser(jo.getString("nf_sugars")),
                myDoubleParser(jo.getString("nf_protein"))
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