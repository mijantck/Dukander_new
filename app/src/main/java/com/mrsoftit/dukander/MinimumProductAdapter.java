package com.mrsoftit.dukander;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class MinimumProductAdapter extends FirestoreRecyclerAdapter<ProductNote, MinimumProductAdapter.NotViewHolde> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public MinimumProductAdapter(@NonNull FirestoreRecyclerOptions<ProductNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotViewHolde holder, int position, @NonNull ProductNote model) {

        if (model.getProMin() > model.getProQua()|| model.getProMin()==model.getProQua()){
            holder.minimumproductname.setText(model.getProName());
            holder.minimumproduct.setText(model.getProQua()+"");
            holder.minimumproductname.setTextColor(Color.RED);
            holder.minimumproduct.setTextColor(Color.RED);

        }
        else if (model.getProMin() < model.getProQua()){
            holder.minimumproductname.setText(model.getProName());
            holder.minimumproduct.setText(model.getProQua()+"");
            holder.minimumproduct.setTextColor(Color.BLACK);
        }

    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.minimun_product_single_item,
                parent, false);
        return new NotViewHolde(v);
    }

    public class NotViewHolde extends RecyclerView.ViewHolder {


        private TextView minimumproductname,minimumproduct;

        public NotViewHolde(@NonNull View itemView) {
            super(itemView);
            minimumproductname = itemView.findViewById(R.id.minimumproductName);
            minimumproduct = itemView.findViewById(R.id.minimumproduct);



        }
    }



}
