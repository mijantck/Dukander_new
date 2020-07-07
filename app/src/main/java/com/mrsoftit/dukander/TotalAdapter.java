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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TotalAdapter extends FirestoreRecyclerAdapter<TotalSaleNote, TotalAdapter.NotViewHolde> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private OnItemClickListener listener;


    public TotalAdapter(@NonNull FirestoreRecyclerOptions<TotalSaleNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotViewHolde holder, int position, @NonNull TotalSaleNote model) {

        holder.product_name_textview.setText(model.getItemName());
        holder.product_quantedy_textview.setText(model.getQuantedt()+"");
        holder.product_price_textview.setText(model.getTotalPrice()+"");

        if (model.getItemName().equals("Payment")){
            holder.product_quantedy_textview.setVisibility(View.GONE);
            holder.product_price_textview.setText(model.getPayment()+"");

        }
if (model.isConfirm() == true){
    holder.confirmTextView.setVisibility(View.VISIBLE);
}
        Integer value = model.getDate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = originalFormat.parse(value.toString());
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formatedDate = newFormat.format(date);
            holder.dateproductsale.setText(formatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_sale_list_product_single_item,
                parent, false);
        return new NotViewHolde(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();

    }

    public class NotViewHolde extends RecyclerView.ViewHolder {

        TextView product_name_textview,product_price_textview,product_quantedy_textview,dateproductsale,confirmTextView;

        public NotViewHolde(@NonNull View itemView) {
            super(itemView);

            product_name_textview = itemView.findViewById(R.id.totalproductName);
            product_quantedy_textview = itemView.findViewById(R.id.Totalquantidy);
            product_price_textview = itemView.findViewById(R.id.Totalpariceto);
            dateproductsale = itemView.findViewById(R.id.dateproductsale);
            confirmTextView = itemView.findViewById(R.id.confirmTextView);
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
