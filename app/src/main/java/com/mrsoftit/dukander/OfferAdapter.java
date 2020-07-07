package com.mrsoftit.dukander;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OfferAdapter extends FirestoreRecyclerAdapter<OfferNote, OfferAdapter.NotViewHolde> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public OfferAdapter(@NonNull FirestoreRecyclerOptions<OfferNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotViewHolde holder, int position, @NonNull OfferNote model) {

       holder.Coin_textview.setText(model.getOfferCoin());
       holder.coinTaka_textview.setText(model.getCointaka());

    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_card,
                parent, false);
        return new NotViewHolde(v);
    }

    public class NotViewHolde extends RecyclerView.ViewHolder {

        TextView Coin_textview,coinTaka_textview;

        public NotViewHolde(@NonNull View itemView) {
            super(itemView);

            Coin_textview = itemView.findViewById(R.id.offer_card_totalproductName);
            coinTaka_textview = itemView.findViewById(R.id.offer_card_dilaslepariceto);


        }
    }

}
