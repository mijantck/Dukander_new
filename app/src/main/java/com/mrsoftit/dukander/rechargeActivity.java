package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class rechargeActivity extends AppCompatActivity {


    TabLayout  tabLayout;
    ViewPager viewPager;


    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String user_id = Objects.requireNonNull(currentUser).getUid();


    CollectionReference coainCollecton = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Coian");

    TextView activeCoine,acticHeaderTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_support);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rechargeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        activeCoine = findViewById(R.id.activeCoine);
        acticHeaderTextView = findViewById(R.id.acticHeaderTextView);

        PagerAdapter PagerAdapter = new PagerAdapter(getSupportFragmentManager());

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(PagerAdapter);
        tabLayout.setupWithViewPager(viewPager);




        coainCollecton.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {


                    if (doc.get("customerStatus")!=null){
                       acticHeaderTextView.setText("আপনি আছেন  ");

                        activeCoine.setText("অগ্রিম");

                    }else if (doc.get("coin") != null) {

                        int totalcoin =Integer.parseInt(doc.get("coin").toString());
                        activeCoine.setText(totalcoin+"");

                    }

                }


            }
        });

    }





}
