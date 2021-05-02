package com.example.wooriservice.report;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.wooriservice.MainActivity;
import com.example.wooriservice.R;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Report_Content extends AppCompatActivity {
    private LineChart linechart;
    private BarChart barchart;
    private PieChart pieChart;
    static ArrayList<ArrayList<String>> translist = new ArrayList<>();
    static ArrayList<ArrayList<String>> report = new ArrayList<>();
    ArrayList<ArrayList<String>> reportlist1 = new ArrayList<>();
    ArrayList<ArrayList<String>> reportlist2 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report__content);

        settingData();

        try {
            drawLinechart();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            drawBarchart();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        drawPiechart();



    }

    public static void setTransList(ArrayList<ArrayList<String>> translists) {
        translist = translists;

    }
    public static void setReportList(ArrayList<ArrayList<String>> reports) {
        report = reports;

    }

    public  void settingData(){
        for(int i = 0; i<translist.size(); i++){
            if(translist.get(i).get(7).equals("9")){
                reportlist1.add(translist.get(i));
            }else if(translist.get(i).get(7).equals("10")){
                reportlist2.add(translist.get(i));
            }
        }
    }
    public static LinkedHashMap<String, Integer> sortMapByKey(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> entries = new LinkedList<>(map.entrySet());
        Collections.sort(entries, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));

        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public void drawLinechart() throws ParseException {

        linechart = findViewById(R.id.moneyimage);
//        Map<String, Integer> map1 = new HashMap<>();
//        Map<String, Integer> map2 = new HashMap<>();

        ArrayList<Entry> values = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();

        ArrayList<Integer> val = new ArrayList<>();
        ArrayList<Integer> val2 = new ArrayList<>();

        for(int a = 0; a <7; a++){
            val.add(0);
            val2.add(0);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(7);


        for (int i = 0; i < reportlist1.size(); i++) {

            Date nDate = dateFormat.parse(reportlist1.get(i).get(0));
            cal.setTime(nDate);

            int dayNum = cal.get(Calendar.DAY_OF_WEEK);
            int bug = val.get(dayNum -1);
            bug += Integer.parseInt(reportlist1.get(i).get(3));
            val.set(dayNum -1, bug);
        }
        for (int i = 0; i < reportlist2.size(); i++) {

            Date nDate = dateFormat.parse(reportlist2.get(i).get(0));
            cal.setTime(nDate);

            int dayNum = cal.get(Calendar.DAY_OF_WEEK);
            int bug = val.get(dayNum -1);
            bug += Integer.parseInt(reportlist2.get(i).get(3));
            val2.set(dayNum -1, bug);
        }
        int temp1 = 0;
        int temp2 = 0;
        for(int i = 0; i < 7; i++){
            temp1 += val.get(i);
            temp2 += val2.get(i);
            val.set(i, temp1);
            val2.set(i, temp2);
        }

        for(int b = 0; b <7; b++){
            values.add(new BarEntry(b, val.get(b)));
            values2.add(new BarEntry(b, val2.get(b)));
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
        data.setValueTextColor(Color.WHITE);

        ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("월");
        xAxisLabel.add("화");
        xAxisLabel.add("수");
        xAxisLabel.add("목");
        xAxisLabel.add("금");
        xAxisLabel.add("토");
        xAxisLabel.add("일");

        XAxis xAxis = linechart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);// X축 줄의 컬러 설정
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));


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
        set1.setColor(Color.GRAY);
        set1.setCircleColor(Color.GRAY);
        set2.setColor(Color.BLUE);
        set2.setCircleColor(Color.BLUE);
        linechart.getLegend().setEnabled(false);

        // set data
        linechart.setData(data);
        linechart.setDescription(null);

    }
    public void drawBarchart() throws ParseException {

        barchart = findViewById(R.id.catimage);

        ArrayList<Integer> val = new ArrayList<>();

        ArrayList values = new ArrayList();
        for(int a = 0; a <7; a++){
            val.add(0);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        for (int i = 0; i < reportlist1.size(); i++) {

            Date nDate = dateFormat.parse(reportlist1.get(i).get(0));

            Calendar cal = Calendar.getInstance();
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            cal.setMinimalDaysInFirstWeek(7);
            cal.setTime(nDate);

            int dayNum = cal.get(Calendar.DAY_OF_WEEK);
            int bug = val.get(dayNum -1);
            bug += Integer.parseInt(reportlist1.get(i).get(3));
            val.set(dayNum -1, bug);
        }

        for(int b = 0; b <7; b++){
            values.add(new BarEntry(b, val.get(b)));
        }
        ArrayList<String> xAxisLabel = new ArrayList<>();
        xAxisLabel.add("월");
        xAxisLabel.add("화");
        xAxisLabel.add("수");
        xAxisLabel.add("목");
        xAxisLabel.add("금");
        xAxisLabel.add("토");
        xAxisLabel.add("일");

        BarDataSet set1 = new BarDataSet(values, "DataSet 1");
        set1.setColors(ColorTemplate.PASTEL_COLORS);
        BarData data = new BarData(set1);

        XAxis xAxis = barchart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);// X축 줄의 컬러 설정
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));



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
        barchart.setDescription(null);
        barchart.setData(data);

    }
    public void drawPiechart(){

        pieChart = findViewById(R.id.weekimage);

        pieChart.setUsePercentValues(true);
        pieChart.setCenterTextSize(0);
        pieChart.setCenterTextColor(Color.GREEN);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);


        ArrayList values = new ArrayList();
        int ttl_amount = Integer.parseInt(report.get(0).get(2));
        String Maxct = report.get(0).get(4);
        int maxct_amount = Integer.parseInt(report.get(0).get(5));
        int shopping_amt = Integer.parseInt(report.get(0).get(10));
        int etc = Integer.parseInt(report.get(0).get(12));

        int amt = ttl_amount - maxct_amount -shopping_amt -etc;

        values.add(new PieEntry(maxct_amount, Maxct));
        values.add(new PieEntry(shopping_amt, " "));
        values.add(new PieEntry(etc, " "));
        values.add(new PieEntry(amt, " "));



        PieDataSet set1 = new PieDataSet(values, "DataSet 1");
        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
//        set1.setColors(Color.BLUE, Color. Color.LTGRAY);
        PieData data = new PieData(set1);
        data.setValueTextSize(10);
        data.setValueTextColor(Color.LTGRAY);

        // set data
        pieChart.getLegend().setEnabled(false);
        pieChart.setData(data);

    }
}