package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.mrsoftit.dukander.adapter.BoyAdapter;
import com.mrsoftit.dukander.adapter.ProductOrderAdapter;
import com.mrsoftit.dukander.adapter.ShopProductOrderAdapter;
import com.mrsoftit.dukander.modle.DeliveryBoyListNote;
import com.mrsoftit.dukander.modle.OrderNote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShopOrderlistActivity extends AppCompatActivity {



    FirebaseUser currentUser ;
    private FirebaseAuth mAuth;
    ShopProductOrderAdapter productOrderAdapter ;
    BoyAdapter boyAdapter;
    String  user_id;


    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAttd1svE:APA91bFocWSMpJ4WTI-CI_plcvO9Cj31dB3ENhHybDmR4t2Do9yZZC4jEvylhxPfz-7RoTiWzUT3zZYUSb8pYy0-R4SUMhY5BmzXzZ9pYfrJljKvJgjFPyEw_mV_Z8xpzclcM6phwTkN";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_orderlist);


        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser !=null) {

            user_id = currentUser.getUid();
        }
        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopOrderlistActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


            recyclear();
    }


    private void recyclear() {

        CollectionReference customer = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("OrderList");

        Query query = customer;

        final FirestoreRecyclerOptions<OrderNote> options = new FirestoreRecyclerOptions.Builder<OrderNote>()
                .setQuery(query, OrderNote.class)
                .build();

        productOrderAdapter = new ShopProductOrderAdapter(options);

        recyclerView = findViewById(R.id.shopOrderListRecyclervview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productOrderAdapter);
        productOrderAdapter.setOnItemClickListener(new ShopProductOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                OrderNote orderNote = documentSnapshot.toObject(OrderNote.class);
                String order =  orderNote.getOrderID();
                Toast.makeText(ShopOrderlistActivity.this, order+" Its click Item", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConfirmClick(DocumentSnapshot documentSnapshot, int position) {
                final OrderNote orderNote = documentSnapshot.toObject(OrderNote.class);
                String customerID =  orderNote.getCutomerID();
                String shopUserID =  orderNote.getUserID();
                final String customerToken = orderNote.getCustomerToken();
                final String productID =  orderNote.getOrderID();


                final CollectionReference customerForOrder = FirebaseFirestore.getInstance()
                        .collection("Globlecustomers").document(customerID).collection("OrderList");

                final CollectionReference shopForOrder = FirebaseFirestore.getInstance()
                        .collection("users").document(shopUserID).collection("OrderList");

                final CollectionReference globleForOrder = FirebaseFirestore.getInstance()
                        .collection("GlobleOrderList");


                new MaterialAlertDialogBuilder(ShopOrderlistActivity.this)
                        .setTitle("Your Order confirmation ")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                Toast.makeText(ShopOrderlistActivity.this, orderNote+"  ", Toast.LENGTH_SHORT).show();

                                shopForOrder.document(productID).update("confirmetionStatus","Confirm from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        customerForOrder.document(productID).update("confirmetionStatus","Confirm from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                globleForOrder.document(productID).update("confirmetionStatus","Confirm from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        notificationSend(customerToken);

                                                    }
                                                })  ;

                                            }
                                        });

                                    }
                                });
                                dialogInterface.dismiss();

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                shopForOrder.document(productID).update("confirmetionStatus","Cancel from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        customerForOrder.document(productID).update("confirmetionStatus","Cancel from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                globleForOrder.document(productID).update("confirmetionStatus","Cancel from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        notificationSend(customerToken);

                                                    }
                                                })  ;

                                            }
                                        });

                                    }
                                });
                                dialogInterface.dismiss();
                            }
                        })
                        .show();


            }

            @Override
            public void onBoySelectClick(DocumentSnapshot documentSnapshot, int position) {

                final OrderNote orderNote = documentSnapshot.toObject(OrderNote.class);
                final String customerID =  orderNote.getCutomerID();
                final String shopUserID =  orderNote.getUserID();
                final String customerToken = orderNote.getCustomerToken();
                final String productID =  orderNote.getOrderID();

                Intent intent = new  Intent(ShopOrderlistActivity.this,DeliveryBoyListActivity.class);

                intent.putExtra("customerID",customerID);
                intent.putExtra("shopUserID",shopUserID);
                intent.putExtra("customerToken",customerToken);
                intent.putExtra("productID",productID);

                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        productOrderAdapter.startListening();
    }


    public  void  notificationSend( String Token){

        TOPIC = "new sms "; //topic must match with what the receiver subscribed to

        String news_feed = "NewsFeeed.getText().toString()";
        NOTIFICATION_TITLE = TOPIC;
        NOTIFICATION_MESSAGE = news_feed;

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();
        try {
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);

            notification.put("to", Token);

            notification.put("data", notifcationBody);
        } catch (JSONException e) {
            // Log.e(TAG, "onCreate: " + e.getMessage() );
        }
        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        Toast.makeText(ShopOrderlistActivity.this, response.toString()+"  ", Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShopOrderlistActivity.this, "Request error", Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);
        requestQueue.add(jsonObjectRequest);

    }


}
