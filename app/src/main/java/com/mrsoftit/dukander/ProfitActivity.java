package com.mrsoftit.dukander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ProfitActivity extends AppCompatActivity {



    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    String user_id = Objects.requireNonNull(currentUser).getUid();

    PieChart pieChart;
    Button date;
    String selectetDate;

   ProgressDialog progressDialog;
    PieData pieData;
    PieDataSet pieDataSet;
    //total sale
    CollectionReference TotalcustomerProductSale = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("Sales");

    CollectionReference Profit = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("profit");


    final CollectionReference myInfo = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("DukanInfo");


    CollectionReference cashOutBalance = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("cashout");

    CollectionReference investmentgetacticBalance = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("investment");


    final CollectionReference dukandertakaUpdate = FirebaseFirestore.getInstance()
            .collection("users").document(user_id).collection("DukanInfo");




    Double totalsum = 0.0;
    Double ProfitSum = 0.0;
    Double totalpaybilint = 0.0;
    Double investment = 0.0;

    AnyChartView anyChartView;
    Pie pie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profit);

        date = findViewById(R.id.choose_month);

        pieChart = findViewById(R.id.pieChart);
        anyChartView = findViewById(R.id.anyChart);
        pie = AnyChart.pie();



        progressDialog = new ProgressDialog(ProfitActivity.this);
        // Setting Message
        progressDialog.setTitle("তথ্য প্রস্তুত হচ্ছে..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        Date calendar1 = Calendar.getInstance().getTime();
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        String todayString = df1.format(calendar1);
        final int datenew = Integer.parseInt(todayString);

        final List<DataEntry> data = new ArrayList<>();

        Query query = cashOutBalance;

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                        if (document.get("withdrow") != null) {
                            double totalpaybil  = (double) document.get("withdrow");

                            totalpaybilint += totalpaybil;

                        }

                    }

                    data.add(new ValueDataEntry("উত্তোলন", totalpaybilint));

                    TotalcustomerProductSale.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                    if (document.get("totalPrice") != null) {
                                        double totalsumS = (double) document.get("totalPrice");

                                        totalsum += totalsumS;

                                    }

                                }
                                data.add(new ValueDataEntry("মোট বিক্রয় ", totalsum));

                                Profit.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                                if (document.get("Profit") != null) {
                                                    double totalsumS = (double) document.get("Profit");

                                                    ProfitSum  += totalsumS;

                                                }

                                            }
                                            data.add(new ValueDataEntry("মোট লাভ ", ProfitSum));


                                            investmentgetacticBalance.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                                            if (document.get("invesment") != null) {
                                                                double totalsumS = (double) document.get("invesment");

                                                                investment += totalsumS;

                                                            }

                                                        }
                                                        data.add(new ValueDataEntry("বিনিয়োগ ", investment));

                                                        anyPichart( data);
                                                    }
                                                    }
                                            });




                                        }

                                    }
                                });


                            }

                        }
                    });

                }


            }


        });


        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(ProfitActivity.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });




        final Calendar today = Calendar.getInstance();
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date calendar1 = Calendar.getInstance().getTime();
                DateFormat df1 = new SimpleDateFormat("yyyy");
                String todayString = df1.format(calendar1);
                final int presentYear = Integer.parseInt(todayString);

                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ProfitActivity.this, new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {

                       int selectmonth = 1+ selectedMonth;

                       if (selectmonth<10){

                           String selectm = "0"+selectmonth;

                           selectetDate = selectm +""+ selectedYear;


                       }else {

                           selectetDate = selectedMonth +""+ selectedYear;
                       }

                        Toast.makeText(ProfitActivity.this,  selectetDate, Toast.LENGTH_SHORT).show();

                    }
                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setActivatedMonth(Calendar.JULY)
                        .setMinYear(1990)
                        .setActivatedYear(presentYear)
                        .setMaxYear(2050)
                        .setMinMonth(Calendar.JANUARY)
                        .setTitle("Select trading month")
                        .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                        // .setMaxMonth(Calendar.OCTOBER)
                        // .setYearRange(1890, 1890)
                        // .setMonthAndYearRange(Calendar.FEBRUARY, Calendar.OCTOBER, 1890, 1890)
                        //.showMonthOnly()
                        // .showYearOnly()
                        .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                            @Override
                            public void onMonthChanged(int selectedMonth) {

                            }
                        })
                        .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                            @Override
                            public void onYearChanged(int selectedYear) {

                                // Toast.makeText(MainActivity.this, " Selected year : " + selectedYear, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build()
                        .show();


            }
        });
    }



    public void anyPichart(List<DataEntry> data){

        pie.data(data);

        pie.title("আপনার দোকানের এক নজরে হিসাবনিকাশ ");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Retail channels")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
        anyChartView.setChart(pie);

        progressDialog.dismiss();
    }



    }
