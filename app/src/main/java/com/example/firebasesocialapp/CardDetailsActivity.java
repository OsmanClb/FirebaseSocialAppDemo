package com.example.firebasesocialapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebasesocialapp.databinding.ActivityCardDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class CardDetailsActivity extends AppCompatActivity {
    ActivityCardDetailsBinding binding;
    Image image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar.setVisibility(View.VISIBLE);

        image = (Image) getIntent().getSerializableExtra("image");

        if (image != null){
            binding.progressBar.setVisibility(View.GONE);
            binding.toolbarCardDetails.setTitle("Details");
            binding.toolbarCardDetails.setSubtitle(image.getImageTitle());
            setSupportActionBar(binding.toolbarCardDetails);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.textViewTitle.setText(image.getImageTitle());
            binding.textViewBody.setText(image.getImageBody());
            imgLoad(image.getImageUrl(),binding.imageViewCard);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_card_details_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (R.id.action_logout == item.getItemId()){
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(CardDetailsActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void imgLoad(String url, ImageView imageView){
        Picasso.get().load(url)
                .resize(500,500).centerCrop().into(imageView);
    }

}