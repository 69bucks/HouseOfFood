package com.example.houseoffood;

import static com.example.houseoffood.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.houseoffood.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private MainActivity mainActivity;

    ImageView ivImage;
    EditText edtName, edtEmail, edtMobile, edtAddress;
    Button btnUpdate, btnUpdateEmail;
    Uri mUri;
    ProgressDialog progressDialog;

    FirebaseDatabase fDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;


    View mView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);


        initUI();
        mainActivity =(MainActivity) getActivity();
        progressDialog = new ProgressDialog(getActivity());

        setUserInfo();
        initListener();


        return mView;
    }



    private void onClickRequestPermission() {
        if (mainActivity == null){
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            mainActivity.openGallery();
            return;
        }

        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            mainActivity.openGallery();
        }
        else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permissions,MY_REQUEST_CODE);

        }
    }

    private void setUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null){
            return;
        }
        else {
            edtName.setText(user.getDisplayName());
            edtEmail.setText(user.getEmail());
            Glide.with(mView.getContext()).load(user.getPhotoUrl()).error(R.drawable.blankuser).into(ivImage);
            edtMobile.setText(user.getPhoneNumber());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUI(){
        ivImage = mView.findViewById(R.id.ivImage);
        edtName = mView.findViewById(R.id.edtName);
        edtEmail = mView.findViewById(R.id.edtEmail);
        edtMobile = mView.findViewById(R.id.edtMobile);
        btnUpdateEmail = mView.findViewById(R.id.btnChangeEmail);
        btnUpdate = mView.findViewById(R.id.btnUpdate);
        edtAddress = mView.findViewById(R.id.edtAddress);
    }

    private void initListener() {
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fDatabase = FirebaseDatabase.getInstance("https://house-of-food-ec6b9-default-rtdb.asia-southeast1.firebasedatabase.app");
                reference = fDatabase.getReference("User");
                String UserName = edtName.getText().toString();
                String Email  = edtEmail.getText().toString();
                String phoneNumber = edtMobile.getText().toString();
                String Address = edtAddress.getText().toString();
                Users user = new Users(UserName,Email,phoneNumber,Address);
                reference.child(phoneNumber).setValue(user);

                onClickUpdateProfile();
            }
        });

        btnUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateEmail();
            }
        });
    }


    private void onClickUpdateEmail() {
        String newEmail = edtEmail.getText().toString().trim();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog.show();

        user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "User email address updated",Toast.LENGTH_SHORT).show();
                            mainActivity.showUserInfo();
                        }
                    }
                });
    }

    public void setBitmapImageView(Bitmap bitmapImageView){
        ivImage.setImageBitmap(bitmapImageView);
    }

    public void setUri(Uri mUri){
        this.mUri=mUri;
    }

    private void onClickUpdateProfile(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user==null){
            return;
        }


        progressDialog.show();
        String name = edtName.getText().toString().trim();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"Update profile success",Toast.LENGTH_SHORT).show();
                            mainActivity.showUserInfo();
                        }
                    }
                });



    }



}