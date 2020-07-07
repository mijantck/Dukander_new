package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;


import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MyInfoActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    StorageReference mStorageReferenceImage;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;

    FirebaseFirestore db;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String user_id = currentUser.getUid();

    CollectionReference myInfo = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("DukanInfo");



    private static final int PICK_IMAGE_REQUEST = 1;
    public Uri mImageUri;
    public String imageuploadurl ;
    String id;
    boolean image = false;
    boolean firstTime = false;




    String token;

    private LinearLayout shopediteView,shopdelaisView,passChange,PinLayout;
    private MaterialButton etideButton;
    private boolean vigivity =true;
    private ImageView appCompatImageView,shopeImageView;
    private TextView shopname,shopphone,shopaddres;
    private FloatingActionButton imageSelet;




    private TextInputEditText dukanName, dukanPhone,dukanaddess,orldpass,newpass;
    private MaterialButton addmyinfo,confirm,HomeId;

    FirebaseFirestore firestore;
    TextView chagepasswordtextview;
    TextView chagepintextview;
    TextView recharchButton;

    boolean open = true;

    CollectionReference GlobleSoplist = FirebaseFirestore.getInstance()
            .collection("GlobleSoplist");

    @Override
    public void onStart() {
        super.onStart();

        firestore = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String user_id = currentUser.getUid();

        CollectionReference myInfo = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("DukanInfo");

        myInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        MyInfoNote myInfoNote = document.toObject(MyInfoNote.class);
                        imageuploadurl = myInfoNote.getDukanaddpicurl();
                        id = myInfoNote.getMyid();
                        firstTime = myInfoNote.isFirsttime();

                    }

                    if (TextUtils.isEmpty(id)){
                        chagepasswordtextview.setVisibility(View.GONE);
                        passChange.setVisibility(View.GONE);
                        shopdelaisView.setVisibility(View.GONE);
                        etideButton.setVisibility(View.GONE);
                        shopediteView.setVisibility(View.VISIBLE);
                    }
                }

                progressDialog.dismiss();

            }
        });


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        db = FirebaseFirestore.getInstance();


        progressDialog = new ProgressDialog(MyInfoActivity.this);
        // Setting Message
        progressDialog.setTitle("তথ্য প্রস্তুত হচ্ছে..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyInfoActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        appCompatImageView = findViewById(R.id.appCompatImageView);
        dukanName = findViewById(R.id.DukantName);
        dukanPhone = findViewById(R.id.DukantPhone);
        dukanaddess = findViewById(R.id.DukantAddres);
        addmyinfo = findViewById(R.id.addedmyinfo);
        HomeId = findViewById(R.id.HomeId);

        chagepasswordtextview = findViewById(R.id.chagepasswordtextview);
        chagepintextview = findViewById(R.id.chagepintextview);



        shopediteView= findViewById(R.id.shopdetailEditor);
        shopdelaisView= findViewById(R.id.shopdelaisView);
        etideButton= findViewById(R.id.myinfoEditeButton);

        shopeImageView =findViewById(R.id.shopeImageView);
        shopname =findViewById(R.id.shopNamneView);
        shopphone =findViewById(R.id.shopphnoneView);
        shopaddres =findViewById(R.id.shopAddresView);


        orldpass =findViewById(R.id.oldPasword);
        newpass =findViewById(R.id.newPassword);
        confirm =findViewById(R.id.newPasswordButton);

        passChange = findViewById(R.id.chagepintextviewLiniarlayout);

        chagepintextview = findViewById(R.id.chagepintextview);


        imageSelet = findViewById(R.id.imageSelet);





        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (task.isSuccessful()){
                    token = task.getResult().getToken();

                }

            }
        });



        etideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final File docsFolder1 = new File(Environment.getExternalStorageDirectory() +"/Dukandar/dont_delete/");
                File newFile = new File(docsFolder1,"01743.jpeg");

                if (newFile.exists()) {
                    newFile.delete();
                }
                if (vigivity ==true){
                    chagepasswordtextview.setVisibility(View.GONE);
                    passChange.setVisibility(View.GONE);
                    shopdelaisView.setVisibility(View.GONE);
                    shopediteView.setVisibility(View.VISIBLE);
                    vigivity=false;
                    etideButton.setVisibility(View.GONE);
                    HomeId.setVisibility(View.GONE);

                }else if (vigivity == false){
                    chagepasswordtextview.setVisibility(View.VISIBLE);
                    passChange.setVisibility(View.VISIBLE);
                    shopdelaisView.setVisibility(View.VISIBLE);
                    shopediteView.setVisibility(View.GONE);
                    shopdelaisView.setVisibility(View.VISIBLE);

                    vigivity=true;
                }
            }
        });


        updateadtaall();

        HomeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyInfoActivity.this,MainActivity.class));
                finish();

            }
        });


        addmyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkIntert()) {

                    Toast.makeText(MyInfoActivity.this, " কোনও ইন্টারনেট সংযোগ নেই ", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = dukanName.getText().toString();
                String price = dukanPhone.getText().toString();
                String ppq = dukanaddess.getText().toString();

                if (TextUtils.isEmpty(name) ){
                    Toast.makeText(getApplicationContext(), "নাম লিখুন...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(price) ){
                    Toast.makeText(getApplicationContext(), "মোবাইল নম্বর লিখুন...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(ppq) ){
                    Toast.makeText(getApplicationContext(), "ঠিকানা লিখুন...", Toast.LENGTH_LONG).show();
                    return;
                }



                progressDialog = new ProgressDialog(MyInfoActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setTitle("আপলোড হচ্ছে...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);


                if (imageuploadurl == null && id!=null && mImageUri!=null){

                  //  MyInfoUploadWithpicnew(mImageUri);
                    uploadImageUri(mImageUri);
                }
                if ( image == false && imageuploadurl != null) {

                    progressDialog = new ProgressDialog(MyInfoActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("আপলোড হচ্ছে...");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);

                    final String dukanName1 = dukanName.getText().toString();
                    final String dukanphon1 = dukanPhone.getText().toString();
                    final String dukanAddres1 = dukanaddess.getText().toString();



                    myInfo.document(id).update( "dukanName", dukanName1, "dukanphone", dukanphon1, "dukanaddress", dukanAddres1,"token",token)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    shopdelaisView.setVisibility(View.VISIBLE);
                                    shopediteView.setVisibility(View.GONE);
                                    etideButton.setVisibility(View.GONE);

                                    updateadtaall();

                                    setGlobleSoplist(true,user_id,id,dukanName1,dukanphon1,dukanAddres1);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }

                else if (mImageUri!=null){

                    uploadImageUri(mImageUri);

                }

                else if (mImageUri == null && id ==null ){

                    final String dukanName1 = dukanName.getText().toString();
                    final String dukanphon1 = dukanPhone.getText().toString();
                    final String dukanAddres1 = dukanaddess.getText().toString();




                    Random rand = new Random();
                    String picname = String.format("%05d", rand.nextInt(10000));
                    myInfo.add(new MyInfoNote(null, dukanName1, dukanphon1, dukanAddres1,true,picname,0.0,0.0,0.0,0,token)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if (task.isSuccessful()) {

                                final String id = task.getResult().getId();


                                myInfo.document(id).update("myid", id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        shopdelaisView.setVisibility(View.VISIBLE);
                                        shopediteView.setVisibility(View.GONE);
                                        etideButton.setVisibility(View.GONE);
                                        HomeId.setVisibility(View.VISIBLE);
                                        updateadtaall();

                                        setGlobleSoplist(false,user_id,id,dukanName1,dukanphon1,dukanAddres1);

                                        Toast.makeText(MyInfoActivity.this, " সফল ", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                        }
                    });

                }
                else if (mImageUri == null && id !=null ){

                    final String dukanName1 = dukanName.getText().toString();
                    final String dukanphon1 = dukanPhone.getText().toString();
                    final String dukanAddres1 = dukanaddess.getText().toString();
                    Random rand = new Random();
                    String picname = String.format("%05d", rand.nextInt(10000));

                    myInfo.document(id).update("myid", id, "dukanName", dukanName1, "dukanphone", dukanphon1, "dukanaddress", dukanAddres1,"firsttime",firstTime,"token",token)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    setGlobleSoplist(true,user_id,id,dukanName1,dukanphon1,dukanAddres1);

                                    shopdelaisView.setVisibility(View.VISIBLE);
                                    shopediteView.setVisibility(View.GONE);
                                    etideButton.setVisibility(View.GONE);
                                    updateadtaall();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    });

                }


            }
        });



        imageSelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getIMEGE();
                image = true;
            }
        });

        TextView chagepasswordtextview = findViewById(R.id.chagepasswordtextview);
        chagepasswordtextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout PasswordLayout = findViewById(R.id.PasswordLayout);

                if ( open == true){
                    PasswordLayout.setVisibility(View.VISIBLE);
                    open = false;
                }else  if (open == false){
                    PasswordLayout.setVisibility(View.GONE);
                    open = true;
                }

            }
        });

        chagepintextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              startActivity(new Intent(MyInfoActivity.this,ChangePinActivity.class));

            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

              String olrdpassowr=  orldpass.getText().toString();
              final String newdpassowr=  newpass.getText().toString();

              if (olrdpassowr.isEmpty()){
                  Toast.makeText(MyInfoActivity.this, " পুরানো পাসওয়ার্ড লিখুন", Toast.LENGTH_SHORT).show();
                  return;
              }
              if (newdpassowr.isEmpty()){
                    Toast.makeText(MyInfoActivity.this, "নতুন  পাসওয়ার্ড লিখুন", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email = currentUser.getEmail();
                if (email.isEmpty()){
                    return;
                }

                AuthCredential credential = EmailAuthProvider.getCredential(email,olrdpassowr);

                currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            currentUser.updatePassword(newdpassowr).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()){
                                        Snackbar snackbar_fail = Snackbar
                                                .make(v, "কিছু ভুল হয়েছে. পরে আবার চেষ্টা করুন", Snackbar.LENGTH_LONG);
                                        snackbar_fail.show();
                                    }else {
                                        Snackbar snackbar_su = Snackbar
                                                .make(v, "পাসওয়ার্ড সফলভাবে সংশোধিত হয়েছে", Snackbar.LENGTH_LONG);
                                        snackbar_su.show();
                                    }
                                }
                            });
                        }else {
                            Snackbar snackbar_su = Snackbar
                                    .make(v, "প্রমাণীকরণ ব্যর্থ হয়েছে", Snackbar.LENGTH_LONG);
                            snackbar_su.show();
                        }
                    }
                });
            }
        });
    }

    @AfterPermissionGranted(PICK_IMAGE_REQUEST)
    private void getIMEGE() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            List<Integer> lPermission = new ArrayList<>();
            List<String> stringPermissionList1 = getPermissionList();
            for (int i = 0; i < stringPermissionList1.size(); i++) {
                lPermission.add(ContextCompat.checkSelfPermission(getApplicationContext(), stringPermissionList1.get(i)));
            }
            boolean bPermissionDenied = false;
            for (int i = 0; i < lPermission.size(); i++) {
                int a = lPermission.get(i);
                if (PackageManager.PERMISSION_DENIED == a) {
                    bPermissionDenied = true;
                    break;
                }
            }


            if (bPermissionDenied) {
                String sMessage = "Please allow all permissions shown in upcoming dialog boxes, so that app functions properly";
                //make request to the user
                List<String> stringPermissionList = getPermissionList();
                String[] sPermissions = stringPermissionList.toArray(new String[stringPermissionList.size()]);

                //request the permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(sPermissions, PICK_IMAGE_REQUEST);
                }
            } else {


            }


        } else {
/*

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Image Add "), PICK_IMAGE_REQUEST);
*/

        }








        ///////////////////////////////////////////////////////////////////
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "চিত্র নির্বাচন করুন"), PICK_IMAGE_REQUEST);

        } else {
          /*  EasyPermissions.requestPermissions(this, "আমাদের অনুমতি দরকার",
                    PICK_IMAGE_REQUEST , perms);*/
        }
    }


    private List<String> getPermissionList(){
        List<String> stringPermissionList=new ArrayList<>();

        stringPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        stringPermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return  stringPermissionList;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isAllPermissionGranted = true;

        for (int i = 0; i < grantResults.length; i++) {
            int iPermission = grantResults[i];
            if (iPermission == PackageManager.PERMISSION_DENIED) {
                isAllPermissionGranted = false;
                break;
            }
        }
        if (isAllPermissionGranted) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Image Add "), PICK_IMAGE_REQUEST);
        } else {
            // Prompt the user to grant all permissions
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                mImageUri = data.getData();
                Picasso.get().load(mImageUri).into(appCompatImageView);
            } else {
                Toast.makeText(this, "কোনো ফাইল পছন্দ করা হইনি", Toast.LENGTH_SHORT).show();
            }
        }}



