package com.mrsoftit.dukander;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InvestmentWithdrowAdapter extends FirestoreRecyclerAdapter<MyInfoNote, InvestmentWithdrowAdapter.NotViewHolde> {
    private OnItemClickListener listener;


    public InvestmentWithdrowAdapter(@NonNull FirestoreRecyclerOptions<MyInfoNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotViewHolde holder, int position, @NonNull MyInfoNote model) {


        if (model.getInvesment()>0){
            holder.investmoney.setText(model.getInvesment()+"");
        }

        if (model.getWithdrow()>0){
            holder.investmoney.setText(model.getWithdrow()+"");
        }
        if (model.getInvestmentDeleils()!=null){
            holder.detaisl.setText(model.getInvestmentDeleils()+"");
        }
        if (model.getWithdrowDeleils()!=null){
            holder.detaisl.setText(model.getWithdrowDeleils()+"");

        }  if (model.getProductname()!=null){
            holder.productName.setText(model.getProductname());
        }
        if (model.getProduct()>0){
            holder.producthave.setText(model.getProduct()+"");
        }

        Integer value = model.getDate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = originalFormat.parse(value.toString());
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formatedDate = newFormat.format(date);
            holder.date.setText(formatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sales,
                parent, false);
        return new NotViewHolde(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    class NotViewHolde extends RecyclerView.ViewHolder {

        TextView investmoney,detaisl,date,productName,producthave;

        NotViewHolde(@NonNull View itemView) {
            super(itemView);
              investmoney = itemView.findViewById(R.id.totalproductName);
              detaisl = itemView.findViewById(R.id.dilaslepariceto);
              date = itemView.findViewById(R.id.datein_cash);
              productName = itemView.findViewById(R.id.productName);
            producthave = itemView.findViewById(R.id.producthave);




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
