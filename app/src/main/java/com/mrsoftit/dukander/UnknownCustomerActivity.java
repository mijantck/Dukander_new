package com.mrsoftit.dukander;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class UnknownCustomerActivity extends AppCompatActivity {

    FloatingActionButton floating_action_button_customer;

    CusomareAdapter adapter;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String user_id = currentUser.getUid();

    CollectionReference customer = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("UnknownCustomer");

    double cutomartk;

    TextView TotalunKnownDue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unknown_customer);


        TotalunKnownDue = findViewById(R.id.TotalunKnownDue);

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UnknownCustomerActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });






        recyclear();



        CollectionReference custumertaka = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("UnknownCustomer");

        Query query1 = custumertaka;
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
                        double d = Double.parseDouble(doc.get("taka").toString());
                        cutomartk += d;

                    }
                    TotalunKnownDue.setText(cutomartk+"");

                }
            }
        });

    }

    private void recyclear() {
        Query query = customer.orderBy("nameCUstomer", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<CustomerNote> options = new FirestoreRecyclerOptions.Builder<CustomerNote>()
                .setQuery(query, CustomerNote.class)
                .build();

        adapter = new CusomareAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.unknown_cutomer_info_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new CusomareAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UnknownCustomerActivity.this);
                String[] option = {"প্রোফাইল দেখুন","মুছে ফেলা"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db = FirebaseFirestore.getInstance();

                        if (which == 0) {


                            CustomerNote customerNote = documentSnapshot.toObject(CustomerNote.class);
                            String id = documentSnapshot.getId();
                            String imageurl = customerNote.getImageUrl();
                            String name = customerNote.getNameCUstomer();
                            String phone = customerNote.getPhone();
                            double takadobul = customerNote.getTaka();

                            String taka = Double.toString(takadobul);

                            String addreds = customerNote.getAddres();

                            Intent pdfIntent = new Intent(UnknownCustomerActivity.this, CustomerProfileViewActivity.class);

                            pdfIntent.putExtra("uid", id);

                            if (imageurl != null) {
                                pdfIntent.putExtra("imageurl", imageurl);
                            }
                            pdfIntent.putExtra("name", name);

                            pdfIntent.putExtra("phone", phone);
                            if (taka != null) {

                                pdfIntent.putExtra("taka", taka);
                            }

                            if (addreds != null) {
                                pdfIntent.putExtra("addreds", addreds);
                            }
                            startActivity(pdfIntent);
                        }
                        if(which == 1){
                            CustomerNote customerNote = documentSnapshot.toObject(CustomerNote.class);
                            String name = customerNote.getNameCUstomer();

                            new AlertDialog.Builder(UnknownCustomerActivity.this)
                                    .setIcon(R.drawable.ic_delete)
                                    .setTitle(name)
                                    .setMessage("আপনি কি নিশ্চিত মুছে ফেলেন?")
                                    .setPositiveButton("হ্যাঁ",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    adapter.deleteItem(position);
                                                    dialog.dismiss();
                                                }
                                            })
                                    .setNegativeButton("না", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Do nothing
                                            dialog.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                }).create().show();



            }
        });

    }

    private void recyclear(String search) {

        Query query = customer.whereLessThanOrEqualTo("nameCUstomer",search).orderBy("nameCUstomer", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<CustomerNote> options = new FirestoreRecyclerOptions.Builder<CustomerNote>()
                .setQuery(query, CustomerNote.class)
                .build();

        adapter = new CusomareAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.unknown_cutomer_info_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new CusomareAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UnknownCustomerActivity.this);
                String[] option = {"প্রোফাইল দেখুন","মুছে ফেলা"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db = FirebaseFirestore.getInstance();

                        if (which == 0) {
                            CustomerNote customerNote = documentSnapshot.toObject(CustomerNote.class);
                            String id = documentSnapshot.getId();
                            String imageurl = customerNote.getImageUrl();
                            String name = customerNote.getNameCUstomer();
                            String phone = customerNote.getPhone();
                            double takadobul = customerNote.getTaka();

                            String taka = Double.toString(takadobul);

                            String addreds = customerNote.getAddres();

                            Intent pdfIntent = new Intent(UnknownCustomerActivity.this, CustomerProfileViewActivity.class);

                            pdfIntent.putExtra("uid", id);
                            if (imageurl != null) {
                                pdfIntent.putExtra("imageurl", imageurl);
                            }
                            pdfIntent.putExtra("name", name);

                            pdfIntent.putExtra("phone", phone);
                            if (taka != null) {

                                pdfIntent.putExtra("taka", taka);
                            }

                            if (addreds != null) {
                                pdfIntent.putExtra("addreds", addreds);
                            }
                           startActivity(pdfIntent);
                        }
                        if(which == 1){

                            CustomerNote customerNote = documentSnapshot.toObject(CustomerNote.class);
                            String name = customerNote.getNameCUstomer();

                            new AlertDialog.Builder(UnknownCustomerActivity.this)
                                    .setIcon(R.drawable.ic_delete)
                                    .setTitle(name)
                                    .setMessage("আপনি কি নিশ্চিত মুছে ফেলেন?")
                                    .setPositiveButton("হ্যাঁ",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    adapter.deleteItem(position);
                                                    dialog.dismiss();
                                                }
                                            })
                                    .setNegativeButton("না", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Do nothing
                                            dialog.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                }).create().show();



            }
        });

        adapter.startListening();

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

                recyclear(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }
}
