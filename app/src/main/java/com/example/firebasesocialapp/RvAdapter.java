package com.example.firebasesocialapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.MyViewHolder>{
    private  final Context mContext;
    private List<Image> imageList;

    public RvAdapter(Context mContext, List<Image> imageList) {
        this.mContext = mContext;
        this.imageList = imageList;
    }
    public void setImageList(List<Image> imageList){
        this.imageList =imageList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Image img = imageList.get(position);
        holder.textViewTitle.setText(img.getImageTitle());
        imgLoad(img.getImageUrl(),holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,CardDetailsActivity.class);
                intent.putExtra("image",img);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }


    void imgLoad(String url,ImageView imageView){
        Picasso.get().load(url)
                .resize(150,150).centerCrop().into(imageView);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewTitle;
        ImageView imageView;
        CardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            imageView = itemView.findViewById(R.id.imageViewCard);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
