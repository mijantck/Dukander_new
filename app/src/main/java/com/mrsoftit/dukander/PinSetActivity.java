package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PinSetActivity extends AppCompatActivity {

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = Objects.requireNonNull(currentUser).getUid();
    String cutomerGmail = currentUser.getEmail();
    boolean  firstTime;

    PinNote pinNote;
    String pin ;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_set);

        progressDialog = new ProgressDialog(PinSetActivity.this);
        progressDialog.setTitle("তথ্য প্রস্তুত হচ্ছে...");
        progressDialog.setCancelable(false);

        final CollectionReference myPin = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("Pin");

        final PinEntryEditText pinNewPin = findViewById(R.id.txt_pin_entry_new);

        if (pinNewPin != null) {
            pinNewPin.setAnimateText(true);
            pinNewPin.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    pin = str.toString();

                    new MaterialAlertDialogBuilder(PinSetActivity.this, R.style.CutShapeTheme)
                            .setTitle("আপনার সুরক্ষা কোড সেট করন ")
                            .setMessage("আপনার সুরক্ষা কোড :" +pin)
                            .setPositiveButton("বুঝেছি ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    progressDialog.show();
                                    myPin.add(new PinNote(null,cutomerGmail,pin,true)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()){

                                                final String idnew = task.getResult().getId();

                                                myPin.document(idnew).update("id",idnew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        final CollectionReference myPin = FirebaseFirestore.getInstance()
                                                                .collection("AllPin");

                                                        Map<String, Object> PinData = new HashMap<>();
                                                        PinData.put("id",idnew);
                                                        PinData.put("gmail",cutomerGmail);
                                                        PinData.put("pin",pin);
                                                        PinData.put("fairtTime",true);


                                                        myPin.document(idnew).set(PinData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                progressDialog.dismiss();
                                                                startActivity(new Intent(PinSetActivity.this,PinViewActivity.class));
                                                                finish();
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        }
                                    });

                                }
                            })
                            .show();
                }
            });
        }
    }
}
