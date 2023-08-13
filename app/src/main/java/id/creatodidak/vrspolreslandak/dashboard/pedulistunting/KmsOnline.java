package id.creatodidak.vrspolreslandak.dashboard.pedulistunting;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;
import id.creatodidak.vrspolreslandak.api.ClientStunting;
import id.creatodidak.vrspolreslandak.api.EndpointStunting;
import id.creatodidak.vrspolreslandak.api.models.stunting.DataKms;
import id.creatodidak.vrspolreslandak.api.models.stunting.DataperkembanganItem;
import id.creatodidak.vrspolreslandak.api.models.stunting.MbbtbItem;
import id.creatodidak.vrspolreslandak.api.models.stunting.MbbuItem;
import id.creatodidak.vrspolreslandak.api.models.stunting.MimtuItem;
import id.creatodidak.vrspolreslandak.api.models.stunting.MtbuItem;
import id.creatodidak.vrspolreslandak.helper.PdfExportUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KmsOnline extends AppCompatActivity {
    LineChart lcimtu, lcbbu, lctbu, lcbbtb;
    EndpointStunting endpointStunting;
    ProgressBar pbload, pbsearch;
    ImageView ivSearch;
    EditText etNik;
    TextView nodata;
    CardView btSearch;
    ScrollView grafikKms;
    LinearLayout charts;
    Button pdf;

    LinearLayout grafikimtu, grafiktbu, grafikbbu, grafikbbtb;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kms_online);
        lcimtu = findViewById(R.id.lcimtu);
        lcbbtb = findViewById(R.id.lcbbtb);
        lcbbu = findViewById(R.id.lcbbu);
        lctbu = findViewById(R.id.lctbu);
        endpointStunting = ClientStunting.getClient().create(EndpointStunting.class);
        etNik = findViewById(R.id.etSearchNik);
        pbload = findViewById(R.id.pbLoadDataKms);
        pbsearch = findViewById(R.id.pbSearch);
        ivSearch = findViewById(R.id.icSearch);
        nodata = findViewById(R.id.noDataKms);
        btSearch = findViewById(R.id.btSearch);
        grafikKms = findViewById(R.id.grafikKms);
        pdf = findViewById(R.id.btexportPdf);
        grafikimtu = findViewById(R.id.grafikimtu);
        grafiktbu = findViewById(R.id.grafiktbu);
        grafikbbu = findViewById(R.id.grafikbbu);
        grafikbbtb = findViewById(R.id.grafikbbtb);
        charts = findViewById(R.id.Chart);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNik.getText().toString().length() == 16){
                    ivSearch.setVisibility(View.GONE);
                    pbsearch.setVisibility(View.VISIBLE);
                    pbload.setVisibility(View.VISIBLE);
                    nodata.setVisibility(View.GONE);
                    loadData(etNik.getText().toString());
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(KmsOnline.this);
                    builder.setTitle("Periksa kembali data anda!")
                            .setMessage("NIK HARUS 16 DIGIT!")
                            .setPositiveButton("PERBAIKI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            })
                            .setIcon(R.drawable.icon)
                            .setCancelable(false);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
        checkAndRequestPermission();

    }

    private void loadData(String nik) {
        Call<DataKms> call = endpointStunting.getKms(nik);
        call.enqueue(new Callback<DataKms>() {
            @Override
            public void onResponse(Call<DataKms> call, Response<DataKms> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().isFound()) {
                        pdf.setVisibility(View.VISIBLE);
                        pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                exportPdf(charts, response.body().getNama());
                            }
                        });
                        grafikKms.setVisibility(View.VISIBLE);
                        etNik.setBackgroundResource(R.drawable.tvbordergreen);
                        ivSearch.setVisibility(View.VISIBLE);
                        pbsearch.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        pbload.setVisibility(View.GONE);

                        List<MbbtbItem> Mbbtb = response.body().getMbbtb();
                        List<MbbuItem> Mbbu = response.body().getMbbu();
                        List<MimtuItem> Mimtu = response.body().getMimtu();
                        List<MtbuItem> Mtbu = response.body().getMtbu();
                        List<DataperkembanganItem> datakembang = response.body().getDataperkembangan();

                        populateImtuChart(Mimtu, datakembang, response.body().getNama());
                        populateBbuChart(Mbbu, datakembang, response.body().getNama());
                        populateTbuChart(Mtbu, datakembang, response.body().getNama());
                        populateBbtbChart(Mbbtb, datakembang, response.body().getNama());

                    } else {
                        etNik.setBackgroundResource(R.drawable.tvborderred);
                        ivSearch.setVisibility(View.VISIBLE);
                        pbsearch.setVisibility(View.GONE);
                        pbload.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                        grafikKms.setVisibility(View.GONE);
                        pdf.setVisibility(View.GONE);

                    }
                }else{
                    etNik.setBackgroundResource(R.drawable.tvborderred);
                    ivSearch.setVisibility(View.VISIBLE);
                    pbsearch.setVisibility(View.GONE);
                    pbload.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                    nodata.setText("JARINGAN ERROR!");
                    grafikKms.setVisibility(View.GONE);
                    pdf.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<DataKms> call, Throwable t) {
                etNik.setBackgroundResource(R.drawable.tvborderred);
                ivSearch.setVisibility(View.VISIBLE);
                pbsearch.setVisibility(View.GONE);
                pbload.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                pdf.setVisibility(View.GONE);

            }
        });
    }

    private void populateImtuChart(List<MimtuItem> mimtuList, List<DataperkembanganItem> datakembang, String nama) {
        YAxis yAxis = lcimtu.getAxisLeft(); // Replace with the appropriate chart object
        yAxis.setAxisMinimum(-10);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);

        lcimtu.setBackgroundColor(Color.parseColor("#ffffff"));
        Description description = new Description();
        description.setText("KMS IMT/U | "+nama);
        lcimtu.setDescription(description);

        XAxis xAxis = lcimtu.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Mengatur posisi garis di bawah sumbu
        xAxis.setDrawAxisLine(true); // Menggambar garis sumbu X
        xAxis.setDrawGridLines(false); // Menghilangkan garis-garis kisi sumbu X

        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        List<Entry> entries3 = new ArrayList<>();
        List<Entry> entries4 = new ArrayList<>();
        List<Entry> entries5 = new ArrayList<>();
        List<Entry> entries6 = new ArrayList<>();
        List<Entry> entries7 = new ArrayList<>();
        List<Entry> entries8 = new ArrayList<>();


        for (int i = 0; i < mimtuList.size(); i++) {
            MimtuItem item = mimtuList.get(i);
            entries1.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMin3())));
            entries2.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMin2())));
            entries3.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMin1())));
            entries4.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMed())));
            entries5.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getPls1())));
            entries6.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getPls2())));
            entries7.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getPls3())));
        }

        if(datakembang.size() == 1) {
            entries8.add(new Entry(Float.parseFloat(String.valueOf(datakembang.get(0).getUmur())), Float.parseFloat(String.valueOf(datakembang.get(0).getImtu()))));

            for (int i = 0; i < datakembang.size(); i++) {
                DataperkembanganItem item = datakembang.get(i);
                entries8.add(new Entry(Float.parseFloat(String.valueOf(item.getUmur())), Float.parseFloat(String.valueOf(item.getImtu()))));
            }
        }else {
            for (int i = 0; i < datakembang.size(); i++) {
                DataperkembanganItem item = datakembang.get(i);
                entries8.add(new Entry(Float.parseFloat(String.valueOf(item.getUmur())), Float.parseFloat(String.valueOf(item.getImtu()))));
            }
        }



        LineDataSet dataSet1 = new LineDataSet(entries1, "");
        LineDataSet dataSet2 = new LineDataSet(entries2, "");
        LineDataSet dataSet3 = new LineDataSet(entries3, "");
        LineDataSet dataSet4 = new LineDataSet(entries4, "");
        LineDataSet dataSet5 = new LineDataSet(entries5, "");
        LineDataSet dataSet6 = new LineDataSet(entries6, "");
        LineDataSet dataSet7 = new LineDataSet(entries7, "");
        LineDataSet dataSet8 = new LineDataSet(entries8, "TIMELINE PERKEMBANGAN ANAK");
