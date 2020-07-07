package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.dd.CircularProgressButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mrsoftit.dukander.CustomerNote;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
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
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class CustomerAddActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private CircleImageView circleCutomerImageView;
    private DrawerLayout drawer;
    private TextInputEditText customerName, customerPhone,customerTaka,CustomerAddress,customerTaxetId,customerNIDnumber;
    private MaterialButton addCustomer;


    StorageReference mStorageReference;
    StorageReference mStorageReferenceImage;
    DatabaseReference mDatabaseReference;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    String  idup, nameup,phoneup,takaup,addresup,imageup;
    FloatingActionButton imageSeletcutomer;

    boolean image = false;



    private static final int PICK_IMAGE_REQUEST = 1;
    public Uri mImageUri;
    String id;

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = currentUser.getUid();

    CollectionReference customer = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Customers");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add);



        firebaseFirestore = FirebaseFirestore.getInstance();
        mStorageReferenceImage = FirebaseStorage.getInstance().getReference().child(user_id).child("CUstomer_image");
       // mDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_support);
        toolbar.setTitle("Dukandar ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerAddActivity.this,CustumarActivity.class);
                startActivity(intent);
                finish();
            }
        });



        circleCutomerImageView = findViewById(R.id.customar_add_pictur);
        customerName = findViewById(R.id.custumar_name_add);
        customerPhone = findViewById(R.id.custumar_phone_add);
        customerTaka = findViewById(R.id.custumar_taka_add);
        CustomerAddress = findViewById(R.id.custumar_address_add);
        addCustomer = findViewById(R.id.addedCustomer);
        imageSeletcutomer = findViewById(R.id.imageSeletcutomer);
        customerTaxetId = findViewById(R.id.custumar_TaxtId);
        customerNIDnumber = findViewById(R.id.custumar_NID_Number);

        final Bundle bundle = getIntent().getExtras();

        if (bundle!=null){

            idup = bundle.getString("id");
            nameup = bundle.getString("name");
            phoneup = bundle.getString("phone");
            takaup = bundle.getString("taka");
            addresup = bundle.getString("addreds");
            imageup = bundle.getString("imageurl");

            if (imageup!=null) {
                Uri myUri = Uri.parse(imageup);
                mImageUri = myUri;
            }

            id=idup;

            if (imageup!=null) {
                String Url = imageup;
                Picasso.get().load(Url).into(circleCutomerImageView);
            }
            if (nameup!=null){
                customerName.setText(nameup);
            }
            if (phoneup!=null){
                customerPhone.setText(phoneup);
            }if (takaup!=null){
                customerTaka.setText(takaup);
            }
            if (addresup!=null){
                CustomerAddress.setText(addresup);
            }



        }



        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!checkIntert()) {
                    Toast.makeText(CustomerAddActivity.this, " কোনও ইন্টারনেট সংযোগ নেই ", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String name1 = customerName.getText().toString();
                final String phone1 = customerPhone.getText().toString();
                final String takaChake = customerTaka.getText().toString();
                final String addreschack = CustomerAddress.getText().toString();

                if (TextUtils.isEmpty(name1)) {
                    Toast.makeText(getApplicationContext(), "কাস্টমার নাম লিখুন ", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(phone1)) {
                    Toast.makeText(getApplicationContext(), "গ্রাহক ফোন নাম্বার লিখুন", Toast.LENGTH_LONG).show();
                    return;
                } if (TextUtils.isEmpty(takaChake)) {
                    Toast.makeText(getApplicationContext(), "গ্রাহক টাকার পরিমান লিখুন", Toast.LENGTH_LONG).show();
                    return;
                } if (TextUtils.isEmpty(addreschack)) {
                    Toast.makeText(getApplicationContext(), "গ্রাহক ঠিকানা  লিখুন", Toast.LENGTH_LONG).show();
                    return;
                }

                if (bundle!=null){
                    progressDialog = new ProgressDialog(CustomerAddActivity.this);
                    progressDialog.setMessage("লোড করছে..."); // Setting Message
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    if (image!=false){

                       // CustomerInfoUpload(mImageUri);
                        uploadImageUri(mImageUri);
                    }
                    else {
                        final String name = customerName.getText().toString();
                        final String phone = customerPhone.getText().toString();
                        final String taka = customerTaka.getText().toString();
                        double dtaka = Double.parseDouble(taka);
                        final String addres = CustomerAddress.getText().toString();
                        final String taxtId = customerTaxetId.getText().toString();
                        final String NID = customerNIDnumber.getText().toString();

                        customer.document(id).update("customerIdDucunt", id, "nameCUstomer", name, "phone", phone, "taka", dtaka, "addres", addres,"TaxtId",taxtId,"NID",NID).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                progressDialog.dismiss();
                                Toast.makeText(CustomerAddActivity.this, " সফল ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CustomerAddActivity.this, CustumarActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                    }
                }else {


                if (mImageUri == null) { progressDialog = new ProgressDialog(CustomerAddActivity.this);
                    progressDialog.setMessage("লোড করছে..."); // Setting Message
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    final String name = customerName.getText().toString();
                    final String phone = customerPhone.getText().toString();
                    final String taka = customerTaka.getText().toString();
                    double dtaka = Double.parseDouble(taka);
                    final String addres = CustomerAddress.getText().toString();
                    final String taxtId = customerTaxetId.getText().toString();
                    final String NID = customerNIDnumber.getText().toString();


                    customer.add(new CustomerNote(null, name, phone, dtaka, addres,00.00,00.00,taxtId,NID)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if (task.isSuccessful()) {

                                id = task.getResult().getId().toString();


                                customer.document(id).update("customerIdDucunt", id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        progressDialog.dismiss();

                                        Toast.makeText(CustomerAddActivity.this, " সফল ", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CustomerAddActivity.this,CustumarActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                });
                            }

                        }
                    });

                }
                else {
                   // CustomerInfoUpload(mImageUri);
                    uploadImageUri( mImageUri);
                   }
                }

            }


        });

        imageSeletcutomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bundle!=null){
                    image = true;
                    getIMEGE();
                }else {
                    getIMEGE();
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
                Picasso.get().load(mImageUri).into(circleCutomerImageView);
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

    public void CustomerInfoUpload(final Uri mImageUri) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("আপলোড হচ্ছে...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        final String fileName = System.currentTimeMillis() + "";

        final StorageReference putImage = mStorageReferenceImage.child(fileName);

        putImage.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        putImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(final Uri uri) {


                               final String name = customerName.getText().toString();
                                final String phone = customerPhone.getText().toString();
                                final String taka = customerTaka.getText().toString();
                                double dtaka = Double.parseDouble(taka);
                                final String addres = CustomerAddress.getText().toString();
                                final String taxtId = customerTaxetId.getText().toString();
                                final String NID = customerNIDnumber.getText().toString();

                                if (image!=false){

                                    customer.document(id).update("customerIdDucunt", id,"nameCUstomer",name,"phone",phone,"taka",dtaka,"addres",addres,"imageUrl",uri.toString(),"TaxtId",taxtId,"NID",NID)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                        }
                                    });

                                }else {

                                customer.add(new CustomerNote(null,name,phone,dtaka,addres,uri.toString(),00.00,00.00,taxtId,NID)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        if (task.isSuccessful()){

                                            String id = task.getResult().getId();


                                            customer.document(id).update("customerIdDucunt",id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {


                                                    Toast.makeText(CustomerAddActivity.this, " সফল ", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }

                                    }
                                });}

                                progressDialog.dismiss();
                                Intent intent = new Intent(CustomerAddActivity.this,CustumarActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                int countProgres = (int) (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(countProgres);
            }
        });


    }



    public boolean checkIntert(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }

    private void uploadImageUri(Uri imageUri){


        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("আপলোড হচ্ছে...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);


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

            upload(file, new UploadCallback() {
                @Override
                public void onSuccess(String downloadLink) {


                    final String name = customerName.getText().toString();
                    final String phone = customerPhone.getText().toString();
                    final String taka = customerTaka.getText().toString();
                    double dtaka = Double.parseDouble(taka);
                    final String addres = CustomerAddress.getText().toString();
                    final String taxtId = customerTaxetId.getText().toString();
                    final String NID = customerNIDnumber.getText().toString();


                    if (image!=false){

                        customer.document(id).update("customerIdDucunt", id,"nameCUstomer",name,"phone",phone,"taka",dtaka,"addres",addres,"imageUrl",downloadLink,"TaxtId",taxtId,"NID",NID)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        });

                    }else {

                        customer.add(new CustomerNote(null,name,phone,dtaka,addres,downloadLink,00.00,00.00,taxtId,NID)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                if (task.isSuccessful()){

                                    String id = task.getResult().getId();


                                    customer.document(id).update("customerIdDucunt",id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {


                                            Toast.makeText(CustomerAddActivity.this, " সফল ", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                            }
                        });}

                    progressDialog.dismiss();
                    Intent intent = new Intent(CustomerAddActivity.this,CustumarActivity.class);
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
