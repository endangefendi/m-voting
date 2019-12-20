package com.fend.m_voting.Hasil;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.fend.m_voting.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class Hasil extends  AppCompatActivity {
    private BarChart mBarChart;
    BarData barData;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hasil);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        mBarChart = findViewById(R.id.chart);

        // Pengaturan sumbu X
        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(false);

        // Agar ketika di zoom tidak menjadi pecahan
        xAxis.setGranularity(1f);

        // Diubah menjadi integer, kemudian dijadikan String
        // Ini berfungsi untuk menghilankan koma, dan tanda ribuah pada tahun
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);

            }
        });

        //Menghilangkan sumbu Y yang ada di sebelah kanan
        mBarChart.getAxisRight().setEnabled(false);

        // Menghilankan deskripsi pada Chart
        mBarChart.getDescription().setEnabled(false);


        // Data-data yang akan ditampilkan di Chart
        List<BarEntry> hasilVote = new ArrayList<BarEntry>();
        hasilVote.add(new BarEntry(1, 90));
        hasilVote.add(new BarEntry(2, 10));
        hasilVote.add(new BarEntry(3, 17));


        // Pengaturan atribut bar, seperti warna dan lain-lain
        BarDataSet dataSet = new BarDataSet(hasilVote, "Hasil Vote");
        dataSet.setColor(ColorTemplate.JOYFUL_COLORS[0]);


        // Membuat Bar data yang akan di set ke Chart
        barData = new BarData(dataSet);
        mBarChart.setData(barData);
        progressDialog.dismiss();
    }

}
