package com.example.houseoffood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.SearchView;
import android.widget.SearchView;


import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.houseoffood.Adapter.FoodAdapter;
import com.example.houseoffood.Adapter.SearchAdapter;
import com.example.houseoffood.Model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchAdapter.OnFoodSearchItemClickListener {

    SearchAdapter searchAdapter;
    ArrayList<Food> foods;
    RecyclerView rvTopFood;
    SearchView searchView;

    FirebaseDatabase fDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rvTopFood = findViewById(R.id.rvTopFood);
        searchView = findViewById(R.id.search);


        rvTopFood.setHasFixedSize(true);
        rvTopFood.setLayoutManager(new LinearLayoutManager(this));

        foods = new ArrayList<>();
        searchAdapter = new SearchAdapter(this,foods,this);
        rvTopFood.setAdapter(searchAdapter);


        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchAdapter.getFilter().filter(query);
                return false;
            }
        });

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
                searchAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void onFoodSearchItemClick(Food food) {

    }
}