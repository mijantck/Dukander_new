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
import com.mrsoftit.dukander.modle.GlobleProductNote1;
import com.mrsoftit.dukander.modle.ReviewComentNote;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReviewAdapter extends FirestoreRecyclerAdapter<ReviewComentNote, ReviewAdapter.ViewHolde> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private OnItemClickListener listener;

    public ReviewAdapter(@NonNull FirestoreRecyclerOptions<ReviewComentNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolde holder, int position, @NonNull ReviewComentNote model) {
        holder.reviewname.setText(model.getReviewCustomerName());
        holder.reviewComment.setText(model.getReviewComment());


        Integer value = model.getDateAndTime();
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyMMddHHmm");
        try {
            Date date = originalFormat.parse(value.toString());
            SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yy-HH:mm");
            String formatedDate = newFormat.format(date);
            holder.date.setText(formatedDate);
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


        TextView reviewname,reviewComment,date;

        public ViewHolde(@NonNull View itemView) {
            super(itemView);


            reviewname = itemView.findViewById(R.id.customer_name_for_review);
            reviewComment = itemView.findViewById(R.id.review_comment_textView);
            date = itemView.findViewById(R.id.review_Date_And_Time);

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
