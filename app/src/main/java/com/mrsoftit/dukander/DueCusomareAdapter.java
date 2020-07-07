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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DueCusomareAdapter extends FirestoreRecyclerAdapter<CustomerNote, DueCusomareAdapter.NotViewHolde> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DueCusomareAdapter(@NonNull FirestoreRecyclerOptions<CustomerNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotViewHolde holder, int position, @NonNull CustomerNote model) {

        if (model.getNameCUstomer()!=null) {
            holder.name.setText(model.getNameCUstomer());
        }
            holder.taka.setText(model.getTaka()+"");

    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.due_customer_single,
                parent, false);
        return new NotViewHolde(v);
    }

    public class NotViewHolde extends RecyclerView.ViewHolder {

        TextView  name,taka;


        public NotViewHolde(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.due_cutomer_name);
            taka = itemView.findViewById(R.id.due_cutomer_cush);



        }
    }


}