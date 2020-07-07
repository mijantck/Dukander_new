package com.mrsoftit.dukander;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mrsoftit.dukander.adapter.GlobleProductListAdapter6;
import com.mrsoftit.dukander.adapter.ShopListAdapter;
import com.mrsoftit.dukander.modle.GlobleProductNote6;
import com.mrsoftit.dukander.modle.Shop_list_note;

import java.util.Objects;

public class ShopListActivity extends AppCompatActivity {




    ShopListAdapter shopListAdapter;

    CollectionReference GlobleSoplist = FirebaseFirestore.getInstance()
            .collection("GlobleSoplist");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopListActivity.this,GlobleProductListActivity.class);
                startActivity(intent);
                finish();
            }
        });



        Query query = GlobleSoplist;

        FirestoreRecyclerOptions<Shop_list_note> options = new FirestoreRecyclerOptions.Builder<Shop_list_note>()
                .setQuery(query, Shop_list_note.class)
                .build();

        shopListAdapter = new ShopListAdapter(options);


        RecyclerView recyclerView = findViewById(R.id.shopListRecyrview);
        recyclerView.setHasFixedSize(true);
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GlobleProductListActivity.this,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //  recyclerView.setLayoutManager(ne4 LinearLayoutManager(this));
        // recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(shopListAdapter);
        shopListAdapter.setOnItemClickListener(new ShopListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Shop_list_note shop_list_note = documentSnapshot.toObject(Shop_list_note.class);

                Intent shop_list_intent = new Intent(ShopListActivity.this,ShopViewActivity.class);
                shop_list_intent.putExtra("useId",shop_list_note.getShopUserId());
                shop_list_intent.putExtra("shpId",shop_list_note.getShopId());
                shop_list_intent.putExtra("shopName",shop_list_note.getShopName());
                if (shop_list_note.getShopImageURL()!=null){
                    shop_list_intent.putExtra("shopImageURL",shop_list_note.getShopImageURL());

                }
                shop_list_intent.putExtra("shopPhone",shop_list_note.getShopPhone());
                shop_list_intent.putExtra("shopAdderss",shop_list_note.getShopAddress());

                startActivity(shop_list_intent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        shopListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        shopListAdapter.stopListening();
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

                ShopSearch(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ShopSearch(newText);
                return false;
            }
        });
        return true;
    }


    public void ShopSearch(String ShopName){


        Query query = GlobleSoplist.orderBy("search").startAt(ShopName.toLowerCase()).endAt(ShopName.toLowerCase()+ "\uf8ff");

        FirestoreRecyclerOptions<Shop_list_note> options = new FirestoreRecyclerOptions.Builder<Shop_list_note>()
                .setQuery(query, Shop_list_note.class)
                .build();

        shopListAdapter = new ShopListAdapter(options);


        RecyclerView recyclerView = findViewById(R.id.shopListRecyrview);
        recyclerView.setHasFixedSize(true);
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(GlobleProductListActivity.this,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //  recyclerView.setLayoutManager(ne4 LinearLayoutManager(this));
        // recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(shopListAdapter);
        shopListAdapter.startListening();
        shopListAdapter.setOnItemClickListener(new ShopListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Shop_list_note shop_list_note = documentSnapshot.toObject(Shop_list_note.class);

                Intent shop_list_intent = new Intent(ShopListActivity.this,ShopViewActivity.class);
                shop_list_intent.putExtra("useId",shop_list_note.getShopUserId());
                shop_list_intent.putExtra("shpId",shop_list_note.getShopId());
                shop_list_intent.putExtra("shopName",shop_list_note.getShopName());
                shop_list_intent.putExtra("shopPhone",shop_list_note.getShopPhone());
                shop_list_intent.putExtra("shopAdderss",shop_list_note.getShopAddress());
                startActivity(shop_list_intent);
            }
        });

    }
}
