package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class ProductAddActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    ImageView pruductImage,barcodeIcon;

    private TextInputEditText productName, productPrice,productQuantayn,pruductMin,pruductBuyPrice,pruductDiscount
            ,pruductColor,pruductType,pruductDescription,pruductSize;
    EditText productBarcodeNumber;
    private MaterialButton addProduct;

    StorageReference mStorageReferenceImage;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String   proIdup, productNameup,pruductBuyPriceup, productPriceup,productQuantaynup,pruductMinup,addresup,
            pruductImageup,barcodenumber,privecyup,catagoryup,dicountup,descriptuionup,typeup,colorup,sizeup;

    FloatingActionButton imageSeletprioduct;


   private Spinner privacyspinner;
    String privacyspinneritem;
   private SearchableSpinner Categoryspinner;
    String Categoryspinneritem;
    int CategoryspinneritemInt;

    String[] privacyspinnerList = { "Public", "private" };

    String[] CategoryspinnerList = { "Mobiles","Tablets","Used Mobile","Mobile accessories","Foods","jewellery","Motorcycle accessories","Cosmetics","Grocery",
    "Panjabi","Pajama","Shirts","Pant","T-Shirt","Polo","Lungi","Man Shoes","Man Accessories","Saree",
            "Shalwar Kameez", "Shawls","Girls Panjabi", "Nightwear", "Scarves", "Dupatta", "Girls Shoes", "Girls Accessories",
            "kids","Medicine","Sports", "Computer accessories","Home accessories","Books","Electronics","Game"};

    String productCode;
     String color;
     String typ;
    String descreption;
    String Size;
    int datenew;
    int pdicount;

    boolean image = false;

    double haveProductInvestment,totalPriceInvestment;

    private static final int PICK_IMAGE_REQUEST = 1;
    public Uri mImageUri;
    String id,comonCatagory;



    private  String token;

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

    String user_id = currentUser.getUid();

   //  Bundle bundle = getIntent().getExtras();

    CollectionReference product = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Product");

     CollectionReference myInfo = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("DukanInfo");

    CollectionReference GlobleProduct = FirebaseFirestore.getInstance()
            .collection("GlobleProduct");

    CollectionReference confirmSoplist = FirebaseFirestore.getInstance()
            .collection("GlobleSoplist");


    @Override
    protected void onStart() {
        super.onStart();

        confirmSoplist.whereEqualTo("shopUserId",user_id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    String Approved = null;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        MyInfoNote myInfoNote = document.toObject(MyInfoNote.class);
                        Approved = myInfoNote.getApproved();
                    }

                    if (Approved.equals("pending")){

                        new MaterialAlertDialogBuilder(ProductAddActivity.this)
                                .setMessage("Please! Approved from A2ZLOJA. Contact us")
                                .setCancelable(false)
                                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(ProductAddActivity.this,ContactUsActivity.class));

                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        startActivity(new Intent(ProductAddActivity.this,ProductListActivity.class));
                                        finish();

                                    }
                                })
                                .show();

                    }

                }


            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductAddActivity.this,ProductListActivity.class);
                startActivity(intent);
                finish();
            }
        });




        firebaseFirestore = FirebaseFirestore.getInstance();
        mStorageReferenceImage = FirebaseStorage.getInstance().getReference().child(user_id).child("ProdctImage");



        pruductImage = findViewById(R.id.pruductPic);
        productName = findViewById(R.id.productName);
        productBarcodeNumber = findViewById(R.id.productBarcodeNumber);
        productPrice = findViewById(R.id.pruductPrice);
        productQuantayn = findViewById(R.id.pruductquntidy);
        pruductMin = findViewById(R.id.pruductMini);
        addProduct = findViewById(R.id.addpruduct);
        pruductBuyPrice = findViewById(R.id.pruductBuyPrice);
        imageSeletprioduct = findViewById(R.id.imageSeletprioduct);
        barcodeIcon = findViewById(R.id.barcodeIcon);

        privacyspinner = findViewById(R.id.privacyspinner);
        Categoryspinner = findViewById(R.id.Categoryspinner);
        pruductDiscount = findViewById(R.id.pruductDiscount);


        pruductColor = findViewById(R.id.pruductColor);
        pruductType = findViewById(R.id.pruductType);
        pruductDescription = findViewById(R.id.pruductDescription);
        pruductSize = findViewById(R.id.pruductSize);



        final Bundle bundle = getIntent().getExtras();

        if (bundle!=null) {


            proIdup = bundle.getString("id");
            productNameup = bundle.getString("name");
            privecyup = bundle.getString("privecy");
            catagoryup = bundle.getString("catagory");
            barcodenumber = bundle.getString("code");
            productPriceup = bundle.getString("pprice");
            if (bundle.getString("pBprice")!=null){
                pruductBuyPriceup = bundle.getString("pBprice");
            }else {
                pruductBuyPriceup = bundle.getString("pprice");
            }
            productQuantaynup = bundle.getString("pQuan");
            pruductMinup = bundle.getString("pmini");
            pruductImageup = bundle.getString("imageurl");
            if (bundle.getString("dicount")!=null){
                dicountup =bundle.getString("dicount");
                pruductDiscount.setText(dicountup);
            }
            if (bundle.getString("color")!=null){
                colorup =bundle.getString("color");
                pruductColor.setText(colorup);
            }
            if (bundle.getString("type")!=null){
                typeup =bundle.getString("type");
                pruductType.setText(typeup);
            }
            if (bundle.getString("Size")!=null){
                sizeup =bundle.getString("Size");
                pruductSize.setText(sizeup);
            }
            if (bundle.getString("descriptuion")!=null){
                descriptuionup =bundle.getString("descriptuion");
                pruductDescription.setText(descriptuionup);
            }
            id = proIdup;
            if (pruductImageup!=null){
              Uri myUri = Uri.parse(pruductImageup);
              mImageUri = myUri;
              Picasso.get().load(myUri).into(pruductImage);
            }

            productName.setText(productNameup);
            if (barcodenumber!=null){
             productBarcodeNumber.setText(barcodenumber);

                    }
            productPrice.setText(productPriceup);
            pruductBuyPrice.setText(pruductBuyPriceup);
            productQuantayn.setText(productQuantaynup);
            pruductMin.setText(pruductMinup);

        }




        imageSeletprioduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bundle!=null){
                    image = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getIMEGE();
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        getIMEGE();
                    }
                }
            }
        });


        barcodeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               barDialog = new Dialog(ProductAddActivity.this);
                // Include dialog.xml file
                barDialog.setContentView(R.layout.bar_code_dialog_view);
                // Set dialog title
                barDialog.setTitle("Pay Bill");
                barDialog.show();
                barDialog.setCanceledOnTouchOutside(false);

                toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,     100);
                surfaceView = barDialog.findViewById(R.id.surface_view);
                barcodeText = barDialog.findViewById(R.id.barcode_text);

                initialiseDetectorsAndSources();


            }
        });

        ArrayAdapter<String> privacyspinneradapter =  new ArrayAdapter<>(ProductAddActivity.this, android.R.layout.simple_spinner_dropdown_item, privacyspinnerList);
        privacyspinner.setAdapter(privacyspinneradapter);

        privacyspinner.setPrompt("privacy");
        ArrayAdapter CategoryspinnerarrayAdapter = new ArrayAdapter(ProductAddActivity.this,android.R.layout.simple_spinner_dropdown_item,CategoryspinnerList);
        Categoryspinner.setAdapter(CategoryspinnerarrayAdapter);

        Categoryspinner.setPrompt("Category");

        if (bundle!=null){

            int privecyupInt = new ArrayList<String>(Arrays.asList(privacyspinnerList)).indexOf(privecyup);
            int catagoryupInt = new ArrayList<String>(Arrays.asList(CategoryspinnerList)).indexOf(catagoryup);

            privacyspinner.setSelection(privecyupInt);
            Categoryspinner.setSelection(catagoryupInt);


            if (catagoryupInt<=3){
                comonCatagory = "Mobiles";

            }else if (catagoryupInt == 4){
                comonCatagory = "Foods";

            }else if (catagoryupInt == 5){
                comonCatagory = "jewellery";

            }else if (catagoryupInt == 6){
                comonCatagory = "Motorcycle accessories";

            }else if ( catagoryupInt == 7){
                comonCatagory = "Cosmetics";

            }
            else if (catagoryupInt == 8){
                comonCatagory = "Grocery";
            } else if (catagoryupInt >=9 && catagoryupInt <=17){
                comonCatagory = "Mans";
            }else if (catagoryupInt >=18 && catagoryupInt <=23){
                comonCatagory = "Girls";
            }

            else if (catagoryupInt ==24){

                comonCatagory = "kids";
            }
            else if (catagoryupInt ==25){

                comonCatagory = "medicine";
            }
            else if (catagoryupInt ==26){

                comonCatagory = "sports";
            }
            else if (catagoryupInt ==27){

                comonCatagory = "computer accessories";
            }
            else if (catagoryupInt ==28){

                comonCatagory = "home accessories";
            }
            else if (catagoryupInt ==29){

                comonCatagory = "books";
            }
            else if (catagoryupInt ==30){

                comonCatagory = "electronics";
            }
            else if (catagoryupInt ==31){

                comonCatagory = "game";
            }

        }
        privacyspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

              privacyspinneritem = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               Categoryspinneritem = parent.getItemAtPosition(position).toString();

                CategoryspinneritemInt = position;

                if (position<=3){
                    comonCatagory = "Mobiles";
                }else if (position == 4){
                    comonCatagory = "Foods";
                }else if (position == 5){
                    comonCatagory = "jewellery";

                }else if (position == 6){
                    comonCatagory = "Motorcycle accessories";

                }else if ( position == 7){
                    comonCatagory = "Cosmetics";

                }
                else if (position == 8){
                    comonCatagory = "Grocery";
                } else if (position >=9 && position <=17){
                    comonCatagory = "Mans";
                }else if (position >=18 && position <=23){
                    comonCatagory = "Girls";
                }
                else if (position ==24){

                    comonCatagory = "kids";
                }
                else if (position ==25){

                    comonCatagory = "medicine";
                }
                else if (position ==26){

                    comonCatagory = "sports";
                }
                else if (position ==27){

                    comonCatagory = "computer accessories";
                }
                else if (position ==28){

                    comonCatagory = "home accessories";
                }
                else if (position ==29){

                    comonCatagory = "books";
                }
                else if (position ==30){

                    comonCatagory = "electronics";
                }
                else if (position ==31){

                    comonCatagory = "game";
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkIntert()) {
                    Toast.makeText(ProductAddActivity.this, " No Internet ", Toast.LENGTH_SHORT).show();
                    return;
                }

                Random r = new Random();
                int randomNumber = r.nextInt(1000);

                Date calendar1 = Calendar.getInstance().getTime();
                DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
                String todayString = df1.format(calendar1);
               datenew = Integer.parseInt(todayString);

                String s =String.valueOf(randomNumber);
                productCode = todayString+s;


                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        if (task.isSuccessful()){
                         token = task.getResult().getToken();

                        }

                    }
                });



                String name = productName.getText().toString();
                String barCode = productBarcodeNumber.getText().toString();
                String price = productPrice.getText().toString();
                String ppq = productQuantayn.getText().toString();
                String pBpq = pruductBuyPrice.getText().toString();
                String pmq = pruductMin.getText().toString();
                String pruductDiscountCheck = pruductDiscount.getText().toString();
                String pruductColorCheck  = pruductColor.getText().toString();
                String pruductTypeCheck  = pruductType.getText().toString();
                String pruductDescriptionCheck  = pruductDescription.getText().toString();
                String pruductSizeCheck  = pruductSize.getText().toString();

              color = pruductColor.getText().toString();
               typ = pruductType.getText().toString();
               descreption = pruductDescription.getText().toString();
                Size = pruductSize.getText().toString();

                final String dicount = pruductDiscount.getText().toString();



                if (TextUtils.isEmpty(name) ){
                    Toast.makeText(getApplicationContext(), "Product Name...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(privacyspinneritem) ){
                    Toast.makeText(getApplicationContext(), "Privacy ...", Toast.LENGTH_LONG).show();
                    return;
                }  if (TextUtils.isEmpty(Categoryspinneritem) ){
                    Toast.makeText(getApplicationContext(), "Categorys ...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(price) ){
                    Toast.makeText(getApplicationContext(), " Buy Price ...", Toast.LENGTH_LONG).show();
                    return;
                } if (TextUtils.isEmpty(pBpq) ){
                    Toast.makeText(getApplicationContext(), " Sale Price ...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(ppq) ){
                    Toast.makeText(getApplicationContext(), "Quantity ...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(pmq) ){
                    Toast.makeText(getApplicationContext(), " Minimum Quantity...", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(pruductDiscountCheck) || TextUtils.isEmpty(pruductColorCheck) || TextUtils.isEmpty(pruductTypeCheck) ||
                        TextUtils.isEmpty(pruductDescriptionCheck) || TextUtils.isEmpty(pruductSizeCheck)){
                    Toast.makeText(getApplicationContext(), " Product Information filup ", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!dicount.isEmpty()){
                    if (pdicount>=100 && pdicount<=1){

                        Toast.makeText(ProductAddActivity.this, " discount input wong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    pdicount = Integer.parseInt(dicount);
                }

                progressDialog = new ProgressDialog(ProductAddActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);

                final String pnmae = productName.getText().toString();
                String pbarCode = productBarcodeNumber.getText().toString();
                final String pps = productPrice.getText().toString();
                final double pp = Double.parseDouble(pps);
                final String pBps = pruductBuyPrice.getText().toString();
                final double pBp = Double.parseDouble(pBps);
                final String pqs = productQuantayn.getText().toString();
                final double pq = Double.parseDouble(pqs);
                final String pms = pruductMin.getText().toString();
                double pm = Double.parseDouble(pms);



                if ( productQuantaynup != null && !productQuantaynup.equals(pqs)) {
                    if (productQuantaynup != null) {
                        double pqinvestment = Double.parseDouble(productQuantaynup);
                        if (pqinvestment < pq) {
                            haveProductInvestment = pq - pqinvestment;
                            totalPriceInvestment = haveProductInvestment * pp;
                            invest(pnmae,haveProductInvestment,totalPriceInvestment);
                        } else {
                            haveProductInvestment = pqinvestment - pq;
                            totalPriceInvestment = haveProductInvestment * pp;
                            invest(pnmae,haveProductInvestment,totalPriceInvestment);
                        }
                    }

                }else if ( productQuantaynup == null && pps != null ){
                        haveProductInvestment = pq;
                        totalPriceInvestment = haveProductInvestment * pp;
                        invest(pnmae,haveProductInvestment,totalPriceInvestment);
                }

                if ( bundle == null && mImageUri == null  ){
                    product.add(new ProductNote(null, pnmae, pp,pBp, pq, pm,pbarCode,productCode,privacyspinneritem,
                            Categoryspinneritem,datenew,pnmae.toLowerCase(),pdicount,comonCatagory)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {

                                final String id = task.getResult().getId();

                                product.document(id).update("proId", id,"Size",Size,"color",color,"type",typ,"description",descreption).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {


                                        setGlobleProduct(id,pnmae,pp,pq,productCode,privacyspinneritem,Categoryspinneritem,datenew,
                                                pdicount,comonCatagory,Size,color,typ,descreption);

                                        Toast.makeText(ProductAddActivity.this, " Successful", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                        }
                    });

                    Intent intent = new Intent(ProductAddActivity.this,ProductListActivity.class);
                    startActivity(intent);
                    finish();

                }


                if (bundle!=null && image == false) {

                    product.document(id).update("proId", id, "proName", pnmae, "proPrice", pp,"proBuyPrice",pBp, "proQua", pq, "proMin", pm, "proImgeUrl",
                            pruductImageup,"barCode",pbarCode,"productPrivacy",privacyspinneritem,"productCategory",Categoryspinneritem,"search",pnmae.toLowerCase(),"comomCatagory",comonCatagory,
                            "Size",Size,"color",color,"type",typ,"description",descreption)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    setGlobleProductupdate(id,pnmae,pp,pq,productCode,privacyspinneritem,Categoryspinneritem,
                                            datenew,pdicount,comonCatagory,Size,color,typ,descreption);
                                    Intent intent = new Intent(ProductAddActivity.this, ProductListActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }

                else if (mImageUri!=null){

                  //  CustomerInfoUpload( mImageUri);
                    uploadImageUri( mImageUri);
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
                Picasso.get().load(mImageUri).into(pruductImage);
            } else {
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }}
/*

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
*/

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
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




          //  Log.d("Test", "uploadImageUri: " +);


            upload(file, new UploadCallback() {
                @Override
                public void onSuccess(final String downloadLink) {


                    final String pnmae = productName.getText().toString();
                    String barCode = productBarcodeNumber.getText().toString();
                    final String pps = productPrice.getText().toString();
                    final double pp = Double.parseDouble(pps);
                    final String pBps = pruductBuyPrice.getText().toString();
                    double pBp = Double.parseDouble(pBps);
                    final String pqs = productQuantayn.getText().toString();
                    final double pq = Double.parseDouble(pqs);
                    final String pms = pruductMin.getText().toString();
                    double pm = Double.parseDouble(pms);

                    final String dicount = pruductDiscount.getText().toString();
                    if (!dicount.isEmpty()){
                        pdicount = Integer.parseInt(dicount);
                    }

                    if (image != false) {

                        product.document(id).update("proId", id, "proName", pnmae, "proPrice",
                                pp,"proBuyPrice", pBp, "proQua", pq, "proMin", pm, "proImgeUrl",downloadLink,"barCode",
                                barCode,"search",pnmae.toLowerCase(),"comomCatagory",comonCatagory,"color",color,"type",
                                "Size",Size,typ,"description",descreption)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        setGlobleProductupdate(id,pnmae,pp,pq,productCode,privacyspinneritem,Categoryspinneritem,datenew,
                                                downloadLink,pdicount,comonCatagory,Size,color,typ,descreption);

                                        progressDialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();

                                Intent intent = new Intent(ProductAddActivity.this,ProductListActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                    } else {
                        product.add(new ProductNote(null, pnmae, pp,pBp, pq, pm, downloadLink,barCode,productCode,privacyspinneritem,
                                Categoryspinneritem,datenew,pnmae.toLowerCase(),pdicount,comonCatagory)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                if (task.isSuccessful()) {

                                    final String id = task.getResult().getId();


                                    product.document(id).update("proId", id,"color",color,"Size",Size,"type",typ,"description",descreption).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            setGlobleProduct(id,pnmae,pp,pq,productCode,privacyspinneritem,Categoryspinneritem,
                                                    datenew,downloadLink,pdicount,comonCatagory,Size,color,typ,descreption);

                                            Toast.makeText(ProductAddActivity.this, " successful ", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                            }
                        });
                    }

                    progressDialog.dismiss();

                    Intent intent = new Intent(ProductAddActivity.this, ProductListActivity.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
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


    public  void invest(String pnmae ,double haveProductInvestment,double totalPriceInvestment){

        final CollectionReference investment = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("investment");

        Date calendar1 = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat")
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(calendar1);
        final int datenew = Integer.parseInt(todayString);


        investment.add(new MyInfoNote(pnmae, haveProductInvestment, null, totalPriceInvestment, datenew, "")).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                if (task.isSuccessful()) {
                    String id = task.getResult().getId();
                    investment.document(id).update("myid", id).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });
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
                    if (ActivityCompat.checkSelfPermission(ProductAddActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ProductAddActivity.this, new
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
                                productBarcodeNumber.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                barDialog.dismiss();
                            } else {

                                barcodeData = barcodes.valueAt(0).displayValue;
                                productBarcodeNumber.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                barDialog.dismiss();

                            }
                        }
                    });

                }
            }
        });
    }


    public void setGlobleProduct(final String productId, final String productName, final double productPrice,
                                 final double productQuantidy, final String productCode, final String privecy,
                                 final String Catagury, final int date, final int pdicount, final String comomCatagory,
                                 final String size1, final  String color1,final String typ,final String description) {

        myInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    String DukanName = null;
                    String DukanImageUrl = null;
                    String dukanderid = null;
                    String dukanderPhone = null;
                    String dukanderAddress = null;

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        MyInfoNote myInfoNote = document.toObject(MyInfoNote.class);

                        if (myInfoNote.getDukanName()!=null){
                           DukanName = myInfoNote.getDukanName();
                        }

                        if (myInfoNote.getDukanaddpicurl()!=null){
                            DukanImageUrl = myInfoNote.getDukanaddpicurl();
                        }
                        if (myInfoNote.getDukanphone()!=null){
                            dukanderPhone = myInfoNote.getDukanphone();
                        }
                        if (myInfoNote.getDukanaddress()!=null){
                            dukanderAddress = myInfoNote.getDukanaddress();
                        }
                       dukanderid = document.get("myid").toString();

                    }

                    Map<String, Object> GlobaleProductObject = new HashMap<>();
                    GlobaleProductObject.put("proId", productId);
                    GlobaleProductObject.put("proName", productName);
                    GlobaleProductObject.put("search", productName.toLowerCase());
                    GlobaleProductObject.put("proPrice", productPrice);
                    GlobaleProductObject.put("pruductDiscount", pdicount);
                    GlobaleProductObject.put("productCode", productCode);
                    GlobaleProductObject.put("productPrivacy", privecy);
                    GlobaleProductObject.put("comomCatagory", comomCatagory);
                    GlobaleProductObject.put("ShopName", DukanName);
                    GlobaleProductObject.put("ShopID", user_id);
                    GlobaleProductObject.put("ShopPhone", dukanderPhone);
                    GlobaleProductObject.put("ShopAddress", dukanderAddress);
                    GlobaleProductObject.put("ShopId", dukanderid);
                    GlobaleProductObject.put("productCategory", Catagury);
                    GlobaleProductObject.put("date", date);
                    GlobaleProductObject.put("proQua", productQuantidy);
                    GlobaleProductObject.put("UserId", user_id);
                    GlobaleProductObject.put("token", token);
                    GlobaleProductObject.put("Size", size1);
                    GlobaleProductObject.put("color", color1);
                    GlobaleProductObject.put("type", typ);
                    GlobaleProductObject.put("description", description);
                    GlobaleProductObject.put("productVerification", "certify");


                    GlobleProduct.document(productId).set(GlobaleProductObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });


    }
    public void setGlobleProduct(final String productId, final String productName, final double productPrice,
                                 final double productQuantidy, final String productCode, final String privecy,
                                 final String Catagury, final int date, final String ImageUrl, final int pdicount,
                                 final String comomCatagory,final String size1, final  String color1,final String typ,final String description){

        myInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    String DukanName = null;
                    String DukanImageUrl = null;
                    String dukanderid = null;
                    String dukanderPhone = null;
                    String dukanderAddress = null;

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        MyInfoNote myInfoNote = document.toObject(MyInfoNote.class);

                        if (myInfoNote.getDukanName()!=null){
                           DukanName = myInfoNote.getDukanName();
                        }

                        if (myInfoNote.getDukanaddpicurl()!=null){
                            DukanImageUrl = myInfoNote.getDukanaddpicurl();
                        }
                        if (myInfoNote.getDukanphone()!=null){
                            dukanderPhone = myInfoNote.getDukanphone();
                        }
                        if (myInfoNote.getDukanaddress()!=null){
                            dukanderAddress = myInfoNote.getDukanaddress();
                        }
                       dukanderid = document.get("myid").toString();

                    }

                    Map<String, Object> GlobaleProductObject = new HashMap<>();
                    GlobaleProductObject.put("proId", productId);
                    GlobaleProductObject.put("proName", productName);
                    GlobaleProductObject.put("search", productName.toLowerCase());
                    GlobaleProductObject.put("proPrice", productPrice);
                    GlobaleProductObject.put("pruductDiscount", pdicount);
                    GlobaleProductObject.put("proImgeUrl",ImageUrl);
                    GlobaleProductObject.put("productCode", productCode);
                    GlobaleProductObject.put("comomCatagory", comomCatagory);
                    GlobaleProductObject.put("productPrivacy", privecy);
                    GlobaleProductObject.put("ShopName", DukanName);
                    GlobaleProductObject.put("ShopPhone", dukanderPhone);
                    GlobaleProductObject.put("ShopAddress", dukanderAddress);
                    GlobaleProductObject.put("ShopId", dukanderid);
                    GlobaleProductObject.put("productCategory", Catagury);
                    GlobaleProductObject.put("date", date);
                    GlobaleProductObject.put("proQua", productQuantidy);
                    GlobaleProductObject.put("UserId", user_id);
                    GlobaleProductObject.put("token", token);
                    GlobaleProductObject.put("Size", size1);
                    GlobaleProductObject.put("color", color1);
                    GlobaleProductObject.put("type", typ);
                    GlobaleProductObject.put("description", description);
                    GlobaleProductObject.put("productVerification", "certify");


                    GlobleProduct.document(productId).set(GlobaleProductObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });


    }
    public void setGlobleProductupdate(final String productId, final String productName, final double productPrice,
                                       final double productQuantidy, final String productCode, final String privecy,
                                       final String Catagury, final int date, final String ImageUrl, final int pdicount,
                                       final String comomCatagory,final String size1, final  String color1,final String typ,
                                       final String description){

        myInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    String DukanName = null;
                    String DukanImageUrl = null;
                    String dukanderid = null;
                    String dukanderPhone = null;
                    String dukanderAddress = null;

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        MyInfoNote myInfoNote = document.toObject(MyInfoNote.class);

                        if (myInfoNote.getDukanName()!=null){
                           DukanName = myInfoNote.getDukanName();
                        }

                        if (myInfoNote.getDukanaddpicurl()!=null){
                            DukanImageUrl = myInfoNote.getDukanaddpicurl();
                        }
                        if (myInfoNote.getDukanphone()!=null){
                            dukanderPhone = myInfoNote.getDukanphone();
                        }
                        if (myInfoNote.getDukanaddress()!=null){
                            dukanderAddress = myInfoNote.getDukanaddress();
                        }
                       dukanderid = document.get("myid").toString();

                    }

                    Map<String, Object> GlobaleProductObject = new HashMap<>();
                    GlobaleProductObject.put("proId", productId);
                    GlobaleProductObject.put("proName", productName);
                    GlobaleProductObject.put("search", productName.toLowerCase());
                    GlobaleProductObject.put("proPrice", productPrice);
                    GlobaleProductObject.put("pruductDiscount", pdicount);
                    GlobaleProductObject.put("comomCatagory", comomCatagory);
                    GlobaleProductObject.put("proImgeUrl",ImageUrl);
                    GlobaleProductObject.put("productPrivacy", privecy);
                    GlobaleProductObject.put("ShopName", DukanName);
                    GlobaleProductObject.put("ShopPhone", dukanderPhone);
                    GlobaleProductObject.put("ShopAddress", dukanderAddress);
                    GlobaleProductObject.put("ShopId", dukanderid);
                    GlobaleProductObject.put("productCategory", Catagury);
                    GlobaleProductObject.put("date", date);
                    GlobaleProductObject.put("proQua", productQuantidy);
                    GlobaleProductObject.put("UserId", user_id);
                    GlobaleProductObject.put("token", token);
                    GlobaleProductObject.put("Size", size1);
                    GlobaleProductObject.put("color", color1);
                    GlobaleProductObject.put("type", typ);
                    GlobaleProductObject.put("description", description);
                    GlobaleProductObject.put("productVerification", "certify");

                    GlobleProduct.document(productId).set(GlobaleProductObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });


    }
    public void setGlobleProductupdate(final String productId, final String productName, final double productPrice,
                                       final double productQuantidy, final String productCode, final String privecy,
                                       final String Catagury, final int date, final int pdicount, final String comomCatagory,
                                       final String size1,final  String color1,final String typ,final String description){

        myInfo.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    String DukanName = null;
                    String DukanImageUrl = null;
                    String dukanderid = null;
                    String dukanderPhone = null;
                    String dukanderAddress = null;

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        MyInfoNote myInfoNote = document.toObject(MyInfoNote.class);

                        if (myInfoNote.getDukanName()!=null){
                            DukanName = myInfoNote.getDukanName();
                        }

                        if (myInfoNote.getDukanaddpicurl()!=null){
                            DukanImageUrl = myInfoNote.getDukanaddpicurl();
                        }
                        if (myInfoNote.getDukanphone()!=null){
                            dukanderPhone = myInfoNote.getDukanphone();
                        }
                        if (myInfoNote.getDukanaddress()!=null){
                            dukanderAddress = myInfoNote.getDukanaddress();
                        }
                        dukanderid = document.get("myid").toString();

                    }

                    Map<String, Object> GlobaleProductObject = new HashMap<>();
                    GlobaleProductObject.put("proId", productId);
                    GlobaleProductObject.put("proName", productName);
                    GlobaleProductObject.put("search", productName.toLowerCase());
                    GlobaleProductObject.put("proPrice", productPrice);
                    GlobaleProductObject.put("pruductDiscount", pdicount);
                    GlobaleProductObject.put("comomCatagory", comomCatagory);
                    GlobaleProductObject.put("productPrivacy", privecy);
                    GlobaleProductObject.put("ShopName", DukanName);
                    GlobaleProductObject.put("ShopPhone", dukanderPhone);
                    GlobaleProductObject.put("ShopAddress", dukanderAddress);
                    GlobaleProductObject.put("ShopId", dukanderid);
                    GlobaleProductObject.put("productCategory", Catagury);
                    GlobaleProductObject.put("date", date);
                    GlobaleProductObject.put("proQua", productQuantidy);
                    GlobaleProductObject.put("UserId", user_id);
                    GlobaleProductObject.put("token", token);
                    GlobaleProductObject.put("Size", size1);
                    GlobaleProductObject.put("color", color1);
                    GlobaleProductObject.put("type", typ);
                    GlobaleProductObject.put("description", description);

                    GlobleProduct.document(productId).update(GlobaleProductObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });


    }



}
