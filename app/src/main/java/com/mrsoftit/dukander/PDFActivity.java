package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static java.lang.Double.parseDouble;

public class PDFActivity extends AppCompatActivity {


    String id,unkcutomarId,name,phone,taka,addrs,invoise,totaltaka,takacutomerup,paymonysendup;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;

    private File pdfFile;

    FirebaseFirestore db;

    SaleProductIndevicualAdapter saleProductIndevicualAdapter;


    public static final String FONT = "/assets/fonts/solaiman_lipi.ttf";
    ArrayList<SaleProductCutomerNote> PDFList = new ArrayList<>();

    SaleProductCutomerNote saleProductCutomerNote;
    SaleProductCutomerNote itemName;
    SaleProductCutomerNote quantedt;
    SaleProductCutomerNote Sprice;
    SaleProductCutomerNote price;


    ProgressDialog progressDialog;


    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String user_id = Objects.requireNonNull(currentUser).getUid();

    Image image;

    Date calendar1 = Calendar.getInstance().getTime();
    @SuppressLint("SimpleDateFormat")
    DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
    String todayString = df1.format(calendar1);
    final int datenew = Integer.parseInt(todayString);


    TextView invoiceNmaber,invoiceDate,BilCutomerName,BilCustomerPhone,
            BilCutomerAddress,BilShopName,BilShopPhone,BilShopAddrss,SubTottal,Total,dautaka,PDFpayment;
    TextView BilShoppiciname;

    String BilShopImage;
     //ExtendedFloatingActionButton createPDF ;
    String picName;
    String picName2;
    ImageView imageView;
    double sum =00.00;

    String pdfname;

    String urlImage;
    String filename;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String user_id = Objects.requireNonNull(currentUser).getUid();