//        dataSet8.setAxisDependency(YAxis.AxisDependency.LEFT);

        Legend l = lcimtu.getLegend();
        l.setDrawInside(false);
        l.setEnabled(false);


        dataSet1.setDrawFilled(true); // Don't fill dataSet1
        dataSet2.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet3.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet4.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet5.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet6.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet7.setDrawFilled(true); // Don't fill dataSet7

        dataSet1.setFillColor(Color.argb(255, 255, 255, 255));   // #dac107
        dataSet2.setFillColor(Color.argb(255, 218, 193, 7));  // #7db128
        dataSet3.setFillColor(Color.argb(255, 125, 177, 40));   // #2d9f38
        dataSet4.setFillColor(Color.argb(255, 45, 159, 56));   // #2d9f38
        dataSet5.setFillColor(Color.argb(255, 45, 159, 56));  // #7db128
        dataSet6.setFillColor(Color.argb(255, 125, 177, 40));   // #dac107
        dataSet7.setFillColor(Color.argb(255, 218, 193, 7)); // #ffffff

        dataSet1.setFillAlpha(255);
        dataSet2.setFillAlpha(255);
        dataSet3.setFillAlpha(255);
        dataSet4.setFillAlpha(255);
        dataSet5.setFillAlpha(255);
        dataSet6.setFillAlpha(255);
        dataSet7.setFillAlpha(255);

        dataSet1.setDrawCircles(false);
        dataSet2.setDrawCircles(false);
        dataSet3.setDrawCircles(false);
        dataSet4.setDrawCircles(false);
        dataSet5.setDrawCircles(false);
        dataSet6.setDrawCircles(false);
        dataSet7.setDrawCircles(false);
        dataSet8.setDrawCircles(true);

        dataSet1.setColor(Color.argb(0, 0, 0, 0));
        dataSet2.setColor(Color.argb(0, 0, 0, 0));
        dataSet3.setColor(Color.argb(0, 0, 0, 0));
        dataSet4.setColor(Color.argb(0, 0, 0, 0));
        dataSet5.setColor(Color.argb(0, 0, 0, 0));
        dataSet6.setColor(Color.argb(0, 0, 0, 0));
        dataSet7.setColor(Color.argb(0, 0, 0, 0));
        dataSet8.setColor(Color.parseColor("#ff0000"));

        dataSet1.setDrawValues(false);
        dataSet2.setDrawValues(false);
        dataSet3.setDrawValues(false);
        dataSet4.setDrawValues(false);
        dataSet5.setDrawValues(false);
        dataSet6.setDrawValues(false);
        dataSet7.setDrawValues(false);
        dataSet8.setDrawValues(true);
        dataSet8.setLineWidth(2);
        dataSet8.enableDashedLine(10, 10, 0);
        dataSet8.setMode(LineDataSet.Mode.LINEAR);
        dataSet8.setCircleRadius(3);
        dataSet8.setCircleColor(Color.parseColor("#ff0000"));

        LineData lineData = new LineData(dataSet7, dataSet6, dataSet5, dataSet4, dataSet3, dataSet2, dataSet1, dataSet8);



        lcimtu.setData(lineData);

        lcimtu.invalidate();
    }

    private void populateBbuChart(List<MbbuItem> mbbuList, List<DataperkembanganItem> datakembang, String nama) {
        YAxis yAxis = lcbbu.getAxisLeft(); // Replace with the appropriate chart object
        yAxis.setAxisMinimum(-10);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);

        lcbbu.setBackgroundColor(Color.parseColor("#ffffff"));
        Description description = new Description();
        description.setText("KMS BB/U | "+nama);
        lcbbu.setDescription(description);

        XAxis xAxis = lcbbu.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Mengatur posisi garis di bawah sumbu
        xAxis.setDrawAxisLine(true); // Menggambar garis sumbu X
        xAxis.setDrawGridLines(false); // Menghilangkan garis-garis kisi sumbu X

        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        List<Entry> entries3 = new ArrayList<>();
        List<Entry> entries4 = new ArrayList<>();
        List<Entry> entries5 = new ArrayList<>();
        List<Entry> entries6 = new ArrayList<>();
        List<Entry> entries7 = new ArrayList<>();
        List<Entry> entries8 = new ArrayList<>();


        for (int i = 0; i < mbbuList.size(); i++) {
            MbbuItem item = mbbuList.get(i);
            entries1.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMin3())));
            entries2.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMin2())));
            entries3.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMin1())));
            entries4.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMed())));
            entries5.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getPls1())));
            entries6.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getPls2())));
            entries7.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getPls3())));
        }

        if(datakembang.size() == 1) {
            entries8.add(new Entry(Float.parseFloat(String.valueOf(datakembang.get(0).getUmur())), Float.parseFloat(String.valueOf(datakembang.get(0).getBbu()))));

            for (int i = 0; i < datakembang.size(); i++) {
                DataperkembanganItem item = datakembang.get(i);
                entries8.add(new Entry(Float.parseFloat(String.valueOf(item.getUmur())), Float.parseFloat(String.valueOf(item.getBbu()))));
            }
        }else {
            for (int i = 0; i < datakembang.size(); i++) {
                DataperkembanganItem item = datakembang.get(i);
                entries8.add(new Entry(Float.parseFloat(String.valueOf(item.getUmur())), Float.parseFloat(String.valueOf(item.getBbu()))));
            }
        }


        LineDataSet dataSet1 = new LineDataSet(entries1, "");
        LineDataSet dataSet2 = new LineDataSet(entries2, "");
        LineDataSet dataSet3 = new LineDataSet(entries3, "");
        LineDataSet dataSet4 = new LineDataSet(entries4, "");
        LineDataSet dataSet5 = new LineDataSet(entries5, "");
        LineDataSet dataSet6 = new LineDataSet(entries6, "");
        LineDataSet dataSet7 = new LineDataSet(entries7, "");
        LineDataSet dataSet8 = new LineDataSet(entries8, "TIMELINE PERKEMBANGAN ANAK");
