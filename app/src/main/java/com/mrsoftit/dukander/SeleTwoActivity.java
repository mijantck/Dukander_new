package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SeleTwoActivity extends AppCompatActivity {


    SaleProductIndevicualAdapter saleProductIndevicualAdapter;

    FirebaseFirestore db;

    TextView bundelCustomerName,bundelCustomerphone,bundelCustomeraddrss,bundelCustomertaka,invoiseIdTextView,TotalAmount;

    ImageView barcoeScanner;

    FirebaseFirestore firestore;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String user_id = Objects.requireNonNull(currentUser).getUid();


    CollectionReference product = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Product");


    CollectionReference invoiseFb = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("invoise");


     List<String> titleList;

    String  idup, nameup,phoneup,takaup,addresup,imageup;

    String bundelId,takacutomerup;

    LinearLayout bundl,unkonwn,showitemLinearLayout;
    Button addItemButton;

    List<ProductNote> exampleList;

    SealProductSelectAdapter adapter1 ;
     String paymonysend;


     //barcode

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;
    private MaterialButton barcode_Buton;
    Dialog barDialog;


    int invoisenumber;

    String invoisenumberID;

    String unknonwnCustomerId;

    FloatingActionButton  OKfloatingActionButton,PDFfloatingActionButton;

    EditText payeditetext;
    TextView paymentLooding,TitleTExt;

    double totallestAvount;
    Double dueBalance ;
    double withpaytaka;

    SearchableSpinner searchableSpinner;
    double daubill;

    Date calendar1 = Calendar.getInstance().getTime();
    @SuppressLint("SimpleDateFormat")
    DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
    String todayString = df1.format(calendar1);
    final int datenew = Integer.parseInt(todayString);

    EditText unKnoneName,unKnonePhone,unKnoneAddres,unKnonetaka;

    int invoiceINt ;

      String myinfoid;

      Double activeBalance;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sele_two);

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeleTwoActivity.this,SaleoOneActivity.class);
                startActivity(intent);
                finish();
            }
        });


        db = FirebaseFirestore.getInstance();



        bundelCustomerName = findViewById(R.id.bundelCustomerName);
        bundelCustomeraddrss = findViewById(R.id.bundelCustomerAddress);
        bundelCustomerphone= findViewById(R.id.bundelCustomerPhone);
        bundelCustomertaka= findViewById(R.id.bundelCustomertaka);
        bundl = findViewById(R.id.bundellLayout);

        unkonwn = findViewById(R.id.unknownCustomerinfo);
        showitemLinearLayout = findViewById(R.id.showitemLinearLayout);

        addItemButton = findViewById(R.id.addItemButton);
        invoiseIdTextView = findViewById(R.id.invoiseId);
        TotalAmount = findViewById(R.id.totalAmount);
        OKfloatingActionButton = findViewById(R.id.okfab);
        PDFfloatingActionButton = findViewById(R.id.PDFfab);
        barcoeScanner = findViewById(R.id.barcoeScanner);
        searchableSpinner = (SearchableSpinner) findViewById(R.id.searchable_spinner);



        unKnoneName = findViewById(R.id.unKnoneName);
        unKnonePhone = findViewById(R.id.unKnonePhone);
        unKnoneAddres = findViewById(R.id.unKnoneAddres);
        unKnonetaka = findViewById(R.id.unKnonetaka);

        CollectionReference myInfo = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("DukanInfo");


        myInfo.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {

                    return;
                }
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("myid") != null) {
                        myinfoid = doc.getId();
                    }
                    if (doc.get("activeBalance") != null) {

                        Double activeBalanceConvert = Double.parseDouble(doc.get("activeBalance").toString());
                        activeBalance = activeBalanceConvert;
                    }

                    if (doc.get("totalpaybil") != null) {

                        Double dueBalanceConvert = Double.parseDouble(doc.get("totalpaybil").toString());

                        dueBalance = dueBalanceConvert;
                    }
                }

            }
        });


        Date calendar1 = Calendar.getInstance().getTime();
        DateFormat df1 = new SimpleDateFormat("yyMMddhhmm");
        String todayStringUn = df1.format(calendar1);
        final int dateun = Integer.parseInt(todayStringUn);


        unKnoneName.setText("Customer("+dateun+")");
        invoiceINt = Integer.parseInt(invoiseIdTextView.getText().toString());
        if (invoiceINt == 0){
            invoiseIdTextView.setText("1");
        }


        Bundle bundleUnkown= getIntent().getExtras();

        if (bundleUnkown!=null){

            unknonwnCustomerId = bundleUnkown.getString("unkownId");

            bundl.setVisibility(View.GONE);
            unkonwn.setVisibility(View.VISIBLE);
        }

        final Bundle bundle = getIntent().getExtras();

        if (bundle!=null) {

            idup = bundle.getString("id");
            nameup = bundle.getString("name");
            phoneup = bundle.getString("phone");
            takaup = bundle.getString("taka");
            addresup = bundle.getString("addreds");
            imageup = bundle.getString("imageurl");

            bundelId=idup;
            takacutomerup = takaup;


            if (bundelId!=null){
                bundl.setVisibility(View.VISIBLE);
                unkonwn.setVisibility(View.GONE);
            }
            if (nameup!=null){
                bundelCustomerName.setText(nameup);
            }
            if (phoneup!=null){
                bundelCustomerphone.setText(phoneup);

            }
            if (addresup!=null){
                bundelCustomeraddrss.setText(addresup);
            }

        }

        barcoeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                barDialog = new Dialog(SeleTwoActivity.this);
                // Include dialog.xml file
                barDialog.setContentView(R.layout.bar_code_dialog_view);
                // Set dialog title
                barDialog.setTitle("বিল পরিশোধ");
                barDialog.show();
                barDialog.setCanceledOnTouchOutside(false);

                toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,     100);
                surfaceView = barDialog.findViewById(R.id.surface_view);
                barcodeText = barDialog.findViewById(R.id.barcode_text);

                initialiseDetectorsAndSources();


            }
        });
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productListView();
            }
        });


        searchableSpinner.setTitle("Select Item");
        searchableSpinner.setPositiveButton("OK");

        searchableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String productName = searchableSpinner.getSelectedItem().toString();

                // recyclear( productName);

                DataLoad(productName);

                setCustomerList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        if (bundelId!=null){
            recyclearinvoiser();
        }else if ( unknonwnCustomerId!=null){
            UnkownCustumerrecyclearinvoiser();
        }
        OKfloatingActionButton.setVisibility(View.VISIBLE);
        PDFfloatingActionButton.setVisibility(View.GONE);


        OKfloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SeleTwoActivity.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.cutomar_pay_taka);
                // Set dialog title
                dialog.setTitle("বিল পরিশোধ");
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                final Button okButton = dialog.findViewById(R.id.okButton);
                Button cancelButton = dialog.findViewById(R.id.cancelButton);

                payeditetext= dialog.findViewById(R.id.dialogpayMoney);
                TitleTExt= dialog.findViewById(R.id.TitleTExt);
                paymentLooding = dialog.findViewById(R.id.loadingTExt);
                final TextView loadingTExt = dialog.findViewById(R.id.loadingTExt);


                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       String paymony = payeditetext.getText().toString();
                        if (paymony.isEmpty()){
                            Toast.makeText(SeleTwoActivity.this, "খালি ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        double paymonyDouable  = Double.parseDouble(paymony);
                        final double totallastbill = Double.parseDouble(TotalAmount.getText().toString());

                        double dautakaccustomer = 00.0;
                        if (bundelId!=null){
                            dautakaccustomer = Double.parseDouble(takacutomerup);

                        }if (unknonwnCustomerId!=null){
                            double unktotallast = Double.parseDouble(unKnonetaka.getText().toString());
                            dautakaccustomer = unktotallast;
                        }

                        withpaytaka = totallastbill - paymonyDouable;

                        Double conditonBil = totallastbill + dautakaccustomer;

                        daubill = dautakaccustomer + withpaytaka;

                        if (conditonBil < paymonyDouable){
                            payeditetext.setError("ভুল ইনপুট");
                            return;
                        }

                        paymonysend = paymony;
                      /*  payeditetext.setVisibility(View.GONE);
                        paymentLooding.setVisibility(View.VISIBLE);
                        okButton.setVisibility(View.GONE);
                        TitleTExt.setVisibility(View.GONE);
                        loadingTExt.setVisibility(View.VISIBLE);*/
                        PDFfloatingActionButton.setVisibility(View.VISIBLE);

                        if (activeBalance!=null){
                            activeBalance += paymonyDouable;
                        }

                        if (dueBalance != null){
                            dueBalance += paymonyDouable;

                        }


                        CollectionReference myInfo = FirebaseFirestore.getInstance()
                                .collection("users").document(user_id).collection("DukanInfo");

                        if (myinfoid!=null){
                            myInfo.document(myinfoid).update("activeBalance",activeBalance,"totalpaybil",dueBalance,"date",datenew);

                        }

                        if (invoisenumberID == null) {

                            final CollectionReference invoiseFb = FirebaseFirestore.getInstance()
                                    .collection("users").document(user_id).collection("invoise");
                            invoiseFb.add(new InvoiceseNote(null, 1)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        String id = task.getResult().getId();
                                        invoiseFb.document(id).update("id", id);

                                    }
                                }
                            });
                        }
                        else {
                            String invoice = invoiseIdTextView.getText().toString();
                            int i = Integer.parseInt(invoice);
                            invoiseFb.document(invoisenumberID).update("invoice", i);


                        }

                        if (bundelId!=null) {
                            final ProgressDialog pd = new ProgressDialog(SeleTwoActivity.this);
                            pd.setMessage("তথ্য প্রস্তুত হচ্ছে...");
                            pd.setCancelable(false);
                            pd.show();
                            final CollectionReference customer = FirebaseFirestore.getInstance()
                                    .collection("users").document(user_id).collection("Customers");
                            customer.document(bundelId).update("taka", daubill).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    customer.document(bundelId).update("lastTotal", 00.0,"pdfTotal",totallastbill).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            CollectionReference customerProductSale = FirebaseFirestore.getInstance()
                                                    .collection("users").document(user_id).collection("Customers").document(bundelId).collection("saleProduct");
                                            Query query = customerProductSale.whereEqualTo("update", false).whereEqualTo("date",datenew);
                                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {

                                                        List<String> list = new ArrayList<>();
                                                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                            list.add(document.getId());
                                                        }

                                                        saveCustomerupdateData((ArrayList) list); // *** new ***

                                                        totalupdateData((ArrayList) list);
                                                    }
                                                }


                                            });
                                            dialog.dismiss();
                                            pd.dismiss();
                                        }
                                    });
                                }
                            });
                        }if (unknonwnCustomerId!=null){

                            final ProgressDialog pd = new ProgressDialog(SeleTwoActivity.this);
                            pd.setMessage("তথ্য প্রস্তুত হচ্ছে...");
                            pd.setCancelable(false);
                            pd.show();
                            String name = unKnoneName.getText().toString();
                            String phone = unKnonePhone.getText().toString();
                            final CollectionReference unkonwnCustomarksdf = FirebaseFirestore.getInstance()
                                    .collection("users").document(user_id).collection("UnknownCustomer");
                            unkonwnCustomarksdf.document(unknonwnCustomerId).update("taka", withpaytaka,"nameCUstomer",name,"phone",phone,"lastTotal", 00.00,"pdfTotal",totallastbill).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                            final CollectionReference unkonwnCustomar = FirebaseFirestore.getInstance()
                                                    .collection("users").document(user_id).collection("UnknownCustomer").document(unknonwnCustomerId).collection("saleProduct");
                                            Query query = unkonwnCustomar.whereEqualTo("update", false);
                                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        List<String> list = new ArrayList<>();
                                                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                            list.add(document.getId());

                                                        }

                                                        unknowCustomerupdateData((ArrayList) list);
                                                        // *** new ***

                                                        totalupdateData((ArrayList) list);
                                                        dialog.dismiss();
                                                         pd.dismiss();
                                                    }
                                                }


                                            });

                                }
                            });

                        }




                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        OKfloatingActionButton.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                });

            }
        });

        PDFfloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent pdfIntent = new Intent(SeleTwoActivity.this, PDFActivity.class);
                if (bundelId!=null){

                         String customarName = bundelCustomerName.getText().toString();
                         String cutomerPhone = bundelCustomerphone.getText().toString();
                         String customertaka = bundelCustomertaka.getText().toString();
                         String customerAddres = bundelCustomeraddrss.getText().toString();
                         String invoice = invoiseIdTextView.getText().toString();
                         String totaltaka  = bundelCustomertaka.getText().toString();
                    pdfIntent.putExtra("cutomarId",bundelId);
                    pdfIntent.putExtra("customarName",customarName);
                    pdfIntent.putExtra("cutomerPhone",cutomerPhone);
                    pdfIntent.putExtra("customertaka",customertaka);
                    pdfIntent.putExtra("customerAddres",customerAddres);
                    pdfIntent.putExtra("invoise",invoice);
                    pdfIntent.putExtra("totaltaka",totaltaka);
                    pdfIntent.putExtra("takacutomerup",takacutomerup);
                    pdfIntent.putExtra("paymonysend",paymonysend);



                }
                if (unknonwnCustomerId!=null){
                    String customarName = unKnoneName.getText().toString();
                    String cutomerPhone = unKnonePhone.getText().toString();
                    String customertaka = unKnonetaka.getText().toString();
                    String customerAddres = unKnoneAddres.getText().toString();
                    String invoice = invoiseIdTextView.getText().toString();
                    String totaltaka = unKnonetaka.getText().toString();

                    pdfIntent.putExtra("unkcutomarId",unknonwnCustomerId);
                    pdfIntent.putExtra("customarName",customarName);
                    pdfIntent.putExtra("cutomerPhone",cutomerPhone);
                    pdfIntent.putExtra("customertaka",customertaka);
                    pdfIntent.putExtra("customerAddres",customerAddres);
                    pdfIntent.putExtra("invoise",invoice);
                    pdfIntent.putExtra("totaltaka",totaltaka);
                    pdfIntent.putExtra("paymonysend",paymonysend);
                    pdfIntent.putExtra("takacutomerup",takacutomerup);


                }
                startActivity(pdfIntent);

            }
        });


    }

    private void setCustomerList() {

        RecyclerView recyclerView = findViewById(R.id.productseletrecyclearview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

             exampleList = new ArrayList<>();

            adapter1 = new SealProductSelectAdapter(exampleList);

        recyclerView.setAdapter(adapter1);

        adapter1.notifyDataSetChanged();
    }
    private void DataLoad(String name){

        product.whereEqualTo("proName",name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        ProductNote productNote = document.toObject(ProductNote.class);

                        exampleList.add(productNote);
                        if (bundelId!=null) {

                            productNote.setUserID(bundelId);
                        }else {
                            productNote.setUnkid(unknonwnCustomerId);

                        }

                        productNote.setInvoiseid(invoisenumberID);

                        productNote.setInvoice(invoisenumber);

                        adapter1.notifyDataSetChanged();
                    }

                }


            }
        });
    }
    private void BarcodeDataLoad(String code){

        product.whereEqualTo("barCode",code).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        ProductNote productNote = document.toObject(ProductNote.class);

                        exampleList.add(productNote);

                        if (bundelId!=null) {
                            productNote.setUserID(bundelId);
                        }else {
                            productNote.setUnkid(unknonwnCustomerId);

                        }

                        productNote.setInvoiseid(invoisenumberID);

                        productNote.setInvoice(invoisenumber);

                        adapter1.notifyDataSetChanged();
                    }

                }


            }
        });
    }

    private void recyclearinvoiser() {

        CollectionReference customerProductSale = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("Customers").document(bundelId).collection("saleProduct");


        Query query = customerProductSale.whereEqualTo("update",false).whereEqualTo("date",datenew);


        FirestoreRecyclerOptions<SaleProductCutomerNote> options = new FirestoreRecyclerOptions.Builder<SaleProductCutomerNote>()
                .setQuery(query, SaleProductCutomerNote.class)
                .build();


        saleProductIndevicualAdapter = new SaleProductIndevicualAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.invoiceRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));


            final CollectionReference customerProductSaleUptatlasttaka = FirebaseFirestore.getInstance()
                    .collection("users").document(user_id).collection("Customers");

            Query query1 = customerProductSaleUptatlasttaka.whereEqualTo("customerIdDucunt",bundelId);

            query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {

                        return;
                    }
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        if (doc.get("lastTotal") != null) {

                            double totaltest = Double.parseDouble(Objects.requireNonNull(doc.get("lastTotal")).toString());

                            totallestAvount = totaltest;
                            TotalAmount.setText(totaltest+"");

                        }

                        if (doc.get("taka") != null) {


                            double totaltaka = Double.parseDouble(Objects.requireNonNull(doc.get("taka")).toString());
                            bundelCustomertaka.setText(totaltaka+"");
                        }

                    }

                }
            });



        recyclerView.setAdapter(saleProductIndevicualAdapter);



        saleProductIndevicualAdapter.setOnItemClickListener(new SaleProductIndevicualAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, final int position) {

                final SaleProductCutomerNote saleProductCutomerNote = documentSnapshot.toObject(SaleProductCutomerNote.class);

                final Dialog dialogInvdiveoalProduct = new Dialog(SeleTwoActivity.this);
                // Include dialog.xml file
                dialogInvdiveoalProduct.setContentView(R.layout.dialog_idivsiual_delete_item);
                dialogInvdiveoalProduct.show();
                dialogInvdiveoalProduct.setCanceledOnTouchOutside(false);

                final TextView proDuctName,productquantdy,productprice,canclebutton,deletebutton,workingId;

                proDuctName=dialogInvdiveoalProduct.findViewById(R.id.dilog_product_name);
                productquantdy=dialogInvdiveoalProduct.findViewById(R.id.dilog_product_quantidy);
                productprice = dialogInvdiveoalProduct.findViewById(R.id.dilog_product_price);
                canclebutton=dialogInvdiveoalProduct.findViewById(R.id.dilog_product_cancle);
                deletebutton=dialogInvdiveoalProduct.findViewById(R.id.dilog_product_delete);
                workingId=dialogInvdiveoalProduct.findViewById(R.id.workingId);

                assert saleProductCutomerNote != null;
                proDuctName.setText(saleProductCutomerNote.getItemName());
                productquantdy.setText(saleProductCutomerNote.getQuantedt()+"");
                productprice.setText(saleProductCutomerNote.getTotalPrice()+"");
                canclebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogInvdiveoalProduct.dismiss();
                    }
                });

                deletebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       deletebutton.setVisibility(View.GONE);
                       canclebutton.setVisibility(View.GONE);
                        workingId.setVisibility(View.VISIBLE);

                        if (saleProductCutomerNote.getPaid()==false) {

                            double subduble = totallestAvount - saleProductCutomerNote.getTotalPrice();
                            customerProductSaleUptatlasttaka.document(bundelId).update("lastTotal", subduble).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    saleProductIndevicualAdapter.deleteItem(position);
                                    dialogInvdiveoalProduct.dismiss();
                                }
                            });
                        }else {

                            saleProductIndevicualAdapter.deleteItem(position);
                            dialogInvdiveoalProduct.dismiss();
                        }

                    }
                });

            }
        });

    }
    private void UnkownCustumerrecyclearinvoiser() {

        final CollectionReference unkonwnCustomar = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("UnknownCustomer").document(unknonwnCustomerId).collection("saleProduct");

        Query query = unkonwnCustomar.whereEqualTo("update",false).whereEqualTo("date",datenew);

        FirestoreRecyclerOptions<SaleProductCutomerNote> options = new FirestoreRecyclerOptions.Builder<SaleProductCutomerNote>()
                .setQuery(query, SaleProductCutomerNote.class)
                .build();

        saleProductIndevicualAdapter = new SaleProductIndevicualAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.invoiceRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(saleProductIndevicualAdapter);


            final CollectionReference customerProductSaleUptatlasttaka = FirebaseFirestore.getInstance()
                    .collection("users").document(user_id).collection("UnknownCustomer");

            Query query1 = customerProductSaleUptatlasttaka.whereEqualTo("customerIdDucunt",unknonwnCustomerId);

            query1.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {

                        return;
                    }
                    assert queryDocumentSnapshots != null;
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        if (doc.get("lastTotal") != null) {
                            double totaltest  = (double) doc.get("lastTotal");
                            totallestAvount = totaltest;

                            TotalAmount.setText(totaltest+"");
                        }
                    }

                }
            });


        saleProductIndevicualAdapter.setOnItemClickListener(new SaleProductIndevicualAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, final int position) {

                final SaleProductCutomerNote saleProductCutomerNote = documentSnapshot.toObject(SaleProductCutomerNote.class);

                final Dialog dialogInvdiveoalProduct = new Dialog(SeleTwoActivity.this);
                // Include dialog.xml file
                dialogInvdiveoalProduct.setContentView(R.layout.dialog_idivsiual_delete_item);
                dialogInvdiveoalProduct.show();
                dialogInvdiveoalProduct.setCanceledOnTouchOutside(false);

                final TextView proDuctName,productquantdy,productprice,canclebutton,deletebutton,workingId;

                proDuctName=dialogInvdiveoalProduct.findViewById(R.id.dilog_product_name);
                productquantdy=dialogInvdiveoalProduct.findViewById(R.id.dilog_product_quantidy);
                productprice=dialogInvdiveoalProduct.findViewById(R.id.dilog_product_price);
                canclebutton=dialogInvdiveoalProduct.findViewById(R.id.dilog_product_cancle);
                deletebutton=dialogInvdiveoalProduct.findViewById(R.id.dilog_product_delete);
                workingId=dialogInvdiveoalProduct.findViewById(R.id.workingId);

                proDuctName.setText(saleProductCutomerNote.getItemName());
                productquantdy.setText(saleProductCutomerNote.getQuantedt()+"");
                productprice.setText(saleProductCutomerNote.getTotalPrice()+"");
                canclebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogInvdiveoalProduct.dismiss();
                    }
                });

                deletebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deletebutton.setVisibility(View.GONE);
                        canclebutton.setVisibility(View.GONE);
                        workingId.setVisibility(View.VISIBLE);

                        double subduble = totallestAvount - saleProductCutomerNote.getTotalPrice();

                        customerProductSaleUptatlasttaka.document(unknonwnCustomerId).update("lastTotal",subduble).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                saleProductIndevicualAdapter.deleteItem(position);

                                dialogInvdiveoalProduct.dismiss();
                            }
                        });

                    }
                });

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        firestore = FirebaseFirestore.getInstance();

        saleProductIndevicualAdapter.startListening();



        invoiseFb.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {

                    return;
                }
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("id") != null) {
                       invoisenumberID = doc.getId();
                    }
                    if (doc.get("invoice") != null) {

                        int i =Integer.parseInt(doc.get("invoice").toString());
                        i = i+1;

                        invoiseIdTextView.setText(i+"");
                    }
                }
            }
        });



    }

    @Override
    protected void onStop() {
        super.onStop();
       saleProductIndevicualAdapter.stopListening();

    }

    public void saveCustomerupdateData(ArrayList list) {

        String invoice = invoiseIdTextView.getText().toString();
        int i=Integer.parseInt(invoice);
        // Get a new write batch
        WriteBatch batch = db.batch();

        // Iterate through the list
        for (int k = 0; k < list.size(); k++) {

            // Update each list item
            DocumentReference ref = db.collection("users").document(user_id).collection("Customers")
                    .document(bundelId).collection("saleProduct").document((String) list.get(k));


            batch.update(ref, "paid", true,"invoiceNumber",i);

            DocumentReference Profitref = db.collection("users").document(user_id).collection("profit").document((String) list.get(k));


            batch.update(Profitref, "ProfitTrue", true);


        }

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Yay its all done in one go!
            }
        });

    }

    public void unknowCustomerupdateData(ArrayList list) {
        String invoice = invoiseIdTextView.getText().toString();
        int i=Integer.parseInt(invoice);
        // Get a new write batch
        WriteBatch batch = db.batch();
        // Iterate through the list
        for (int k = 0; k < list.size(); k++) {
            // Update each list item
            DocumentReference ref = db.collection("users").document(user_id).collection("UnknownCustomer")
                    .document(unknonwnCustomerId).collection("saleProduct").document((String) list.get(k));
            batch.update(ref, "paid", true,"invoiceNumber",i);
        }
        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Yay its all done in one go!
            }
        });
    }

    public void totalupdateData(ArrayList list) {

        String invoice = invoiseIdTextView.getText().toString();
        int i=Integer.parseInt(invoice);
        // Get a new write batch
        WriteBatch batch = db.batch();

        // Iterate through the list
        for (int k = 0; k < list.size(); k++) {

            // Update each list item
            DocumentReference ref = db.collection("users").document(user_id).collection("Sales")
                    .document((String) list.get(k));

            batch.update(ref, "paid", true,"invoiceNumber",i);


            DocumentReference Profitref = db.collection("users").document(user_id).collection("profit").document((String) list.get(k));

            batch.update(Profitref, "ProfitTrue", true);


        }

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Yay its all done in one go!
            }
        });

    }


    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(SeleTwoActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(SeleTwoActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {


                    barcodeText.post(new Runnable() {

                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                                BarcodeDataLoad(barcodeData);
                                setCustomerList();
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                barDialog.dismiss();
                                addItemButton.setVisibility(View.GONE);
                                showitemLinearLayout.setVisibility(View.VISIBLE);
                            } else {

                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                BarcodeDataLoad(barcodeData);
                                setCustomerList();
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                 barDialog.dismiss();
                                addItemButton.setVisibility(View.GONE);
                                showitemLinearLayout.setVisibility(View.VISIBLE);

                            }
                        }
                    });

                }
            }
        });
    }


    public void productListView(){


        product.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    titleList = new ArrayList<>();

                    assert queryDocumentSnapshots != null;
                    for(DocumentSnapshot readData: queryDocumentSnapshots.getDocuments()){
                        String titlename = Objects.requireNonNull(readData.get("proName")).toString();

                        titleList.add(titlename);
                    }


                    ArrayAdapter arrayAdapter2 = new ArrayAdapter(SeleTwoActivity.this,android.R.layout.simple_spinner_dropdown_item,titleList);
                    searchableSpinner.setAdapter(arrayAdapter2);

                }
            }
        });

        addItemButton.setVisibility(View.GONE);
        showitemLinearLayout.setVisibility(View.VISIBLE);

    }
}
