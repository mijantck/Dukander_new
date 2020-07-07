package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class ChangePinActivity extends AppCompatActivity {

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String user_id = currentUser.getUid();

    PinEntryEditText old_pin_entry,new_pin_entry;

    MaterialButton newPindButton ;


    ProgressDialog progressDialog;
    String oldpin,newpin;

    String pin,idpin;
    boolean open = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePinActivity.this,MyInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        old_pin_entry = findViewById(R.id.old_pin_entry);
        new_pin_entry = findViewById(R.id.new_pin_entry);
        newPindButton = findViewById(R.id.newPindButton);


        final CollectionReference myPin = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("Pin");

        myPin.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    PinNote pinNote = new PinNote();
                    for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        pinNote.setFairtTime((Boolean) document.get("fairtTime"));
                        pin = Objects.requireNonNull(document.get("pin")).toString();
                        idpin = Objects.requireNonNull(document.get("id")).toString();
                    }


                }}
        });


        if (old_pin_entry != null) {
            old_pin_entry.setAnimateText(true);
            old_pin_entry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    oldpin = str.toString();

                }
            });
        }
        if (new_pin_entry != null) {
            new_pin_entry.setAnimateText(true);
            new_pin_entry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    newpin = str.toString();

                }
            });
        }




        newPindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (oldpin == null || oldpin.isEmpty() ){
                    Toast.makeText(ChangePinActivity.this, "পুরাতন পিন খালি", Toast.LENGTH_SHORT).show();
                    return;
                } if (newpin ==null ||newpin.isEmpty()){

                    Toast.makeText(ChangePinActivity.this, "নতুন পিন খালি", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pin.isEmpty() ){
                    return;
                }
                if (oldpin.equals(pin)){
                    progressDialog = new ProgressDialog(ChangePinActivity.this);
                    progressDialog.setTitle("তথ্য প্রস্তুত হচ্ছে..."); // Setting Title
                    progressDialog.show();

                    myPin.document(idpin).update("pin",newpin).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            old_pin_entry.setText(null);
                            new_pin_entry.setText(null);
                            progressDialog.dismiss();
                        }
                    });

                }else {
                    Toast.makeText(ChangePinActivity.this, "পুরানো পিন ভুল  ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
