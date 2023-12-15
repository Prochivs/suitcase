package com.example.suit_case_application.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suit_case_application.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Register extends AppCompatActivity {
    private EditText username;
    private EditText names;
    private EditText emailAddress;
    private EditText password;
    private Button signup;
    private TextView signin;

    private DatabaseReference mRootRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth=FirebaseAuth.getInstance();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        username = findViewById(R.id.username);
        names = findViewById(R.id.names);
        emailAddress = findViewById(R.id.email);
        password = findViewById(R.id.password);
        signup=findViewById(R.id.signup_user);
        signin=findViewById(R.id.signin_user);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtUsername = username.getText().toString();
                String txtFullName = names.getText().toString();
                String txtEmail = emailAddress.getText().toString().trim();
                String txtPassword = password.getText().toString();

                if(TextUtils.isEmpty(txtUsername) || (TextUtils.isEmpty(txtFullName))
                        || (TextUtils.isEmpty(txtEmail)) || (TextUtils.isEmpty(txtPassword))
                ){
                    Toast.makeText(getApplicationContext(),"Please Enter All Details",Toast.LENGTH_LONG).show();
                }else if(txtPassword.length()<6){
                    Toast.makeText(getApplicationContext(),"Password Too Short !",Toast.LENGTH_LONG).show();
                }else {
                    registerUser(txtUsername,txtFullName, txtEmail,txtPassword);
                }
            }
        });

    }

    private void registerUser(String username, String fullName, String email, String password) {


        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map = new HashMap<>();
                map.put("fullNames",fullName);
                map.put("email",email);
                map.put("username",username);
                map.put("id",firebaseAuth.getCurrentUser().getUid());
                map.put("profileimageurl","default");

                mRootRef.child("Users").child(firebaseAuth.getCurrentUser().getUid()).setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){


                                    Intent intent= new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage().toString(),
                        Toast.LENGTH_LONG).show();

            }
        });

    }
}