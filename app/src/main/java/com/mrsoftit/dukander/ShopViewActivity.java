package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.mrsoftit.dukander.modle.Shop_list_note;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ShopViewActivity extends AppCompatActivity {

    String user_id,shop_id,shop_name,Shop_phone,shop_addres,shopImgeUrl,shop;

    ImageView shopImage;
    TextView name,phone,address,shareButtonShop;
    GlobleProductListAdapter6 globleProductListAdapter6;

    CollectionReference GlobleSoplist = FirebaseFirestore.getInstance()
            .collection("GlobleSoplist");

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_view);


        progressDialog = new ProgressDialog(ShopViewActivity.this);
        // Setting Message
        progressDialog.setMessage("Loading..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        shopImage = findViewById(R.id.shop_list_image_view_activity);
        name = findViewById(R.id.shop_list_view_name);
        phone = findViewById(R.id.shop_list_view_phone);
        address = findViewById(R.id.shop_list_view_address);
        shareButtonShop = findViewById(R.id.shareButtonShop);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopViewActivity.this,ShopListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.getString("proIdURL")!=null){
                shop_id = bundle.getString("proIdURL");
            }
            if ( bundle.getString("user_IdURL")!=null){
                user_id = bundle.getString("user_IdURL");
            }
            if (bundle.getString("fromURL") !=null){
                shop = bundle.getString("fromURL");
            }

            if (bundle.getString("useId") !=null){
                user_id = bundle.getString("useId");

            }
            if (bundle.getString("shpId") !=null){
                shop_id = bundle.getString("shpId");

            }

            if (bundle.getString("shopImageURL")!=null){
                String Url = bundle.getString("shopImageURL");
                Picasso.get().load(Url).into(shopImage);
               shopImgeUrl = bundle.getString("shopImageURL");
            }
            shop_name = bundle.getString("shopName");
            name.setText(shop_name);
            Shop_phone = bundle.getString("shopPhone");
            phone.setText(Shop_phone);
            shop_addres = bundle.getString("shopAdderss");
            address.setText(shop_addres);
        }

        if ( shop !=null ){

            Query query = GlobleSoplist.whereEqualTo("shopId",shop_id);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Shop_list_note model = document.toObject(Shop_list_note.class);

                            if (model.getShopImageURL()!=null) {
                                String Url = model.getShopImageURL();
                                Picasso.get().load(Url).into(shopImage);
                                shopImgeUrl = model.getShopImageURL();

                            }
                            if (model.getShopName()!=null){
                                name.setText(model.getShopName());
                                shop_name = model.getShopName();
                            }
                            if (model.getShopPhone()!=null){
                                phone.setText(model.getShopPhone());
                                Shop_phone = model.getShopPhone();
                            }
                            if (model.getShopAddress()!=null){
                                address.setText(model.getShopAddress());
                                shop_addres = model.getShopAddress();
                            }

                        }
                    }



                }
            });
        }
        shareButtonShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String sharelinktext  = "https://a2zloja.page.link/?"+
                        "link=https://a2zloja.page.link/jdF1?"+
                        "proID="+"-"+"shop"+
                        "-"+shop_id+
                        "-"+user_id+
                        "&st="+shop_name+
                        "&sd="+"Contract us this is "+
                        "&si="+shopImgeUrl+
                        "&apn="+getPackageName();
                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        // .setLongLink(dynamicLink.getUri())
                        .setLongLink(Uri.parse(sharelinktext))// manually
                        .buildShortDynamicLink()
                        .addOnCompleteListener(ShopViewActivity.this, new OnCompleteListener<ShortDynamicLink>() {
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


        if (user_id !=null && shop_id!=null){
            CollectionReference ShopGlobleproduct = FirebaseFirestore.getInstance()
                    .collection("GlobleProduct");
            Query query = ShopGlobleproduct.whereEqualTo("productPrivacy","Public")
                    .whereEqualTo("productVerification","certify").whereEqualTo("UserId",user_id);

        FirestoreRecyclerOptions<GlobleProductNote6> options = new FirestoreRecyclerOptions.Builder<GlobleProductNote6>()
                .setQuery(query, GlobleProductNote6.class)
                .build();

        globleProductListAdapter6 = new GlobleProductListAdapter6(options);

        RecyclerView recyclerView = findViewById(R.id.selectetShopProductShow);
        recyclerView.setHasFixedSize(true);
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GlobleProductListActivity.this,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(globleProductListAdapter6);
            globleProductListAdapter6.startListening();
            globleProductListAdapter6.setOnItemClickListener(new GlobleProductListAdapter6.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                final GlobleProductNote6 globleProductNote = documentSnapshot.toObject(GlobleProductNote6.class);
                Intent intent = new Intent(ShopViewActivity.this, ProductFullViewOrderActivity.class);
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
                intent.putExtra("tokenup",globleProductNote.getToken());
                intent.putExtra("discuntup",globleProductNote.getPruductDiscount()+"");

                intent.putExtra("size",globleProductNote.getSize());
                intent.putExtra("color",globleProductNote.getColor());
                intent.putExtra("type", globleProductNote.getType());
                intent.putExtra("descriptuion", globleProductNote.getDescription());

                startActivity(intent);

            }
        });

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                allProductShow(query.toLowerCase());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                allProductShow(newText.toLowerCase());
                return false;
            }
        });
        return true;
    }

    private void allProductShow(String productName) {

            CollectionReference ShopGlobleproduct = FirebaseFirestore.getInstance()
                    .collection("users").document(user_id).collection("Product");

            Query query = ShopGlobleproduct.orderBy("search").startAt(productName.toLowerCase()).endAt(productName.toLowerCase() + "\uf8ff")
                    .whereEqualTo("productPrivacy", "Public").whereEqualTo("productVerification","certify");


            FirestoreRecyclerOptions<GlobleProductNote6> options = new FirestoreRecyclerOptions.Builder<GlobleProductNote6>()
                    .setQuery(query, GlobleProductNote6.class)
                    .build();

            globleProductListAdapter6 = new GlobleProductListAdapter6(options);

            RecyclerView recyclerView = findViewById(R.id.selectetShopProductShow);
            recyclerView.setHasFixedSize(true);
            // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GlobleProductListActivity.this,RecyclerView.HORIZONTAL,false);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            //  recyclerView.setLayoutManager(ne4 LinearLayoutManager(this));
            // recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(globleProductListAdapter6);
            globleProductListAdapter6.startListening();
            globleProductListAdapter6.setOnItemClickListener(new GlobleProductListAdapter6.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    final GlobleProductNote6 globleProductNote = documentSnapshot.toObject(GlobleProductNote6.class);
                    Intent intent = new Intent(ShopViewActivity.this, ProductFullViewOrderActivity.class);
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

                    intent.putExtra("size",globleProductNote.getSize());
                    intent.putExtra("color",globleProductNote.getColor());
                    intent.putExtra("type", globleProductNote.getType());
                    intent.putExtra("descriptuion", globleProductNote.getDescription());
                    startActivity(intent);

                }
            });



    }
    @Override
    protected void onStart() {
        super.onStart();

        globleProductListAdapter6.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        globleProductListAdapter6.stopListening();
    }
}
