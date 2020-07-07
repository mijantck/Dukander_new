package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class InvesmentActivity extends AppCompatActivity {


    String myId ;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = currentUser.getUid();



    InvestmentWithdrowAdapter adapter;


    ExtendedFloatingActionButton addFloatringButton;


    EditText payeditetext,moneyInvest_withdrow_ditails_editeTEaxt;
    TextView TitleTExt,paymentLooding,Totalinvestment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invesment);

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvesmentActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addFloatringButton = findViewById(R.id.addedflotingButton);
        Totalinvestment = findViewById(R.id.Totalinvestment);


        final Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            myId = bundle.getString("id");
        }

       // recyclear();

        addFloatringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialoginvest = new Dialog(InvesmentActivity.this);
                // Include dialog.xml file
                dialoginvest.setContentView(R.layout.investment_withdrow_dialog);
                // Set dialog title
                dialoginvest.show();
                dialoginvest.setCanceledOnTouchOutside(false);
                final Button okButton = dialoginvest.findViewById(R.id.addmoney);
                final TextView Titel = dialoginvest.findViewById(R.id.TitleTExt);
                Titel.setText("বিনিয়োগ ");
                Button cancelButton = dialoginvest.findViewById(R.id.cancelButton);


                payeditetext= dialoginvest.findViewById(R.id.moneyInvest_withdrow_Money);
                moneyInvest_withdrow_ditails_editeTEaxt= dialoginvest.findViewById(R.id.moneyInvest_withdrow_ditails_editeTEaxt);
                TitleTExt= dialoginvest.findViewById(R.id.TitleTExt);
                paymentLooding = dialoginvest.findViewById(R.id.loadingTExt);
                final TextView loadingTExt = dialoginvest.findViewById(R.id.loadingTExt);


                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final ProgressDialog pd = new ProgressDialog(InvesmentActivity.this);
                        pd.setMessage("লোড করছে...");
                        pd.setCancelable(false);
                        String investmenttaka=    payeditetext.getText().toString();

                        if (investmenttaka.isEmpty()){
                            Toast.makeText(InvesmentActivity.this, "বিনিয়োগ ফাঁকা ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                   double aDoubleinvestmenttaka = Double.parseDouble(investmenttaka);
                   String details =    moneyInvest_withdrow_ditails_editeTEaxt.getText().toString();


                   pd.show();
                        Date calendar1 = Calendar.getInstance().getTime();
                        @SuppressLint("SimpleDateFormat")
                        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
                        String todayString = df1.format(calendar1);
                        final int datenew = Integer.parseInt(todayString);

                        final CollectionReference investment = FirebaseFirestore.getInstance()
                                .collection("users").document(user_id).collection("investment");

                        investment.add(new MyInfoNote(" ",0,null,aDoubleinvestmenttaka,datenew,details)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                if (task.isSuccessful()){

                                    String id = task.getResult().getId();
                                    investment.document(id).update("myid",id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            dialoginvest.dismiss();

                                            pd.dismiss();
                                        }
                                    });


                                }

                            }
                        });



                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialoginvest.dismiss();
                    }
                });
            }
        });
        recyclear();


        CollectionReference investment = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("investment");

        investment.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e!=null){
                    return;
                }

                double totalpaybilint = 0;
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    if (doc.get("invesment") != null) {
                        double totalpaybil  = (double) doc.get("invesment");

                        totalpaybilint += totalpaybil;

                    }

                    Totalinvestment.setText(totalpaybilint+"");

                }
            }
        });
    }


    private void recyclear() {

        CollectionReference investment = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("investment");

        Query query = investment.orderBy("date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MyInfoNote> options = new FirestoreRecyclerOptions.Builder<MyInfoNote>()
                .setQuery(query, MyInfoNote.class)
                .build();

        adapter = new InvestmentWithdrowAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.Totalinvestmentrecycleariew);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.startListening();
        adapter.setOnItemClickListener(new InvestmentWithdrowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, final int position) {

                MyInfoNote myInfoNote = documentSnapshot.toObject(MyInfoNote.class);

                double invest = myInfoNote.getInvesment();
                new AlertDialog.Builder(InvesmentActivity.this)
                        .setIcon(R.drawable.ic_delete)
                        .setTitle(invest+"")
                        .setMessage("আপনি কি নিশ্চিত মুছে ফেলেন?")
                        .setPositiveButton("হ্যাঁ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        adapter.deleteItem(position);
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
