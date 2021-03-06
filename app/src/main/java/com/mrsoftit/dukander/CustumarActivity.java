package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CustumarActivity extends AppCompatActivity {

    FloatingActionButton floating_action_button_customer;

    CusomareAdapter adapter;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String user_id = currentUser.getUid();

    CollectionReference customer = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Customers");

    List<CustomerNote> exampleList;


 TextView customer_text_view;
    RecyclerView recyclerView;

    String myinfoid;
    Double dueBalance ;
    Double activeBalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custumar);

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustumarActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        customer_text_view = findViewById(R.id.customer_text_view);



        customer.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("nameCUstomer") != null) {
                        String name = doc.get("nameCUstomer").toString();

                        if (name!=null){

                            customer_text_view.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                }

            }
        });

        recyclear();



        floating_action_button_customer = findViewById(R.id.floating_action_button_customer);


        floating_action_button_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CustumarActivity.this,CustomerAddActivity.class);

                startActivity(intent);

            }
        });
    }


    private void recyclear() {
        Query query = customer.orderBy("nameCUstomer", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<CustomerNote> options = new FirestoreRecyclerOptions.Builder<CustomerNote>()
                .setQuery(query, CustomerNote.class)
                .build();

        adapter = new CusomareAdapter(options);

        recyclerView = findViewById(R.id.customar_info_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);



        adapter.setOnItemClickListener(new CusomareAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CustumarActivity.this);
                String[] option = {"Profile Edit", "Profile View","Delete"};
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

                            Intent pdfIntent = new Intent(CustumarActivity.this, CustomerAddActivity.class);

                            pdfIntent.putExtra("id", id);
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
                        if (which == 1) {


                            CustomerNote customerNote = documentSnapshot.toObject(CustomerNote.class);
                            String id = documentSnapshot.getId();
                            String imageurl = customerNote.getImageUrl();
                            String name = customerNote.getNameCUstomer();
                            double totalPaytakaget = customerNote.getTotalPaytaka();
                            String phone = customerNote.getPhone();
                            double takadobul = customerNote.getTaka();

                            String taka = Double.toString(takadobul);
                            String totalPaytaka = Double.toString(totalPaytakaget);

                            String addreds = customerNote.getAddres();

                            Intent pdfIntent = new Intent(CustumarActivity.this, CustomerProfileViewActivity.class);

                            pdfIntent.putExtra("id", id);
                            if (imageurl != null) {
                                pdfIntent.putExtra("imageurl", imageurl);
                            }
                            pdfIntent.putExtra("name", name);

                            pdfIntent.putExtra("phone", phone);
                            if (taka != null) {

                                pdfIntent.putExtra("taka", taka);
                            }
                            if (totalPaytaka != null) {
                                pdfIntent.putExtra("totalPaytaka", totalPaytaka);
                            }

                            if (addreds != null) {
                                pdfIntent.putExtra("addreds", addreds);
                            }
                            startActivity(pdfIntent);


                        }
                        if(which == 2){
                            CustomerNote customerNote = documentSnapshot.toObject(CustomerNote.class);
                            String name = customerNote.getNameCUstomer();
                            String id = documentSnapshot.getId();
                            String imageurl = customerNote.getImageUrl();
                            final double totalPaytakaget = customerNote.getTotalPaytaka();
                            String phone = customerNote.getPhone();
                            final double takadobul = customerNote.getTaka();

                            new AlertDialog.Builder(CustumarActivity.this)
                                    .setIcon(R.drawable.ic_delete)
                                    .setTitle(name)
                                    .setMessage("Confirm Cancel?")
                                    .setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(final DialogInterface dialog, int which) {

                                                    CollectionReference myInfo = FirebaseFirestore.getInstance()
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

                                                                    if (activeBalance!=null ){
                                                                        if ( activeBalance >= totalPaytakaget){
                                                                            activeBalance -= totalPaytakaget;
                                                                        }else {
                                                                            activeBalance = 0.0;
                                                                        }

                                                                    }

                                                                    if (dueBalance != null){

                                                                        if ( dueBalance >= takadobul){
                                                                            dueBalance -= takadobul;
                                                                        }else {
                                                                            dueBalance = 0.0;
                                                                        }


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


                                                    adapter.deleteItem(position);
                                                    dialog.dismiss();

                                                }
                                            })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
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

        Query query = customer.orderBy("search").startAt(search.toLowerCase()).endAt(search.toLowerCase()+ "\uf8ff");

        FirestoreRecyclerOptions<CustomerNote> options = new FirestoreRecyclerOptions.Builder<CustomerNote>()
                .setQuery(query, CustomerNote.class)
                .build();

        adapter = new CusomareAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.customar_info_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new CusomareAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CustumarActivity.this);
                String[] option = {"Profile Edit ", "View Profile","Delete"};
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

                            Intent pdfIntent = new Intent(CustumarActivity.this, CustomerAddActivity.class);

                            pdfIntent.putExtra("id", id);
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
                        if (which == 1) {


                            CustomerNote customerNote = documentSnapshot.toObject(CustomerNote.class);
                            String id = documentSnapshot.getId();
                            String imageurl = customerNote.getImageUrl();
                            String name = customerNote.getNameCUstomer();
                            double totalPaytakaget = customerNote.getTotalPaytaka();
                            String phone = customerNote.getPhone();
                            double takadobul = customerNote.getTaka();

                            String taka = Double.toString(takadobul);
                            String totalPaytaka = Double.toString(totalPaytakaget);

                            String addreds = customerNote.getAddres();

                            Intent pdfIntent = new Intent(CustumarActivity.this, CustomerProfileViewActivity.class);

                            pdfIntent.putExtra("id", id);
                            if (imageurl != null) {
                                pdfIntent.putExtra("imageurl", imageurl);
                            }
                            pdfIntent.putExtra("name", name);

                            pdfIntent.putExtra("phone", phone);
                            if (taka != null) {
                                pdfIntent.putExtra("taka", taka);
                            }
                            if (totalPaytaka != null) {
                                pdfIntent.putExtra("totalPaytaka", totalPaytaka);
                            }
                            if (addreds != null) {
                                pdfIntent.putExtra("addreds", addreds);
                            }
                            startActivity(pdfIntent);
                        }
                        if(which == 2){

                            CustomerNote customerNote = documentSnapshot.toObject(CustomerNote.class);
                            String name = customerNote.getNameCUstomer();

                            new AlertDialog.Builder(CustumarActivity.this)
                                    .setIcon(R.drawable.ic_delete)
                                    .setTitle(name)
                                    .setMessage("Confirm delete?")
                                    .setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {


                                                    adapter.deleteItem(position);
                                                    dialog.dismiss();
                                                }
                                            })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
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

                recyclear(newText);

                return false;
            }
        });
        return true;
    }
}
