package com.mrsoftit.dukander;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter  extends FirestoreRecyclerAdapter<ProductNote,ProductAdapter.NotViewHolde> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private OnItemClickListener listener;


    public ProductAdapter(@NonNull FirestoreRecyclerOptions<ProductNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotViewHolde holder, int position, @NonNull ProductNote model) {

        if (model.getProImgeUrl()!=null) {
            String Url = model.getProImgeUrl();
            Picasso.get().load(Url).into(holder.ImageView);
        }

        holder.product_name_textview.setText(model.getProName());

        String pp = String.valueOf(model.getProPrice());
        holder.product_price_textview.setText(pp);


        String pq = String.valueOf(model.getProQua());
        holder.product_quantedy_textview.setText(pq);

        String pm = String.valueOf(model.getProMin());
        holder.product_mini_textview.setText(pm);


    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_sing_item,
                parent, false);
        return new NotViewHolde(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    public class NotViewHolde extends RecyclerView.ViewHolder {

        ImageView ImageView;

        TextView product_name_textview,product_price_textview,product_quantedy_textview,product_mini_textview;

        public NotViewHolde(@NonNull View itemView) {
            super(itemView);

            ImageView = itemView.findViewById(R.id.product_profile_pic);
            product_name_textview = itemView.findViewById(R.id.product_name_textview);
            product_price_textview = itemView.findViewById(R.id.product_price_textview);
            product_quantedy_textview = itemView.findViewById(R.id.product_quantedy_textview);
            product_mini_textview = itemView.findViewById(R.id.product_mini_textview);
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
