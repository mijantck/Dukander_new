package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrsoftit.dukander.modle.GlobleCustomerNote;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CustomerLoginActivity extends AppCompatActivity {


    FirebaseAuth Auth;

    TextInputEditText input_name_customer,input_email_cutomer,input_password_customer,confirm_input_password,input_phoneNumber_customer,input_Address_customer;

    ProgressDialog progressDialog;
    Button customer_singup_button;

    String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    String randomStr ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        Auth = FirebaseAuth.getInstance();

        input_name_customer = findViewById(R.id.input_name_customer);
        input_email_cutomer = findViewById(R.id.input_email_cutomer);
        input_password_customer = findViewById(R.id.input_password_customer);
        confirm_input_password = findViewById(R.id.confirm_input_password);
        input_phoneNumber_customer = findViewById(R.id.input_phoneNumber_customer);
        input_Address_customer = findViewById(R.id.input_Address_customer);
        customer_singup_button = findViewById(R.id.customer_singup_button);

        customer_singup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkIntert()) {
                    Toast.makeText(CustomerLoginActivity.this, " NO INTERNET", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String name = input_name_customer.getText().toString();
                final String email = input_email_cutomer.getText().toString();
                final String password1 = input_password_customer.getText().toString();
                String password2 = confirm_input_password.getText().toString();
                final String phoneNumber = input_phoneNumber_customer.getText().toString();
                final String address = input_Address_customer.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(),"Name is empty",Toast.LENGTH_SHORT).show();

                    return;
                }
                if(TextUtils.isEmpty(password1)){
                    Toast.makeText(getApplicationContext(),"Password is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password2)){
                    Toast.makeText(getApplicationContext(),"Confirm Password is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password1.length()<6){
                    Toast.makeText(getApplicationContext(),"Minimum 6 digits password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password1.equals(password2)){
                    Toast.makeText(CustomerLoginActivity.this, "Confirm password not equal", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(getApplicationContext(),"Phone Number is Empty",Toast.LENGTH_SHORT).show();

                    return;
                }
                if(TextUtils.isEmpty(address)){
                    Toast.makeText(getApplicationContext(),"Address is empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(CustomerLoginActivity.this);
                // Setting Message
                progressDialog.setTitle("Loading..."); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();


                randomStr =generateRandom(candidateChars);

                Auth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                try {
                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                                    if (isNewUser) {
                                        Auth.createUserWithEmailAndPassword(email,password1)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if(task.isSuccessful()){


                                                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                                            final String user_id = currentUser.getUid();

                                                            final CollectionReference Info = FirebaseFirestore.getInstance()
                                                                    .collection("Globlecustomers").document(user_id).collection("info");

                                                            final CollectionReference globleRefercode = FirebaseFirestore.getInstance()
                                                                    .collection("globleRefercode");



                                                            Info.add(new GlobleCustomerNote(user_id,"id","globleCustomer",name,email,phoneNumber,address,0,randomStr,false))
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                            if (task.isSuccessful()){
                                                                               final String CustomerID = task.getResult().getId();
                                                                               Info.document(CustomerID).update("id",CustomerID).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                   @Override
                                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                                       Map<String, Object> RefercodeOdject = new HashMap<>();
                                                                                       RefercodeOdject.put("glovleCustomerID",user_id);
                                                                                       RefercodeOdject.put("Id",CustomerID);
                                                                                       RefercodeOdject.put("name",name);
                                                                                       RefercodeOdject.put("email",email);
                                                                                       RefercodeOdject.put("phoneNumber",phoneNumber);
                                                                                       RefercodeOdject.put("address",address);
                                                                                       RefercodeOdject.put("referCode",randomStr);

                                                                                       globleRefercode.document(user_id).set(RefercodeOdject).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                           @Override
                                                                                           public void onComplete(@NonNull Task<Void> task) {

                                                                                               progressDialog.dismiss();
                                                                                               startActivity(new Intent(getApplicationContext(),GlobleProductListActivity.class));
                                                                                               finish();
                                                                                           }
                                                                                       });


                                                                                   }
                                                                               });
                                                                            }

                                                                        }
                                                                    });




                                                        }
                                                        else{
                                                            progressDialog.dismiss();
                                                        }
                                                    }
                                                });
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Already sign up "+email,Toast.LENGTH_SHORT).show();

                                    }
                                }catch (Exception e){
                                    Toast.makeText(CustomerLoginActivity.this, e+"", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });



    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
    public boolean checkIntert(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }

    private static String generateRandom(String aToZ) {
        Random rand=new Random();
        StringBuilder res=new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int randIndex=rand.nextInt(aToZ.length());
            res.append(aToZ.charAt(randIndex));
        }
        return res.toString();
    }
}
