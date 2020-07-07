package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrsoftit.dukander.modle.GlobleCustomerNote;

import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {


     private TextInputEditText emailTV, passwordTV,forgetEmail;
     private TextView forgetPassword,tvToSignUp;
     LinearLayout loginlayout,forgetPasswordlaouy;

      private Button loginButton,forgetPasswordLink;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;


    private static final int RC_SIGN_IN = 1001;

    GoogleSignInClient googleSignInClient;

    private FirebaseAuth firebaseAuth;

    FirebaseAuth.AuthStateListener mAuthLisenar;

    String cType;


    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {

            showToastMessage("Currently Logged in: " + currentUser.getEmail());
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailTV = findViewById(R.id.input_email);
        passwordTV = findViewById(R.id.input_password);
        loginButton =findViewById(R.id.btnFirebaseSignIn);
        forgetPasswordLink =findViewById(R.id.forgetPasswordLink);
        loginlayout =findViewById(R.id.loginLayout);
        forgetPasswordlaouy =findViewById(R.id.forgetPasswordLayout);
        forgetEmail =findViewById(R.id.forgetPassword_email);
        forgetPassword =findViewById(R.id.tvForgotPassword);
        tvToSignUp =findViewById(R.id.tvToSignUp);

        tvToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(LoginActivity.this);
                // Include dialogpayment.xml file
                dialog.setContentView(R.layout.signup_choos_dialoge);
                // Set dialogpayment title
                dialog.setTitle("Choose your option");
                dialog.show();

                TextView customer = dialog.findViewById(R.id.customer_signup);
                TextView shopkeper = dialog.findViewById(R.id.shopkeper_signup);

                customer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(LoginActivity.this,CustomerLoginActivity.class));

                    }
                });

         shopkeper.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        dialog.dismiss();
        onRegestetionClick(v);

    }
});

            }
        });

        SignInButton signInButton = findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                // Launch Sign In
                signInToGoogle();

            }
        });

        // Configure Google Client
        configureGoogleClient();




        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }



        mAuthLisenar = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }
            }
        };




        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                // Setting Message
                progressDialog.setTitle("তথ্য প্রস্তুত হচ্ছে..."); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                if(checkIntert()){
                    loginUserAccount();
                }else {
                    Toast.makeText(LoginActivity.this, "কোনও ইন্টারনেট সংযোগ নেই ", Toast.LENGTH_SHORT).show();
                }


            }
        });

        if(mAuth.getCurrentUser()!=null){


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

                        if (cType!=null){

                            startActivity(new Intent(getApplicationContext(),GlobleProductListActivity.class));
                            finish();

                        }else {
                            startActivity(new Intent(getApplicationContext(),PinViewActivity.class));
                            finish();
                        }
                    }
                }
            });


            finish();
        }


        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setVisibility(View.GONE);
                loginlayout.setVisibility(View.GONE);
                forgetPasswordlaouy.setVisibility(View.VISIBLE);
                forgetPassword.setVisibility(View.GONE);
            }
        });

        forgetPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = forgetEmail.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"দয়া করে ইমেলটি পূরণ করুন",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new ProgressDialog(LoginActivity.this);
                // Setting Message
                progressDialog.setTitle("তথ্য প্রস্তুত হচ্ছে..."); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"পাসওয়ার্ড রিসেট লিঙ্কটি আপনার ইমেল ঠিকানা প্রেরণ করা হয়েছিল",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"মেল প্রেরণে ত্রুটি",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
    public void onRegestetionClick(View View){
        startActivity(new Intent(this, RegestationActivity.class));

    }
    private void loginUserAccount() {

        String email, password;
        email = emailTV.getText().toString();
        password = passwordTV.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "ইমেল প্রবেশ করুন...", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "পাসওয়ার্ড প্রবেশ করুন!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {



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

                                        if (cType!=null){
                                            progressDialog.dismiss();
                                            startActivity(new Intent(getApplicationContext(),GlobleProductListActivity.class));
                                            Toast.makeText(getApplicationContext(), "সফল লগইন!", Toast.LENGTH_LONG).show();

                                            finish();

                                        }else {

                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                            });

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "লগইন ব্যর্থ! পরে আবার চেষ্টা করুন", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    }
                });

    }

    public boolean checkIntert(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }


    private void configureGoogleClient() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // for the requestIdToken, this is in the values.xml file that
                // is generated from your google-services.json
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);

        signInButton.setSize(SignInButton.SIZE_WIDE);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
    }


    public void signInToGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                showToastMessage("Google Sign in Succeeded");
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                showToastMessage("Google Sign in Failed " + e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            launchMainActivity(user);
                        } else {
                            // If sign in fails, display a message to the user.


                            showToastMessage("Firebase Authentication failed:" + task.getException());
                        }
                    }
                });
    }

    private void showToastMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void launchMainActivity(FirebaseUser user) {
        if (user != null) {

            startActivity(new Intent(getApplicationContext(),MyInfoActivity.class));
            finish();
        }
    }
}


