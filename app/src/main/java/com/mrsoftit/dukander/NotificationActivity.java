package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrsoftit.dukander.adapter.NotificationAdapter;
import com.mrsoftit.dukander.adapter.ReviewAdapter;
import com.mrsoftit.dukander.modle.GlobleCustomerNote;
import com.mrsoftit.dukander.modle.NotificationNote;
import com.mrsoftit.dukander.modle.ReviewComentNote;

import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {


    FirebaseUser currentUser;
    String globlecutouser_id ;
    private FirebaseAuth mAuth;
    String cType;


    NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuth = FirebaseAuth.getInstance();

        if (currentUser!=null){
            globlecutouser_id = currentUser.getUid();
        }
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_support);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
/*
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });*/

        if (mAuth.getCurrentUser() != null) {

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String user_id = currentUser.getUid();

            CollectionReference Info = FirebaseFirestore.getInstance()
                    .collection("Globlecustomers").document(user_id).collection("info");

            Info.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            GlobleCustomerNote globleCustomerNote = document.toObject(GlobleCustomerNote.class);
                            cType = globleCustomerNote.getCustomerType();
                        }

                        if (cType != null) {

                            NotificationCustomer(globlecutouser_id);

                        } else {

                           NotificationShop(globlecutouser_id);
                        }
                    }
                }
            });
        }
    }





    public void NotificationShop(String globlecutouser_id){

        CollectionReference Review = FirebaseFirestore.getInstance()
                .collection("users").document(globlecutouser_id).collection("Notification");

        Query query = Review.orderBy("notidate", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NotificationNote> options = new FirestoreRecyclerOptions.Builder<NotificationNote>()
                .setQuery(query, NotificationNote.class)
                .build();
        notificationAdapter = new NotificationAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.NotificationRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notificationAdapter);
        notificationAdapter.startListening();
    }

    public void NotificationCustomer(String globlecutouser_id){

        CollectionReference Review = FirebaseFirestore.getInstance()
                .collection("Globlecustomers").document(globlecutouser_id).collection("Notification");

        Query query = Review.orderBy("notidate", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<NotificationNote> options = new FirestoreRecyclerOptions.Builder<NotificationNote>()
                .setQuery(query, NotificationNote.class)
                .build();
        notificationAdapter = new NotificationAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.NotificationRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notificationAdapter);
        notificationAdapter.startListening();
    }

}
