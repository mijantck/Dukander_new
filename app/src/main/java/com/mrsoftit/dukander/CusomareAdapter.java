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

public class CusomareAdapter extends FirestoreRecyclerAdapter<CustomerNote,CusomareAdapter.NotViewHolde> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private OnItemClickListener listener;

    public CusomareAdapter(@NonNull FirestoreRecyclerOptions<CustomerNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NotViewHolde holder, int position, @NonNull CustomerNote model) {

        if (model.getImageUrl()!=null) {
            String Url = model.getImageUrl();
            Picasso.get().load(Url).into(holder.CircleImageView);
        }
        if (model.getNameCUstomer()!=null) {
            holder.name.setText(model.getNameCUstomer());
        }
        if (model.getPhone()!=null) {
            holder.phone.setText(model.getPhone());
        }

            holder.taka.setText(model.getTaka()+"");

        if (model.getAddres()!=null) {
            holder.addres.setText(model.getAddres());
        }



    }



    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_sing_item,
                parent, false);
        return new NotViewHolde(v);
    }



    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();

    }


    public class NotViewHolde extends RecyclerView.ViewHolder {


        CircleImageView CircleImageView;
        TextView  name,phone,taka,addres;


        public NotViewHolde(@NonNull View itemView) {
            super(itemView);
            CircleImageView = itemView.findViewById(R.id.cutomer_profile_pic);
            name = itemView.findViewById(R.id.cutomer_name_textview);
            phone = itemView.findViewById(R.id.cutomer_number_textview);
            taka = itemView.findViewById(R.id.cutomer_taka_textview);
            addres = itemView.findViewById(R.id.cutomer_addrees_textview);

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