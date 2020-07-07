package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrsoftit.dukander.adapter.GlobleProductListAdapter6;
import com.mrsoftit.dukander.modle.GlobleProductNote6;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SelecedCatagoryActivity extends AppCompatActivity {


    TextView CatagoryTitel;
    GlobleProductListAdapter6 globleProductListAdapter6;


    String catagoryUp,comomCatagory;

    TextView noData;
    CollectionReference GlobleProduct = FirebaseFirestore.getInstance()
            .collection("GlobleProduct");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleced_catagory);


        Toolbar toolbar = findViewById(R.id.toolbar_support);
        noData = findViewById(R.id.noData);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelecedCatagoryActivity.this, GlobleProductListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        CatagoryTitel = findViewById(R.id.CatagoryTitel);


        final Bundle bundle = getIntent().getExtras();

        if (bundle != null) {

            catagoryUp = bundle.getString("catagory");
            comomCatagory = bundle.getString("comomCatagory");


        }


          if (comomCatagory != null) {
        Query querytest = GlobleProduct.whereEqualTo("comomCatagory", comomCatagory).whereEqualTo("productPrivacy", "Public");
        querytest.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        GlobleProductNote6 globleProductNote6 = document.toObject(GlobleProductNote6.class);

                        if (globleProductNote6.getProName() != null) {
                            noData.setVisibility(View.GONE);
                        }
                    }
                }


            }
        });
    }
          if (catagoryUp != null) {
        Query querytest = GlobleProduct.whereEqualTo("productCategory", catagoryUp).whereEqualTo("productPrivacy", "Public");
        querytest.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        GlobleProductNote6 globleProductNote6 = document.toObject(GlobleProductNote6.class);

                        if (globleProductNote6.getProName() != null) {
                            noData.setVisibility(View.GONE);
                        }
                    }
                }


            }
        });
    }



           if (comomCatagory!=null){

            CatagoryTitel.setText(comomCatagory);

            Query query = GlobleProduct.whereEqualTo("comomCatagory",comomCatagory).whereEqualTo("productPrivacy","Public");

            FirestoreRecyclerOptions<GlobleProductNote6> options = new FirestoreRecyclerOptions.Builder<GlobleProductNote6>()
                    .setQuery(query, GlobleProductNote6.class)
                    .build();

            globleProductListAdapter6 = new GlobleProductListAdapter6(options);


            RecyclerView recyclerView = findViewById(R.id.selectedCatagory);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setAdapter(globleProductListAdapter6);

            globleProductListAdapter6.setOnItemClickListener(new GlobleProductListAdapter6.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    final GlobleProductNote6 globleProductNote = documentSnapshot.toObject(GlobleProductNote6.class);
                    Intent intent = new Intent(SelecedCatagoryActivity.this, ProductFullViewOrderActivity.class);
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
                    startActivity(intent);
                }
            });


        }
          else if (catagoryUp!=null){

            CatagoryTitel.setText(catagoryUp);

            Query query = GlobleProduct.whereEqualTo("productCategory",catagoryUp).whereEqualTo("productPrivacy","Public");

            FirestoreRecyclerOptions<GlobleProductNote6> options = new FirestoreRecyclerOptions.Builder<GlobleProductNote6>()
                    .setQuery(query, GlobleProductNote6.class)
                    .build();

            globleProductListAdapter6 = new GlobleProductListAdapter6(options);


            RecyclerView recyclerView = findViewById(R.id.selectedCatagory);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            recyclerView.setAdapter(globleProductListAdapter6);

               globleProductListAdapter6.setOnItemClickListener(new GlobleProductListAdapter6.OnItemClickListener() {
        @Override
        public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
            final GlobleProductNote6 globleProductNote = documentSnapshot.toObject(GlobleProductNote6.class);
            Intent intent = new Intent(SelecedCatagoryActivity.this, ProductFullViewOrderActivity.class);
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


                if (comomCatagory!=null){
                 comomCatagoryProductShow(query.toLowerCase());
                }else {
                    allProductShow(query.toLowerCase());

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (comomCatagory!=null){
                    comomCatagoryProductShow(newText.toLowerCase());
                }else{
                    allProductShow(newText.toLowerCase());
                }
                return false;
            }
        });
        return true;
    }

    private void allProductShow(String productName) {
        Query query = GlobleProduct.orderBy("search").startAt(productName.toLowerCase()).endAt(productName.toLowerCase()+ "\uf8ff")
                .whereEqualTo("productCategory",catagoryUp).whereEqualTo("productPrivacy","Public");


        FirestoreRecyclerOptions<GlobleProductNote6> options = new FirestoreRecyclerOptions.Builder<GlobleProductNote6>()
                .setQuery(query, GlobleProductNote6.class)
                .build();

        globleProductListAdapter6 = new GlobleProductListAdapter6(options);

        RecyclerView recyclerView = findViewById(R.id.selectedCatagory);
        recyclerView.setHasFixedSize(true);
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GlobleProductListActivity.this,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //  recyclerView.setLayoutManager(ne4 LinearLayoutManager(this));
        // recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(globleProductListAdapter6);
        globleProductListAdapter6.startListening();


        globleProductListAdapter6.setOnItemClickListener(new GlobleProductListAdapter6.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                final GlobleProductNote6 globleProductNote = documentSnapshot.toObject(GlobleProductNote6.class);
                Intent intent = new Intent(SelecedCatagoryActivity.this, ProductFullViewOrderActivity.class);
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
                startActivity(intent);
            }
        });
    }
    private void comomCatagoryProductShow(String productName) {
        Query query = GlobleProduct.orderBy("search").startAt(productName.toLowerCase()).endAt(productName.toLowerCase()+ "\uf8ff")
                .whereEqualTo("comomCatagory",comomCatagory).whereEqualTo("productPrivacy","Public");


        FirestoreRecyclerOptions<GlobleProductNote6> options = new FirestoreRecyclerOptions.Builder<GlobleProductNote6>()
                .setQuery(query, GlobleProductNote6.class)
                .build();

        globleProductListAdapter6 = new GlobleProductListAdapter6(options);

        RecyclerView recyclerView = findViewById(R.id.selectedCatagory);
        recyclerView.setHasFixedSize(true);
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GlobleProductListActivity.this,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        //  recyclerView.setLayoutManager(ne4 LinearLayoutManager(this));
        // recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(globleProductListAdapter6);
        globleProductListAdapter6.startListening();

        globleProductListAdapter6.setOnItemClickListener(new GlobleProductListAdapter6.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                final GlobleProductNote6 globleProductNote = documentSnapshot.toObject(GlobleProductNote6.class);

                Intent intent = new Intent(SelecedCatagoryActivity.this, ProductFullViewOrderActivity.class);
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

