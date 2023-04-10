package com.example.occupeye;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Login extends AppCompatActivity {
    Button login;
    Button signin;
    EditText username;
    EditText password;
    boolean password_status;
    boolean username_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef=database.getReference().child("Users");

        //INITIALLIZING THE VARIABLES
        login=findViewById(R.id.loggin);
        signin=findViewById(R.id.signup);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        password_status=false;
        username_status=false;


        //
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                String username_tag=username.getText().toString();
                if(!b&&username_tag.length()<6){
                    Toast.makeText(Login.this,"Invalid Username",Toast.LENGTH_SHORT).show();
                    username_status=false;
                } else if (!b&&username_tag.contains(" ")) {

                    Toast.makeText(Login.this,"Username Cannot Have Space",Toast.LENGTH_SHORT).show();
                    username_status=false;
                }else if(!b){
                    username_status=true;
                }
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                String password_tag=password.getText().toString();
                if(!b && password_tag.length()<6){
                    Toast.makeText(Login.this,"Invalid Password",Toast.LENGTH_SHORT).show();
                    password_status=false;
                } else if (!b&& password_tag.contains(" ")) {
                    Toast.makeText(Login.this,"Password Cannot Have Space",Toast.LENGTH_SHORT).show();
                    password_status=false;
                } else if (!b) {
                    password_status=true;
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user=new User(username.getText().toString(),password.getText().toString());

                if(user.validate_login()){
                    mRef.child(username.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                                Toast.makeText(Login.this,"User not registered",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.d("firebase","connected");

                                HashMap<String,String>data= (HashMap<String, String>) task.getResult().getValue();
                                try{if(password.getText().toString().equals(data.get("password"))){
                                    Intent intent=new Intent(Login.this,Home.class);
                                    startActivity(intent);

                                }
                                else {
                                    Toast.makeText(Login.this,"Invalid Username/Password",Toast.LENGTH_SHORT).show();
                                }}catch (Exception e){
                                    Toast.makeText(Login.this,"User not registered",Toast.LENGTH_SHORT).show();
                            }

                            }
                        }
                    });

                }else {
                    Toast.makeText(Login.this,"Invalid Entries",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}