        CollectionReference myInfo = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("DukanInfo");
        myInfo.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                String picurl = null;
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("dukanName") != null) {
                        BilShopName.setText(Objects.requireNonNull(doc.get("dukanName")).toString());
                    }
                    if (doc.get("dukanaddress") != null) {
                        BilShopAddrss.setText(Objects.requireNonNull(doc.get("dukanaddress")).toString());
                    }
                   if (doc.get("dukanphone") != null) {
                        BilShopPhone.setText(Objects.requireNonNull(doc.get("dukanphone")).toString());
                    }if (doc.get("picName") != null) {
                        picName= doc.get("picName").toString();
                    } if (doc.get("dukanaddpicurl") != null) {
                        BilShopImage = Objects.requireNonNull(doc.get("dukanaddpicurl")).toString();
                        Picasso.get().load(Uri.parse(BilShopImage)).into(imageView);
                        picurl = BilShopImage;
                    }
                }

                urlImage = picurl;

            }
        });


        saleProductIndevicualAdapter.startListening();



    }

    @Override
    protected void onStop() {
        super.onStop();
        saleProductIndevicualAdapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);


        saleProductCutomerNote  = new SaleProductCutomerNote();

                    invoiceNmaber =findViewById(R.id.invoiceID);
                    invoiceDate =findViewById(R.id.invoiceDate);
                    BilCutomerName=findViewById(R.id.BilCustomrName);
                    BilCustomerPhone=findViewById(R.id.BilCustomrhone);
                    BilCutomerAddress=findViewById(R.id.BilCustomrAddress);
                    BilShopName=findViewById(R.id.BilShopName);
                    imageView=findViewById(R.id.shopeBanner);
                    BilShopPhone=findViewById(R.id.BilShopPhone);
                    BilShopAddrss=findViewById(R.id.BilShopAddress);
                    SubTottal=findViewById(R.id.Subtotal);
                    Total=findViewById(R.id.PDFtotal);
                    dautaka=findViewById(R.id.dautaka);
                    PDFpayment=findViewById(R.id.PDFpayment);
                    BilShoppiciname = findViewById(R.id.BilShoppiciname);

                   ExtendedFloatingActionButton createPDF = (ExtendedFloatingActionButton) findViewById(R.id.PDFcreate);



            final Bundle bundle = getIntent().getExtras();

            if (bundle!=null){

                id = bundle.getString("cutomarId");
                unkcutomarId = bundle.getString("unkcutomarId");
                name = bundle.getString("customarName");
                phone = bundle.getString("cutomerPhone");
                taka = bundle.getString("customertaka");
                addrs = bundle.getString("customerAddres");
                invoise = bundle.getString("invoise");
                totaltaka = bundle.getString("totaltaka");
                takacutomerup = bundle.getString("takacutomerup");
                paymonysendup = bundle.getString("paymonysend");


                if (name!=null){
                    BilCutomerName.setText(name);
                }if (phone!=null){
                    BilCustomerPhone.setText(phone);
                }
                if (addrs!=null){
                    BilCutomerAddress.setText(addrs);
                }
                if (invoise!=null){
                    invoiceNmaber.setText(invoise);
                }
                if (takacutomerup!=null){
                    dautaka.setText(takacutomerup);
                }
            }



        db = FirebaseFirestore.getInstance();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("তথ্য প্রস্তুত হচ্ছে...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();




        final File docsFolder1 = new File(Environment.getExternalStorageDirectory() +"/Dukandar/");

        File newFile = new File(docsFolder1,"01743.jpeg");


        if (newFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(newFile.getAbsolutePath());
            ImageView myImage =  findViewById(R.id.shopeBanner);
            myImage.setImageBitmap(myBitmap);
        }


        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        String date = simpleDateFormat.format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());

        invoiceDate.setText(date+"\n"+currentTime);

        if (id!=null) {
            loadData();
            recyclearinvoiser();


        }if (unkcutomarId!=null){
            UnknownloadData();
            UnkownCustumerrecyclearinvoiser();

        }



        createPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(PDFActivity.this);
                progressDialog.setMessage("তথ্য প্রস্তুত হচ্ছে...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                try {
                    createPdfWrapper();
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }

            }
        });

    }




    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(PDFActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel(
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        } else {


            createPdf();

        }
    }


    private void createPdf() throws FileNotFoundException, DocumentException {


        final File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Dukandar");

         if (!docsFolder.exists()) {
            docsFolder.mkdir();

        }




         pdfname = name+datenew+".pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        final Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, output);





        try {
            final File docsFolder1 = new File(Environment.getExternalStorageDirectory() +"/Dukandar/dont_delete/");
            File newFile1 = new File(docsFolder1,"01743.jpeg");
            image = Image.getInstance(String.valueOf(newFile1));
            image.scaleAbsolute(540f, 72f);//image width,height
            Toast.makeText(PDFActivity.this, " pic add ", Toast.LENGTH_SHORT).show();
        }catch (Exception e1) {

            e1.printStackTrace();
        }


        PdfPTable irdTable = new PdfPTable(2);
        irdTable.addCell(getIRDCell("Invoice No"));

        irdTable.addCell(getIRDCell("Invoice Date"));
        String invoiceNmaberS = invoiceNmaber.getText().toString();
        irdTable.addCell(getIRDCell(invoiceNmaberS)); // pass invoice number
        String invoicedateS = invoiceDate.getText().toString();
        irdTable.addCell(getIRDCell(invoicedateS)); // pass invoice date
        PdfPTable irhTable = new PdfPTable(3);
        irhTable.setWidthPercentage(100);

        irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
        irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
        irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
        irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
        irhTable.addCell(getIRHCell("", PdfPCell.ALIGN_RIGHT));
        PdfPCell invoiceTable = new PdfPCell (irdTable);
        invoiceTable.setBorder(0);
        irhTable.addCell(invoiceTable);

        PdfPTable FromTable  = new PdfPTable(2);
        FromTable.setWidthPercentage(100);
        FromTable.addCell(getFROMCell("Bill To ",PdfPTable.ALIGN_LEFT));
        FromTable.addCell(getFROMCell("Bill From ",PdfPTable.ALIGN_RIGHT));
        String cName = BilCutomerName.getText().toString();

        FromTable.addCell(getCell(" "+cName, PdfPCell.ALIGN_LEFT));
        FromTable.addCell(getCell(" "+BilShopName.getText().toString(), PdfPCell.ALIGN_RIGHT));
        FromTable.addCell(getCell(" "+BilCustomerPhone.getText().toString(), PdfPCell.ALIGN_LEFT));
        FromTable.addCell(getCell(" "+BilShopPhone.getText().toString(), PdfPCell.ALIGN_RIGHT));
        FromTable.addCell(getCell(" "+BilCutomerAddress.getText().toString(), PdfPCell.ALIGN_LEFT));
        FromTable.addCell(getCell(" "+BilShopAddrss.getText().toString(), PdfPCell.ALIGN_RIGHT));




        final PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3, 3});
        table.setWidthPercentage(100);
        table.setSpacingBefore(30f);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Products");
        table.addCell("Quantity");
        table.addCell("S price");
        table.addCell("price");
        table.addCell("Status");
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();
        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        }



       for (int i = 0; i < PDFList.size(); i++) {
           itemName = PDFList.get(i);
           quantedt = PDFList.get(i);
           Sprice = PDFList.get(i);
           price = PDFList.get(i);

         //  totalPrice = PDFList.get(i);


            String namen = itemName.getItemName()+"";
            String quantidy = quantedt.getQuantedt()+"";
            String sprice = Sprice.getPrice()+"";
            String TotalPrice = price.getTotalPrice()+"";



            table.addCell(fontCoveter(namen));
            table.addCell(quantidy);
            table.addCell(sprice);
            table.addCell(TotalPrice);
            table.addCell("Paid");

        }

        PdfPTable  validity = new PdfPTable(1);
        validity.setWidthPercentage(100);
        validity.addCell(getValidityCell(" "));
        validity.addCell(getValidityCell("warranty"));
       // validity.addCell(getValidityCell(" * Products purchased comes with 1 year national warranty \n   (if applicable)"));
       // validity.addCell(getValidityCell(" * Warranty should be claimed only from the respective manufactures"));
        PdfPCell summaryL = new PdfPCell (validity);
        summaryL.setColspan (3);
        summaryL.setPadding (1.0f);
        table.addCell(summaryL);

        PdfPTable accounts = new PdfPTable(2);
        accounts.setWidthPercentage(100);
        accounts.addCell(getAccountsCell("Due"));
        accounts.addCell(getAccountsCellR(dautaka.getText().toString()));
        accounts.addCell(getAccountsCell("Total"));
        accounts.addCell(getAccountsCellR(SubTottal.getText().toString()));
        accounts.addCell(getAccountsCell("Amount paid"));
        accounts.addCell(getAccountsCellR(paymonysendup.toString()));
        accounts.addCell(getAccountsCell("Current due"));
        accounts.addCell(getAccountsCellR(Total.getText().toString()));
        PdfPCell summaryR = new PdfPCell (accounts);
        summaryR.setColspan (2);
        table.addCell(summaryR);

        PdfPTable describer = new PdfPTable(1);
        describer.setWidthPercentage(100);
        describer.addCell(getdescCell(" "));
        describer.addCell(getdescCell("Operation and maintenance by MR.SOFT.IT || © MR.SOFT.IT 2020 || http://www.mrsoftit.com || 01733-883310 "));

        document.open();

        if (image!=null){
            document.add(image);
        }

        document.add(irhTable);
        document.add(FromTable);
        document.add(table);
        document.add(describer);

        document.close();

        previewPdf();


    }


    private void previewPdf() {

            if (id!=null){

             CollectionReference customerProductSale = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Customers").document(id).collection("saleProduct");

             Query query = customerProductSale.whereEqualTo("update",false).whereEqualTo("date",datenew).whereEqualTo("paid",true);
             query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                List<String> list = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    list.add(document.getId());
                }
                saveCustomerupdateData((ArrayList) list); // *** new ***



                final File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Dukandar");
                pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                if(pdfFile.exists()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), pdfFile);
                        target.setDataAndType(uri, "application/pdf");
                        target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }else {
                        target.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
                    }
                    Intent intent = Intent.createChooser(target, "Open File");
                    try {
                        progressDialog.dismiss();

                        startActivity(intent);

                    } catch (ActivityNotFoundException e) {
                        // Instruct the user to install a PDF reader here, or something
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "File path is incorrect." , Toast.LENGTH_LONG).show();

            }
            progressDialog.dismiss();

        }


                 });
}
                if (unkcutomarId!=null){

                CollectionReference UNcustomerProductSale = FirebaseFirestore.getInstance()
                        .collection("users").document(user_id).collection("UnknownCustomer").document(unkcutomarId).collection("saleProduct");

                Query query = UNcustomerProductSale.whereEqualTo("update",false).whereEqualTo("paid",true);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                list.add(document.getId());
                            }
                            unknowCustomerupdateData((ArrayList) list); // *** new ***

                            final File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Dukandar");
                            pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
                            Intent target = new Intent(Intent.ACTION_VIEW);
                            target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            if(pdfFile.exists()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), pdfFile);
                                    target.setDataAndType(uri, "application/pdf");
                                    target.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                }else {
                                    target.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
                                }
                                Intent intent = Intent.createChooser(target, "Open File");
                                try {
                                    progressDialog.dismiss();

                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    // Instruct the user to install a PDF reader here, or something
                                }
                            }
                            else
                                Toast.makeText(getApplicationContext(), "File path is incorrect." , Toast.LENGTH_LONG).show();

                        }
                        progressDialog.dismiss();

                    }


                });
            }


    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(PDFActivity.this)
                .setMessage("You need to allow access to Storage")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public  void  loadData(){
        CollectionReference customerProductSale = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("Customers").document(id).collection("saleProduct");

        Query query = customerProductSale.whereEqualTo("update",false).whereEqualTo("date",datenew).whereEqualTo("paid",true);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    QuerySnapshot queryDocumentSnapshots = task.getResult();

                    PDFList = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for(DocumentSnapshot readData: queryDocumentSnapshots.getDocuments()){

                        String name = Objects.requireNonNull(readData.get("itemName")).toString();
                        saleProductCutomerNote.setItemName(name);

                        String quantidy = Objects.requireNonNull(readData.get("quantedt")).toString();
                        double quantdylist = parseDouble(quantidy);
                        saleProductCutomerNote.setQuantedt(quantdylist);

                        String sprice = Objects.requireNonNull(readData.get("price")).toString();
                        double SinglePrice = parseDouble(sprice);
                        saleProductCutomerNote.setPrice(SinglePrice);

                        String price = readData.get("totalPrice").toString();

                        double totalPrice = parseDouble(price);

                        saleProductCutomerNote.setTotalPrice(totalPrice);


                        PDFList.add(saleProductCutomerNote);

                        saleProductCutomerNote = new SaleProductCutomerNote();


                    }

                }

                progressDialog.dismiss();
            }
        });


    }
    public  void  UnknownloadData(){
        CollectionReference UNcustomerProductSale = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("UnknownCustomer").document(unkcutomarId).collection("saleProduct");

        Query query = UNcustomerProductSale.whereEqualTo("update",false).whereEqualTo("paid",true);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    QuerySnapshot queryDocumentSnapshots = task.getResult();

                    PDFList = new ArrayList<>();
                    assert queryDocumentSnapshots != null;
                    for(DocumentSnapshot readData: queryDocumentSnapshots.getDocuments()){

                        String name = Objects.requireNonNull(readData.get("itemName")).toString();
                        saleProductCutomerNote.setItemName(name);

                        String quantidy = Objects.requireNonNull(readData.get("quantedt")).toString();
                        double quantdylist = parseDouble(quantidy);

                        saleProductCutomerNote.setQuantedt(quantdylist);
                        String sprice = Objects.requireNonNull(readData.get("price")).toString();
                        double SinglePrice = parseDouble(sprice);
                        saleProductCutomerNote.setPrice(SinglePrice);

                        String price = readData.get("totalPrice").toString();

                        double totalPrice = parseDouble(price);

                        saleProductCutomerNote.setTotalPrice(totalPrice);

                      //  Toast.makeText(PDFActivity.this, price+" unkown Load", Toast.LENGTH_SHORT).show();
                        PDFList.add(saleProductCutomerNote);


                        saleProductCutomerNote = new SaleProductCutomerNote();


                    }

                }

                progressDialog.dismiss();
            }
        });

    }

