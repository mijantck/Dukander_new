package com.mrsoftit.dukander.adapter;

import android.graphics.Color;
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
import com.mrsoftit.dukander.R;
import com.mrsoftit.dukander.modle.GlobleProductNote1;
import com.mrsoftit.dukander.modle.Shop_list_note;
import com.squareup.picasso.Picasso;

public class ShopListAdapter extends FirestoreRecyclerAdapter<Shop_list_note, ShopListAdapter.ViewHolde> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private OnItemClickListener listener;

    public ShopListAdapter(@NonNull FirestoreRecyclerOptions<Shop_list_note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolde holder, int position, @NonNull Shop_list_note model) {

        if (model.getShopImageURL()!=null) {
            String Url = model.getShopImageURL();
            Picasso.get().load(Url).into(holder.ImageView);
        }
        holder.ShopName.setText(model.getShopName());
    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list_single_item,
                parent, false);
        return new ViewHolde(v);
    }

    public class ViewHolde extends RecyclerView.ViewHolder {



        ImageView ImageView;
        TextView ShopName;
        public ViewHolde(@NonNull View itemView) {
            super(itemView);

            ImageView = itemView.findViewById(R.id.shop_list_image_view);
            ShopName = itemView.findViewById(R.id.shop_list_image_name);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }


    }


    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
