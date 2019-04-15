package com.example.beachfitlogin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView foodNameTextView;
        ImageView foodPhotoThumb;

        ViewHolder(View itemView) {
            super(itemView);

            foodNameTextView = itemView.findViewById(R.id.food_text_view);
            foodPhotoThumb = itemView.findViewById(R.id.food_image);
        }
    }

    private List<FoodModel> mFoods;

    FoodAdapter(List<FoodModel> foods) {
        mFoods = foods;
    }

    @NonNull
    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.food_item, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdapter.ViewHolder viewHolder, int position) {
        FoodModel food = mFoods.get(position);

        TextView textView = viewHolder.foodNameTextView;
        textView.setText(food.getFoodName());

        ImageView imageView = viewHolder.foodPhotoThumb;
        Picasso.get().load(food.getPhotoThumb()).into(imageView);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mFoods.size();
    }

    public interface OnFoodClickListener{
        void onFoodClick(int position);
    }
}
