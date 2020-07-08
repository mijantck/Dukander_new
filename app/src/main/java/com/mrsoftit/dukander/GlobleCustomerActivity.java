package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrsoftit.dukander.adapter.GiftAdapter;
import com.mrsoftit.dukander.modle.GiftNote;
import com.mrsoftit.dukander.modle.GlobleCustomerNote;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class GlobleCustomerActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    private FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    String globlecutouser_id ;

    private static final int PICK_IMAGE_REQUEST = 1;
    public Uri mImageUri;
    String id;

    FloatingActionButton imageSeletglobleCustomer;

    CollectionReference globleRefercode = FirebaseFirestore.getInstance()
            .collection("Gift");

    private TextInputEditText globleCutomerNameEdite, globleCutomerPhoneEdite,globleCutomerAddressEdite,globleCutomerZipCodeEdite;
    EditText referCode;
    ImageView referButton,profileEditebutton,globle_customer_profile_pic;
    Button updateMyinfo,globle_customer_refer_link_send;
    TextView globle_customer_name_textView,globle_customer_phone_textView,globle_customer_address_textView
            ,globle_customer_referCode_textView,globle_customer_coin_textView,globle_customer_zipCode_textView;

    RelativeLayout friendreferEditebox;
    LinearLayout editeProfileLayout,infoView;

    ProgressDialog progressDialog;
    boolean openORclosse = false;

    GiftAdapter giftAdapter;

    boolean image = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_globle_customer);

        progressDialog = new ProgressDialog(GlobleCustomerActivity.this);
        // Setting Message
        progressDialog.setMessage("Loading..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);



        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();



        referCode = findViewById(R.id.globle_customer_refer_code_EditetextView);
        referButton = findViewById(R.id.globle_customer_refer_code_Button);
        globle_customer_name_textView = findViewById(R.id.globle_customer_name_textView);
        globle_customer_phone_textView = findViewById(R.id.globle_customer_phone_textView);
        globle_customer_address_textView = findViewById(R.id.globle_customer_address_textView);
        globle_customer_referCode_textView = findViewById(R.id.globle_customer_referCode_textView);
        globle_customer_coin_textView = findViewById(R.id.globle_customer_coin_textView);
        globle_customer_zipCode_textView = findViewById(R.id.globle_customer_zipCode_textView);
        globleCutomerZipCodeEdite = findViewById(R.id.globleCutomerZipCodeEdite);
        friendreferEditebox = findViewById(R.id.friendreferEditebox);
        friendreferEditebox = findViewById(R.id.friendreferEditebox);
        globleCutomerNameEdite = findViewById(R.id.globleCutomerNameEdite);
        globleCutomerPhoneEdite = findViewById(R.id.globleCutomerPhoneEdite);
        globleCutomerAddressEdite = findViewById(R.id.globleCutomerAddressEdite);
        profileEditebutton = findViewById(R.id.profileEditebutton);
        updateMyinfo = findViewById(R.id.updateMyinfo);
        infoView = findViewById(R.id.infoView);
        globle_customer_profile_pic = findViewById(R.id.globle_customer_profile_pic);
        editeProfileLayout = findViewById(R.id.editeProfileLayout);
        imageSeletglobleCustomer = findViewById(R.id.imageSeletglobleCustomer);
        globle_customer_refer_link_send = findViewById(R.id.globle_customer_refer_link_send);

        if (currentUser!=null){
            globlecutouser_id = currentUser.getUid();


            final CollectionReference MyInfo = FirebaseFirestore.getInstance()
                    .collection("Globlecustomers").document(globlecutouser_id).collection("info");

            MyInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            final GlobleCustomerNote myglobleCustomerNote = document.toObject(GlobleCustomerNote.class);
                            if (myglobleCustomerNote.isFirstTimeRafer() ==false){
                                friendreferEditebox.setVisibility(View.VISIBLE);
                            }
                            globle_customer_name_textView.setText(myglobleCustomerNote.getName());
                            globleCutomerNameEdite.setText(myglobleCustomerNote.getName());

                            globle_customer_phone_textView.setText(myglobleCustomerNote.getPhoneNumber());
                            globleCutomerPhoneEdite.setText(myglobleCustomerNote.getPhoneNumber());

                            globle_customer_address_textView.setText(myglobleCustomerNote.getAddress());
                            globleCutomerAddressEdite.setText(myglobleCustomerNote.getAddress());

                            globle_customer_coin_textView.setText(myglobleCustomerNote.getCoine()+"");
                            globle_customer_referCode_textView.setText(myglobleCustomerNote.getReferCode());

                            if (myglobleCustomerNote.getZipCode() !=null){
                                globleCutomerZipCodeEdite.setText(myglobleCustomerNote.getZipCode());
                                globle_customer_zipCode_textView.setText(myglobleCustomerNote.getZipCode());
                            }

                            if (myglobleCustomerNote.getImageURL() !=null){
                                String Url = myglobleCustomerNote.getImageURL();
                                Picasso.get().load(Url).into(globle_customer_profile_pic);
                            }

                        }

                    }
                }
            });

        }


        imageSeletglobleCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getIMEGE();
                }
            }
        });


        globle_customer_refer_link_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentUser != null){

                    final CollectionReference MyInfo = FirebaseFirestore.getInstance()
                            .collection("Globlecustomers").document(globlecutouser_id).collection("info");

                    MyInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()){


                                String name =null;
                                String id =null;
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    final GlobleCustomerNote myglobleCustomerNote = document.toObject(GlobleCustomerNote.class);
                                    name = myglobleCustomerNote.getName();
                                    id = myglobleCustomerNote.getId();
                                }

                                if (name !=null) {

                                    String sharelinktext = "https://sellersmarkets.page.link/?" +
                                            "link=https://sellersmarkets.page.link/c2Sd?" +
                                            "proID=" + "-" + "refer" +
                                            "-" + globlecutouser_id +
                                            "-" + id +
                                            "&st=" + name  +
                                            "&sd=" + " An online shopping mall where hundreds, thousands " +
                                            "of physical and online shop can offer their price. Sellers Market  will ensure delivery! " +
                                            "- You can pick the shop you trust from your past buyings - " +
                                            "First ever product search engine including everything means every thing! " +
                                            "- Delivery time could be even 10 min and must be faster than any delivery service.\""+
                                            "&si=" + "https://www.linkpicture.com/q/logo_2.jpg" +
                                            "&apn=" + getPackageName();


                                    Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                            // .setLongLink(dynamicLink.getUri())
                                            .setLongLink(Uri.parse(sharelinktext))// manually
                                            .buildShortDynamicLink()
                                            .addOnCompleteListener(GlobleCustomerActivity.this, new OnCompleteListener<ShortDynamicLink>() {
                                                @Override
                                                public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                                    if (task.isSuccessful()) {
                                                        // Short link created
                                                        Uri shortLink = task.getResult().getShortLink();
                                                        Uri flowchartLink = task.getResult().getPreviewLink();
                                                        Log.e("main ", "short link " + shortLink.toString());
                                                        // share app dialog
                                                        Intent intent = new Intent();
                                                        intent.setAction(Intent.ACTION_SEND);
                                                        intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                                                        intent.setType("text/plain");
                                                        startActivity(intent);


                                                    } else {
                                                        // Error
                                                        // ...
                                                        Log.e("main", " error " + task.getException());

                                                    }
                                                }
                                            });

                                }
                            }
                        }
                    });

                }else {

                    Toast.makeText(GlobleCustomerActivity.this, " Signup please", Toast.LENGTH_SHORT).show();
                }
            }
        });


        giftRecyclearview();

        referButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                final String tesTcode =   referCode.getText().toString().trim();

                final CollectionReference MyInfo11 = FirebaseFirestore.getInstance()
                        .collection("Globlecustomers").document(globlecutouser_id).collection("info");

                MyInfo11.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            String checkcode = null;

                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                final GlobleCustomerNote testglobleCustomerNote = document.toObject(GlobleCustomerNote.class);

                                checkcode = testglobleCustomerNote.getReferCode();
                               }

                            if (!checkcode.equals(tesTcode)){

                                referMethod();
                            }
                            else {
                                new MaterialAlertDialogBuilder(GlobleCustomerActivity.this)
                                        .setTitle("Opps..Sorry...")
                                        .setMessage(tesTcode+" its your refer code")
                                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .show();
                                }
                        }
                    }
                });
            }
        });

        profileEditebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (openORclosse == false){
                    editeProfileLayout.setVisibility(View.VISIBLE);
                    infoView.setVisibility(View.GONE);
                    imageSeletglobleCustomer.setVisibility(View.VISIBLE);
                    openORclosse = true;
                }else if (openORclosse == true){
                    editeProfileLayout.setVisibility(View.GONE);
                    infoView.setVisibility(View.VISIBLE);
                    imageSeletglobleCustomer.setVisibility(View.GONE);
                    openORclosse = false;
                }

            }
        });

        updateMyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser !=null){

                    progressDialog.show();
                    final String Name = globleCutomerNameEdite.getText().toString();
                    final String phone = globleCutomerPhoneEdite.getText().toString();
                    final String address = globleCutomerAddressEdite.getText().toString();

                    final CollectionReference globleRefercode = FirebaseFirestore.getInstance()
                            .collection("globleRefercode");

                    final CollectionReference MyInfo = FirebaseFirestore.getInstance()
                            .collection("Globlecustomers").document(currentUser.getUid()).collection("info");

                    MyInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                String id = null;
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    final GlobleCustomerNote myglobleCustomerNote = document.toObject(GlobleCustomerNote.class);

                                    id = myglobleCustomerNote.getId();
                                }

                                if (mImageUri !=null){
                                    uploadImageUri(mImageUri,id);

                                    progressDialog.dismiss();
                                }
                                MyInfo.document(id).update("name",Name,"phoneNumber",phone,"address",address).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        globleRefercode.document(currentUser.getUid()).update("name",Name,"phoneNumber",phone,"address",address).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                editeProfileLayout.setVisibility(View.GONE);
                                                openORclosse = false;
                                            }
                                        });

                                    }
                                });
                            }
                        }
                    });

                }else {

                    Toast.makeText(GlobleCustomerActivity.this, " Signup please", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
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






        /////////////////////////////////////////////////////////////////////////////////////////////
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(this, perms)) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Image Add "), PICK_IMAGE_REQUEST);

        } else {
           /* EasyPermissions.requestPermissions(this, "Permission  ",
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
                Picasso.get().load(mImageUri).into(globle_customer_profile_pic);
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }}

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }



    public void referMethod(){
        progressDialog.show();

        final CollectionReference globleRefercode1 = FirebaseFirestore.getInstance()
                .collection("globleRefercode");

        final String code =   referCode.getText().toString().trim();
        Query query = globleRefercode1.whereEqualTo("referCode",code);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    String testName = null;
                    String testFriendGlobleID = null;
                    String testPhone = null;
                    String testEmail = null;
                    String testReferCode = null;

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        final GlobleCustomerNote globleCustomerNote = document.toObject(GlobleCustomerNote.class);

                        testName =  globleCustomerNote.getName();
                        testFriendGlobleID =  globleCustomerNote.getGlovleCustomerID();
                        testPhone =  globleCustomerNote.getPhoneNumber();
                        testEmail =  globleCustomerNote.getEmail();
                        testReferCode =  globleCustomerNote.getReferCode();
                    }
                    if (testName!=null){
                        progressDialog.dismiss();
                        final Dialog dialogrefer = new Dialog(GlobleCustomerActivity.this);
                        // Include dialog.xml file
                        dialogrefer.setContentView(R.layout.refer_dialog_view);
                        dialogrefer.show();
                        TextView name = dialogrefer.findViewById(R.id.friendName);
                        TextView email = dialogrefer.findViewById(R.id.friendEmail);
                        TextView phone = dialogrefer.findViewById(R.id.friendPhone);
                        TextView refercode = dialogrefer.findViewById(R.id.friendrefercode);
                        Button confirmbutton = dialogrefer.findViewById(R.id.referConfirmButton);

                        name.setText(testName);
                        email.setText(testEmail);
                        phone.setText(testPhone);
                        refercode.setText(testReferCode);
                        final String finalTestFriendGlobleID = testFriendGlobleID;
                        confirmbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progressDialog.show();
                                final CollectionReference Info = FirebaseFirestore.getInstance()
                                        .collection("Globlecustomers").document(finalTestFriendGlobleID).collection("info");

                                Info.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            int friendCoin = 0;
                                            String referFrindID = null;
                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                final GlobleCustomerNote globleCustomerNote = document.toObject(GlobleCustomerNote.class);

                                                friendCoin = globleCustomerNote.getCoine();
                                                referFrindID = globleCustomerNote.getId();
                                            }

                                            friendCoin = friendCoin +100;

                                            Info.document(referFrindID).update("coine",friendCoin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull final Task<Void> task) {

                                                    final CollectionReference MyInfo1 = FirebaseFirestore.getInstance()
                                                            .collection("Globlecustomers").document(globlecutouser_id).collection("info");

                                                    MyInfo1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                            if (task.isSuccessful()){
                                                                int myfriendCoin = 0;
                                                                String id = null;
                                                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                                    final GlobleCustomerNote myglobleCustomerNote = document.toObject(GlobleCustomerNote.class);

                                                                    myfriendCoin = myglobleCustomerNote.getCoine();
                                                                    id = myglobleCustomerNote.getId();
                                                                }
                                                                myfriendCoin = myfriendCoin +100;


                                                                MyInfo1.document(id).update("coine",myfriendCoin,"firstTimeRafer",true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if(task.isSuccessful()){
                                                                            friendreferEditebox.setVisibility(View.GONE);
                                                                            progressDialog.dismiss();
                                                                            new MaterialAlertDialogBuilder(GlobleCustomerActivity.this)
                                                                                    .setTitle(" Congratulation")
                                                                                    .setMessage(" You win 100 coin  ")
                                                                                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                                            dialogrefer.dismiss();
                                                                                            dialogInterface.dismiss();
                                                                                        }
                                                                                    })
                                                                                    .show();
                                                                        }


                                                                    }
                                                                });
                                                            }
                                                        }

                                                    });

                                                }
                                            });
                                        }
                                    }
                                });

                            }
                        });
                    }else {
                        progressDialog.dismiss();
                        new MaterialAlertDialogBuilder(GlobleCustomerActivity.this)
                                .setTitle(" Wong refer Code")
                                .setMessage("places  try again later....")
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();

                    }
                }
            }
        });
    }

    public void  giftRecyclearview(){

      Query query = globleRefercode;

        FirestoreRecyclerOptions<GiftNote> options = new FirestoreRecyclerOptions.Builder<GiftNote>()
                .setQuery(query, GiftNote.class)
                .build();

        giftAdapter = new GiftAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.giftRecyclearview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(giftAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        giftAdapter.startListening();
    }



    public boolean checkIntert(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }

    private void uploadImageUri(Uri imageUri, final String id){


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        final CollectionReference MyInfo = FirebaseFirestore.getInstance()
                .collection("Globlecustomers").document(currentUser.getUid()).collection("info");


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



            Log.d("Test", "uploadImageUri: " + imageUri.getPath());

            upload(file, new CustomerAddActivity.UploadCallback() {
                @Override
                public void onSuccess(String downloadLink) {


                    MyInfo.document(id).update("imageURL",downloadLink).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                    progressDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onFailed(String message) {
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }


    }

    private void upload(File file, final CustomerAddActivity.UploadCallback uploadCallback) {

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
}
