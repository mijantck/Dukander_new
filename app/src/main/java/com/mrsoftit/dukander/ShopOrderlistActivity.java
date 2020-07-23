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
import android.widget.TextView;
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

    String ProductName;


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
               ProductName =  orderNote.getProductName();
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


                final Dialog dialog = new Dialog(ShopOrderlistActivity.this);
                // Include dialogpayment.xml file
                dialog.setContentView(R.layout.product_confirmetion_choos_dialoge);
                // Set dialogpayment title
                dialog.setTitle("Choose your option");
                dialog.show();

                TextView orderConfirm = dialog.findViewById(R.id.confirm_order);
                TextView diliveyComplet = dialog.findViewById(R.id.Delivery_Complet);
                TextView cancel = dialog.findViewById(R.id.Order_cancel);

                orderConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        shopForOrder.document(productID).update("confirmetionStatus","Confirm from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                customerForOrder.document(productID).update("confirmetionStatus","Confirm from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        globleForOrder.document(productID).update("confirmetionStatus","Confirm from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                dialog.dismiss();
                                                notificationSend(customerToken,"Confirm Order",ProductName);


                                            }
                                        })  ;

                                    }
                                });

                            }
                        });

                    }
                });
                diliveyComplet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        shopForOrder.document(productID).update("confirmetionStatus","Delivery DONE. Thanks And welcome ").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                customerForOrder.document(productID).update("confirmetionStatus","Delivery DONE. Thanks And welcome ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        globleForOrder.document(productID).update("confirmetionStatus","Delivery DONE. Thanks And welcome ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                dialog.dismiss();
                                                notificationSend(customerToken,"Delivery DONE. Thanks And welcome",ProductName);

                                            }
                                        })  ;

                                    }
                                });

                            }
                        });

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        shopForOrder.document(productID).update("confirmetionStatus","Cancel from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                customerForOrder.document(productID).update("confirmetionStatus","Cancel from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        globleForOrder.document(productID).update("confirmetionStatus","Cancel from Shop ").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                notificationSend(customerToken,"Cancel from Shop",ProductName);

                                            }
                                        })  ;

                                    }
                                });

                            }
                        });

                    }
                });


            }

            @Override
            public void onBoySelectClick(DocumentSnapshot documentSnapshot, int position) {

                final OrderNote orderNote = documentSnapshot.toObject(OrderNote.class);


                final String cutomerName =  orderNote.getCutomerName();
                final String cutomerPhone =  orderNote.getCutomerPhone();
                final String cutomerAddress =  orderNote.getCutomerAddress();
                final String cutomerID =  orderNote.getCutomerID();
                final String shopName =  orderNote.getShopName();
                final String shopPhone =  orderNote.getShopPhone();
                final String shopAddress =  orderNote.getShopAddress();
                final String shopID =  orderNote.getShopID();
                final String userID =  orderNote.getUserID();
                final String orderID =  orderNote.getOrderID();
                final int orderDate = orderNote.getOrderDate();
                final String productName =  orderNote.getProductName();
                final String productID =  orderNote.getOrderID();
                final String productURL =  orderNote.getProductURL();
                final String productCode =  orderNote.getProductCode();
                final String productPrice =  orderNote.getProductPrice();
                final String productQuantity =  orderNote.getProductQuantity();
                final String offerForShop =  orderNote.getOfferForShop();
                final String offerForcoupon =  orderNote.getOfferForcoupon();
                final String confirmetionStatus =  orderNote.getConfirmetionStatus();
                final String orderStatus =  orderNote.getOrderStatus();
                final String deliveryBoyName =  orderNote.getDeliveryBoyName();
                final String deliveryBoyPhone =  orderNote.getDeliveryBoyPhone();
                final String customerToken  = orderNote.getCustomerToken();
                final String size =  orderNote.getSize();
                final String color =  orderNote.getColor();
                final String type=  orderNote.getType();


                Intent intent = new  Intent(ShopOrderlistActivity.this,DeliveryBoyListActivity.class);

                intent.putExtra("cutomerName",cutomerName);
                intent.putExtra("cutomerPhone",cutomerPhone);
                intent.putExtra("cutomerAddress",cutomerAddress);
                intent.putExtra("cutomerID",cutomerID);
                intent.putExtra("shopName",shopName);
                intent.putExtra("shopPhone",shopPhone);
                intent.putExtra("shopAddress",shopAddress);
                intent.putExtra("shopID",shopID);
                intent.putExtra("userID",userID);
                intent.putExtra("orderID",orderID);
                intent.putExtra("orderDate",orderDate);
                intent.putExtra("productName",productName);
                intent.putExtra("productID",productID);
                intent.putExtra("productURL",productURL);
                intent.putExtra("productCode",productCode);
                intent.putExtra("productPrice",productPrice);
                intent.putExtra("productQuantity",productQuantity);
                intent.putExtra("offerForShop",offerForShop);
                intent.putExtra("offerForcoupon",offerForcoupon);
                intent.putExtra("confirmetionStatus",confirmetionStatus);
                intent.putExtra("orderStatus",orderStatus);
                intent.putExtra("deliveryBoyName",deliveryBoyName);
                intent.putExtra("deliveryBoyPhone",deliveryBoyPhone);
                intent.putExtra("customerToken",customerToken);
                intent.putExtra("size",size);
                intent.putExtra("color",color);
                intent.putExtra("type",type);

                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        productOrderAdapter.startListening();
    }


    public  void  notificationSend( String Token,String OrderStutas,String orderDiscription){


        NOTIFICATION_TITLE = orderDiscription;
        NOTIFICATION_MESSAGE = OrderStutas;

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
