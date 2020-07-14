package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrsoftit.dukander.modle.GlobleCustomerNote;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;

    boolean enterMainActivity = true;
    String cType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                .withFullScreen()
                .withSplashTimeOut(3000)
                .withBackgroundColor(Color.WHITE)
                .withFooterText("Wellcome to a2z loja")
                .withLogo(R.mipmap.ic_launcher_foreground);


        final FirebaseAnalytics mFirebaseAnalytics;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            Uri uri = Uri.parse(String.valueOf(deepLink));

                            String getid = deepLink.getQueryParameter("proID");

                            List<String> list = new ArrayList<>();

                            for (String retval : getid.split("-")) {
                                list.add(retval);
                            }

                            String productCategoryup = list.get(1);
                            String productID = list.get(2);
                            String userID = list.get(3);

                            if (productCategoryup.contains("product")) {
                                enterMainActivity = false;
                                Intent intent = new Intent(SplashScreenActivity.this, ProductFullViewOrderActivity.class);
                                intent.putExtra("proIdURL", productID);
                                intent.putExtra("fromURL", "product");
                                startActivity(intent);
                                finish();
                            }
                            if (productCategoryup.contains("shop")) {
                                enterMainActivity = false;
                                Intent intent = new Intent(SplashScreenActivity.this, ShopViewActivity.class);
                                intent.putExtra("proIdURL", productID);
                                intent.putExtra("user_IdURL", userID);
                                intent.putExtra("fromURL", "shop");

                                startActivity(intent);
                                finish();
                            }
                            if (productCategoryup.contains("refer")) {
                                enterMainActivity = false;
                                Intent intent = new Intent(SplashScreenActivity.this, CustomerLoginActivity.class);
                                intent.putExtra("UserID", productID);
                                intent.putExtra("id", userID);
                                intent.putExtra("fromURL", "refer");

                                startActivity(intent);
                                finish();
                            }

                        }

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


        if (mAuth.getCurrentUser() != null) {

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String user_id = currentUser.getUid();

            CollectionReference Info = FirebaseFirestore.getInstance()
                    .collection("Globlecustomers").document(user_id).collection("info");

            Info.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            GlobleCustomerNote globleCustomerNote = document.toObject(GlobleCustomerNote.class);
                            cType = globleCustomerNote.getCustomerType();
                        }

                        if (cType != null) {

                            startActivity(new Intent(SplashScreenActivity.this, GlobleProductListActivity.class));
                            finish();

                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, PinViewActivity.class));
                            finish();
                        }
                    }
                }
            });

        }

        else if (enterMainActivity == true) {
            startActivity(new Intent(SplashScreenActivity.this, GlobleProductListActivity.class));
            finish();
        }


    View easySplashScreen = config.create();

    setContentView(easySplashScreen);

}

    public boolean checkIntert(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }
}
