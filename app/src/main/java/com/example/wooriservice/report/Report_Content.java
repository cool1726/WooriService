package com.example.wooriservice.report;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.wooriservice.MainActivity;
import com.example.wooriservice.R;

import com.example.wooriservice.calendars.CalendarAdapter;
import com.example.wooriservice.calendars.Calendars;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Report_Content extends AppCompatActivity {
    private LineChart linechart;
    private BarChart barchart;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__content);

        drawLinechart();
        drawBarchart();
        drawPiechart();



    }

    public void drawLinechart(){

        linechart = findViewById(R.id.moneyimage);

        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            float val = (float) (Math.random() * 10);
            float val2 = (float) (Math.random()*10);
            values.add(new Entry(i, val));
            values2.add(new Entry(i, val2));
        }

        LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");
        LineDataSet set2;
        set2 = new LineDataSet(values2, "DataSet 2");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets
        dataSets.add(set2);

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        XAxis xAxis = linechart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);// X축 줄의 컬러 설정


        YAxis yAxisLeft = linechart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        //Y축의 왼쪽면 설정

        YAxis yAxisRight = linechart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);


        // black lines and points
        linechart.setBackgroundColor(Color.WHITE);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set2.setColor(Color.BLUE);
        set2.setCircleColor(Color.BLUE);
        linechart.getLegend().setEnabled(false);

        // set data
        linechart.setData(data);

    }
    public void drawBarchart(){

        barchart = findViewById(R.id.catimage);

        ArrayList values = new ArrayList();
        ArrayList values2 = new ArrayList();

        for (int i = 0; i < 10; i++) {

            int val = (int) (Math.random() * 10);
            values.add(new BarEntry(val, i));
            values2.add(i);

        }


        BarDataSet set1 = new BarDataSet(values, "DataSet 1");
        set1.setColors(ColorTemplate.PASTEL_COLORS);
        BarData data = new BarData(set1);

        XAxis xAxis = barchart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);// X축 줄의 컬러 설정


        YAxis yAxisLeft = barchart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawAxisLine(false);
        //Y축의 왼쪽면 설정

        YAxis yAxisRight = barchart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);




        // set data
        barchart.getLegend().setEnabled(false);
        barchart.setData(data);

    }
    public void drawPiechart(){

        pieChart = findViewById(R.id.weekimage);

        ArrayList values = new ArrayList();
        ArrayList values2 = new ArrayList();

        for (int i = 0; i < 10; i++) {

            int val = (int) (Math.random() * 10);
            values.add(new PieEntry(val, i));
            values2.add(i);

        }


        PieDataSet set1 = new PieDataSet(values, "DataSet 1");
        set1.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(set1);

        // set data
        pieChart.getLegend().setEnabled(false);
        pieChart.setData(data);

    }

}