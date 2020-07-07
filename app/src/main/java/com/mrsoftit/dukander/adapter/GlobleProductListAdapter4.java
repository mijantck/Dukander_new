package com.mrsoftit.dukander.adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mrsoftit.dukander.R;
import com.mrsoftit.dukander.modle.GlobleProductNote3;
import com.mrsoftit.dukander.modle.GlobleProductNote4;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GlobleProductListAdapter4 extends FirestoreRecyclerAdapter<GlobleProductNote4, GlobleProductListAdapter4.ViewHolde> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private OnItemClickListener listener;

    public GlobleProductListAdapter4(@NonNull FirestoreRecyclerOptions<GlobleProductNote4> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolde holder, int position, @NonNull GlobleProductNote4 model) {
        holder.name.setText(model.getProName());
        holder.price.setText(model.getProPrice()+"");
        if (model.getProImgeUrl()!=null){
            String Url = model.getProImgeUrl();
            Picasso.get().load(Url).into(holder.productImage);
        }
        if (model.getProQua()<=0){
            holder.instock.setText("Stock out");
            holder.instock.setTextColor(Color.RED);

        }else {

            holder.instock.setText("In stock");
            holder.instock.setTextColor(Color.GREEN);

        }

        Date calendar1 = Calendar.getInstance().getTime();
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(calendar1);
        int datenew = Integer.parseInt(todayString);
        if (model.getPruductDiscount()>0){
            holder.discountViewLayout.setVisibility(View.VISIBLE);
            holder.discuontView.setText(model.getPruductDiscount()+"");
            holder.dicuntLayout.setVisibility(View.VISIBLE);
            holder.price.setPaintFlags(holder.price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            Double d2 =Double.valueOf(model.getPruductDiscount());
            holder.discountParsent.setText(calcuateDiscount(model.getProPrice(),d2)+"");
        }

        int newTag = model.getDate()+1;
        if (model.getDate()== datenew){
            holder.newLayout.setVisibility(View.VISIBLE);
        }else if (newTag==datenew){
            holder.newLayout.setVisibility(View.VISIBLE);
        }

    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,
                parent, false);
        return new ViewHolde(v);
    }

    public class ViewHolde extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView name,instock,price,discountParsent,discuontView;
        RelativeLayout dicuntLayout;
        LinearLayout discountViewLayout,newLayout;

        public ViewHolde(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.ProductName);
            instock = itemView.findViewById(R.id.inStock);
            price = itemView.findViewById(R.id.productPrice);
            dicuntLayout = itemView.findViewById(R.id.dicuntLayout);
            discountParsent = itemView.findViewById(R.id.discountParsent);
            discuontView = itemView.findViewById(R.id.discuontView);
            discountViewLayout = itemView.findViewById(R.id.discountViewLayout);
            newLayout = itemView.findViewById(R.id.newLayout);

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

    static double calcuateDiscount(double markedprice, double s)
    {
        double dis = 100-s;
        double amount= (dis*markedprice)/100;

        return amount;

    }
}
