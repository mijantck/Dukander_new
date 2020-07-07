package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrsoftit.dukander.adapter.GiftAdapter;
import com.mrsoftit.dukander.modle.GiftNote;
import com.mrsoftit.dukander.modle.GlobleCustomerNote;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class GlobleCustomerActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    String globlecutouser_id ;

    CollectionReference globleRefercode = FirebaseFirestore.getInstance()
            .collection("Gift");

    private TextInputEditText globleCutomerNameEdite, globleCutomerPhoneEdite,globleCutomerAddressEdite;
    EditText referCode;
    ImageView referButton,profileEditebutton;
    Button updateMyinfo,globle_customer_refer_link_send;
    TextView globle_customer_name_textView,globle_customer_phone_textView,globle_customer_address_textView
            ,globle_customer_referCode_textView,globle_customer_coin_textView;

    RelativeLayout friendreferEditebox;
    LinearLayout editeProfileLayout;

    ProgressDialog progressDialog;
    boolean openORclosse = false;

    GiftAdapter giftAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_globle_customer);

        progressDialog = new ProgressDialog(GlobleCustomerActivity.this);
        // Setting Message
        progressDialog.setTitle("Loading..."); // Setting Title
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
        friendreferEditebox = findViewById(R.id.friendreferEditebox);
        globleCutomerNameEdite = findViewById(R.id.globleCutomerNameEdite);
        globleCutomerPhoneEdite = findViewById(R.id.globleCutomerPhoneEdite);
        globleCutomerAddressEdite = findViewById(R.id.globleCutomerAddressEdite);
        profileEditebutton = findViewById(R.id.profileEditebutton);
        updateMyinfo = findViewById(R.id.updateMyinfo);
        editeProfileLayout = findViewById(R.id.editeProfileLayout);
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


                        }

                    }
                }
            });

        }



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
                    openORclosse = true;
                }else if (openORclosse == true){
                    editeProfileLayout.setVisibility(View.GONE);
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
}
