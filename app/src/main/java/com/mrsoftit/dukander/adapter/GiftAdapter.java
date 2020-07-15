package com.mrsoftit.dukander.adapter;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrsoftit.dukander.GlobleCustomerActivity;
import com.mrsoftit.dukander.R;
import com.mrsoftit.dukander.modle.GiftNote;
import com.mrsoftit.dukander.modle.GlobleCustomerNote;
import com.mrsoftit.dukander.modle.ReviewComentNote;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class GiftAdapter extends FirestoreRecyclerAdapter<GiftNote, GiftAdapter.ViewHolde> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private OnItemClickListener listener;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String  globlecutouser_id;


    public GiftAdapter(@NonNull FirestoreRecyclerOptions<GiftNote> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolde holder, int position, @NonNull final GiftNote model) {

        if (currentUser !=null){
            currentUser.getUid();
        }

       holder.giftcoin.setText(model.getGiftCoin()+"");
        holder.giftName.setText(model.getGiftName());
        if (model.getGiftNewL()!=null){
            holder.newGift.setVisibility(View.VISIBLE);

        }
        if (model.getGiftPicURL()!=null){
            String Url = model.getGiftPicURL();
            Picasso.get().load(Url).into(holder.giftImage);
        }

    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_single_item,
                parent, false);
        return new ViewHolde(v);
    }

    public class ViewHolde extends RecyclerView.ViewHolder {


              ImageView giftImage;
              TextView giftName,giftcoin;
              LinearLayout newGift;
        public ViewHolde(@NonNull final View itemView) {
            super(itemView);

            newGift = itemView.findViewById(R.id.newGift);
            giftName = itemView.findViewById(R.id.gifttName);
            giftcoin = itemView.findViewById(R.id.giftcoinbuy);
            giftImage = itemView.findViewById(R.id.giftImage);



            giftcoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onCoinClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });

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
        void onCoinClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static double calcuateDiscount(double markedprice, double s) {
        double dis = 100-s;
        double amount= (dis*markedprice)/100;
        return amount;


    }
}
