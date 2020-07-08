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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.mrsoftit.dukander.adapter.ShopProductOrderAdapter;
import com.mrsoftit.dukander.modle.DeliveryBoyListNote;
import com.mrsoftit.dukander.modle.OrderNote;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DeliveryBoyListActivity extends AppCompatActivity {


    FirebaseUser currentUser ;
    private FirebaseAuth mAuth;

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

      String customerID,shopUserID,customerToken,productID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_list);

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryBoyListActivity.this,ProductFullViewOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();

        if (bundle !=null){
            customerID = bundle.getString("customerID");
            shopUserID = bundle.getString("shopUserID");
            customerToken = bundle.getString("customerToken");
            productID = bundle.getString("productID");

        }





        recyclear();

    }


    private void recyclear() {

        CollectionReference customer = FirebaseFirestore.getInstance()
                .collection("GlobleBoy");

        Query query = customer;

        final FirestoreRecyclerOptions<DeliveryBoyListNote> options = new FirestoreRecyclerOptions.Builder<DeliveryBoyListNote>()
                .setQuery(query, DeliveryBoyListNote.class)
                .build();

        boyAdapter = new BoyAdapter(options);

        recyclerView = findViewById(R.id.boyListRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(boyAdapter);
        boyAdapter.startListening();

        boyAdapter.setOnItemClickListener(new BoyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                DeliveryBoyListNote deliveryBoyListNote = documentSnapshot.toObject(DeliveryBoyListNote.class);
                final String boyName = deliveryBoyListNote.getBoyName();
                final String boyPhone = deliveryBoyListNote.getBoyPhone();


                final CollectionReference customerForOrder = FirebaseFirestore.getInstance()
                        .collection("Globlecustomers").document(customerID).collection("OrderList");

                final CollectionReference shopForOrder = FirebaseFirestore.getInstance()
                        .collection("users").document(shopUserID).collection("OrderList");

                final CollectionReference globleForOrder = FirebaseFirestore.getInstance()
                        .collection("GlobleOrderList");



                shopForOrder.document(productID).update("deliveryBoyName",boyName,"deliveryBoyPhone",boyPhone).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        customerForOrder.document(productID).update("deliveryBoyName",boyName,"deliveryBoyPhone",boyPhone).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                globleForOrder.document(productID).update("deliveryBoyName",boyName,"deliveryBoyPhone",boyPhone).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        notificationSend(customerToken);

                                        startActivity(new Intent(DeliveryBoyListActivity.this,ShopOrderlistActivity.class));
                                        finish();

                                    }
                                });

                            }
                        });

                    }
                });

            }
        });
    }


    public  void  notificationSend( String Token){

        TOPIC = "Deliver Boy set"; //topic must match with what the receiver subscribed to

        String news_feed = "Your Product is ready for deliver";
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

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeliveryBoyListActivity.this, "Request error", Toast.LENGTH_LONG).show();

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
