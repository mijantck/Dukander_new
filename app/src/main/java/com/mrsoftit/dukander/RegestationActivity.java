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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class RegestationActivity extends AppCompatActivity {


    FirebaseAuth Auth;

    TextInputEditText editTextEmail,editTextPassword,ConfirmTextPassword;

    TextView alredyResistetionTextView;
    Button cirRegisterButton;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regesstion);

        alredyResistetionTextView =findViewById(R.id.alredyResistetionTextView);

        editTextEmail =findViewById(R.id.editTextEmailregistesion);
        editTextPassword =findViewById(R.id.editTextPassword);
        ConfirmTextPassword =findViewById(R.id.ConfirmTextPassword);
        cirRegisterButton =findViewById(R.id.cirRegisterButton);

        Auth = FirebaseAuth.getInstance();

        alredyResistetionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClick();
            }
        });


        changeStatusBarColor();

        cirRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(!checkIntert()) {
                    Toast.makeText(RegestationActivity.this, "NO Intertnet ", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String email = editTextEmail.getText().toString();
                final String password = editTextPassword.getText().toString();
                final String confimpassword = ConfirmTextPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Input Email",Toast.LENGTH_SHORT).show();

                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Input Password ",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<6){

                    Toast.makeText(getApplicationContext(),"Minimum 6 digit ",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confimpassword)){
                    Toast.makeText(getApplicationContext(),"Confirm Password not match",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new ProgressDialog(RegestationActivity.this);
                // Setting Message
                progressDialog.setMessage("Loading..."); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                //check email already exist or not.
                Auth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            try {
    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
    if (isNewUser) {
        Auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),MyInfoActivity.class));
                            finish();

                        }
                        else{
                            progressDialog.dismiss();
                        }
                    }
                });
    } else {
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(),email+" Already Signup  ",Toast.LENGTH_SHORT).show();

    }
}catch (Exception e){
                                 Toast.makeText(RegestationActivity.this, e+"", Toast.LENGTH_SHORT).show();
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

    public void onLoginClick(){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }


    public boolean checkIntert(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }

}
