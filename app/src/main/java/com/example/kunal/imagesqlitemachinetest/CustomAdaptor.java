package com.example.kunal.imagesqlitemachinetest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by KUNAL on 10/8/2016.
 */

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.MyViewHolder> {
    private List<byte[]> imageList;
    String commonName ;

    public CustomAdaptor(List<byte[]> imageList){
        this.imageList = imageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        byte[] image = imageList.get(position);
        holder.imageView.setImageBitmap(Utility.getImage(image));
        holder.name.setText("Image at " + position);
    }

    @Override
    public int getItemCount() {
        return imageList.size() > 0 ? imageList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgIcon);
            name = (TextView) itemView.findViewById(R.id.txtTitle);

        }
    }
}
