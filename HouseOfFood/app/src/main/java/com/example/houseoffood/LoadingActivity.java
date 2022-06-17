package com.example.houseoffood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                nextActivity();
            }
        }, 2000);
    }
    private void nextActivity(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null){
            //Chua log in
            startActivity(new Intent(this,LoginActivity.class));
        }
        else {
            //Da log in
            startActivity(new Intent(this,MainActivity.class));
        }
        finish();
    }
}