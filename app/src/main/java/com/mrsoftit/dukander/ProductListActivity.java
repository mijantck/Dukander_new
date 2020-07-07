package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.Objects;

public class ProductListActivity extends AppCompatActivity {


    FloatingActionButton floating_action_button_customer;


    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    ProductAdapter adapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    MaterialButton shearchWithBarcode;

    String user_id = currentUser.getUid();

    CollectionReference product = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Product");

    TextView product_text_view;

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

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar =  findViewById(R.id.toolbar_support);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Objects.requireNonNull(toolbar.getNavigationIcon()).setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        toolbar.setSubtitleTextColor(getResources().getColor(R.color.grey));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        floating_action_button_customer = findViewById(R.id.floating_action_button_product);
        product_text_view = findViewById(R.id.product_text_view);
        shearchWithBarcode = findViewById(R.id.shearchWithBarcode);
        shearchWithBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                barDialog = new Dialog(ProductListActivity.this);
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


        product.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("proName") != null) {
                        String name = doc.get("proName").toString();


                        if (name!=null){
                            product_text_view.setVisibility(View.GONE);
                        }
                    }

                }

            }
        });





        recyclear();

        floating_action_button_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProductListActivity.this,ProductAddActivity.class);

                startActivity(intent);

            }
        });



    }



    private void recyclear() {


        Query query = product.orderBy("proName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProductNote> options = new FirestoreRecyclerOptions.Builder<ProductNote>()
                .setQuery(query, ProductNote.class)
                .build();

        adapter = new ProductAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.product_info_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        product_text_view.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);


        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {



                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProductListActivity.this);
                String[] option = {"প্রোডাক্ট এডিট ","মুছে ফেলা"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db = FirebaseFirestore.getInstance();

                        if (which == 0) {
                            ProductNote productNote = documentSnapshot.toObject(ProductNote.class);
                            String id = documentSnapshot.getId();
                            String imageurl = productNote.getProImgeUrl();
                            String name = productNote.getProName();
                            String privecy = productNote.getProductPrivacy();
                            String catagory = productNote.getProductCategory();
                            String barcode = productNote.getBarCode();
                            String pp = String.valueOf(productNote.getProPrice());
                            String pBp = String.valueOf(productNote.getProBuyPrice());
                            String pq = String.valueOf(productNote.getProQua());
                            String pm = String.valueOf(productNote.getProMin());
                            String discount = String.valueOf(productNote.getPruductDiscount());
                            Intent pdfIntent = new Intent(ProductListActivity.this, ProductAddActivity.class);
                            pdfIntent.putExtra("id", id);
                            if (imageurl != null) {
                                pdfIntent.putExtra("imageurl", imageurl);
                            }
                            pdfIntent.putExtra("name", name);
                            pdfIntent.putExtra("code", barcode);
                            pdfIntent.putExtra("pprice", pp);

                            pdfIntent.putExtra("pBprice", pBp);

                            pdfIntent.putExtra("pQuan", pq);

                            pdfIntent.putExtra("pmini", pm);
                            pdfIntent.putExtra("privecy", privecy);
                            pdfIntent.putExtra("catagory", catagory);
                            if (!discount.isEmpty())
                            {
                                pdfIntent.putExtra("dicount", discount);
                            }



                            startActivity(pdfIntent);


                        }
                        if(which == 1){
                            new AlertDialog.Builder(ProductListActivity.this).setTitle("নিশ্চিত বাতিল?")
                                    .setMessage("তুমি কি নিশ্চিত?")
                                    .setPositiveButton("হ্যাঁ",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(final DialogInterface dialog, int which) {

                                                    progressDialog = new ProgressDialog(ProductListActivity.this);
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progressDialog.setTitle("আপলোড হচ্ছে...");
                                                    progressDialog.show();

                                                    CollectionReference GlobleProduct = FirebaseFirestore.getInstance()
                                                            .collection("GlobleProduct");

                                                    String id = documentSnapshot.getId();

                                                    GlobleProduct.document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            adapter.deleteItem(position);
                                                            dialog.dismiss();
                                                            progressDialog.dismiss();

                                                        }
                                                    });

                                                }
                                            })
                                    .setNegativeButton("না", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Do nothing
                                            dialog.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                }).create().show();






            }
        });

    }


    private void recyclear( String search) {


        Query query = product.whereLessThanOrEqualTo("proName",search).orderBy("proName", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ProductNote> options = new FirestoreRecyclerOptions.Builder<ProductNote>()
                .setQuery(query, ProductNote.class)
                .build();

        adapter = new ProductAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.product_info_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {



                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProductListActivity.this);
                String[] option = {"প্রোডাক্ট এডিট ","মুছে ফেলা"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db = FirebaseFirestore.getInstance();

                        if (which == 0) {
                            ProductNote productNote = documentSnapshot.toObject(ProductNote.class);
                            String id = documentSnapshot.getId();
                            String imageurl = productNote.getProImgeUrl();
                            String name = productNote.getProName();
                            String barcode = productNote.getBarCode();
                            String pp = String.valueOf(productNote.getProPrice());
                            String pBp = String.valueOf(productNote.getProBuyPrice());
                            String pq = String.valueOf(productNote.getProQua());
                            String pm = String.valueOf(productNote.getProMin());
                            String discount = String.valueOf(productNote.getPruductDiscount());
                            Intent pdfIntent = new Intent(ProductListActivity.this, ProductAddActivity.class);
                            pdfIntent.putExtra("id", id);
                            if (imageurl != null) {
                                pdfIntent.putExtra("imageurl", imageurl);
                            }
                            pdfIntent.putExtra("name", name);
                            pdfIntent.putExtra("code", barcode);

                            pdfIntent.putExtra("pprice", pp);

                            pdfIntent.putExtra("pBprice", pBp);

                            pdfIntent.putExtra("pQuan", pq);
                            pdfIntent.putExtra("pmini", pm);
                            if (!discount.isEmpty())
                            {
                                pdfIntent.putExtra("dicount", discount);
                            }

                            startActivity(pdfIntent);


                        }
                        if(which == 1){
                            new AlertDialog.Builder(ProductListActivity.this).setTitle("নিশ্চিত বাতিল?")
                                    .setMessage("তুমি কি নিশ্চিত?")
                                    .setPositiveButton("হ্যাঁ",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(final DialogInterface dialog, int which) {
                                                    CollectionReference GlobleProduct = FirebaseFirestore.getInstance()
                                                            .collection("GlobleProduct");

                                                    progressDialog = new ProgressDialog(ProductListActivity.this);
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progressDialog.setTitle("আপলোড হচ্ছে...");
                                                    progressDialog.show();

                                                    String id = documentSnapshot.getId();

                                                    GlobleProduct.document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            adapter.deleteItem(position);
                                                            dialog.dismiss();
                                                            progressDialog.dismiss();

                                                        }
                                                    });
                                                }
                                            })
                                    .setNegativeButton("না", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Do nothing
                                            dialog.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                }).create().show();

            }
        });

        adapter.startListening();

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                recyclear(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

    private void initialiseDetectorsAndSources() {



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
                    if (ActivityCompat.checkSelfPermission(ProductListActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ProductListActivity.this, new
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
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                barcode(barcodeData);
                                barDialog.dismiss();

                            } else {

                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                barcode( barcodeData);
                                barDialog.dismiss();

                            }
                        }
                    });

                }
            }
        });
    }


    private void barcode( String search) {


        Query query = product.whereEqualTo("barCode",search);

        FirestoreRecyclerOptions<ProductNote> options = new FirestoreRecyclerOptions.Builder<ProductNote>()
                .setQuery(query, ProductNote.class)
                .build();

        adapter = new ProductAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.product_info_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {



                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProductListActivity.this);
                String[] option = {"প্রোডাক্ট এডিট ","মুছে ফেলা"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db = FirebaseFirestore.getInstance();

                        if (which == 0) {
                            ProductNote productNote = documentSnapshot.toObject(ProductNote.class);
                            String id = documentSnapshot.getId();
                            String imageurl = productNote.getProImgeUrl();
                            String name = productNote.getProName();
                            String barcode = productNote.getBarCode();
                            String pp = String.valueOf(productNote.getProPrice());
                            String pBp = String.valueOf(productNote.getProBuyPrice());
                            String pq = String.valueOf(productNote.getProQua());
                            String pm = String.valueOf(productNote.getProMin());
                            String discount = String.valueOf(productNote.getPruductDiscount());

                            Intent pdfIntent = new Intent(ProductListActivity.this, ProductAddActivity.class);
                            pdfIntent.putExtra("id", id);
                            if (imageurl != null) {
                                pdfIntent.putExtra("imageurl", imageurl);
                            }

                            pdfIntent.putExtra("name", name);
                            pdfIntent.putExtra("code", barcode);

                            pdfIntent.putExtra("pprice", pp);

                            pdfIntent.putExtra("pBprice", pBp);

                            pdfIntent.putExtra("pQuan", pq);
                            pdfIntent.putExtra("pmini", pm);
                            if (!discount.isEmpty())
                            {
                                pdfIntent.putExtra("dicount", discount);
                            }
                            startActivity(pdfIntent);


                        }
                        if(which == 1){
                            new AlertDialog.Builder(ProductListActivity.this).setTitle("নিশ্চিত বাতিল?")
                                    .setMessage("তুমি কি নিশ্চিত?")
                                    .setPositiveButton("হ্যাঁ",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(final DialogInterface dialog, int which) {

                                                    progressDialog = new ProgressDialog(ProductListActivity.this);
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progressDialog.setTitle("আপলোড হচ্ছে...");
                                                    progressDialog.show();

                                                    CollectionReference GlobleProduct = FirebaseFirestore.getInstance()
                                                            .collection("GlobleProduct");

                                                    String id = documentSnapshot.getId();

                                                    GlobleProduct.document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            adapter.deleteItem(position);
                                                            dialog.dismiss();
                                                            progressDialog.dismiss();

                                                        }
                                                    });
                                                }
                                            })
                                    .setNegativeButton("না", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Do nothing
                                            dialog.dismiss();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    }
                }).create().show();

            }
        });

        adapter.startListening();

    }
}