//main for PDF
    public static PdfPCell getAccountsCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthRight(0);
        cell.setBorderWidthTop(0);
        cell.setPadding (5.0f);
        return cell;
    }
    public static PdfPCell getAccountsCellR(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorderWidthLeft(0);
        cell.setBorderWidthTop(0);
        cell.setHorizontalAlignment (Element.ALIGN_RIGHT);
        cell.setPadding (5.0f);
        cell.setPaddingRight(20.0f);
        return cell;
    }

    public static PdfPCell getdescCell(String text) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setBorder(0);
        return cell;
    }

    public static PdfPCell getValidityCell(String text) {

        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        font.setColor(BaseColor.GRAY);
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell (phrase);
        cell.setBorder(0);
        return cell;
    }

    public static PdfPCell getIRHCell(String text, int alignment) {
        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 16);
        /*	font.setColor(BaseColor.GRAY);*/
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static PdfPCell getFROMCell(String text, int alignment) {

        FontSelector fs = new FontSelector();
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
        /*	font.setColor(BaseColor.GRAY);*/
        fs.addFont(font);
        Phrase phrase = fs.process(text);
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(5);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public PdfPCell getCell(String text, int alignment) {

        PdfPCell cell = new PdfPCell(fontCoveter(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static PdfPCell getIRDCell(String text) {

        PdfPCell cell = new PdfPCell (new Paragraph (text));
        cell.setHorizontalAlignment (Element.ALIGN_CENTER);
        cell.setPadding (5.0f);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private void recyclearinvoiser() {


        CollectionReference customerProductSale = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("Customers").document(id).collection("saleProduct");


        Query query = customerProductSale.whereEqualTo("update",false).whereEqualTo("date",datenew).whereEqualTo("paid",true);


        FirestoreRecyclerOptions<SaleProductCutomerNote> options = new FirestoreRecyclerOptions.Builder<SaleProductCutomerNote>()
                .setQuery(query, SaleProductCutomerNote.class)
                .build();


        saleProductIndevicualAdapter = new SaleProductIndevicualAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.PDFRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }
                sum = 0;
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("totalPrice") != null) {
                        double totaltest  = (double) doc.get("totalPrice");
                        sum += totaltest;
                    }
                }
                SubTottal.setText(sum+"");

                double setTotaltaka = Double.parseDouble(takacutomerup);
                double setPymenttaka = Double.parseDouble(paymonysendup);

                double calculate1= setTotaltaka + sum;
                double calculate = calculate1 - setPymenttaka;

                dautaka.setText(setTotaltaka+"");
                PDFpayment.setText(paymonysendup);
                Total.setText(calculate+"");

            }
        });

        recyclerView.setAdapter(saleProductIndevicualAdapter);


    }
    private void UnkownCustumerrecyclearinvoiser() {

        final CollectionReference unkonwnCustomar = FirebaseFirestore.getInstance()
                .collection("users").document(user_id).collection("UnknownCustomer").document(unkcutomarId).collection("saleProduct");

        Query query = unkonwnCustomar.whereEqualTo("update",false).whereEqualTo("paid",true);

        FirestoreRecyclerOptions<SaleProductCutomerNote> options = new FirestoreRecyclerOptions.Builder<SaleProductCutomerNote>()
                .setQuery(query, SaleProductCutomerNote.class)
                .build();

        saleProductIndevicualAdapter = new SaleProductIndevicualAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.PDFRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {

                    return;
                }
                sum = 0;
                assert queryDocumentSnapshots != null;
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("totalPrice") != null) {
                        double totaltest  = (double) doc.get("totalPrice");
                        sum += totaltest;
                    }
                }
                SubTottal.setText(sum+"");

                double setTotaltaka = Double.parseDouble(totaltaka);
                double setPymenttaka = Double.parseDouble(paymonysendup);

                double calculate1= setTotaltaka + sum;

                double calculate = calculate1 - setPymenttaka;

                dautaka.setText(setTotaltaka+"");
                PDFpayment.setText(paymonysendup);
                Total.setText(calculate+"");

            }
        });

        recyclerView.setAdapter(saleProductIndevicualAdapter);
    }


    public void saveCustomerupdateData(ArrayList list) {

        // Get a new write batch
        WriteBatch batch = db.batch();

        // Iterate through the list
        for (int k = 0; k < list.size(); k++) {

            // Update each list item
            DocumentReference ref = db.collection("users").document(user_id).collection("Customers")
                    .document(id).collection("saleProduct").document((String) list.get(k));


            batch.update(ref, "update",true);

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
        // Get a new write batch
        WriteBatch batch = db.batch();

        // Iterate through the list
        for (int k = 0; k < list.size(); k++) {


            // Update each list item
            DocumentReference ref = db.collection("users").document(user_id).collection("UnknownCustomer")
                    .document(unkcutomarId).collection("saleProduct").document((String) list.get(k));

            batch.update(ref, "update",true);

        }

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Yay its all done in one go!
            }
        });

    }

    public Paragraph fontCoveter(String name ){

        Paragraph paragraph ;

        String pattern = "^[A-Za-z0-9. ]+$";
        if (name.matches(pattern)){
            paragraph = new Paragraph(name);

        }else {


            BaseFont bf = null;
            try {
                bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            } catch (IOException | DocumentException e) {
                e.printStackTrace();
            }

            Font font1 = new Font(bf, 10, Font.NORMAL);



            paragraph = new Paragraph(name, font1);
        }

        return paragraph;
    }

}