/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }*/

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    public void  updateadtaall(){


        firebaseFirestore = FirebaseFirestore.getInstance();
        mStorageReferenceImage = FirebaseStorage.getInstance().getReference().child(user_id).child("CUstomer_image");

        myInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        MyInfoNote myInfoNote = document.toObject(MyInfoNote.class);

                        shopname.setText(myInfoNote.getDukanName());
                        shopphone.setText(myInfoNote.getDukanphone());
                        shopaddres.setText(myInfoNote.getDukanaddress());

                        if (myInfoNote.getDukanaddpicurl()!=null){
                            Uri myUri = Uri.parse(myInfoNote.getDukanaddpicurl());
                            Picasso.get().load(myUri).into(shopeImageView);
                            Picasso.get().load(myUri).into(appCompatImageView);
                        }

                        dukanName.setText(myInfoNote.getDukanName());
                        dukanPhone.setText(myInfoNote.getDukanphone());
                        dukanaddess.setText(myInfoNote.getDukanaddress());


                        imageuploadurl = myInfoNote.getDukanaddpicurl();
                        id = myInfoNote.getMyid();
                        firstTime = myInfoNote.isFirsttime();


                    }
                }
            }
        });
    }


    private void uploadImageUri(Uri imageUri){


        try {
            File file = new File(Environment.getExternalStorageDirectory(), "profilePicTemp");

            InputStream in = getContentResolver().openInputStream(imageUri);
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();


            upload(file, new UploadCallback() {
                @Override
                public void onSuccess(final String downloadLink) {

                    final String dukanName1 = dukanName.getText().toString();
                    final String dukanphon1 = dukanPhone.getText().toString();
                    final String dukanAddres1 = dukanaddess.getText().toString();

                    if (image != false && id !=null) {

                        myInfo.document(id).update("myid", id, "dukanName", dukanName1, "dukanphone", dukanphon1, "dukanaddress", dukanAddres1,  "dukanaddpicurl", downloadLink,"firsttime",firstTime,"token",token)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        setGlobleSoplistURL(true,user_id,id,dukanName1,downloadLink,dukanphon1,dukanAddres1);

                                        shopdelaisView.setVisibility(View.VISIBLE);
                                        shopediteView.setVisibility(View.GONE);
                                        etideButton.setVisibility(View.GONE);
                                        updateadtaall();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        });

                    } else if(firstTime == false && image == true ){

                        Random rand = new Random();
                        String picname = String.format("%05d", rand.nextInt(10000));
                        myInfo.add(new MyInfoNote(null, dukanName1, dukanphon1, dukanAddres1, downloadLink,true,picname,0.0,0.0,0.0,0,token)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                if (task.isSuccessful()) {

                                    final String id = task.getResult().getId();


                                    myInfo.document(id).update("myid", id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            shopdelaisView.setVisibility(View.VISIBLE);
                                            shopediteView.setVisibility(View.GONE);
                                            etideButton.setVisibility(View.GONE);
                                            updateadtaall();
                                            setGlobleSoplistURL(false,user_id,id,dukanName1,downloadLink,dukanphon1,dukanAddres1);

                                            progressDialog.dismiss();
                                            Toast.makeText(MyInfoActivity.this, " সফল ", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                            }
                        });
                    }

                    progressDialog.dismiss();
                    HomeId.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailed(String message) {
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private void upload(File file, final UploadCallback uploadCallback) {

        //this is for log message
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        //create file to request body and request
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), saveBitmapToFile(file));
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("image", "filename.png", requestBody)
                .build();

        //request create for imgur
        final Request request = new Request.Builder()
                .url("https://api.imgur.com/3/image")
                .method("POST", body)
                .addHeader("Authorization", "Client-ID 2f4dd94e6dbf1f1")
                .build();

        //okhttp client create
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .build();



        //network request so we need to run on new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response response = client.newCall(request).execute();

                    if(response.isSuccessful()){
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        final String link =  jsonObject.getJSONObject("data").getString("link");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadCallback.onSuccess(link);
                            }
                        });


                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                uploadCallback.onFailed("Error message: " + response.message());
                            }
                        });

                    }

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            uploadCallback.onFailed("Io Exception");
                        }
                    });

                    e.printStackTrace();
                }
            }
        }).start();

    }

    interface UploadCallback{
        void onSuccess(String downloadLink);
        void onFailed(String message);
    }

    public boolean checkIntert(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }

    public File saveBitmapToFile(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public void setGlobleSoplist(boolean update,String shopUserId,String shoId,String shopName,String shopPhone,String shopAddress){


        Map<String, Object> GlobaleShopList = new HashMap<>();
        GlobaleShopList.put("shopUserId", shopUserId);
        GlobaleShopList.put("shopId", shoId);
        GlobaleShopList.put("ShopName", shopName);
        GlobaleShopList.put("search", shopName.toLowerCase());
        GlobaleShopList.put("ShopPhone", shopPhone);
        GlobaleShopList.put("ShopAddress", shopAddress);
        GlobaleShopList.put("token", token);

        if (update == false) {
            GlobleSoplist.document(shoId).set(GlobaleShopList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    progressDialog.dismiss();
                }
            });
        }else if (update == true){
            GlobleSoplist.document(shoId).update(GlobaleShopList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    progressDialog.dismiss();
                }
            });
        }
    }
    public void setGlobleSoplistURL(boolean update,String shopUserId,String shoId,String shopName,String shopImageURL,String shopPhone,String shopAddress){


        Map<String, Object> GlobaleShopList = new HashMap<>();

        GlobaleShopList.put("shopUserId", shopUserId);
        GlobaleShopList.put("shopId", shoId);
        GlobaleShopList.put("ShopName", shopName);
        GlobaleShopList.put("ShopImageURL", shopImageURL);
        GlobaleShopList.put("search", shopName.toLowerCase());
        GlobaleShopList.put("ShopPhone", shopPhone);
        GlobaleShopList.put("ShopAddress", shopAddress);
        GlobaleShopList.put("token", token);

        if (update == false) {
            GlobleSoplist.document(shoId).set(GlobaleShopList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(MyInfoActivity.this, " globale added", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
       else if (update == true) {
            GlobleSoplist.document(shoId).update(GlobaleShopList).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    progressDialog.dismiss();
                }
            });
        }
    }

    public void privecyupdateData(ArrayList list,String Privacy) {


        // Get a new write batch
        WriteBatch batch = db.batch();

        if (Privacy.contains("private")) {
            // Iterate through the list
            for (int k = 0; k < list.size(); k++) {
                // Update each list item
                DocumentReference ref = db.collection("GlobleProduct")
                        .document((String) list.get(k));
                batch.update(ref, "productPrivacy", "private");
            }
        }
        if (Privacy.contains("Public")) {
            for (int k = 0; k < list.size(); k++) {
                // Update each list item
                DocumentReference ref = db.collection("GlobleProduct")
                        .document((String) list.get(k));
                batch.update(ref, "productPrivacy", "Public");
            }
        }
        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Yay its all done in one go!
            }
        });

    }


}


