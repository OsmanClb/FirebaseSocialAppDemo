package com.example.firebasesocialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firebasesocialapp.databinding.ActivitySignInEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignInEmailActivity extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    private ActivitySignInEmailBinding binding;
    private FirebaseAuth mAuth;
    private Person person;
    private FirebaseDatabase database;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(SignInEmailActivity.this,MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.btnSignInEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                String email = String.valueOf(binding.email.getText());
                String password = String.valueOf(binding.password.getText());
                String username = String.valueOf(binding.username.getText());
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(SignInEmailActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(SignInEmailActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(SignInEmailActivity.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                    createAccount(email,password);

            }
        });

    }

    private void createAccount(String email,String password){

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        binding.progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()){
                            Toast.makeText(SignInEmailActivity.this, "Account created", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(user != null){
                                person = new Person();
                                person.setUserId(user.getUid());
                                person.setDisplayName(String.valueOf(binding.username.getText()));
                                person.setEmail(user.getEmail());
                                person.setPhotoUrl("");
                                person.setAuthenticationMethod("Email");

                                database.getReference().child("persons").child(user.getUid()).setValue(person);
                                Intent intent = new Intent(SignInEmailActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Log.e(TAG,"createUserWithEmail:user==null",task.getException());
                                Toast.makeText(SignInEmailActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Log.e(TAG,"createUserWithEmail:failure",task.getException());
                            Toast.makeText(SignInEmailActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}