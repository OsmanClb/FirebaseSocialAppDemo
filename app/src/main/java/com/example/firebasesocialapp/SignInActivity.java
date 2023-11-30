package com.example.firebasesocialapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firebasesocialapp.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firestore.v1.StructuredQuery;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;
    private static final String TAG = "Google";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 20;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);


        binding.btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignInEmailActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
    }

    private void googleSignIn(){
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());

            } catch (ApiException e) {

                throw new RuntimeException(e);

            }
        }

    }

    private void firebaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    FirebaseUser user = mAuth.getCurrentUser();

                    if(user != null){
                        person = new Person();
                        person.setUserId(user.getUid());
                        person.setDisplayName(user.getDisplayName());
                        person.setEmail(user.getEmail());
                        person.setPhotoUrl(String.valueOf(user.getPhotoUrl()));
                        person.setAuthenticationMethod("Google");
                        database.getReference().child("persons").child(user.getUid()).setValue(person);

                        Intent intent = new Intent(SignInActivity.this,HomeActivity.class);
                        intent.putExtra("person",person);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Log.e(TAG,"LoginGoogle:user==null",task.getException());
                        Toast.makeText(SignInActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(SignInActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}