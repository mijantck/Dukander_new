package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerProfileViewActivity extends AppCompatActivity {

    String  idup,uidup, nameup,phoneup,takaup,totalPaytakaup,addresup,imageup;

    private CircleImageView profileImage;
    private TextView profileName,profilePhone,profileadddres,totaldue,totalpayment,totalbuy;
    public Uri mImageUri;

    ExtendedFloatingActionButton floatingActionButton;
    String id,uid;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String user_id = Objects.requireNonNull(currentUser).getUid();


    TotalAdapter adapter ;

    ProgressDialog progressDialog ;

    Double totalsum = 0.0;
    Double cutomartk = 0.0;
    Double activeBalance = 0.0;
    Double dueBalance=0.0;
    String myinfoid ;
    SwipeRefreshLayout swipeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerProfileViewActivity.this, CustumarActivity.class);
                startActivity(intent);
                finish();
            }
        });


        profileName = findViewById(R.id.cutomer_profile_name);
        profileImage = findViewById(R.id.cutomer_profile_pic);
        profilePhone = findViewById(R.id.cutomer_profile_phone);
        profileadddres = findViewById(R.id.cutomer_profile_addres);
        totaldue = findViewById(R.id.cutomer_profile_total_due);
        totalpayment = findViewById(R.id.cutomer_profile_total_payment);
        totalbuy = findViewById(R.id.cutomer_profile_total_buy);
        floatingActionButton = findViewById(R.id.floating_action_button);
        swipeLayout = findViewById(R.id.swiperefreshlayout);


        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            idup = bundle.getString("id");
            uidup = bundle.getString("uid");
            nameup = bundle.getString("name");
            phoneup = bundle.getString("phone");
            takaup = bundle.getString("taka");
            totalPaytakaup = bundle.getString("totalPaytaka");
            addresup = bundle.getString("addreds");
            imageup = bundle.getString("imageurl");


            if (imageup != null) {
                Uri myUri = Uri.parse(imageup);
                mImageUri = myUri;
            }

            if (idup != null) {
                id = idup;
            }

            if (uidup != null) {
                uid = uidup;
            }


            if (imageup != null) {
                String Url = imageup;
                Picasso.get().load(Url).into(profileImage);
            }
            if (nameup != null) {
                profileName.setText(nameup);
            }
            if (phoneup != null) {
                profilePhone.setText(phoneup);
            }
            if (takaup != null) {
                totaldue.setText(takaup);
            } if (totalPaytakaup != null) {
                totalpayment.setText(totalPaytakaup);
            }
            if (addresup != null) {
                profileadddres.setText(addresup);
            }

        }

        if (id != null) {
            recyclear();
        }
        if (uid != null) {
            unknowrecyclear();
        }

        Date calendar1 = Calendar.getInstance().getTime();
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(calendar1);
        final int datenew = Integer.parseInt(todayString);


        CollectionReference custumertaka = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("Customers");

        if (id!=null){


        CollectionReference TotalcustomerProductSale = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("Customers").document(id).collection("saleProduct");


        Query query = TotalcustomerProductSale.whereEqualTo("paid", true);


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                if (e != null) {
                    return;
                }
                totalsum = 00.00;
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("totalPrice") != null) {
                        // double totaltest = (double) ;
                        double totaltest = Double.parseDouble(doc.get("totalPrice").toString());
                        totalsum += totaltest;
                    }

                    totalbuy.setText(totalsum + "");

                }


                double takaD = Double.parseDouble(takaup);

                Double payment = totalsum - takaD;


                totalpayment.setText(totalPaytakaup);
            }
        });

        Query query1 = custumertaka.whereEqualTo("customerIdDucunt", id);
        query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                cutomartk = 00.00;
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("taka") != null) {
                        double totaltest = (double) doc.get("taka");
                        String staka = Double.toString((Double) doc.get("taka"));
                        takaup = staka;
                        totaldue.setText(staka);
                        //  cutomartk = totaltest;
                    }
                }
            }
        });
    }
        else if (uid!=null){
            CollectionReference TotalcustomerProductSale = FirebaseFirestore.getInstance()
                    .collection("users").document(user_id).collection("UnknownCustomer")
                    .document(uid).collection("saleProduct");

            Query query = TotalcustomerProductSale.whereEqualTo("paid", true);


            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if (e != null) {
                        return;
                    }
                    totalsum = 00.00;
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        if (doc.get("totalPrice") != null) {
                            // double totaltest = (double) ;
                            double totaltest = Double.parseDouble(doc.get("totalPrice").toString());
                            totalsum += totaltest;
                        }
                        totalbuy.setText(totalsum + "");
                    }
                    double takaD = Double.parseDouble(takaup);

                    Double payment = totalsum - takaD;

                    totalpayment.setText(totalPaytakaup);
                }
            });

            Query query1 = custumertaka.whereEqualTo("customerIdDucunt", uid);
            query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        return;
                    }
                    cutomartk = 00.00;
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        if (doc.get("taka") != null) {
                            double totaltest = (double) doc.get("taka");
                            String staka = Double.toString((Double) doc.get("taka"));
                            takaup = staka;
                            //  cutomartk = totaltest;
                        }
                        totaldue.setText(takaup);

                    }


                }
            });

        }


        final CollectionReference myInfo = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("DukanInfo");


        myInfo.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e !=null){
                    return;
                }

                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("myid") != null) {
                        myinfoid = doc.getId();
                    }

                }

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CustomerProfileViewActivity.this);
                String[] option = {" Pay Money", "Product Sale"};

                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            final Dialog dialogpayment = new Dialog(CustomerProfileViewActivity.this);
                            // Include dialogpayment.xml file
                            dialogpayment.setContentView(R.layout.cutomar_pay_taka);
                            // Set dialogpayment title
                            dialogpayment.setTitle("Payment");
                            dialogpayment.show();
                            dialogpayment.setCanceledOnTouchOutside(false);
                            final Button okButton = dialogpayment.findViewById(R.id.okButton);
                            Button cancelButton = dialogpayment.findViewById(R.id.cancelButton);

                            final TextView payeditetext,TitleTExt,paymentLooding;

                            payeditetext= dialogpayment.findViewById(R.id.dialogpayMoney);
                            TitleTExt= dialogpayment.findViewById(R.id.TitleTExt);
                            paymentLooding = dialogpayment.findViewById(R.id.loadingTExt);
                            final TextView loadingTExt = dialogpayment.findViewById(R.id.loadingTExt);


                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    progressDialog = new ProgressDialog(CustomerProfileViewActivity.this);
                                    // Setting Message
                                    progressDialog.setMessage("Loading..."); // Setting Title
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();

                                    payeditetext.setVisibility(View.GONE);
                                    paymentLooding.setVisibility(View.VISIBLE);
                                    okButton.setVisibility(View.GONE);
                                    loadingTExt.setVisibility(View.VISIBLE);

                                    String paymony = payeditetext.getText().toString();

                                    if (paymony.isEmpty()){
                                        return;
                                    }

                                    final double paymonyDouable  = Double.parseDouble(paymony);
                                    double totalPay  = Double.parseDouble(totalpayment.getText().toString());


                                    double dautakaccustomer = 00.0;

                                    if (id!=null){
                                        dautakaccustomer = Double.parseDouble(totaldue.getText().toString().trim());
                                    }
                                    if (uid!=null){
                                        dautakaccustomer = Double.parseDouble(totaldue.getText().toString().trim());
                                    }
                                    if (dautakaccustomer <paymonyDouable ){

                                        Toast.makeText(CustomerProfileViewActivity.this, "Wrong Input", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    double withpaytaka = dautakaccustomer - paymonyDouable;
                                    final double totalpayTemp = totalPay + paymonyDouable;
                                    final double daubill = withpaytaka;




                                    CollectionReference myInfo = FirebaseFirestore.getInstance()
                                            .collection("users").document(user_id).collection("DukanInfo");


                                    myInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                MyInfoNote myInfoNote = document.toObject(MyInfoNote.class);

                                                activeBalance = myInfoNote.getActiveBalance();

                                                dueBalance = myInfoNote.getTotalpaybil();
                                            }

                                            if (activeBalance!=null){
                                                activeBalance += paymonyDouable;
                                            }

                                            if (dueBalance != null){
                                                dueBalance += paymonyDouable;

                                            }
                                            Date calendar1 = Calendar.getInstance().getTime();
                                            @SuppressLint("SimpleDateFormat")
                                            DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
                                            String todayString = df1.format(calendar1);
                                            final int datenew = Integer.parseInt(todayString);

                                            CollectionReference myInfo = FirebaseFirestore.getInstance()
                                                    .collection("users").document(user_id).collection("DukanInfo");

                                            if (myinfoid!=null){
                                                myInfo.document(myinfoid).update("activeBalance",activeBalance,"totalpaybil",dueBalance,"date",datenew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {


                                                    }
                                                });

                                            }

                                            }
                                    });


                                    if (id!=null) {
                                        final CollectionReference customer = FirebaseFirestore.getInstance().collection("users").document(user_id).collection("Customers");

                                        customer.document(id).collection("saleProduct").add(new TotalSaleNote("Payment",datenew,paymonyDouable)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                customer.document(id).update("taka", daubill,"totalPaytaka",totalpayTemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        customer.document(id).update("lastTotal", 00.0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                dialogpayment.dismiss();
                                                                progressDialog.dismiss();
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });


                                    }if (uid!=null) {


                                        CollectionReference myInfoUID = FirebaseFirestore.getInstance()
                                                .collection("users").document(user_id).collection("DukanInfo");


                                        myInfo.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                                                if (e != null) {

                                                    return;
                                                }
                                                assert queryDocumentSnapshots != null;
                                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                                    if (doc.get("myid") != null) {
                                                        myinfoid = doc.getId();
                                                    }
                                                    if (doc.get("activeBalance") != null) {

                                                        Double activeBalanceConvert = Double.parseDouble(doc.get("activeBalance").toString());
                                                        activeBalance = activeBalanceConvert;
                                                    }

                                                    if (doc.get("totalpaybil") != null) {

                                                        Double dueBalanceConvert = Double.parseDouble(doc.get("totalpaybil").toString());

                                                        dueBalance = dueBalanceConvert;



                                                        if (activeBalance!=null){
                                                            activeBalance += paymonyDouable;
                                                        }

                                                        if (dueBalance != null){
                                                            dueBalance += paymonyDouable;

                                                        }



                                                        Date calendar1 = Calendar.getInstance().getTime();
                                                        @SuppressLint("SimpleDateFormat")
                                                        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
                                                        String todayString = df1.format(calendar1);
                                                        final int datenew = Integer.parseInt(todayString);

                                                        CollectionReference myInfo = FirebaseFirestore.getInstance()
                                                                .collection("users").document(user_id).collection("DukanInfo");

                                                        if (myinfoid!=null){
                                                            myInfo.document(myinfoid).update("activeBalance",activeBalance,"totalpaybil",dueBalance,"date",datenew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {


                                                                }
                                                            });

                                                        }

                                                    }
                                                }

                                            }
                                        });

                                        final CollectionReference customer = FirebaseFirestore.getInstance().collection("users").document(user_id).collection("UnknownCustomer");

                                       customer.document(uid).collection("saleProduct").add(new TotalSaleNote("Payment",datenew,paymonyDouable)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentReference> task) {

                                               customer.document(uid).update("taka", daubill,"totalPaytaka",totalpayTemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                       customer.document(uid).update("lastTotal", 00.0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task) {
                                                               dialogpayment.dismiss();
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

                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogpayment.dismiss();
                                }
                            });


                        }
                        if (which == 1) {


                            final Dialog dialogpayment = new Dialog(CustomerProfileViewActivity.this);
                            // Include dialogpayment.xml file
                            dialogpayment.setContentView(R.layout.sale_product_with_customer_profile);
                            // Set dialogpayment title
                            dialogpayment.setTitle("Bill Pay ");
                            dialogpayment.show();
                            dialogpayment.setCanceledOnTouchOutside(false);
                            final Button okButton = dialogpayment.findViewById(R.id.addMoneyButtonproductsale);
                            Button cancelButton = dialogpayment.findViewById(R.id.cancelButtonproductsale);

                            final TextView productname,productSinglePrice,paymentProductQuantidy
                                    ,paymentProductToatalPrice,currentpaymentProduct;

                            productname= dialogpayment.findViewById(R.id.productname);
                            productSinglePrice= dialogpayment.findViewById(R.id.productSinglePrice);
                            paymentProductQuantidy= dialogpayment.findViewById(R.id.paymentProductQuantidy);
                            paymentProductToatalPrice = dialogpayment.findViewById(R.id.paymentProductToatalPrice);
                            currentpaymentProduct = dialogpayment.findViewById(R.id.currentpaymentProduct);


                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    okButton.setVisibility(View.GONE);


                                    final String proname = productname.getText().toString();
                                    final String proSingPrice = productSinglePrice.getText().toString();
                                    final String proQuatidy = paymentProductQuantidy.getText().toString();
                                    final String totalPraice = paymentProductToatalPrice.getText().toString();
                                    final String currentpayment = currentpaymentProduct.getText().toString();

                                    double totalPay  = Double.parseDouble(totalpayment.getText().toString());
                                    double currentpaymentD  = Double.parseDouble(totalpayment.getText().toString());


                                    if (proname.isEmpty()){
                                        Toast.makeText(CustomerProfileViewActivity.this, "product Name ", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (proSingPrice.isEmpty()){
                                        Toast.makeText(CustomerProfileViewActivity.this, "Product unit price", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (proQuatidy.isEmpty()){
                                        Toast.makeText(CustomerProfileViewActivity.this, "Quantity", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (totalPraice.isEmpty()){
                                        Toast.makeText(CustomerProfileViewActivity.this, "Total price", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (currentpayment.isEmpty()){
                                        Toast.makeText(CustomerProfileViewActivity.this, "pay maony", Toast.LENGTH_SHORT).show();
                                        return;
                                    }



                                    progressDialog = new ProgressDialog(CustomerProfileViewActivity.this);
                                    // Setting Message
                                    progressDialog.setMessage("Loading..."); // Setting Title
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();

                                    final double prosPrice  = Double.parseDouble(proSingPrice);
                                    final double proquantdy  = Double.parseDouble(proQuatidy);
                                    final double proTotal  = Double.parseDouble(totalPraice);
                                    final double currentpaymentF  = Double.parseDouble(currentpayment);


                                    double dautakaccustomer = 00.0;
                                    if (id!=null){
                                        dautakaccustomer = Double.parseDouble(totaldue.getText().toString().trim());
                                    }
                                    if (uid!=null){
                                        dautakaccustomer = Double.parseDouble(totaldue.getText().toString().trim());
                                    }

                                    double totalPrice = proTotal;


                                    final double withpaytaka = totalPrice - currentpaymentF;

                                    final double totalpayTemp = totalPay + withpaytaka;

                                    final double daubill = withpaytaka;

                                    myInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                MyInfoNote myInfoNote = document.toObject(MyInfoNote.class);

                                                activeBalance = myInfoNote.getActiveBalance();

                                                dueBalance = myInfoNote.getTotalpaybil();
                                            }

                                            if (activeBalance!=null){
                                                activeBalance += currentpaymentF;
                                            }

                                            if (dueBalance != null){
                                                dueBalance += withpaytaka;

                                            }
                                            Date calendar1 = Calendar.getInstance().getTime();
                                            @SuppressLint("SimpleDateFormat")
                                            DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
                                            String todayString = df1.format(calendar1);
                                            final int datenew = Integer.parseInt(todayString);

                                            CollectionReference myInfo = FirebaseFirestore.getInstance()
                                                    .collection("users").document(user_id).collection("DukanInfo");

                                            if (myinfoid!=null){
                                                myInfo.document(myinfoid).update("activeBalance",activeBalance,"totalpaybil",dueBalance,"date",datenew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {


                                                    }
                                                });

                                            }

                                        }
                                    });
                                    if (id!=null){

                                        final CollectionReference customer = FirebaseFirestore.getInstance()
                                                .collection("users").document(user_id).collection("Customers").document(id).collection("saleProduct");

                                        customer.add(new SaleProductCutomerNote(null,proname,prosPrice,proquantdy,totalPrice,datenew,0,true,true))
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                                        if ( task.isSuccessful()){
                                                            final String ids = task.getResult().getId();
                                                            customer.document(ids).update("saleProductId",ids).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    final CollectionReference customer = FirebaseFirestore.getInstance()
                                                                            .collection("users").document(user_id).collection("Customers");
                                                                           customer.document(id).update("taka", daubill,"totalPaytaka",totalpayTemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {


                                                                            //total sale
                                                                            final CollectionReference TotalcustomerProductSale = FirebaseFirestore.getInstance()
                                                                                    .collection("users").document(user_id).collection("Sales");





                                                                            Map<String, Object> user = new HashMap<>();
                                                                            user.put("saleProductId",ids);
                                                                            user.put("date",datenew);
                                                                            user.put("itemName",proname);
                                                                            user.put("quantedt", proquantdy);
                                                                            user.put("totalPrice", proTotal);
                                                                            user.put("paid", true);

                                                                            TotalcustomerProductSale.document(ids).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                                }
                                                                            });


                                                                                    dialogpayment.dismiss();
                                                                                    progressDialog.dismiss();
                                                                        }
                                                                    });



                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                    }
                                    if (uid!=null){

                                        final CollectionReference customer = FirebaseFirestore.getInstance()
                                                .collection("users").document(user_id).collection("UnknownCustomer").document(uid).collection("saleProduct");

                                        customer.add(new SaleProductCutomerNote(null,proname,prosPrice,proquantdy,proTotal,datenew,0,true,true))
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                         if ( task.isSuccessful()){
                                                             final String id = task.getResult().getId();
                                                             customer.document(id).update("saleProductId",id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                 @Override
                                                                 public void onComplete(@NonNull Task<Void> task) {

                                                                     final CollectionReference customer = FirebaseFirestore.getInstance()
                                                                             .collection("users").document(user_id).collection("UnknownCustomer");
                                                                     customer.document(uid).update("taka", daubill,"totalPaytaka",totalpayTemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                         @Override
                                                                         public void onComplete(@NonNull Task<Void> task) {
                                                                             customer.document(uid).update("lastTotal", 00.0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                 @Override
                                                                                 public void onComplete(@NonNull Task<Void> task) {


                                                                                     //total sale
                                                                                     final CollectionReference TotalcustomerProductSale = FirebaseFirestore.getInstance()
                                                                                             .collection("users").document(user_id).collection("Sales");


                                                                                     Map<String, Object> user = new HashMap<>();
                                                                                     user.put("saleProductId",id);
                                                                                     user.put("date",datenew);
                                                                                     user.put("itemName",proname);
                                                                                     user.put("quantedt", proquantdy);
                                                                                     user.put("totalPrice", proTotal);
                                                                                     user.put("paid", true);

                                                                                     TotalcustomerProductSale.document(id).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                         @Override
                                                                                         public void onComplete(@NonNull Task<Void> task) {

                                                                                         }
                                                                                     });


                                                                                     dialogpayment.dismiss();
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




                                }
                            });

                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogpayment.dismiss();
                                }
                            });


                        }

                    }
                }).create().show();


            }
        });


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code her
                // To keep animation for 4 seconds


                Date calendar1 = Calendar.getInstance().getTime();
                DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
                String todayString = df1.format(calendar1);
                final int datenew = Integer.parseInt(todayString);


                CollectionReference custumertaka = FirebaseFirestore.getInstance()
                        .collection("users").document(user_id).collection("Customers");

                if (id!=null){


                    CollectionReference TotalcustomerProductSale = FirebaseFirestore.getInstance()
                            .collection("users").document(user_id).collection("Customers").document(id).collection("saleProduct");


                    Query query = TotalcustomerProductSale.whereEqualTo("paid", true);


                    query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                            if (e != null) {
                                return;
                            }
                            totalsum = 00.00;
                            assert queryDocumentSnapshots != null;
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.get("totalPrice") != null) {
                                    // double totaltest = (double) ;
                                    double totaltest = Double.parseDouble(doc.get("totalPrice").toString());
                                    totalsum += totaltest;
                                }

                                totalbuy.setText(totalsum + "");

                            }


                            double takaD = Double.parseDouble(takaup);

                            Double payment = totalsum - takaD;


                            totalpayment.setText(totalPaytakaup);
                        }
                    });

                    Query query1 = custumertaka.whereEqualTo("customerIdDucunt", id);
                    query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }
                            cutomartk = 00.00;
                            assert queryDocumentSnapshots != null;
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.get("taka") != null) {
                                    double totaltest = (double) doc.get("taka");
                                    String staka = Double.toString((Double) doc.get("taka"));
                                    takaup = staka;
                                    totaldue.setText(staka);
                                    //  cutomartk = totaltest;
                                }
                            }
                        }
                    });
                }
                else if (uid!=null){
                    CollectionReference TotalcustomerProductSale = FirebaseFirestore.getInstance()
                            .collection("users").document(user_id).collection("UnknownCustomer").document(uid).collection("saleProduct");

                    Query query = TotalcustomerProductSale.whereEqualTo("paid", true);


                    query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                            if (e != null) {
                                return;
                            }
                            totalsum = 00.00;
                            assert queryDocumentSnapshots != null;
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.get("totalPrice") != null) {
                                    // double totaltest = (double) ;
                                    double totaltest = Double.parseDouble(doc.get("totalPrice").toString());
                                    totalsum += totaltest;
                                }
                                totalbuy.setText(totalsum + "");
                            }
                            double takaD = Double.parseDouble(takaup);

                            Double payment = totalsum - takaD;

                            totalpayment.setText(totalPaytakaup);
                        }
                    });

                    Query query1 = custumertaka.whereEqualTo("customerIdDucunt", uid);
                    query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }
                            cutomartk = 00.00;
                            assert queryDocumentSnapshots != null;
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.get("taka") != null) {
                                    double totaltest = (double) doc.get("taka");
                                    String staka = Double.toString((Double) doc.get("taka"));
                                    takaup = staka;
                                    //  cutomartk = totaltest;
                                }
                                totaldue.setText(takaup);

                            }


                        }
                    });

                }




                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000); // Delay in millis
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

    }




    private void recyclear() {

        CollectionReference TotalcustomerProductSale = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("Customers").document(id).collection("saleProduct");


        Query query = TotalcustomerProductSale.orderBy("itemName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<TotalSaleNote> options = new FirestoreRecyclerOptions.Builder<TotalSaleNote>()
                .setQuery(query, TotalSaleNote.class)
                .build();

        adapter = new TotalAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.cutomer_profile_recylcearview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }
    private void unknowrecyclear() {

        Date calendar1 = Calendar.getInstance().getTime();
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(calendar1);
        final int datenew = Integer.parseInt(todayString);


        CollectionReference TotalcustomerProductSale = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("UnknownCustomer").document(uid).collection("saleProduct");




        Query query = TotalcustomerProductSale.orderBy("itemName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<TotalSaleNote> options = new FirestoreRecyclerOptions.Builder<TotalSaleNote>()
                .setQuery(query, TotalSaleNote.class)
                .build();

        adapter = new TotalAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.cutomer_profile_recylcearview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
