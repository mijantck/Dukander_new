package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class SaleoOneActivity extends AppCompatActivity {


    CusomareAdapter adapter;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String user_id = currentUser.getUid();
    CollectionReference customer = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Customers");

    ProgressDialog progressDialog;
    private ImageView customerList,unknowncustomer;

    private LinearLayout linearLayout1,cardLayoutchosecustomer;
      RelativeLayout linearLayout2;
     TextView customer_text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saleo_one);


        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        toolbar.setTitle("Dukandar ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaleoOneActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        linearLayout1 = findViewById(R.id.saleFondSide);
        cardLayoutchosecustomer = findViewById(R.id.cardLayoutchosecustomer);
        linearLayout2 = findViewById(R.id.salecustomerlist);
        customerList = findViewById(R.id.customerList);
        unknowncustomer = findViewById(R.id.unknownCustomer);

      //  setCustomerList();


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
                        }
                    }

                }

            }
        });
        recyclear();

        customerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linearLayout1.setVisibility(View.GONE);
                cardLayoutchosecustomer.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);


            }
        });
        unknowncustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(SaleoOneActivity.this);
                progressDialog.setMessage("তথ্য প্রস্তুত হচ্ছে...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();
                final CollectionReference unkonwnCustomar = FirebaseFirestore.getInstance()
                        .collection("users").document(user_id).collection("UnknownCustomer");

                unkonwnCustomar.add(new CustomerNote(null,"Unknown","Unknown",00.00,"Unknown",00.0,00.00,"Unknown","Unknown")).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        if (task.isSuccessful()){

                            final String unid = task.getResult().getId();

                            unkonwnCustomar.document(unid).update("customerIdDucunt",unid).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                       Intent intent = new Intent(SaleoOneActivity.this, SeleTwoActivity.class);

                                        if (unid != null) {
                                            intent.putExtra("unkownId", unid);
                                            startActivity(intent);

                                            progressDialog.dismiss();
                                        }

                                    }
                                }
                            });


                        }


                    }
                });

            }
        });

    }

    private void recyclear() {

        Query query = customer.orderBy("nameCUstomer", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<CustomerNote> options = new FirestoreRecyclerOptions.Builder<CustomerNote>()
                .setQuery(query, CustomerNote.class)
                .build();

        adapter = new CusomareAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.saleCustomerlistRecyclearView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.startListening();

        adapter.setOnItemClickListener(new CusomareAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                CustomerNote customerNote = documentSnapshot.toObject(CustomerNote.class);
                String id = documentSnapshot.getId();
                String imageurl = customerNote.getImageUrl();
                String name = customerNote.getNameCUstomer();
                String phone = customerNote.getPhone();
                double takadobul = customerNote.getTaka();

                String taka = Double.toString(takadobul);


                String addreds = customerNote.getAddres();

                Intent pdfIntent = new Intent(SaleoOneActivity.this, SeleTwoActivity.class);

                pdfIntent.putExtra("id", id);
                if (imageurl != null) {
                    pdfIntent.putExtra("imageurl", imageurl);
                }
                pdfIntent.putExtra("name", name);

                pdfIntent.putExtra("phone", phone);

                if (taka!=null){

                    pdfIntent.putExtra("taka", taka);
                }


                if (addreds != null) {
                    pdfIntent.putExtra("addreds", addreds);
                }
                startActivity(pdfIntent);
            }
        });

    }

    private void recyclear(String search) {

        Query query = customer.whereGreaterThanOrEqualTo("nameCUstomer",search).orderBy("nameCUstomer", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<CustomerNote> options = new FirestoreRecyclerOptions.Builder<CustomerNote>()
                .setQuery(query, CustomerNote.class)
                .build();

        adapter = new CusomareAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.saleCustomerlistRecyclearView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.startListening();

        adapter.setOnItemClickListener(new CusomareAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                CustomerNote customerNote = documentSnapshot.toObject(CustomerNote.class);
                String id = documentSnapshot.getId();
                String imageurl = customerNote.getImageUrl();
                String name = customerNote.getNameCUstomer();
                String phone = customerNote.getPhone();
                double takadubol = customerNote.getTaka();
                String taka = Double.toString(takadubol);
                String addreds = customerNote.getAddres();


                Intent pdfIntent = new Intent(SaleoOneActivity.this, SeleTwoActivity.class);

                pdfIntent.putExtra("id", id);
                if (imageurl != null) {
                    pdfIntent.putExtra("imageurl", imageurl);
                }
                pdfIntent.putExtra("name", name);

                pdfIntent.putExtra("phone", phone);
                if (taka!=null) {
                    pdfIntent.putExtra("taka", taka);
                }
                if (addreds != null) {
                    pdfIntent.putExtra("addreds", addreds);
                }
                startActivity(pdfIntent);
            }
        });

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