//        dataSet8.setAxisDependency(YAxis.AxisDependency.LEFT);

        Legend l = lcbbu.getLegend();
        l.setDrawInside(false);
        l.setEnabled(false);


        dataSet1.setDrawFilled(true); // Don't fill dataSet1
        dataSet2.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet3.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet4.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet5.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet6.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet7.setDrawFilled(true); // Don't fill dataSet7

        dataSet1.setFillColor(Color.argb(255, 255, 255, 255));   // #dac107
        dataSet2.setFillColor(Color.argb(255, 218, 193, 7));  // #7db128
        dataSet3.setFillColor(Color.argb(255, 125, 177, 40));   // #2d9f38
        dataSet4.setFillColor(Color.argb(255, 45, 159, 56));   // #2d9f38
        dataSet5.setFillColor(Color.argb(255, 45, 159, 56));  // #7db128
        dataSet6.setFillColor(Color.argb(255, 125, 177, 40));   // #dac107
        dataSet7.setFillColor(Color.argb(255, 218, 193, 7)); // #ffffff

        dataSet1.setFillAlpha(255);
        dataSet2.setFillAlpha(255);
        dataSet3.setFillAlpha(255);
        dataSet4.setFillAlpha(255);
        dataSet5.setFillAlpha(255);
        dataSet6.setFillAlpha(255);
        dataSet7.setFillAlpha(255);

        dataSet1.setDrawCircles(false);
        dataSet2.setDrawCircles(false);
        dataSet3.setDrawCircles(false);
        dataSet4.setDrawCircles(false);
        dataSet5.setDrawCircles(false);
        dataSet6.setDrawCircles(false);
        dataSet7.setDrawCircles(false);
        dataSet8.setDrawCircles(true);

        dataSet1.setColor(Color.argb(0, 0, 0, 0));
        dataSet2.setColor(Color.argb(0, 0, 0, 0));
        dataSet3.setColor(Color.argb(0, 0, 0, 0));
        dataSet4.setColor(Color.argb(0, 0, 0, 0));
        dataSet5.setColor(Color.argb(0, 0, 0, 0));
        dataSet6.setColor(Color.argb(0, 0, 0, 0));
        dataSet7.setColor(Color.argb(0, 0, 0, 0));
        dataSet8.setColor(Color.parseColor("#ff0000"));

        dataSet1.setDrawValues(false);
        dataSet2.setDrawValues(false);
        dataSet3.setDrawValues(false);
        dataSet4.setDrawValues(false);
        dataSet5.setDrawValues(false);
        dataSet6.setDrawValues(false);
        dataSet7.setDrawValues(false);
        dataSet8.setDrawValues(true);
        dataSet8.setLineWidth(2);
        dataSet8.enableDashedLine(10, 10, 0);
        dataSet8.setMode(LineDataSet.Mode.LINEAR);
        dataSet8.setCircleRadius(3);
        dataSet8.setCircleColor(Color.parseColor("#ff0000"));

        LineData lineData = new LineData(dataSet7, dataSet6, dataSet5, dataSet4, dataSet3, dataSet2, dataSet1, dataSet8);



        lcbbu.setData(lineData);

        lcbbu.invalidate();
    }

    private void populateTbuChart(List<MtbuItem> mtbuList, List<DataperkembanganItem> datakembang, String nama) {
        YAxis yAxis = lctbu.getAxisLeft(); // Replace with the appropriate chart object
        yAxis.setAxisMinimum(-10);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);

        lctbu.setBackgroundColor(Color.parseColor("#ffffff"));
        Description description = new Description();
        description.setText("KMS TB/U | "+nama);
        lctbu.setDescription(description);

        XAxis xAxis = lctbu.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Mengatur posisi garis di bawah sumbu
        xAxis.setDrawAxisLine(true); // Menggambar garis sumbu X
        xAxis.setDrawGridLines(false); // Menghilangkan garis-garis kisi sumbu X

        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        List<Entry> entries3 = new ArrayList<>();
        List<Entry> entries4 = new ArrayList<>();
        List<Entry> entries5 = new ArrayList<>();
        List<Entry> entries6 = new ArrayList<>();
        List<Entry> entries7 = new ArrayList<>();
        List<Entry> entries8 = new ArrayList<>();


        for (int i = 0; i < mtbuList.size(); i++) {
            MtbuItem item = mtbuList.get(i);
            entries1.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMin3())));
            entries2.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMin2())));
            entries3.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMin1())));
            entries4.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getMed())));
            entries5.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getPls1())));
            entries6.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getPls2())));
            entries7.add(new Entry(Float.parseFloat(item.getUmur()), Float.parseFloat(item.getPls3())));
        }

        if(datakembang.size() == 1) {
            entries8.add(new Entry(Float.parseFloat(String.valueOf(datakembang.get(0).getUmur())), Float.parseFloat(String.valueOf(datakembang.get(0).getTbu()))));

            for (int i = 0; i < datakembang.size(); i++) {
                DataperkembanganItem item = datakembang.get(i);
                entries8.add(new Entry(Float.parseFloat(String.valueOf(item.getUmur())), Float.parseFloat(String.valueOf(item.getTbu()))));
            }
        }else {
            for (int i = 0; i < datakembang.size(); i++) {
                DataperkembanganItem item = datakembang.get(i);
                entries8.add(new Entry(Float.parseFloat(String.valueOf(item.getUmur())), Float.parseFloat(String.valueOf(item.getTbu()))));
            }
        }

        LineDataSet dataSet1 = new LineDataSet(entries1, "");
        LineDataSet dataSet2 = new LineDataSet(entries2, "");
        LineDataSet dataSet3 = new LineDataSet(entries3, "");
        LineDataSet dataSet4 = new LineDataSet(entries4, "");
        LineDataSet dataSet5 = new LineDataSet(entries5, "");
        LineDataSet dataSet6 = new LineDataSet(entries6, "");
        LineDataSet dataSet7 = new LineDataSet(entries7, "");
        LineDataSet dataSet8 = new LineDataSet(entries8, "TIMELINE PERKEMBANGAN ANAK");
