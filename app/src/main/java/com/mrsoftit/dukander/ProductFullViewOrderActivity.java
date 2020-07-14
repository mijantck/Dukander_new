package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Constants;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mrsoftit.dukander.adapter.GlobleProductListAdapter6;
import com.mrsoftit.dukander.adapter.ReviewAdapter;
import com.mrsoftit.dukander.modle.GlobleCustomerNote;
import com.mrsoftit.dukander.modle.GlobleProductNote6;
import com.mrsoftit.dukander.modle.ReviewComentNote;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.provider.SettingsSlicesContract.BASE_URI;

public class ProductFullViewOrderActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    String globlecutouser_id ;
    private FirebaseAuth mAuth;

    private Uri dynamicLink1 = null;


    DecimalFormat df2 = new DecimalFormat("#.##");

    GlobleProductListAdapter6 globleProductListAdapter6;

    CollectionReference GlobleProduct = FirebaseFirestore.getInstance()
            .collection("GlobleProduct");



    TextView reviewID;

    Double commonPrice;
    private String proIdup,fromURL, proNameup,proPriceup,productCodeup,productPrivacyup,proImgeUrlup,
            ShopNameup,ShopPhoneup,ShopAddressup,ShopImageUrlup,ShopIdup,UserIdup,productCategoryup,dateup,
            proQuaup,discuntup,tokenup,sizeup,descriptuionup,typeup,colorup;



    Button orderButton,ChartButton;
   private ImageView productImageDetail;
   private TextView ProductNameDetails,shareButton,inStockDetails,productPriceDetails,
           shopDetailName,shopDetailPhone,shopDetailAddress,ProductCode,sellPrice,LigalPrice,
           tetViewDiescunt,ProductDescriptionView,ProductcolorView,productType,ProductSizeView;

   private EditText productQuantidyfromCustomer,size,colorForOrder,typeforeOrder;

    private  double proPrice,proQua;
    private  int date,pruductDiscount;


    private RatingBar BarShop,BarProductSet,BarProductview;


    int coinupdet;
    String globleCustomerName;
    String globleCustomerEmail;
    String globalCustomerInfoId;

    String q ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_full_view_order);
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
                Intent intent = new Intent(ProductFullViewOrderActivity.this,GlobleProductListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        productImageDetail = findViewById(R.id.productImageDetail);
        ProductNameDetails  = findViewById(R.id.ProductNameDetails);
        inStockDetails = findViewById(R.id.inStockDetails);
        productPriceDetails = findViewById(R.id.productPriceDetails);
        shopDetailName = findViewById(R.id.shopDetailName);
        shopDetailPhone = findViewById(R.id.shopDetailPhone);
        ChartButton = findViewById(R.id.ChartButton);
        shopDetailAddress = findViewById(R.id.shopDetailAddress);
        ProductCode =findViewById(R.id.ProductCode);
        shareButton =findViewById(R.id.shareButton);
        reviewID =findViewById(R.id.reviewID);
        size =findViewById(R.id.size);

        LigalPrice =findViewById(R.id.LigalPrice);
        tetViewDiescunt =findViewById(R.id.tetViewDiescunt);
        sellPrice =findViewById(R.id.sellPrice);
        ProductDescriptionView =findViewById(R.id.ProductDescriptionView);
        ChartButton =findViewById(R.id.ChartButton);
        productQuantidyfromCustomer = findViewById(R.id.productQuantidyfromCustomer);
        orderButton =findViewById(R.id.orderButton);

        ProductcolorView =findViewById(R.id.ProductcolorView);
        productType =findViewById(R.id.productType);
        ProductSizeView =findViewById(R.id.ProductSizeView);


        colorForOrder =findViewById(R.id.colorForOrder);
        typeforeOrder =findViewById(R.id.typeforeOrder);

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
                ProductNameDetails.setText(proNameup);
            }

            if (bundle.getString("proPriceup")!=null){
                proPriceup = bundle.getString("proPriceup");
                proPrice = Double.parseDouble(proPriceup);
                productPriceDetails.setText(proPriceup);
                LigalPrice.setText(proPriceup+"");
            }

            if (bundle.getString("productCodeup")!=null){
                productCodeup = bundle.getString("productCodeup");
                ProductCode.setText(productCodeup);
            }
            if (bundle.getString("productPrivacyup")!=null){
                productPrivacyup = bundle.getString("productPrivacyup");
            }
            if (bundle.getString("proImgeUrlup")!=null){
                proImgeUrlup = bundle.getString("proImgeUrlup");
                String Url = proImgeUrlup;
                Picasso.get().load(Url).into(productImageDetail);
            }
            if (bundle.getString("ShopNameup")!=null){
                ShopNameup = bundle.getString("ShopNameup");
                shopDetailName.setText(ShopNameup);

            }
            if (bundle.getString("ShopPhoneup")!=null){
                ShopPhoneup = bundle.getString("ShopPhoneup");
                shopDetailPhone.setText(ShopPhoneup);

            }
            if (bundle.getString("ShopAddressup")!=null){
                ShopAddressup = bundle.getString("ShopAddressup");
                shopDetailAddress.setText(ShopAddressup);

            }
            if (bundle.getString("ShopImageUrlup")!=null){
                ShopImageUrlup = bundle.getString("ShopImageUrlup");
            }
            if (bundle.getString("ShopIdup")!=null){
                ShopIdup = bundle.getString("ShopIdup");
            }
            if (bundle.getString("UserIdup")!=null){
                UserIdup = bundle.getString("UserIdup");
            }if (bundle.getString("tokenup")!=null){
                tokenup = bundle.getString("tokenup");
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
                if (proQua<=0){
                    inStockDetails.setText("Stock out");
                    inStockDetails.setTextColor(Color.RED);
                }else {
                    inStockDetails.setText("In stock");
                    inStockDetails.setTextColor(Color.GREEN);
                }
            }

            if (bundle.getString("color")!=null){
                colorup =bundle.getString("color");
                ProductcolorView.setText(colorup);
            }
            if (bundle.getString("type")!=null){
                typeup =bundle.getString("type");
                productType.setText(typeup);
            }
            if (bundle.getString("size")!=null){
                sizeup=bundle.getString("size");
                ProductSizeView.setText(sizeup);
            }
            if (bundle.getString("descriptuion")!=null){
                descriptuionup =bundle.getString("descriptuion");
                ProductDescriptionView.setText(descriptuionup);
            }
            if (bundle.getString("discuntup")!=null){
                discuntup = bundle.getString("discuntup");
                pruductDiscount =Integer.parseInt(discuntup);
                Double d2 =Double.valueOf(pruductDiscount);
                commonPrice = calcuateDiscount(proPrice,d2);
                productPriceDetails.setText(calcuateDiscount(proPrice,d2)+"");
                sellPrice.setText(calcuateDiscount(proPrice,d2)+"");
                tetViewDiescunt.setText(discuntup);
                LigalPrice.setPaintFlags(LigalPrice.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

            }else {
                commonPrice = proPrice;
                productPriceDetails.setText(proPrice+"");
                sellPrice.setText(proPrice+"");

            }

            if ( bundle.getString("descriptionup")!=null){
                ProductDescriptionView.setText( bundle.getString("descriptionup"));
            }
        }

        if (proIdup !=null && fromURL !=null ){

            CollectionReference fromURLToCollection = FirebaseFirestore.getInstance()
                    .collection("GlobleProduct");

            Query query = fromURLToCollection.whereEqualTo("proId",proIdup);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()){

                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                           GlobleProductNote model = document.toObject(GlobleProductNote.class);
                            Toast.makeText(ProductFullViewOrderActivity.this, model.getProName()+" ", Toast.LENGTH_SHORT).show();
                            ProductNameDetails.setText(model.getProName());
                            proNameup = model.getProName();

                            productPriceDetails.setText(model.getProPrice()+"");
                            proPriceup = model.getProPrice()+"";

                            if (model.getProImgeUrl()!=null){
                                String Url = model.getProImgeUrl();
                                Picasso.get().load(Url).into(productImageDetail);
                                proImgeUrlup = model.getProImgeUrl();
                            }
                            if (model.getProQua()<=0){
                                inStockDetails.setText("Stock out");
                                inStockDetails.setTextColor(Color.RED);

                            }else {
                                inStockDetails.setText("In stock");
                                inStockDetails.setTextColor(Color.GREEN);
                            }

                            Date calendar1 = Calendar.getInstance().getTime();
                            DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
                            String todayString = df1.format(calendar1);
                            int datenew = Integer.parseInt(todayString);
                            if (model.getPruductDiscount()>0){
                                Double d2 =Double.valueOf(model.getPruductDiscount());
                                productPriceDetails.setText(calcuateDiscount(model.getProPrice(),d2)+"");
                                discuntup = model.getPruductDiscount()+"";
                            }
                            if (model.getProductCode() !=null){
                                ProductCode.setText(model.getProductCode());
                            }
                            if (model.getShopName() != null){
                                shopDetailName.setText(model.getShopName());
                            }
                            if (model.getShopPhone() !=null){
                                shopDetailPhone.setText(model.getShopPhone());
                            }
                            if (model.getShopAddress()!=null){
                                shopDetailAddress.setText(model.getShopAddress());
                            }

                        }
                    }


                }
            });

        }



        reviewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductFullViewOrderActivity.this, Review_Product_Activity.class);
                intent.putExtra("proIdup",proIdup);
                intent.putExtra("proNameup",proNameup);
                intent.putExtra("ShopImageUrlup",proImgeUrlup);
                intent.putExtra("UserIdup",UserIdup);
                intent.putExtra("productTokenup",tokenup);
                startActivity(intent);

            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProductFullViewOrderActivity.this, "click ", Toast.LENGTH_SHORT).show();

                String sharelinktext  =  "https://a2zloja.page.link/?"+
                        "link=https://a2zloja.page.link/jdF1?"+
                        "proID="+"-"+"product"+
                        "-"+proIdup+
                        "-"+proIdup+
                        "&st="+proNameup+
                        "&sd="+"Price "+proPriceup+ "\ndiscunt ="+discuntup+
                        "&si="+proImgeUrlup+
                        "&apn="+getPackageName();



                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                       // .setLongLink(dynamicLink.getUri())
                        .setLongLink(Uri.parse(sharelinktext))// manually
                        .buildShortDynamicLink()
                        .addOnCompleteListener(ProductFullViewOrderActivity.this, new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    // Short link created
                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();
                                    Log.e("main ", "short link "+ shortLink.toString());
                                   // share app dialog
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT,  shortLink.toString());
                                    intent.setType("text/plain");
                                    startActivity(intent);


                                } else {
                                    // Error
                                    // ...
                                    Log.e("main", " error "+task.getException() );

                                }
                            }
                        });

            }
        });
        productQuantidyfromCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String s1 = s.toString().trim();
                if (!s1.isEmpty()){
                    double ProductQuantidy =Double.parseDouble(s1);
                    double sumPrice = ProductQuantidy*commonPrice;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        productPriceDetails.setText(df2.format( sumPrice)+"");
                    }
                }else {

                    productPriceDetails.setText(commonPrice+"");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(ProductFullViewOrderActivity.this)
                        .setTitle("Sorry... its working now")
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentUser ==null){
                    new MaterialAlertDialogBuilder(ProductFullViewOrderActivity.this)
                            .setTitle("you have not signup")
                            .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    startActivity(new Intent(ProductFullViewOrderActivity.this,CustomerLoginActivity.class));
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
                    return;
                }


                q =  productQuantidyfromCustomer.getText().toString();;

                Toast.makeText(ProductFullViewOrderActivity.this, q, Toast.LENGTH_SHORT).show();
                if ( !q.isEmpty()){
                    Intent intent = new Intent(ProductFullViewOrderActivity.this, ConfirmOrderActivity.class);
                    intent.putExtra("proIdup",proIdup);
                    intent.putExtra("proNameup",proNameup);
                    intent.putExtra("proPriceup",proPriceup);
                    intent.putExtra("proPriceupSingle",proPriceup);
                    intent.putExtra("commonPrice",productPriceDetails.getText().toString());
                    intent.putExtra("productCodeup",productCodeup);
                    intent.putExtra("productPrivacyup",productPrivacyup);
                    intent.putExtra("proImgeUrlup",proImgeUrlup);
                    intent.putExtra("ShopNameup",ShopNameup);
                    intent.putExtra("ShopPhoneup",ShopPhoneup);
                    intent.putExtra("ShopAddressup",ShopAddressup);
                    intent.putExtra("ShopImageUrlup",ShopImageUrlup);
                    intent.putExtra("ShopIdup",ShopIdup);
                    intent.putExtra("UserIdup",UserIdup);
                    intent.putExtra("productCategoryup",productCategoryup);
                    intent.putExtra("dateup",dateup);
                    intent.putExtra("proQuaup",productQuantidyfromCustomer.getText().toString());
                    intent.putExtra("discuntup",discuntup);
                    intent.putExtra("tokenup",tokenup);
                    intent.putExtra("Size",size.getText().toString());
                    intent.putExtra("color", colorForOrder.getText().toString());
                    intent.putExtra("type", typeforeOrder.getText().toString());
                    startActivity(intent);
                }else {
                    Toast.makeText(ProductFullViewOrderActivity.this, " please fill up quantity ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        reletedProduct(productCategoryup);
    }






    static double calcuateDiscount(double markedprice, double s) {
        double dis = 100-s;
        double amount= (dis*markedprice)/100;

        return amount;

    }




    public void  reletedProduct(String catagory){


            Query query = GlobleProduct.whereEqualTo("productCategory",catagory).whereEqualTo("productPrivacy","Public");

            FirestoreRecyclerOptions<GlobleProductNote6> options = new FirestoreRecyclerOptions.Builder<GlobleProductNote6>()
                    .setQuery(query, GlobleProductNote6.class)
                    .build();

            globleProductListAdapter6 = new GlobleProductListAdapter6(options);


            RecyclerView recyclerView = findViewById(R.id.fullProductViewReletetProuct);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductFullViewOrderActivity.this,RecyclerView.HORIZONTAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(globleProductListAdapter6);
           globleProductListAdapter6.startListening();

            globleProductListAdapter6.setOnItemClickListener(new GlobleProductListAdapter6.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    final GlobleProductNote6 globleProductNote = documentSnapshot.toObject(GlobleProductNote6.class);
                    Intent intent = new Intent(ProductFullViewOrderActivity.this, ProductFullViewOrderActivity.class);
                    intent.putExtra("proIdup",globleProductNote.getProId());
                    intent.putExtra("proNameup",globleProductNote.getProName());
                    intent.putExtra("proPriceup",globleProductNote.getProPrice()+"");
                    intent.putExtra("productCodeup",globleProductNote.getProductCode());
                    intent.putExtra("productPrivacyup",globleProductNote.getProductPrivacy());
                    intent.putExtra("proImgeUrlup",globleProductNote.getProImgeUrl());
                    intent.putExtra("ShopNameup",globleProductNote.getShopName());
                    intent.putExtra("ShopPhoneup",globleProductNote.getShopPhone());
                    intent.putExtra("ShopAddressup",globleProductNote.getShopAddress());
                    intent.putExtra("ShopImageUrlup",globleProductNote.getShopImageUrl());
                    intent.putExtra("ShopIdup",globleProductNote.getShopId());
                    intent.putExtra("UserIdup",globleProductNote.getUserId());
                    intent.putExtra("productCategoryup",globleProductNote.getProductCategory());
                    intent.putExtra("dateup",globleProductNote.getDate()+"");
                    intent.putExtra("proQuaup",globleProductNote.getProQua()+"");
                    intent.putExtra("discuntup",globleProductNote.getPruductDiscount()+"");
                    intent.putExtra("tokenup",globleProductNote.getToken());
                    intent.putExtra("size",globleProductNote.getSize());
                    intent.putExtra("color",globleProductNote.getColor());
                    intent.putExtra("type", globleProductNote.getType());
                    intent.putExtra("descriptuion", globleProductNote.getDescription());

                    startActivity(intent);

                }
            });



    }

}
