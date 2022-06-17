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
import androidx.recyclerview.widget.RecyclerView;

import com.example.houseoffood.MainActivity;
import com.example.houseoffood.Model.Food;
import com.example.houseoffood.R;
import com.example.houseoffood.Model.Food;
import com.example.houseoffood.ShowDetailsActivity;
import com.example.houseoffood.TopFoodFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {



    public interface OnFoodItemClickListener {
        void onFoodItemClick(Food food);
    }

    public class ViewHolderFood extends RecyclerView.ViewHolder {
        TextView tvName, tvRate, tvPrice;
        ImageView ivImage;

        public ViewHolderFood(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

    Context context;
    private List<Food> foods;
    private List<Food> foodsOld;
    private OnFoodItemClickListener foodItemClickListener;


    public FoodAdapter(Context context,ArrayList<Food> foods, OnFoodItemClickListener foodItemClickListener) {
        this.foods = foods;
        this.foodsOld = foods;
        this.context = context;
        this.foodItemClickListener = foodItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_top_food, parent, false);
        return new ViewHolderFood(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Food food = foods.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        ViewHolderFood viewHolderFood = (ViewHolderFood) holder;
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
                foodItemClickListener.onFoodItemClick(food);
                Intent intent = new Intent(context, ShowDetailsActivity.class);
                intent.putExtra("details",foods.get(position));
                context.startActivity(intent );
            }
        });


    }

    @Override
    public int getItemCount() {
        return foods.size();
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

