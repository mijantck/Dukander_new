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
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
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
                barDialog.setTitle("Pay Bill");
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


        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProductListActivity.this);
                String[] option = {"Product Edit ","Delete"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db = FirebaseFirestore.getInstance();

                        if (which == 0) {
                            ProductNote productNote = documentSnapshot.toObject(ProductNote.class);
                            String id = documentSnapshot.getId();
                            String imageurl = productNote.getProImgeUrl();
                            String imageurl1 = productNote.getProImgeUrl1();
                            String imageurl2 = productNote.getProImgeUrl2();
                            String imageurl3 = productNote.getProImgeUrl3();
                            String imageurl4 = productNote.getProImgeUrl4();
                            String name = productNote.getProName();
                            String color = productNote.getColor();
                            String Size = productNote.getSize();
                            String type = productNote.getType();
                            String description = productNote.getDescription();
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
                            if (imageurl1 != null) {
                                pdfIntent.putExtra("imageurl1", imageurl1);
                            }
                            if (imageurl2 != null) {
                                pdfIntent.putExtra("imageurl2", imageurl2);
                            }
                            if (imageurl3 != null) {
                                pdfIntent.putExtra("imageurl3", imageurl3);
                            }
                            if (imageurl4 != null) {
                                pdfIntent.putExtra("imageurl4", imageurl4);
                            }
                            pdfIntent.putExtra("name", name);
                            pdfIntent.putExtra("code", barcode);
                            pdfIntent.putExtra("pprice", pp);
                            pdfIntent.putExtra("pBprice", pBp);
                            pdfIntent.putExtra("pQuan", pq);
                            pdfIntent.putExtra("pmini", pm);
                            pdfIntent.putExtra("privecy", privecy);
                            pdfIntent.putExtra("catagory", catagory);

                            pdfIntent.putExtra("color", color);
                            pdfIntent.putExtra("Size", Size);
                            pdfIntent.putExtra("type", type);
                            pdfIntent.putExtra("descriptuion", description);
                            if (!discount.isEmpty())
                            {
                                pdfIntent.putExtra("dicount", discount);
                            }


                            startActivity(pdfIntent);


                        }
                        if(which == 1){
                            new AlertDialog.Builder(ProductListActivity.this)
                                    .setTitle("Confirm Delete")
                                    .setMessage("Are You sure?")
                                    .setPositiveButton("yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(final DialogInterface dialog, int which) {

                                                    progressDialog = new ProgressDialog(ProductListActivity.this);
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progressDialog.setMessage("Loading...");
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
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
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

            @Override
            public void onShearClick(DocumentSnapshot documentSnapshot, int position) {

                ProductNote productNote = documentSnapshot.toObject(ProductNote.class);

                String id = documentSnapshot.getId();
                String imageurl = productNote.getProImgeUrl();
                String name = productNote.getProName();
                String discount = String.valueOf(productNote.getPruductDiscount());

                String pp = String.valueOf(productNote.getProPrice());

                String sharelinktext  =  "https://a2zloja.page.link/?"+
                        "link=https://a2zloja.page.link/jdF1?"+
                        "proID="+"-"+"product"+
                        "-"+id+
                        "-"+id+
                        "&st="+name+
                        "&sd="+"Price "+pp+ "\ndiscount ="+discount+
                        "&si="+imageurl+
                        "&apn="+getPackageName();



                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        // .setLongLink(dynamicLink.getUri())
                        .setLongLink(Uri.parse(sharelinktext))// manually
                        .buildShortDynamicLink()
                        .addOnCompleteListener(ProductListActivity.this, new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    // Short link created
                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();
                                    Log.e("main ", "short link "+ shortLink.toString());
                                    // share app dialog
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT,  shortLink.toString());
                                    intent.setType("text/plain");
                                    startActivity(intent);


                                } else {
                                    // Error
                                    // ...
                                    Log.e("main", " error "+task.getException() );

                                }
                            }
                        });

            }
        });

    }


    private void recyclear( String search) {


        Query query = product.orderBy("search").startAt(search.toLowerCase()).endAt(search.toLowerCase()+ "\uf8ff");

        FirestoreRecyclerOptions<ProductNote> options = new FirestoreRecyclerOptions.Builder<ProductNote>()
                .setQuery(query, ProductNote.class)
                .build();

        adapter = new ProductAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.product_info_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {



                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProductListActivity.this);
                String[] option = {"Product Edit ","Delete"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db = FirebaseFirestore.getInstance();

                        if (which == 0) {
                            ProductNote productNote = documentSnapshot.toObject(ProductNote.class);
                            String id = documentSnapshot.getId();
                            String imageurl = productNote.getProImgeUrl();
                            String imageurl1 = productNote.getProImgeUrl1();
                            String imageurl2 = productNote.getProImgeUrl2();
                            String imageurl3 = productNote.getProImgeUrl3();
                            String imageurl4 = productNote.getProImgeUrl4();
                            String name = productNote.getProName();
                            String color = productNote.getColor();
                            String Size = productNote.getSize();
                            String type = productNote.getType();
                            String description = productNote.getDescription();
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
                            if (imageurl1 != null) {
                                pdfIntent.putExtra("imageurl1", imageurl1);
                            }
                            if (imageurl2 != null) {
                                pdfIntent.putExtra("imageurl2", imageurl2);
                            }
                            if (imageurl3 != null) {
                                pdfIntent.putExtra("imageurl3", imageurl3);
                            }
                            if (imageurl4 != null) {
                                pdfIntent.putExtra("imageurl4", imageurl4);
                            }
                            pdfIntent.putExtra("name", name);
                            pdfIntent.putExtra("code", barcode);

                            pdfIntent.putExtra("pprice", pp);

                            pdfIntent.putExtra("pBprice", pBp);

                            pdfIntent.putExtra("pQuan", pq);
                            pdfIntent.putExtra("pmini", pm);
                            pdfIntent.putExtra("color", color);
                            pdfIntent.putExtra("Size", Size);
                            pdfIntent.putExtra("type", type);
                            pdfIntent.putExtra("descriptuion", description);
                            if (!discount.isEmpty())
                            {
                                pdfIntent.putExtra("dicount", discount);
                            }

                            startActivity(pdfIntent);


                        }
                        if(which == 1){
                            new AlertDialog.Builder(ProductListActivity.this)
                                    .setTitle("Confirm Delete?")
                                    .setMessage("Are you sure?")
                                    .setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(final DialogInterface dialog, int which) {
                                                    CollectionReference GlobleProduct = FirebaseFirestore.getInstance()
                                                            .collection("GlobleProduct");

                                                    progressDialog = new ProgressDialog(ProductListActivity.this);
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progressDialog.setMessage("Loading...");
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
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
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

            @Override
            public void onShearClick(DocumentSnapshot documentSnapshot, int position) {

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

                recyclear(newText);
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


        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, final int position) {



                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProductListActivity.this);
                String[] option = {"Product Edit ","Delete"};
                builder.setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        db = FirebaseFirestore.getInstance();

                        if (which == 0) {
                            ProductNote productNote = documentSnapshot.toObject(ProductNote.class);
                            String id = documentSnapshot.getId();
                            String imageurl = productNote.getProImgeUrl();
                            String imageurl1 = productNote.getProImgeUrl1();
                            String imageurl2 = productNote.getProImgeUrl2();
                            String imageurl3 = productNote.getProImgeUrl3();
                            String imageurl4 = productNote.getProImgeUrl4();
                            String name = productNote.getProName();
                            String Size = productNote.getSize();
                            String color = productNote.getColor();
                            String type = productNote.getType();
                            String description = productNote.getDescription();
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

                            if (imageurl1 != null) {
                                pdfIntent.putExtra("imageurl1", imageurl1);
                            }
                            if (imageurl2 != null) {
                                pdfIntent.putExtra("imageurl2", imageurl2);
                            }
                            if (imageurl3 != null) {
                                pdfIntent.putExtra("imageurl3", imageurl3);
                            }
                            if (imageurl4 != null) {
                                pdfIntent.putExtra("imageurl4", imageurl4);
                            }
                            pdfIntent.putExtra("name", name);
                            pdfIntent.putExtra("code", barcode);

                            pdfIntent.putExtra("pprice", pp);

                            pdfIntent.putExtra("pBprice", pBp);

                            pdfIntent.putExtra("pQuan", pq);
                            pdfIntent.putExtra("pmini", pm);
                            pdfIntent.putExtra("color", color);
                            pdfIntent.putExtra("Size", Size);
                            pdfIntent.putExtra("type", type);
                            pdfIntent.putExtra("descriptuion", description);
                            if (!discount.isEmpty())
                            {
                                pdfIntent.putExtra("dicount", discount);
                            }
                            startActivity(pdfIntent);


                        }
                        if(which == 1){
                            new AlertDialog.Builder(ProductListActivity.this).setTitle("Confirm Delete?")
                                    .setMessage("Are you sure?")
                                    .setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(final DialogInterface dialog, int which) {

                                                    progressDialog = new ProgressDialog(ProductListActivity.this);
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progressDialog.setTitle("Loading...");
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
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
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

            @Override
            public void onShearClick(DocumentSnapshot documentSnapshot, int position) {


            }
        });
        adapter.startListening();

    }




}

