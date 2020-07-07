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
import com.mrsoftit.dukander.modle.DeliveryBoyListNote;
import com.mrsoftit.dukander.modle.ReviewComentNote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BoyAdapter extends FirestoreRecyclerAdapter<DeliveryBoyListNote, BoyAdapter.ViewHolde> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private OnItemClickListener listener;

    public BoyAdapter(@NonNull FirestoreRecyclerOptions<DeliveryBoyListNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolde holder, int position, @NonNull DeliveryBoyListNote model) {

        holder.boyName.setText(model.getBoyName());
        holder.boyOnline.setText(model.getBoyOnline());
        holder.boyPhone.setText(model.getBoyPhone());
        holder.boyarea.setText(model.getAria());
    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.boy_single_layout,
                parent, false);
        return new ViewHolde(v);
    }

    public class ViewHolde extends RecyclerView.ViewHolder {


        TextView boyName,boyOnline,boyPhone,boyarea;
        ImageView BoyImageView;

        public ViewHolde(@NonNull View itemView) {
            super(itemView);


            boyName = itemView.findViewById(R.id.boyName);
            BoyImageView = itemView.findViewById(R.id.BoyImageView);
            boyOnline = itemView.findViewById(R.id.boyOnline);
            boyPhone = itemView.findViewById(R.id.boyPhone);
            boyarea = itemView.findViewById(R.id.boyarea);

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