//        dataSet8.setAxisDependency(YAxis.AxisDependency.LEFT);

        Legend l = lctbu.getLegend();
        l.setDrawInside(false);
        l.setEnabled(false);


        dataSet1.setDrawFilled(true); // Don't fill dataSet1
        dataSet2.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet3.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet4.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet5.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet6.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet7.setDrawFilled(true); // Don't fill dataSet7

        dataSet1.setFillColor(Color.argb(255, 255, 255, 255));   // #dac107
        dataSet2.setFillColor(Color.argb(255, 218, 193, 7));  // #7db128
        dataSet3.setFillColor(Color.argb(255, 125, 177, 40));   // #2d9f38
        dataSet4.setFillColor(Color.argb(255, 45, 159, 56));   // #2d9f38
        dataSet5.setFillColor(Color.argb(255, 45, 159, 56));  // #7db128
        dataSet6.setFillColor(Color.argb(255, 125, 177, 40));   // #dac107
        dataSet7.setFillColor(Color.argb(255, 218, 193, 7)); // #ffffff

        dataSet1.setFillAlpha(255);
        dataSet2.setFillAlpha(255);
        dataSet3.setFillAlpha(255);
        dataSet4.setFillAlpha(255);
        dataSet5.setFillAlpha(255);
        dataSet6.setFillAlpha(255);
        dataSet7.setFillAlpha(255);

        dataSet1.setDrawCircles(false);
        dataSet2.setDrawCircles(false);
        dataSet3.setDrawCircles(false);
        dataSet4.setDrawCircles(false);
        dataSet5.setDrawCircles(false);
        dataSet6.setDrawCircles(false);
        dataSet7.setDrawCircles(false);
        dataSet8.setDrawCircles(true);

        dataSet1.setColor(Color.argb(0, 0, 0, 0));
        dataSet2.setColor(Color.argb(0, 0, 0, 0));
        dataSet3.setColor(Color.argb(0, 0, 0, 0));
        dataSet4.setColor(Color.argb(0, 0, 0, 0));
        dataSet5.setColor(Color.argb(0, 0, 0, 0));
        dataSet6.setColor(Color.argb(0, 0, 0, 0));
        dataSet7.setColor(Color.argb(0, 0, 0, 0));
        dataSet8.setColor(Color.parseColor("#ff0000"));

        dataSet1.setDrawValues(false);
        dataSet2.setDrawValues(false);
        dataSet3.setDrawValues(false);
        dataSet4.setDrawValues(false);
        dataSet5.setDrawValues(false);
        dataSet6.setDrawValues(false);
        dataSet7.setDrawValues(false);
        dataSet8.setDrawValues(true);
        dataSet8.setLineWidth(2);
        dataSet8.enableDashedLine(10, 10, 0);
        dataSet8.setMode(LineDataSet.Mode.LINEAR);
        dataSet8.setCircleRadius(3);
        dataSet8.setCircleColor(Color.parseColor("#ff0000"));

        LineData lineData = new LineData(dataSet7, dataSet6, dataSet5, dataSet4, dataSet3, dataSet2, dataSet1, dataSet8);



        lctbu.setData(lineData);

        lctbu.invalidate();
    }

    private void populateBbtbChart(List<MbbtbItem> mbbtbList, List<DataperkembanganItem> datakembang, String nama) {
        YAxis yAxis = lcbbtb.getAxisLeft(); // Replace with the appropriate chart object
        yAxis.setAxisMinimum(-10);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(false);

        lcbbtb.setBackgroundColor(Color.parseColor("#ffffff"));
        Description description = new Description();
        description.setText("KMS BB/TB | "+nama);
        lcbbtb.setDescription(description);

        XAxis xAxis = lcbbtb.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Mengatur posisi garis di bawah sumbu
        xAxis.setDrawAxisLine(true); // Menggambar garis sumbu X
        xAxis.setDrawGridLines(false); // Menghilangkan garis-garis kisi sumbu X

        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        List<Entry> entries3 = new ArrayList<>();
        List<Entry> entries4 = new ArrayList<>();
        List<Entry> entries5 = new ArrayList<>();
        List<Entry> entries6 = new ArrayList<>();
        List<Entry> entries7 = new ArrayList<>();
        List<Entry> entries8 = new ArrayList<>();


        for (int i = 0; i < mbbtbList.size(); i++) {
            MbbtbItem item = mbbtbList.get(i);
            entries1.add(new Entry(Float.parseFloat(item.getTinggi()), Float.parseFloat(item.getMin3())));
            entries2.add(new Entry(Float.parseFloat(item.getTinggi()), Float.parseFloat(item.getMin2())));
            entries3.add(new Entry(Float.parseFloat(item.getTinggi()), Float.parseFloat(item.getMin1())));
            entries4.add(new Entry(Float.parseFloat(item.getTinggi()), Float.parseFloat(item.getMed())));
            entries5.add(new Entry(Float.parseFloat(item.getTinggi()), Float.parseFloat(item.getPls1())));
            entries6.add(new Entry(Float.parseFloat(item.getTinggi()), Float.parseFloat(item.getPls2())));
            entries7.add(new Entry(Float.parseFloat(item.getTinggi()), Float.parseFloat(item.getPls3())));
        }

        if(datakembang.size() == 1) {
            entries8.add(new Entry(Float.parseFloat(String.valueOf(datakembang.get(0).getTb())), Float.parseFloat(String.valueOf(datakembang.get(0).getBbtb()))));

            for (int i = 0; i < datakembang.size(); i++) {
                DataperkembanganItem item = datakembang.get(i);
                entries8.add(new Entry(Float.parseFloat(String.valueOf(item.getTb())), Float.parseFloat(String.valueOf(item.getBbtb()))));
            }
        }else {
            for (int i = 0; i < datakembang.size(); i++) {
                DataperkembanganItem item = datakembang.get(i);
                entries8.add(new Entry(Float.parseFloat(String.valueOf(item.getTb())), Float.parseFloat(String.valueOf(item.getBbtb()))));
            }
        }

        LineDataSet dataSet1 = new LineDataSet(entries1, "");
        LineDataSet dataSet2 = new LineDataSet(entries2, "");
        LineDataSet dataSet3 = new LineDataSet(entries3, "");
        LineDataSet dataSet4 = new LineDataSet(entries4, "");
        LineDataSet dataSet5 = new LineDataSet(entries5, "");
        LineDataSet dataSet6 = new LineDataSet(entries6, "");
        LineDataSet dataSet7 = new LineDataSet(entries7, "");
        LineDataSet dataSet8 = new LineDataSet(entries8, "TIMELINE PERKEMBANGAN ANAK");
//        dataSet8.setAxisDependency(YAxis.AxisDependency.LEFT);

        Legend l = lcbbtb.getLegend();
        l.setDrawInside(false);
        l.setEnabled(false);


        dataSet1.setDrawFilled(true); // Don't fill dataSet1
        dataSet2.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet3.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet4.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet5.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet6.setDrawFilled(true); // Fill between dataSet1 and dataSet7
        dataSet7.setDrawFilled(true); // Don't fill dataSet7

        dataSet1.setFillColor(Color.argb(255, 255, 255, 255));   // #dac107
        dataSet2.setFillColor(Color.argb(255, 218, 193, 7));  // #7db128
        dataSet3.setFillColor(Color.argb(255, 125, 177, 40));   // #2d9f38
        dataSet4.setFillColor(Color.argb(255, 45, 159, 56));   // #2d9f38
        dataSet5.setFillColor(Color.argb(255, 45, 159, 56));  // #7db128
        dataSet6.setFillColor(Color.argb(255, 125, 177, 40));   // #dac107
        dataSet7.setFillColor(Color.argb(255, 218, 193, 7)); // #ffffff

        dataSet1.setFillAlpha(255);
        dataSet2.setFillAlpha(255);
        dataSet3.setFillAlpha(255);
        dataSet4.setFillAlpha(255);
        dataSet5.setFillAlpha(255);
        dataSet6.setFillAlpha(255);
        dataSet7.setFillAlpha(255);

        dataSet1.setDrawCircles(false);
        dataSet2.setDrawCircles(false);
        dataSet3.setDrawCircles(false);
        dataSet4.setDrawCircles(false);
        dataSet5.setDrawCircles(false);
        dataSet6.setDrawCircles(false);
        dataSet7.setDrawCircles(false);
        dataSet8.setDrawCircles(true);

        dataSet1.setColor(Color.argb(0, 0, 0, 0));
        dataSet2.setColor(Color.argb(0, 0, 0, 0));
        dataSet3.setColor(Color.argb(0, 0, 0, 0));
        dataSet4.setColor(Color.argb(0, 0, 0, 0));
        dataSet5.setColor(Color.argb(0, 0, 0, 0));
        dataSet6.setColor(Color.argb(0, 0, 0, 0));
        dataSet7.setColor(Color.argb(0, 0, 0, 0));
        dataSet8.setColor(Color.parseColor("#ff0000"));

        dataSet1.setDrawValues(false);
        dataSet2.setDrawValues(false);
        dataSet3.setDrawValues(false);
        dataSet4.setDrawValues(false);
        dataSet5.setDrawValues(false);
        dataSet6.setDrawValues(false);
        dataSet7.setDrawValues(false);
        dataSet8.setDrawValues(true);
        dataSet8.setLineWidth(2);
        dataSet8.enableDashedLine(10, 10, 0);
        dataSet8.setMode(LineDataSet.Mode.LINEAR);
        dataSet8.setCircleRadius(3);
        dataSet8.setCircleColor(Color.parseColor("#ff0000"));

        LineData lineData = new LineData(dataSet7, dataSet6, dataSet5, dataSet4, dataSet3, dataSet2, dataSet1, dataSet8);



        lcbbtb.setData(lineData);

        lcbbtb.invalidate();
    }

    private void exportPdf(LinearLayout layout, String nama){
        String pdfFileName = "KMS "+nama+".pdf"; // Nama berkas PDF yang dihasilkan
        PdfExportUtil.exportScrollViewToPdf(this, grafikKms, pdfFileName, grafikimtu, grafiktbu, grafikbbu, grafikbbtb);

//        PdfExportUtil.exportViewToPdf(this, layout, pdfFileName);
//        PdfExportUtil.exportChartsToSeparatePages(this,layout, pdfFileName);
    }


    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // Permission already granted, proceed with exporting
//            exportPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                exportPdf();
            } else {
                // Permission denied, handle accordingly
            }
        }
    }
}
