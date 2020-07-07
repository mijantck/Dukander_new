package com.mrsoftit.dukander;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class SaleProductAdapter extends FirestoreRecyclerAdapter<ProductNote, SaleProductAdapter.NotViewHolde> {

    private OnItemClickListener listener;



    FirebaseFirestore firestore;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String user_id = currentUser.getUid();

    CollectionReference customer = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Customers").document().collection("customerSale");


    public SaleProductAdapter(@NonNull FirestoreRecyclerOptions<ProductNote> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final NotViewHolde holder, final int position, @NonNull final ProductNote model) {

        if (model.getProImgeUrl()!=null) {
            String Url = model.getProImgeUrl();
            Picasso.get().load(Url).into(holder.ImageView);
        }
        holder.calculatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double totalQantidy = Double.parseDouble(holder.product_quantedy_Editetview.getText().toString());
                double price= Double.parseDouble(holder.product_price_textview.getText().toString());

                double total = totalQantidy * price;

                holder.product_total_editetview.setText(total+"");

                holder.calculatebutton.setVisibility(View.GONE);
                holder.cutomerAddButton.setVisibility(View.VISIBLE);




            }
        });


        holder.cutomerAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SeleTwoActivity seleTwoActivity = new SeleTwoActivity();


                Toast.makeText(v.getContext(), seleTwoActivity.bundelCustomerName.getText()+"", Toast.LENGTH_SHORT).show();
            }
        });
        holder.product_name_textview.setText(model.getProName());

        String pp = String.valueOf(model.getProPrice());
        holder.product_price_textview.setText(pp);


    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sale_product_sing_item,
                parent, false);
        return new NotViewHolde(v);
    }

    public class NotViewHolde extends RecyclerView.ViewHolder {

        ImageView ImageView,calculatebutton,cutomerAddButton;

        TextView product_name_textview;
        EditText product_price_textview,product_quantedy_Editetview,product_total_editetview;


        public NotViewHolde(@NonNull View itemView) {
            super(itemView);

            ImageView = itemView.findViewById(R.id.sale_product_profile_pic);
            calculatebutton = itemView.findViewById(R.id.productSaleCaculation);
            cutomerAddButton = itemView.findViewById(R.id.product_add_customer);
            product_name_textview = itemView.findViewById(R.id.sale_product_name_textview);
            product_price_textview = itemView.findViewById(R.id.Sale_product_price);
            product_quantedy_Editetview = itemView.findViewById(R.id.Sale_product_quantedy);
            product_total_editetview = itemView.findViewById(R.id.product_totla_this_textview);
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
