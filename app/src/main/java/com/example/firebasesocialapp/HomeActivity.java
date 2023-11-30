package com.example.firebasesocialapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;

import com.example.firebasesocialapp.databinding.ActivityHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "FirestoreReadAllUsers";
    private ActivityHomeBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private CollectionReference imagesRef;
    private RvAdapter rvAdapter;
    private List<Image> imageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar.setVisibility(View.VISIBLE);
        setSupportActionBar(binding.toolbarHome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        imagesRef = db.collection("images");

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(user == null){
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }


        imagesRef.get().addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                imageList = new ArrayList<>();
                if (task.isSuccessful()){
                    binding.progressBar.setVisibility(View.GONE);
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){

                        Image image = documentSnapshot.toObject(Image.class);
                        imageList.add(image);
                        Log.d(TAG, documentSnapshot.getId() + " => " + image.getImageBody());
                    }

                    rvAdapter = new RvAdapter(HomeActivity.this,imageList);
                    binding.recyclerView.setAdapter(rvAdapter);

                }else {
                    Log.e(TAG, "Error Read", task.getException());
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_home_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        assert searchView != null;
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        performSearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        performSearch(newText);
        return false;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void performSearch(String query) {
        List<Image> filtredList = filterList(query);
        rvAdapter.setImageList(filtredList);
        rvAdapter.notifyDataSetChanged();
    }
    public List<Image> filterList(String query){
        List<Image> filtredList = new ArrayList<>();
        for(Image k : imageList ){
            if(k.getImageTitle().toLowerCase().contains(query.toLowerCase())){
                filtredList.add(k);
            }
        }
        return filtredList;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_logout){
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(HomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if(item.getItemId() == android.R.id.home){

            Snackbar.make(binding.toolbarHome,"Logout ?",Snackbar.LENGTH_SHORT).setAction("yes", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}