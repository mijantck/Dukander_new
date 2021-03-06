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
import android.graphics.Paint;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrsoftit.dukander.adapter.GlobleProductListAdapter6;
import com.mrsoftit.dukander.modle.GlobleProductNote6;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
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

public class ProductFullViewOrderActivity extends AppCompatActivity {

    FirebaseUser currentUser;
    String globlecutouser_id ;
    private FirebaseAuth mAuth;

    private Uri dynamicLink1 = null;


    SliderView sliderView;
    private SliderAdapterExamplepro adapter;
    List<SliderItem> sliderItemList;
    SliderItem sliderItem;
    SliderItem sliderItem1;
    SliderItem sliderItem2;
    SliderItem sliderItem3;
    SliderItem sliderItem4;
    String cType;

    DecimalFormat df2 = new DecimalFormat("#.##");

    GlobleProductListAdapter6 globleProductListAdapter6;

    CollectionReference GlobleProduct = FirebaseFirestore.getInstance()
            .collection("GlobleProduct");

   MaterialButton favorite,add_shopping;


    TextView reviewID;

    Double commonPrice;
    private String proIdup,fromURL, proNameup,proPriceup,productCodeup,productPrivacyup,proImgeUrlup,
            proImgeUrlup1,proImgeUrlup2,proImgeUrlup3,proImgeUrlup4, ShopNameup,ShopPhoneup,ShopAddressup,
            ShopImageUrlup,ShopIdup,UserIdup,productCategoryup,dateup,
            proQuaup,discuntup,tokenup,sizeup,descriptuionup,typeup,colorup;

    ProgressDialog progressDialog;


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

