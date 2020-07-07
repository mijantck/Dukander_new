package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mrsoftit.dukander.modle.GlobleCustomerNote;
import com.mrsoftit.dukander.modle.OrderNote;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConfirmOrderActivity extends AppCompatActivity {



    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    String globlecutouser_id ;

    private String proIdup,proNameup,proPriceup,proPriceupSingle,productCodeup,productPrivacyup,proImgeUrlup,ShopNameup,ShopPhoneup,ShopAddressup,ShopImageUrlup,
            ShopIdup,UserIdup,productCategoryup,dateup,proQuaup,discuntup,commonPriceup,tokenup,size;


    ProgressDialog progressDialog;

    String customertoken;



    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAttd1svE:APA91bFocWSMpJ4WTI-CI_plcvO9Cj31dB3ENhHybDmR4t2Do9yZZC4jEvylhxPfz-7RoTiWzUT3zZYUSb8pYy0-R4SUMhY5BmzXzZ9pYfrJljKvJgjFPyEw_mV_Z8xpzclcM6phwTkN";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;


   private TextInputEditText Shipping_name,Shipping_Phone,Shipping_Address;
   private TextView orderProductName,orderProductPriceSingle,orderProductQuantidy,orderProductTotalPrice,orderProductCoponCodeParsent,
           withCoponCodeprice,orderProductShipingLearnMore,orderProductShippingCharge,orderProductTotalBill,withDiscuntprice,orderProductDicunt,coponProviderTextView;
   private EditText orderProductCoponCode;
   private Button OrderConfirm;
   private ImageView orderProductCoponCodeButton;
   private LinearLayout coponcodesetLayout,coponcodProviderLayout;

   Double proPrice,proQua,proPriceSingle;
   int pruductDiscount;

   Double withCoponcode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser!=null){
            globlecutouser_id = currentUser.getUid();
        }
        progressDialog = new ProgressDialog(ConfirmOrderActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);

                Shipping_name = findViewById(R.id.Shipping_name);
                Shipping_Phone = findViewById(R.id.Shipping_Phone);
                Shipping_Address = findViewById(R.id.Shipping_Address);


                coponcodesetLayout = findViewById(R.id.coponcodesetLayout);
                coponcodProviderLayout = findViewById(R.id.coponcodProviderLayout);

                orderProductName = findViewById(R.id.orderProductName);
                orderProductPriceSingle = findViewById(R.id.orderProductPriceSingle);
                orderProductQuantidy = findViewById(R.id.orderProductQuantidy);
                orderProductTotalPrice = findViewById(R.id.orderProductTotalPrice);
                orderProductCoponCodeParsent = findViewById(R.id.orderProductCoponCodeParsent);
                withCoponCodeprice = findViewById(R.id.withCoponCodeprice);
                orderProductDicunt = findViewById(R.id.orderProductDicunt);
                withDiscuntprice = findViewById(R.id.withDiscuntprice);
                orderProductShipingLearnMore = findViewById(R.id.orderProductShipingLearnMore);
                orderProductShippingCharge = findViewById(R.id.orderProductShippingCharge);
                orderProductTotalBill = findViewById(R.id.orderProductTotalBill);
                coponProviderTextView = findViewById(R.id.coponProviderTextView);

                orderProductCoponCode = findViewById(R.id.orderProductCoponCode);
                orderProductCoponCodeButton = findViewById(R.id.orderProductCoponCodeButton);
                OrderConfirm = findViewById(R.id.OrderConfirm);



        final Bundle bundle = getIntent().getExtras();

        if (bundle!=null){
            if (bundle.getString("proIdup")!=null){
                proIdup = bundle.getString("proIdup");
            }
            if (bundle.getString("proNameup")!=null){
                proNameup = bundle.getString("proNameup");
                orderProductName.setText(proNameup);
            }

            if (bundle.getString("proPriceupSingle")!=null){
                proPriceupSingle = bundle.getString("proPriceupSingle");
                proPriceSingle = Double.parseDouble(proPriceupSingle);
                orderProductPriceSingle.setText(proPriceupSingle);
            }
            if (bundle.getString("commonPrice")!=null){
                commonPriceup = bundle.getString("commonPrice");

            }

            withDiscuntprice.setText(commonPriceup);

            if (bundle.getString("proPriceup")!=null){
                proPriceup = bundle.getString("proPriceup");
                proPrice = Double.parseDouble(proPriceup);
                orderProductTotalPrice.setText(proPriceup);
            }

            if (bundle.getString("productCodeup")!=null){
                productCodeup = bundle.getString("productCodeup");
               // ProductCode.setText(productCodeup);
            }
            if (bundle.getString("productPrivacyup")!=null){
                productPrivacyup = bundle.getString("productPrivacyup");
            }
            if (bundle.getString("proImgeUrlup")!=null){
                proImgeUrlup = bundle.getString("proImgeUrlup");
              /*  String Url = proImgeUrlup;
                Picasso.get().load(Url).into(productImageDetail);*/
            }
            if (bundle.getString("ShopNameup")!=null){
                ShopNameup = bundle.getString("ShopNameup");
               // shopDetailName.setText(ShopNameup);

            }
            if (bundle.getString("ShopPhoneup")!=null){
                ShopPhoneup = bundle.getString("ShopPhoneup");
               // shopDetailPhone.setText(ShopPhoneup);

            }
            if (bundle.getString("ShopAddressup")!=null){
                ShopAddressup = bundle.getString("ShopAddressup");
               // shopDetailAddress.setText(ShopAddressup);

            }
            if (bundle.getString("ShopImageUrlup")!=null){
                ShopImageUrlup = bundle.getString("ShopImageUrlup");
            }
            if (bundle.getString("ShopIdup")!=null){
                ShopIdup = bundle.getString("ShopIdup");
            }
            if (bundle.getString("UserIdup")!=null){
                UserIdup = bundle.getString("UserIdup");
            }
            if (bundle.getString("productCategoryup")!=null){
                productCategoryup = bundle.getString("productCategoryup");
            }
            if (bundle.getString("dateup")!=null){
                dateup = bundle.getString("dateup");
                pruductDiscount =Integer.parseInt(dateup);
            }
            if (bundle.getString("proQuaup")!=null){
                proQuaup = bundle.getString("proQuaup");
                proQua = Double.parseDouble(proQuaup);
                orderProductQuantidy.setText(proQuaup);
                Double WithOutDiscunt = proQua*proPriceSingle;
                orderProductTotalPrice.setText(WithOutDiscunt+"");
            }
            if (bundle.getString("discuntup")!=null){
                discuntup = bundle.getString("discuntup");
                pruductDiscount =Integer.parseInt(discuntup);
              //  Double d2 =Double.valueOf(pruductDiscount);
               // orderProductTotalBill.setText(calcuateDiscount(proPrice,d2)+"");
                orderProductDicunt.setText(discuntup);
            }
            if (bundle.getString("tokenup")!=null){
                tokenup = bundle.getString("tokenup");
               // pruductDiscount =Integer.parseInt(discuntup);
                //  Double d2 =Double.valueOf(pruductDiscount);
                // orderProductTotalBill.setText(calcuateDiscount(proPrice,d2)+"");
               // orderProductDicunt.setText(discuntup);
            }
            if (bundle.getString("size")!=null){
                size = bundle.getString("size");
               // pruductDiscount =Integer.parseInt(discuntup);
                //  Double d2 =Double.valueOf(pruductDiscount);
                // orderProductTotalBill.setText(calcuateDiscount(proPrice,d2)+"");
               // orderProductDicunt.setText(discuntup);
            }
        }





        CollectionReference Globlecustomer = FirebaseFirestore.getInstance()
                .collection("Globlecustomers").document(currentUser.getUid()).collection("info");
        Globlecustomer.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        GlobleCustomerNote globleCustomerNote = document.toObject(GlobleCustomerNote.class);

                        Shipping_name.setText(globleCustomerNote.getName());
                        Shipping_Phone.setText(globleCustomerNote.getPhoneNumber());
                        Shipping_Address.setText(globleCustomerNote.getAddress());

                    }

                }
            }
        });


        OrderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final String  ShippingName =   Shipping_name.getText().toString();
               final String  ShippingPhone =   Shipping_Phone.getText().toString();
               final String  ShippingAddress =   Shipping_Address.getText().toString();
               final String  TotalproductPrice =   orderProductTotalBill.getText().toString();


               if (ShippingName.isEmpty() && ShippingPhone.isEmpty() && ShippingAddress.isEmpty() ){

                   Toast.makeText(ConfirmOrderActivity.this, " Shipping address fillup", Toast.LENGTH_SHORT).show();

                   return;
               }



                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){

                            customertoken = task.getResult().getToken();
                            // notificationSend(token);
                        }
                    }
                });
               progressDialog.show();


                Date calendar1 = Calendar.getInstance().getTime();
                DateFormat df1 = new SimpleDateFormat("yyMMddHHmm");
                String todayString = df1.format(calendar1);
                final int datereview = Integer.parseInt(todayString);

                final CollectionReference customerForOrder = FirebaseFirestore.getInstance()
                        .collection("Globlecustomers").document(globlecutouser_id).collection("OrderList");

                final CollectionReference shopForOrder = FirebaseFirestore.getInstance()
                        .collection("users").document(UserIdup).collection("OrderList");

                final CollectionReference globleForOrder = FirebaseFirestore.getInstance()
                        .collection("GlobleOrderList");






                customerForOrder.add(new OrderNote(ShippingName,ShippingPhone,ShippingAddress,globlecutouser_id,ShopNameup,ShopPhoneup,
                        ShopAddressup,ShopIdup,UserIdup,null,datereview,proNameup,proIdup,proImgeUrlup,productCodeup,
                        TotalproductPrice,proQuaup,discuntup,"promocode",null,null,null,null,customertoken,size))
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()){
                                    final String orderID = task.getResult().getId();
                                    customerForOrder.document(orderID).update("orderID",orderID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            final Map<String, Object> ConfirmOrder = new HashMap<>();
                                            ConfirmOrder.put("cutomerName",ShippingName);
                                            ConfirmOrder.put("cutomerPhone",ShippingPhone);
                                            ConfirmOrder.put("cutomerAddress",ShippingAddress);
                                            ConfirmOrder.put("cutomerID",globlecutouser_id);
                                            ConfirmOrder.put("shopName",ShopNameup);
                                            ConfirmOrder.put("shopPhone",ShopPhoneup);
                                            ConfirmOrder.put("shopAddress",ShopAddressup);
                                            ConfirmOrder.put("shopID",ShopIdup);
                                            ConfirmOrder.put("userID",UserIdup);
                                            ConfirmOrder.put("orderID",orderID);
                                            ConfirmOrder.put("orderDate",datereview);
                                            ConfirmOrder.put("productName",proNameup);
                                            ConfirmOrder.put("productID",proIdup);
                                            ConfirmOrder.put("productURL",proImgeUrlup);
                                            ConfirmOrder.put("productCode",productCodeup);
                                            ConfirmOrder.put("productPrice",TotalproductPrice);
                                            ConfirmOrder.put("productQuantity",proQuaup);
                                            ConfirmOrder.put("offerForShop",discuntup);
                                            ConfirmOrder.put("offerForcoupon","promocode");
                                            ConfirmOrder.put("confirmetionStatus",null);
                                            ConfirmOrder.put("orderStatus",null);
                                            ConfirmOrder.put("deliveryBoyName",null);
                                            ConfirmOrder.put("deliveryBoyPhone",null);
                                            ConfirmOrder.put("customerToken",customertoken);
                                            ConfirmOrder.put("size",size);

                                            shopForOrder.document(orderID).set(ConfirmOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    globleForOrder.document(orderID).set(ConfirmOrder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {


                                                            notificationSend(tokenup);

                                                       progressDialog.dismiss();

                                                        }
                                                    });

                                                }
                                            });

                                        }
                                    });
                                }
                            }
                        });

                     }
                });

        orderProductCoponCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
               String couponCode =  orderProductCoponCode.getText().toString();

               if (couponCode.isEmpty()){

                   progressDialog.dismiss();
                   Toast.makeText(ConfirmOrderActivity.this, " Coupon Code is not Empty", Toast.LENGTH_SHORT).show();
                   return;
               }
               if (!couponCode.isEmpty()){
                   CollectionReference cuopneCode = FirebaseFirestore.getInstance()
                           .collection("couponCode");

                   Query query = cuopneCode.whereEqualTo("couponCode",couponCode);

                   query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()){

                               progressDialog.dismiss();

                               coponcodProviderLayout.setVisibility(View.VISIBLE);
                               coponcodesetLayout.setVisibility(View.GONE);
                               Double discount = null;
                               Double price = Double.valueOf(commonPriceup);

                               for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                   CouponCode couponCode1 = document.toObject(CouponCode.class);

                                   coponProviderTextView.setText(couponCode1.getNameProvider());
                                   orderProductCoponCodeParsent.setText(couponCode1.getCopuOffer()+"");

                                   discount =Double.valueOf(couponCode1.getCopuOffer());
                               }

                               if (discount!=null && price!=null){

                                   Toast.makeText(ConfirmOrderActivity.this, price + discount+" ", Toast.LENGTH_SHORT).show();

                                   withCoponcode = calcuateDiscount(price,discount);

                                   withCoponCodeprice.setText( withCoponcode+"");

                                   withCoponcode = withCoponcode+30;
                                   orderProductTotalBill.setText(withCoponcode+"");
                               }


                               if (discount == null){


                                   new MaterialAlertDialogBuilder(ConfirmOrderActivity.this)
                                           .setTitle(" Sorry ")
                                           .setMessage("This Coupon code not use")
                                           .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                               @Override
                                               public void onClick(DialogInterface dialogInterface, int i) {
                                                   dialogInterface.dismiss();
                                               }
                                           })
                                           .show();
                               }

                           }else {
                               progressDialog.dismiss();
                               new MaterialAlertDialogBuilder(ConfirmOrderActivity.this)
                                       .setTitle(" Sorry ")
                                       .setMessage("This Coupon code not use")
                                       .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialogInterface, int i) {
                                               dialogInterface.dismiss();
                                           }
                                       })
                                       .show();
                           }
                       }
                   });

               }


            }
        });



        if (withCoponcode!=null){

        }else {

          Double  commonPriceup1 = Double.parseDouble(commonPriceup);
            commonPriceup1 = commonPriceup1+30;
            orderProductTotalBill.setText(commonPriceup1+"");
        }

    }




    static double calcuateDiscount(double markedprice, double s) {
        double dis = 100-s;
        double amount= (dis*markedprice)/100;

        return amount;

    }

    public  void  notificationSend( String Token){

        Toast.makeText(this, Token+"", Toast.LENGTH_SHORT).show();
        TOPIC = "Product Order ";
                //"Order From "+Shipping_name; //topic must match with what the receiver subscribed to

        String news_feed = "Order fom "+Shipping_name +"Product Name "+proNameup;
                //" Product Name "+proNameup+" Quantity "+proQuaup+"  Price :"+proPriceup;
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
                        Toast.makeText(ConfirmOrderActivity.this, response.toString()+"  ", Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ConfirmOrderActivity.this, "Request error", Toast.LENGTH_LONG).show();

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
