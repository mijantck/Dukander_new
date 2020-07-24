package com.mrsoftit.dukander;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mrsoftit.dukander.adapter.GlobleProductListAdapter6;
import com.mrsoftit.dukander.adapter.ProductOrderAdapter;
import com.mrsoftit.dukander.modle.GlobleProductNote6;

public class wishListActivity extends AppCompatActivity {


    GlobleProductListAdapter6 globleProductListAdapter6;

    FirebaseUser currentUser ;
    ProductOrderAdapter productOrderAdapter ;
    String  user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        currentUser  = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            user_id =currentUser.getUid();

            comomCatagoryProductShow();
        }else {
            new MaterialAlertDialogBuilder(wishListActivity.this)
                    .setMessage("you have not signup")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            startActivity(new Intent(wishListActivity.this,CustomerLoginActivity.class));
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

    private void comomCatagoryProductShow() {

        CollectionReference customerForAddCrat = FirebaseFirestore.getInstance()
                .collection("Globlecustomers").document(user_id).collection("fevertList");

        Query query = customerForAddCrat.orderBy("date", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<GlobleProductNote6> options = new FirestoreRecyclerOptions.Builder<GlobleProductNote6>()
                .setQuery(query, GlobleProductNote6.class)
                .build();

        globleProductListAdapter6 = new GlobleProductListAdapter6(options);

        RecyclerView recyclerView = findViewById(R.id.addcart);
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

                Intent intent = new Intent(wishListActivity.this, ProductFullViewOrderActivity.class);
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
