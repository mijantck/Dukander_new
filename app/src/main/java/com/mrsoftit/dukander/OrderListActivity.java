package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mrsoftit.dukander.adapter.ProductOrderAdapter;
import com.mrsoftit.dukander.modle.OrderNote;

import java.util.Objects;

public class OrderListActivity extends AppCompatActivity {


    FirebaseUser currentUser ;
    ProductOrderAdapter productOrderAdapter ;
    String  user_id;

    RecyclerView recyclerView;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ProgressDialog progressDialog =new ProgressDialog(OrderListActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderListActivity.this,GlobleProductListActivity.class);
                startActivity(intent);
                finish();
            }
        });


        progressDialog = new ProgressDialog(this);


        currentUser  = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            user_id =currentUser.getUid();
            recyclear();
        }else {
            new MaterialAlertDialogBuilder(OrderListActivity.this)
                    .setTitle("you have not signup")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            startActivity(new Intent(OrderListActivity.this,CustomerLoginActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }



    }

    private void recyclear() {


        CollectionReference customer = FirebaseFirestore.getInstance()
                .collection("Globlecustomers").document(user_id).collection("OrderList");

        Query query = customer;

        FirestoreRecyclerOptions<OrderNote> options = new FirestoreRecyclerOptions.Builder<OrderNote>()
                .setQuery(query, OrderNote.class)
                .build();

        productOrderAdapter = new ProductOrderAdapter(options);

        recyclerView = findViewById(R.id.orderListCustomer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productOrderAdapter);

        productOrderAdapter.setOnItemClickListener(new ProductOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {


            }

            @Override
            public void onCancelClick(DocumentSnapshot documentSnapshot, int position) {
                OrderNote orderNote = documentSnapshot.toObject(OrderNote.class);
                final String orederID =orderNote.getOrderID();
                String orederCustomerID =orderNote.getCutomerID();
                String shopUserID =orderNote.getUserID();

                final CollectionReference customer = FirebaseFirestore.getInstance()
                        .collection("Globlecustomers").document(user_id).collection("OrderList");

                final CollectionReference globle = FirebaseFirestore.getInstance()
                        .collection("GlobleOrderList");
                final CollectionReference owner = FirebaseFirestore.getInstance()
                        .collection("users").document(shopUserID).collection("OrderList");

                customer.document(orederID).update("confirmetionStatus","Cancel from Customer").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        owner.document(orederID).update("confirmetionStatus","Cancel from Customer").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                globle.document(orederID).update("confirmetionStatus","Cancel from Customer").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                    }
                                });

                            }
                        });


                    }
                });



            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser!=null){
            productOrderAdapter.startListening();
        }
    }
}
