package com.mrsoftit.dukander;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BuyCoinAdapter extends FirestoreRecyclerAdapter<BuyNote, BuyCoinAdapter.NotViewHolde> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public BuyCoinAdapter(@NonNull FirestoreRecyclerOptions<BuyNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotViewHolde holder, int position, @NonNull BuyNote model) {


        if (model.getUserId()!=null){
            holder.message_body.setText(model.getSms());

        }if (model.getUserId()==null){
            holder.cumpny_sms.setVisibility(View.VISIBLE);
            holder.name.setText("DUKANDER");
            holder.message_body_cumpany.setText(model.getSms());

        }

    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message,
                parent, false);
        return new NotViewHolde(v);
    }

    public class NotViewHolde extends RecyclerView.ViewHolder {

        TextView message_body,message_body_cumpany,name;
        RelativeLayout cumpny_sms ;


        public NotViewHolde(@NonNull View itemView) {
            super(itemView);

            message_body = itemView.findViewById(R.id.message_body);
            cumpny_sms = itemView.findViewById(R.id.cumpny_sms);
            name = itemView.findViewById(R.id.name);
            message_body_cumpany = itemView.findViewById(R.id.message_body_cumpany);


        }
    }

}
