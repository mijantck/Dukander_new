package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrsoftit.dukander.adapter.ReviewAdapter;
import com.mrsoftit.dukander.modle.GlobleCustomerNote;
import com.mrsoftit.dukander.modle.ReviewComentNote;
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

public class Review_Product_Activity extends AppCompatActivity {



    private   ReviewAdapter reviewAdapter;
    Button reviewsendbutton;
    ProgressDialog progressDialog;
    EditText revieweditText;

    FirebaseUser currentUser;
    String globlecutouser_id ;
    private FirebaseAuth mAuth;

    int coinupdet;
    String globleCustomerName;
    String globleCustomerEmail;
    String globalCustomerInfoId;
    String globalCustomerImageURL;

    private String proIdup,fromURL, proNameup,proPriceup,productCodeup,productPrivacyup,proImgeUrlup,
            ShopNameup,ShopPhoneup,ShopAddressup,ShopImageUrlup,ShopIdup,UserIdup,productCategoryup,dateup,proQuaup,discuntup;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review__product_);

        reviewsendbutton =findViewById(R.id.reviewsendbutton);
        revieweditText = findViewById(R.id.revieweditText);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();

        if (currentUser!=null){
            globlecutouser_id = currentUser.getUid();
        }

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_support);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Review_Product_Activity.this,ProductFullViewOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Bundle bundle = getIntent().getExtras();

        if (bundle!=null){

            if (bundle.getString("proIdURL")!=null){
                proIdup = bundle.getString("proIdURL");
            }
            if (bundle.getString("fromURL")!=null){
                fromURL = bundle.getString("fromURL");
            }
            if (bundle.getString("proIdup")!=null){
                proIdup = bundle.getString("proIdup");
            }
            if (bundle.getString("proNameup")!=null){
                proNameup = bundle.getString("proNameup");
            }

            if (bundle.getString("proPriceup")!=null){
                proPriceup = bundle.getString("proPriceup");

            }

            if (bundle.getString("productCodeup")!=null){
                productCodeup = bundle.getString("productCodeup");

            }
            if (bundle.getString("productPrivacyup")!=null){
                productPrivacyup = bundle.getString("productPrivacyup");
            }
            if (bundle.getString("proImgeUrlup")!=null){
                proImgeUrlup = bundle.getString("proImgeUrlup");

            }
            if (bundle.getString("ShopNameup")!=null){
                ShopNameup = bundle.getString("ShopNameup");

            }
            if (bundle.getString("ShopPhoneup")!=null){
                ShopPhoneup = bundle.getString("ShopPhoneup");

            }
            if (bundle.getString("ShopAddressup")!=null){
                ShopAddressup = bundle.getString("ShopAddressup");
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



        }

        reviewsendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Review_Product_Activity.this);
                // Setting Message
                progressDialog.setMessage("Loading..."); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                final String revieweditText1 = revieweditText.getText().toString();

                if (revieweditText1.isEmpty()){
                    Toast.makeText(Review_Product_Activity.this, " write comment", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                if (mAuth!=null && currentUser!=null){

                    if (currentUser.getUid().equals(UserIdup)){

                        OnerCommentcomment(proIdup,UserIdup,proNameup,proImgeUrlup,revieweditText1);
                        Toast.makeText(Review_Product_Activity.this, "owner", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (currentUser != null) {
                            String customerId = globlecutouser_id;
                            final CollectionReference Reviewcustomer = FirebaseFirestore.getInstance()
                                    .collection("Globlecustomers").document(customerId).collection("info");
                            Reviewcustomer.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {


                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                            GlobleCustomerNote globleCustomerNote = document.toObject(GlobleCustomerNote.class);
                                            coinupdet = globleCustomerNote.getCoine();
                                            globalCustomerInfoId = globleCustomerNote.getId();
                                            globleCustomerName = globleCustomerNote.getName();
                                            globleCustomerEmail = globleCustomerNote.getEmail();
                                            globalCustomerImageURL = globleCustomerNote.getImageURL();
                                        }
                                        String proId = proIdup;
                                        String shopUserId = UserIdup;
                                        String proURL = proImgeUrlup;
                                        String proName = proNameup;

                                        if (globalCustomerInfoId !=null) {
                                            testUserforpreviusComment(proId, shopUserId, globleCustomerName, globleCustomerEmail,
                                                    proName, proURL, revieweditText1, coinupdet, globalCustomerInfoId,globalCustomerImageURL);
                                        }
                                        else {
                                            progressDialog.dismiss();
                                            new MaterialAlertDialogBuilder(Review_Product_Activity.this)
                                                    .setTitle(" Sorry ")
                                                    .setMessage("You are not comment hare  \n because you are shopkeeper ")
                                                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            dialogInterface.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }


                                    }
                                }
                            });


                        }
                    }


                }

                else {
                    progressDialog.dismiss();
                    new MaterialAlertDialogBuilder(Review_Product_Activity.this)
                            .setTitle("you have not signup")
                            .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    startActivity(new Intent(Review_Product_Activity.this,CustomerLoginActivity.class));
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
        });

        review(proIdup);
    }


    public void review(String productID){

        CollectionReference Review = FirebaseFirestore.getInstance()
                .collection("GlobleProduct").document(productID).collection("review");

        Query query = Review.orderBy("dateAndTime", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ReviewComentNote> options = new FirestoreRecyclerOptions.Builder<ReviewComentNote>()
                .setQuery(query, ReviewComentNote.class)
                .build();

        reviewAdapter = new ReviewAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.reviewreciclearview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reviewAdapter);
        reviewAdapter.startListening();
    }



    public void  testUserforpreviusComment(final String productID, final String shopUserID, final String custumerName, final String custumerEmail, final String productName,
                                           final String productURL , final String reviewComment, final int coinupdet1, final String globalCustomerInfoId1, final String globalCustomerImageURL ){

        final CollectionReference Review = FirebaseFirestore.getInstance()
                .collection("GlobleProduct").document(productID).collection("review");

        Review.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    ArrayList<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                      list.add(document.getId());

                    }



                    if (list.contains(globlecutouser_id)) {
                        progressDialog.dismiss();
                        new MaterialAlertDialogBuilder(Review_Product_Activity.this)
                                .setTitle(" Sorry ")
                                .setMessage("You already comment here \n because one product one review ")
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    }
                    else {
                        comment(productID, shopUserID, globlecutouser_id, custumerName,custumerEmail,productName,
                                productURL, reviewComment, coinupdet1, globalCustomerInfoId1,globalCustomerImageURL);
                    }


                }

            }
        });

    }

    public void comment(final String productID,final String shopUserID, final String customerID, final String custumerName, final String reviewCustomerEmail,
                        final String productName , final String productImageURL, final  String reviewComment, final int coinupdet1, final String globalCustomerInfoId1,String globalCustomerImageURL){


        final CollectionReference Review = FirebaseFirestore.getInstance()
                .collection("GlobleProduct").document(productID).collection("review");

        final CollectionReference ReviewShop = FirebaseFirestore.getInstance()
                .collection("users").document(shopUserID).collection("Product");


        final CollectionReference ReviewlistInshop = FirebaseFirestore.getInstance()
                .collection("users").document(shopUserID).collection("reviewall");


        final CollectionReference Reviewcustomer = FirebaseFirestore.getInstance()
                .collection("Globlecustomers").document(customerID).collection("info");

        Date calendar1 = Calendar.getInstance().getTime();
        DateFormat df1 = new SimpleDateFormat("yyMMddHHmm");
        String todayString = df1.format(calendar1);
        final int datereview = Integer.parseInt(todayString);


        final Map<String, Object> reviewindivsualProdut = new HashMap<>();

        reviewindivsualProdut.put("reviewCustomerID",customerID);
        reviewindivsualProdut.put("reviewShopMainID",shopUserID);
        reviewindivsualProdut.put("reviewCustomerName",custumerName);
        reviewindivsualProdut.put("reviewCustomerEmail",reviewCustomerEmail);
        reviewindivsualProdut.put("reviewComment",reviewComment);
        reviewindivsualProdut.put("dateAndTime",datereview);
        reviewindivsualProdut.put("producrName",productName);
        reviewindivsualProdut.put("productEmail",productImageURL);
        reviewindivsualProdut.put("reviewCustomerImageURL",globalCustomerImageURL);

        Review.document(customerID).set(reviewindivsualProdut).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    ReviewShop.document(productID).collection("review").document(customerID).set(reviewindivsualProdut).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                ReviewlistInshop.document(customerID).set(reviewindivsualProdut).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            int coinUpadate = coinupdet1+10;

                                            Reviewcustomer.document(globalCustomerInfoId1).update("coine",coinUpadate)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            progressDialog.dismiss();
                                                            revieweditText.setText("");
                                                            new MaterialAlertDialogBuilder(Review_Product_Activity.this)
                                                                    .setTitle(" You win 10 coin ")
                                                                    .setMessage("This is for your review gift ")
                                                                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            dialogInterface.dismiss();
                                                                        }
                                                                    })
                                                                    .show();

                                                        }
                                                    });

                                        }


                                    }
                                });
                            }

                        }
                    });
                }


            }
        });


    }


    public void OnerCommentcomment(final String productID,final String shopUserID, final String productName , final String productImageURL, final  String reviewComment){


        final CollectionReference Review = FirebaseFirestore.getInstance()
                .collection("GlobleProduct").document(productID).collection("review");

        final CollectionReference ReviewShop = FirebaseFirestore.getInstance()
                .collection("users").document(shopUserID).collection("Product");

        Date calendar1 = Calendar.getInstance().getTime();
        DateFormat df1 = new SimpleDateFormat("yyMMddHHmm");
        String todayString = df1.format(calendar1);
        final int datereview = Integer.parseInt(todayString);


        final Map<String, Object> reviewindivsualProdut = new HashMap<>();


        reviewindivsualProdut.put("reviewShopMainID",shopUserID);
        reviewindivsualProdut.put("reviewCustomerName",ShopNameup+"(Product Owner)");
        reviewindivsualProdut.put("reviewComment",reviewComment);
        reviewindivsualProdut.put("dateAndTime",datereview);
        reviewindivsualProdut.put("producrName",productName);
        reviewindivsualProdut.put("productEmail",productImageURL);
        reviewindivsualProdut.put("reviewCustomerImageURL",ShopImageUrlup);

        Review.document().set(reviewindivsualProdut).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    ReviewShop.document(productID).collection("review").document().set(reviewindivsualProdut).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                revieweditText.setText("");
                                progressDialog.dismiss();
                            }
                        }
                    });
                }


            }
        });


    }



}
