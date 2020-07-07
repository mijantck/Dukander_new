package com.mrsoftit.dukander;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class SaleProductIndevicualAdapter  extends FirestoreRecyclerAdapter<SaleProductCutomerNote,SaleProductIndevicualAdapter.NotViewHolde> {



    private OnItemClickListener listener;


    public SaleProductIndevicualAdapter(@NonNull FirestoreRecyclerOptions<SaleProductCutomerNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotViewHolde holder, int position, @NonNull SaleProductCutomerNote model) {

        holder.imenViewpr.setText(model.getItemName());
        holder.qyt.setText(model.getQuantedt()+"");
        holder.price.setText(model.getTotalPrice()+"");
        holder.receptsinglepric.setText(model.getPrice()+"");
        if (model.getPaid()==true){
            holder.receptpaidview.setText("Paid");
        }
    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reciept_single_item,
                parent, false);
        return new NotViewHolde(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public class NotViewHolde extends RecyclerView.ViewHolder {

        TextView imenViewpr,qyt,price,receptsinglepric,receptpaidview;


        public NotViewHolde(@NonNull View itemView) {
            super(itemView);
            imenViewpr =itemView.findViewById(R.id.receptIemview);
            qyt =itemView.findViewById(R.id.recepQytview);
            price =itemView.findViewById(R.id.receptpriceview);
            receptsinglepric =itemView.findViewById(R.id.receptsinglepric);
            receptpaidview =itemView.findViewById(R.id.receptpaidview);

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
