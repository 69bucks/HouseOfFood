package com.example.houseoffood.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houseoffood.Model.Food;
import com.example.houseoffood.R;
import com.example.houseoffood.ShowDetailsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.FoodViewHolder> implements Filterable {

    public interface OnFoodSearchItemClickListener {
        void onFoodSearchItemClick(Food food);
    }

    Context context;
    private List<Food> foods;
    private List<Food> foodsOld;
    private OnFoodSearchItemClickListener foodSearchItemClickListener;


    public SearchAdapter(Context context,List<Food> foods,OnFoodSearchItemClickListener foodSearchItemClickListener) {
        this.context = context;
        this.foods = foods;
        this.foodsOld = foods;
        this.foodSearchItemClickListener = foodSearchItemClickListener;
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder{

        TextView tvName, tvRate, tvPrice;
        ImageView ivImage;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }



    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_top_food,parent,false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder,@SuppressLint("RecyclerView") int position) {
        Food food = foods.get(position);
        if (food==null){
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        SearchAdapter.FoodViewHolder viewHolderFood = (SearchAdapter.FoodViewHolder) holder;
        StorageReference profileRef = storageReference.child("foods/"+ food.getImage());
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(viewHolderFood.ivImage);
            }
        });
        viewHolderFood.tvName.setText(food.getName());
        viewHolderFood.tvRate.setText("Rate: ".concat(String.valueOf(food.getRate())));
        viewHolderFood.tvPrice.setText(food.getPrice()+"");
        viewHolderFood.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodSearchItemClickListener.onFoodSearchItemClick(food);
                Intent intent = new Intent(context, ShowDetailsActivity.class);
                intent.putExtra("details",foods.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (foods!=null){
            return foods.size();
        }
        return 0;
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()){
                    foods = foodsOld;
                }
                else {
                    List<Food> list = new ArrayList<>();
                    for (Food food : foodsOld){
                        if (food.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(food);
                        }
                    }

                    foods = list;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = foods;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                foods = (List<Food>) results.values;
                notifyDataSetChanged();

            }
        };
    }
}
