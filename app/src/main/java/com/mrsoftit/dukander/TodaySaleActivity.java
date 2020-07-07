package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TodaySaleActivity extends AppCompatActivity {


    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String user_id = Objects.requireNonNull(currentUser).getUid();

    //ProductAdapter adapter;
    TotalAdapter adapter;
    FirebaseFirestore db;

    private TextView dateView,TodayTotalSale,todayTotaldue,todayTotalProfite;
    private MaterialButton ApprovedsalesButton;

    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeLayout;

    boolean firstTimeComfirm = false ;
    String code ;


    DecimalFormat df2 = new DecimalFormat("#.##");


    CollectionReference product = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Product");

    //total sale
    CollectionReference TotalcustomerProductSale = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Sales");

    CollectionReference Profit = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("profit");

    CollectionReference confirmSaleCode = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("confirmSaleCode");



    final CollectionReference customerTakaUpdate = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Customers");


    final CollectionReference dukandertakaUpdate = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("DukanInfo");



    double totalsum = 0.0;
    double ProfitSum = 0.0;
    double totalpaybilint = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_sale);

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodaySaleActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        db = FirebaseFirestore.getInstance();


        swipeLayout = findViewById(R.id.todaysaleRefresh);
        dateView = findViewById(R.id.dateTextView);
        TodayTotalSale = findViewById(R.id.todayTotalSale);
        todayTotaldue = findViewById(R.id.todayTotaldue);
        todayTotalProfite = findViewById(R.id.todayTotalProfite);
        ApprovedsalesButton = findViewById(R.id.ApprovedsalesButton);

        final CollectionReference myInfo = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("DukanInfo");

        recyclear();


        String pattern = "dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        dateView.setText(date);

      //  recyclear();


        Date calendar1 = Calendar.getInstance().getTime();
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(calendar1);
        final int datenew = Integer.parseInt(todayString);



        Query query = TotalcustomerProductSale.whereEqualTo("date",datenew).whereEqualTo("paid",true);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                totalsum = 00.00;
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("totalPrice") != null) {
                        double totaltest  = (double) doc.get("totalPrice");
                        totalsum += totaltest;
                    }


                    if (doc.get("totalpaybil") != null) {
                        double totalpaybil  = (double) doc.get("totalpaybil");
                        totalpaybilint = totalpaybil;

                    }
                }

                TodayTotalSale.setText( df2.format(totalsum)+"");
            }
        });

        myInfo.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                totalpaybilint = 00.00;

                String id = null;
                int  date = 0;
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    if (doc.get("totalpaybil") != null) {
                        double d = Double.parseDouble(doc.get("totalpaybil").toString());

                        totalpaybilint = d;
                    } if (doc.get("totalpaybil") != null) {
                        id = doc.getId();
                    }
                    if (doc.get("date") != null) {
                        String dateS =  doc.get("date").toString();
                        int i =Integer.parseInt(dateS);
                        date = i;
                    }


                    double todayTotaldueCal = totalsum - totalpaybilint;
                    todayTotaldue.setText(df2.format(todayTotaldueCal)+"");
                }

                if (datenew != date ) {

                    myInfo.document(id).update("date",datenew,"totalpaybil",0.0).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }


            }
        });

        Query Profitquery = Profit.whereEqualTo("date",datenew).whereEqualTo("ProfitTrue", true);

        Profitquery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                ProfitSum = 00.00;
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("Profit") != null) {
                        double Profit1  = (double) doc.get("Profit");
                        ProfitSum += Profit1;
                    }



                }
                todayTotalProfite.setText(df2.format(ProfitSum)+"");
            }
        });
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code her
                // To keep animation for 4 seconds


                final CollectionReference myInfo = FirebaseFirestore.getInstance()
                        .collection("users").document(user_id).collection("DukanInfo");


                recyclear();


                String pattern = "dd MMMM yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(new Date());
                dateView.setText(date);

                //  recyclear();


                Date calendar1 = Calendar.getInstance().getTime();
                DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
                String todayString = df1.format(calendar1);
                final int datenew = Integer.parseInt(todayString);



                Query query = TotalcustomerProductSale.whereEqualTo("date",datenew).whereEqualTo("paid",true);

                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        totalsum = 00.00;
                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get("totalPrice") != null) {
                                double totaltest  = (double) doc.get("totalPrice");
                                totalsum += totaltest;
                            }


                            if (doc.get("totalpaybil") != null) {
                                double totalpaybil  = (double) doc.get("totalpaybil");

                                totalpaybilint = totalpaybil;
                                Toast.makeText(TodaySaleActivity.this, totalpaybilint+" tk", Toast.LENGTH_SHORT).show();
                            }
                        }

                        TodayTotalSale.setText( df2.format(totalsum)+"");

                    }
                });
                myInfo.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        totalpaybilint = 00.00;

                        String id = null;
                        int  date = 0;
                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                            if (doc.get("totalpaybil") != null) {
                                double d = Double.parseDouble(doc.get("totalpaybil").toString());

                                totalpaybilint = d;
                            } if (doc.get("totalpaybil") != null) {
                                id = doc.getId();
                            }
                            if (doc.get("date") != null) {
                                String dateS =  doc.get("date").toString();
                                int i =Integer.parseInt(dateS);
                                date = i;
                            }
                            double todayTotaldueCal = totalsum - totalpaybilint;
                            todayTotaldue.setText(df2.format(todayTotaldueCal)+"");
                        }

                        if (datenew != date ) {

                            myInfo.document(id).update("date",datenew,"totalpaybil",0.0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }


                    }
                });

                Query Profitquery = Profit.whereEqualTo("date",datenew).whereEqualTo("ProfitTrue",true);

                Profitquery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        ProfitSum = 00.00;
                        assert queryDocumentSnapshots != null;
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.get("Profit") != null) {
                                double Profit1  = (double) doc.get("Profit");
                                ProfitSum += Profit1;
                            }


                        }
                        todayTotalProfite.setText(df2.format(ProfitSum)+"");
                    }
                });

                recyclear();


                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000); // Delay in millis
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );




        ApprovedsalesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                confirmSaleCode.document("confirmCode").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            // Document found in the offline cache
                            DocumentSnapshot document = task.getResult();

                            if (document.get("firstTime")!=null){
                                boolean firstTi = Boolean.valueOf(document.get("firstTime").toString());
                                firstTimeComfirm = firstTi;
                            }
                            if (document.get("code")!=null){
                                code = document.get("code").toString();
                            }

                        } else {

                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(TodaySaleActivity.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.confirm_dialog, viewGroup, false);
                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();
                        TextView TitailText = dialogView.findViewById(R.id.TitelId);
                        final EditText EnterConfirmCode = dialogView.findViewById(R.id.EnterConfirmCode);
                        final EditText SetConfirmCode = dialogView.findViewById(R.id.SetConfirmCode);
                        final MaterialButton okButton = dialogView.findViewById(R.id.confirmButton);

                        if (firstTimeComfirm == false) {
                            TitailText.setText("আপনার অনুমোদিত বিক্রয় গুলো আলাদা দেখাবে।");
                            EnterConfirmCode.setVisibility(View.GONE);
                            SetConfirmCode.setVisibility(View.VISIBLE);

                        }
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (firstTimeComfirm == false) {
                                    String setCode = SetConfirmCode.getText().toString();
                                    confirmSaleCode.document("confirmCode").set(new ConfirmNot(null, true, setCode)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            alertDialog.dismiss();
                                            Toast.makeText(TodaySaleActivity.this, "  ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    String EnterCode = EnterConfirmCode.getText().toString();
                                    if (EnterCode.equals(code)) {

                                        Query query = TotalcustomerProductSale.whereEqualTo("date", datenew).whereEqualTo("paid", true);

                                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    List<String> list = new ArrayList<>();
                                                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                        list.add(document.getId());
                                                    }

                                                    Approved((ArrayList) list);
                                                    alertDialog.dismiss();
                                                }
                                            }


                                        });

                                    }
                                    else {
                                        Toast.makeText(TodaySaleActivity.this, "ভূল পিন ", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            }
                        });

                        alertDialog.show();

                    }
                });

            }
        });

            }

    private void recyclear() {

        Date calendar1 = Calendar.getInstance().getTime();
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(calendar1);
        final int datenew = Integer.parseInt(todayString);


        Query query = TotalcustomerProductSale.orderBy("itemName", Query.Direction.ASCENDING).whereEqualTo("paid",true).whereEqualTo("date",datenew);

        FirestoreRecyclerOptions<TotalSaleNote> options = new FirestoreRecyclerOptions.Builder<TotalSaleNote>()
                .setQuery(query, TotalSaleNote.class)
                .build();

        adapter = new TotalAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.totalsaletoday);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.setOnItemClickListener(new TotalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, final int position) {
                TotalSaleNote totalSaleNote = documentSnapshot.toObject(TotalSaleNote.class);

                final String customerID = totalSaleNote.getCustomerID();
                final String uncustomerID = totalSaleNote.getUnknownCustomerID();
                String name = totalSaleNote.getItemName();
                final double taka = totalSaleNote.getTotalPrice();
                final String saleID= totalSaleNote.getSaleProductId();
                int date = totalSaleNote.getDate();


                Date calendar1 = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat")
                DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
                String todayString = df1.format(calendar1);
                final int datenew = Integer.parseInt(todayString);

                new AlertDialog.Builder(TodaySaleActivity.this)
                        .setIcon(R.drawable.ic_delete)
                        .setTitle(name)
                        .setMessage("আপনি কি নিশ্চিত মুছে ফেলেন?")
                        .setPositiveButton("হ্যাঁ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        progressDialog = new ProgressDialog(TodaySaleActivity.this);
                                        progressDialog.setMessage("লোড করছে..."); // Setting Message
                                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                        progressDialog.setCancelable(false);
                                        progressDialog.show();


                                        if (customerID!=null) {



                                            final CollectionReference customerProductSale = FirebaseFirestore.getInstance()
                                                    .collection("users").document(user_id).collection("Customers").document(customerID).collection("saleProduct");
                                            customerProductSale.document(saleID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    final CollectionReference customerTakaUpdate = FirebaseFirestore.getInstance()
                                                            .collection("users").document(user_id).collection("Customers");

                                                    Query query1 = customerTakaUpdate.whereEqualTo("customerIdDucunt",customerID);
                                                    query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                double presentTaka = 0.0;
                                                                double presentTaka1 = 0.0;
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    double totaltest = (double) document.get("taka");
                                                                    presentTaka1 = totaltest;
                                                                }
                                                                presentTaka = presentTaka1 - taka;
                                                                Toast.makeText(TodaySaleActivity.this, presentTaka+"", Toast.LENGTH_SHORT).show();

                                                                customerTakaUpdate.document(customerID).update("taka",presentTaka).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        dukandertakaUpdate.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                                if (task.isSuccessful()) {
                                                                                    double totalpaybil = 0.0;
                                                                                    double presentDukantaka = 0.0;
                                                                                    String dukandrId = null;
                                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                        double totaltest = (double) document.get("totalpaybil");
                                                                                        dukandrId = document.getId();
                                                                                        totalpaybil = totaltest;
                                                                                    }
                                                                                    presentDukantaka = totalpaybil - taka;

                                                                                    dukandertakaUpdate.document(dukandrId).update("totalpaybil",totalpaybil).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            adapter.deleteItem(position);
                                                                                            progressDialog.dismiss();

                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });


                                                                    }
                                                                });
                                                            } else {

                                                            }

                                                        }
                                                    });

                                                }
                                            });
                                        }
                                        else  if (uncustomerID!=null) {

                                            final CollectionReference unkonwnCustomar = FirebaseFirestore.getInstance()
                                                    .collection("users").document(user_id).collection("UnknownCustomer").document(uncustomerID).collection("saleProduct");
                                            unkonwnCustomar.document(saleID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    final CollectionReference unncustomerTakaUpdate = FirebaseFirestore.getInstance()
                                                            .collection("users").document(user_id).collection("Customers");

                                                    Query query1 = customerTakaUpdate.whereEqualTo("customerIdDucunt",uncustomerID);
                                                    query1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                            if (task.isSuccessful()) {
                                                                double presentTaka = 0.0;
                                                                double presentTaka1 = 0.0;
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    double totaltest = (double) document.get("taka");
                                                                    presentTaka1 = totaltest;
                                                                }
                                                                presentTaka = presentTaka1 - taka;
                                                                Toast.makeText(TodaySaleActivity.this, presentTaka+"", Toast.LENGTH_SHORT).show();

                                                                unncustomerTakaUpdate.document(uncustomerID).update("taka",presentTaka).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        dukandertakaUpdate.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                                                if (task.isSuccessful()) {
                                                                                    double totalpaybil = 0.0;
                                                                                    double presentDukantaka = 0.0;
                                                                                    String dukandrId = null;
                                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                        double totaltest = (double) document.get("totalpaybil");
                                                                                        dukandrId = document.getId();
                                                                                        totalpaybil = totaltest;
                                                                                    }
                                                                                    presentDukantaka = totalpaybil - taka;

                                                                                    dukandertakaUpdate.document(dukandrId).update("totalpaybil",totalpaybil).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            adapter.deleteItem(position);
                                                                                            progressDialog.dismiss();

                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                            } else {

                                                            }
                                                        }
                                                    });

                                                }
                                            });
                                        }
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
        });

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


    public void Approved(ArrayList list) {

        // Get a new write batch
        WriteBatch batch = db.batch();

        // Iterate through the list
        for (int k = 0; k < list.size(); k++) {

            // Update each list item
            DocumentReference ref = db.collection("users").document(user_id).collection("Sales")
                   .document((String) list.get(k));

            batch.update(ref, "confirm", true);

        }

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Yay its all done in one go!
            }
        });

    }

}