        progressDialog = new ProgressDialog(ProductFullViewOrderActivity.this);
        // Setting Message
        progressDialog.setMessage("Loading..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        sliderItemList = new ArrayList<>();

        sliderItem = new SliderItem();
        sliderItem1 = new SliderItem();
        sliderItem2 = new SliderItem();
        sliderItem3 = new SliderItem();
        sliderItem4 = new SliderItem();

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
        sliderView = findViewById(R.id.imageSliderFullProductView);



        favorite = findViewById(R.id.favorite);
        add_shopping = findViewById(R.id.add_shopping);

        shopDetailAddress = findViewById(R.id.shopDetailAddress);
        ProductCode =findViewById(R.id.ProductCode);
        shareButton =findViewById(R.id.shareButton);
        reviewID =findViewById(R.id.reviewID);
        size =findViewById(R.id.size);

        LigalPrice =findViewById(R.id.LigalPrice);
        tetViewDiescunt =findViewById(R.id.tetViewDiescunt);
        sellPrice =findViewById(R.id.sellPrice);
        ProductDescriptionView =findViewById(R.id.ProductDescriptionView);

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

                sliderItem.setImageUrl(proImgeUrlup);
                sliderItemList.add(sliderItem);
              //  String Url = proImgeUrlup;
               // Picasso.get().load(Url).into(productImageDetail);
            }
            if (bundle.getString("proImgeUrlup1")!=null){
                proImgeUrlup1 = bundle.getString("proImgeUrlup1");

                sliderItem1.setImageUrl(proImgeUrlup1);
                sliderItemList.add(sliderItem1);
                //  String Url = proImgeUrlup;
                // Picasso.get().load(Url).into(productImageDetail);
            }
            if (bundle.getString("proImgeUrlup2")!=null){
                proImgeUrlup2 = bundle.getString("proImgeUrlup2");

                sliderItem2.setImageUrl(proImgeUrlup2);
                sliderItemList.add(sliderItem2);
                //  String Url = proImgeUrlup;
                // Picasso.get().load(Url).into(productImageDetail);
            }
            if (bundle.getString("proImgeUrlup3")!=null){
                proImgeUrlup3 = bundle.getString("proImgeUrlup3");

                sliderItem3.setImageUrl(proImgeUrlup3);

                sliderItemList.add(sliderItem3);
                //  String Url = proImgeUrlup;
                // Picasso.get().load(Url).into(productImageDetail);
            }
            if (bundle.getString("proImgeUrlup4")!=null){
                proImgeUrlup4 = bundle.getString("proImgeUrlup4");
                sliderItem4.setImageUrl(proImgeUrlup4);
                sliderItemList.add(sliderItem4);
                //  String Url = proImgeUrlup;
                // Picasso.get().load(Url).into(productImageDetail);
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

        adapter = new SliderAdapterExamplepro(this);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        adapter.renewItems(sliderItemList);

        reletedProduct(productCategoryup);


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
                progressDialog.show();
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


                                    progressDialog.dismiss();
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


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProductaddInCustomer("f",ShopNameup,ShopPhoneup,ShopAddressup,ShopIdup,UserIdup,tokenup,proIdup,
                        proNameup,proPriceup,proQuaup,productCodeup,productPrivacyup,productCategoryup,dateup,proImgeUrlup,
                        discuntup,sizeup,colorup,typeup,descriptuionup);


            }
        });
        add_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ProductaddInCustomer("chat",ShopNameup,ShopPhoneup,ShopAddressup,ShopIdup,UserIdup,tokenup,proIdup,
                        proNameup,proPriceup,proQuaup,productCodeup,productPrivacyup,productCategoryup,dateup,proImgeUrlup,
                        discuntup,sizeup,colorup,typeup,descriptuionup);

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




    }

    @Override
    protected void onStart() {
        super.onStart();
        globleProductListAdapter6.startListening();

       progressDialog.dismiss();


    }

    static double calcuateDiscount(double markedprice, double s) {
        double dis = 100-s;
        double amount= (dis*markedprice)/100;

        return amount;

    }




    public void  reletedProduct(String catagory){


            Query query = GlobleProduct.whereEqualTo("productCategory",catagory).whereEqualTo("productPrivacy","Public").limit(14);

            FirestoreRecyclerOptions<GlobleProductNote6> options = new FirestoreRecyclerOptions.Builder<GlobleProductNote6>()
                    .setQuery(query, GlobleProductNote6.class)
                    .build();

            globleProductListAdapter6 = new GlobleProductListAdapter6(options);


            RecyclerView recyclerView = findViewById(R.id.fullProductViewReletetProuct);
            recyclerView.setHasFixedSize(true);
           // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProductFullViewOrderActivity.this,RecyclerView.HORIZONTAL,false);
            //  recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(globleProductListAdapter6);


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
                    intent.putExtra("proImgeUrlup1",globleProductNote.getProImgeUrl1());
                    intent.putExtra("proImgeUrlup2",globleProductNote.getProImgeUrl2());
                    intent.putExtra("proImgeUrlup3",globleProductNote.getProImgeUrl3());
                    intent.putExtra("proImgeUrlup4",globleProductNote.getProImgeUrl4());
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


    public void ProductaddInCustomer(String wishOrFavert, String DukanName,  String dukanderPhone,  String dukanderAddress,
                                       String dukanderid,  String user_id,  String token,  final String productId,
                                       final String productName, final String productPrice,
                                       String productQuantidy, final String productCode, final String privecy,
                                       final String Catagury, final String date, final String ImageUrl, final String pdicount,
                                       final String size1, final  String color1,final String typ,
                                       final String description){


        if (productQuantidy.isEmpty() ){
            productQuantidy = "1";
        }
        double productPriceD = Double.parseDouble(productPrice);
        double productQuantidyD = Double.parseDouble(productQuantidy);

        int dateI =Integer.parseInt(date);
        int pdicountI =Integer.parseInt(pdicount);


        Map<String, Object> GlobaleProductObject = new HashMap<>();
        GlobaleProductObject.put("proId", productId);
        GlobaleProductObject.put("proName", productName);
        GlobaleProductObject.put("search", productName.toLowerCase());
        GlobaleProductObject.put("proPrice", productPriceD);
        GlobaleProductObject.put("pruductDiscount", pdicountI);
        GlobaleProductObject.put("proImgeUrl",ImageUrl);
        GlobaleProductObject.put("productPrivacy", privecy);
        GlobaleProductObject.put("ShopName", DukanName);
        GlobaleProductObject.put("ShopPhone", dukanderPhone);
        GlobaleProductObject.put("productCode", productCode);
        GlobaleProductObject.put("ShopAddress", dukanderAddress);
        GlobaleProductObject.put("ShopId", dukanderid);
        GlobaleProductObject.put("productCategory", Catagury);
        GlobaleProductObject.put("date", dateI);
        GlobaleProductObject.put("proQua", productQuantidyD);
        GlobaleProductObject.put("UserId", user_id);
        GlobaleProductObject.put("token", token);
        GlobaleProductObject.put("Size", size1);
        GlobaleProductObject.put("color", color1);
        GlobaleProductObject.put("type", typ);
        GlobaleProductObject.put("description", description);


        CollectionReference customerForFeverite = FirebaseFirestore.getInstance()
                .collection("Globlecustomers").document(globlecutouser_id).collection("fevertList");

        CollectionReference customerForAddCrat = FirebaseFirestore.getInstance()
                .collection("Globlecustomers").document(globlecutouser_id).collection("AdddedCart");

        if (wishOrFavert.equals("f")) {
            customerForFeverite.document(productId).set(GlobaleProductObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    new MaterialAlertDialogBuilder(ProductFullViewOrderActivity.this)
                            .setMessage("Product add to your wish list")
                            .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();

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
            });
        } else if (wishOrFavert.equals("chat"))
            customerForAddCrat.document(productId).set(GlobaleProductObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    new MaterialAlertDialogBuilder(ProductFullViewOrderActivity.this)
                            .setMessage("Product add to your chat")
                            .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
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
            });

    }
}
