package com.prodev.assignment_waycool.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.prodev.assignment_waycool.R;
import com.prodev.assignment_waycool.models.model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Recycle_adp_cart extends RecyclerView.Adapter<Recycle_adp_cart.viewholder> {
    List<model> list = new ArrayList<model>();
    Context mcontext;

    public Recycle_adp_cart(List<model> list, Context mcontext) {
        this.list = list;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //  LayoutInflater inflater=null;
        View v= LayoutInflater.from(mcontext).inflate(R.layout.item_cart_recycle,viewGroup,false);
        viewholder vh = new viewholder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder viewholder, int i) {
        Picasso.with(mcontext).load(list.get(i).getImg()).into(viewholder.img);
        viewholder.name.setText(list.get(i).getName());


    }

    @Override
    public int getItemCount() {

        return list.size();
    }


    public class viewholder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.imageView);
            name=(TextView) itemView.findViewById(R.id.textView);
        }
    }

}
