package com.mrsoftit.dukander.adapter;

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
import com.mrsoftit.dukander.modle.NotificationNote;
import com.mrsoftit.dukander.modle.ReviewComentNote;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends FirestoreRecyclerAdapter<NotificationNote, NotificationAdapter.ViewHolde> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private OnItemClickListener listener;

    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<NotificationNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolde holder, int position, @NonNull NotificationNote model) {
        holder.NotificationTitle.setText(model.getNotiTaile());
        holder.Noti_product_name_textView.setText(model.getNotiProductName());
        holder.Noti_product_detais_textView.setText(model.getNotiproductDetails());

        if (model.getNotiImageURL()!=null){
            String Url = model.getNotiImageURL();
            Picasso.get().load(Url).into(holder.noti_Imgae__URL);
        }

        Integer value = model.getNotidate();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyMMddHHmm");
        try {
            Date date = originalFormat.parse(value.toString());
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yy-HH:mm");
            String formatedDate = newFormat.format(date);
            holder.noti_Date_And_Time.setText(formatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_comment_single_item,
                parent, false);
        return new ViewHolde(v);
    }

    public class ViewHolde extends RecyclerView.ViewHolder {


        TextView NotificationTitle,Noti_product_name_textView,noti_Date_And_Time,Noti_product_detais_textView;
        ImageView  noti_Imgae__URL;

        public ViewHolde(@NonNull View itemView) {
            super(itemView);


            NotificationTitle = itemView.findViewById(R.id.NotificationTitle);
            Noti_product_name_textView = itemView.findViewById(R.id.Noti_product_name_textView);
            noti_Date_And_Time = itemView.findViewById(R.id.noti_Date_And_Time);
            Noti_product_detais_textView = itemView.findViewById(R.id.Noti_product_detais_textView);
            noti_Imgae__URL = itemView.findViewById(R.id.noti_Imgae__URL);

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
