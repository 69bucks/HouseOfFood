package com.example.houseoffood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.houseoffood.Model.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class ShowDetailsActivity extends AppCompatActivity {
     Button btn_addtocart;
     TextView tv_name, tv_price, tv_description, tv_numberorder;
     ImageView btn_plus, btn_minus, imgV_food;
     int numberOrder = 1, totalPrice = 0;
    Food food = null;

    FirebaseDatabase fDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        initUI();
        listener();

        final Object object = getIntent().getSerializableExtra("details");
        if (object instanceof Food){
            food = (Food) object;
        }

        if (food != null){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference profileRef = storageReference.child("foods/"+ food.getImage());
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(imgV_food);
                }
            });
            //Glide.with(getApplicationContext()).load(food.getImage()).into(imgV_food);
            tv_description.setText(food.getDes());
            tv_name.setText(food.getName());
            tv_price.setText(food.getPrice()+"VND");
            totalPrice = food.getPrice() * numberOrder;
        }




    }

    public void listener(){
        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOrder > 1){
                    numberOrder--;
                    tv_numberorder.setText(String.valueOf(numberOrder));
                    totalPrice = food.getPrice() * numberOrder;
                }
            }
        });
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberOrder < 10){
                    numberOrder++;
                    tv_numberorder.setText(String.valueOf(numberOrder));
                    totalPrice = food.getPrice() * numberOrder;
                }
            }
        });

        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });
    }

    private void addToCart() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productName",food.getName());
        cartMap.put("productPrice",food.getPrice());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTIme",saveCurrentTime);
        cartMap.put("totalQuantity",numberOrder);
        cartMap.put("totalPrice",totalPrice);

            firestore.collection("currentUser").document(auth.getCurrentUser().getEmail())
                .collection("AcdToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(ShowDetailsActivity.this, "Add to cart successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


    }


    public void initUI(){
        btn_addtocart = findViewById(R.id.btn_addtocart);
        tv_name = findViewById(R.id.tv_tittle);
        tv_price = findViewById(R.id.tv_price);
        tv_description = findViewById(R.id.tv_description);
        tv_numberorder = findViewById(R.id.tv_ordernumber);
        btn_minus = findViewById(R.id.imgV_remove);
        btn_plus = findViewById(R.id.imgV_add);
        imgV_food = findViewById(R.id.imgV_food);

    }
}