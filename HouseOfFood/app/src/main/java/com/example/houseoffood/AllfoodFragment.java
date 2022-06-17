package com.example.houseoffood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.houseoffood.Adapter.FoodAdapter;
import com.example.houseoffood.Model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllfoodFragment extends Fragment implements FoodAdapter.OnFoodItemClickListener{
    FirebaseDatabase fDatabase;
    FoodAdapter foodAdapter;
    ArrayList<Food> foods;
    RecyclerView rvTopFood;
    DatabaseReference reference;



    // TODO: Rename and change types of parameters

    public AllfoodFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foods = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_food, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvTopFood = view.findViewById(R.id.rvTopFood);
        foodAdapter = new FoodAdapter(getContext(),foods, this);
        rvTopFood.setAdapter(foodAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvTopFood.setLayoutManager(layoutManager);
        rvTopFood.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        fDatabase = FirebaseDatabase.getInstance("https://house-of-food-ec6b9-default-rtdb.asia-southeast1.firebasedatabase.app");
        reference = fDatabase.getReference();


        Query qFood = reference.child("foods");
        qFood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foods.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    foods.add(food);
                }
                foodAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onFoodItemClick(Food food) {

    }
}