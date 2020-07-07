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
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.Objects;

public class ConfirmUnerSaleActivity extends AppCompatActivity {

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String user_id = Objects.requireNonNull(currentUser).getUid();

    //ProductAdapter adapter;
    TotalAdapter adapter;

    private TextView dateView,TodayTotalSale,Totaldue;


    SwipeRefreshLayout swipeLayout;

    //total sale
    CollectionReference TotalcustomerProductSale = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Sales");

    final CollectionReference customerTakaUpdate = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Customers");


    final CollectionReference dukandertakaUpdate = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("DukanInfo");




    double totalsum = 0.0;
    double totalpaybilint = 0.0;
    ProgressDialog progressDialog;

    ImageView calanderid;

    int seceltdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_woner_sale);


        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmUnerSaleActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        swipeLayout = findViewById(R.id.swipeLayout);
        calanderid = findViewById(R.id.calanderid);

        final TextView viewdate = findViewById(R.id.dateTextView);
        TodayTotalSale = findViewById(R.id.todayTotalSale);
        Totaldue = findViewById(R.id.Totaldue);


        String pattern = "dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        viewdate.setText(date);


        final CollectionReference myInfo = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("DukanInfo");





        Query query = TotalcustomerProductSale.whereEqualTo("paid",true).whereEqualTo("confirm", true);

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
                }
                TodayTotalSale.setText(totalsum+"");

            }
        });

        myInfo.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    return;
                }

                totalpaybilint = 00.00;

                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    if (doc.get("totalpaybil") != null) {

                        double d = Double.parseDouble(doc.get("totalpaybil").toString());

                        totalpaybilint = d;

                    }

                    Totaldue.setText(totalsum - totalpaybilint+"");

                }
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code her

        String pattern = "dd MMMM yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        viewdate.setText(date);



        final CollectionReference myInfo = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("DukanInfo");




                recyclear();

        Query query = TotalcustomerProductSale.whereEqualTo("paid",true).whereEqualTo("confirm", true);

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
                }
                TodayTotalSale.setText(totalsum+"");

            }
        });

        myInfo.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    return;
                }

                totalpaybilint = 00.00;

                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    if (doc.get("totalpaybil") != null) {

                        double d = Double.parseDouble(doc.get("totalpaybil").toString());

                        totalpaybilint = d;

                    }

                    Totaldue.setText(totalsum - totalpaybilint+"");

                }
            }
        });



                // To keep animation for 4 seconds
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


        recyclear();
        calanderid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ConfirmUnerSaleActivity.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.calnder);
                // Set dialog title
                dialog.setTitle("Bill Pay ");
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

                TextView getdate =dialog.findViewById(R.id.getdate);
                final DatePicker  picker = dialog.findViewById(R.id.datePicker1);



                getdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int year = picker.getYear();
                        int month = picker.getMonth();
                        int day = picker.getDayOfMonth();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

                        String strDate = format.format(calendar.getTime());
                        int date=Integer.parseInt(strDate);
                        seceltdate = date;
                        viewdate.setText("Selected Date: "+ picker.getDayOfMonth()+"/"+ (picker.getMonth() + 1)+"/"+picker.getYear());
                        recyclear( date );
                        dialog.dismiss();

                    }
                });


            }
        });

    }



    private void recyclear() {

        Query query = TotalcustomerProductSale.orderBy("itemName", Query.Direction.ASCENDING).whereEqualTo("confirm", true);

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

        double d = Double.parseDouble(Totaldue.getText().toString());

        Toast.makeText(ConfirmUnerSaleActivity.this, Totaldue.getText().toString()+"", Toast.LENGTH_SHORT).show();

        new AlertDialog.Builder(ConfirmUnerSaleActivity.this)
                .setIcon(R.drawable.ic_delete)
                .setTitle(name)
                .setMessage("আপনি কি নিশ্চিত মুছে ফেলেন?")
                .setPositiveButton("হ্যাঁ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                progressDialog = new ProgressDialog(ConfirmUnerSaleActivity.this);
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
                                                        Toast.makeText(ConfirmUnerSaleActivity.this, presentTaka+"", Toast.LENGTH_SHORT).show();

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

                                                                            Totaldue.setText(presentDukantaka+"");
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
                                                        Toast.makeText(ConfirmUnerSaleActivity.this, presentTaka+"", Toast.LENGTH_SHORT).show();

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

                                                                            Totaldue.setText(presentDukantaka+"");
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
    private void recyclear(int date) {

        Query query = TotalcustomerProductSale.orderBy("itemName", Query.Direction.ASCENDING).whereEqualTo("confirm", true).whereEqualTo("date",date);

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

                double d = Double.parseDouble(Totaldue.getText().toString());

                Toast.makeText(ConfirmUnerSaleActivity.this, Totaldue.getText().toString()+"", Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder(ConfirmUnerSaleActivity.this)
                        .setIcon(R.drawable.ic_delete)
                        .setTitle(name)
                        .setMessage("আপনি কি নিশ্চিত মুছে ফেলেন?")
                        .setPositiveButton("হ্যাঁ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        progressDialog = new ProgressDialog(ConfirmUnerSaleActivity.this);
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
                                                                Toast.makeText(ConfirmUnerSaleActivity.this, presentTaka+"", Toast.LENGTH_SHORT).show();

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

                                                                                    Totaldue.setText(presentDukantaka+"");
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
                                                                Toast.makeText(ConfirmUnerSaleActivity.this, presentTaka+"", Toast.LENGTH_SHORT).show();

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

                                                                                    Totaldue.setText(presentDukantaka+"");
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


}
