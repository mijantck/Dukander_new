package com.mrsoftit.dukander;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mrsoftit.dukander.adapter.NotificationAdapter;
import com.mrsoftit.dukander.adapter.ReviewAdapter;
import com.mrsoftit.dukander.modle.NotificationNote;
import com.mrsoftit.dukander.modle.ReviewComentNote;

public class NotificationActivity extends AppCompatActivity {


    FirebaseUser currentUser;
    String globlecutouser_id ;
    private FirebaseAuth mAuth;


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

        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        review(globlecutouser_id);



    }





    public void review(String globlecutouser_id){

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

}
