package com.example.wooriservice.report;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wooriservice.MainActivity;
import com.example.wooriservice.R;
import com.example.wooriservice.myacc.Myacc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class reports extends Fragment {
    private View view;

    // DB
    private static final String TAG = "basic REST API";
    private String connMethod;
    private String mURL = "";
    private String API_KEY = "";
    private String bodyJson;
    private static final int LOAD_SUCCESS = 101;

    private ArrayList<ArrayList<String>> reportlist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.reports, container, false);
        ArrayList<String> list = new ArrayList<>();
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = simpleDate.format(mDate);

        connMethod = "GET";
        getJSON(mURL, connMethod);

        for (int i=0; i<10; i++) {
            list.add(getTime + " " + i) ;
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = view.findViewById(R.id.reportsitemview) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        reportadapter adapter = new reportadapter(list) ;
        recyclerView.setAdapter(adapter) ;


        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // DB 호출
    private final reports.MyHandler mHandler = new reports.MyHandler((MainActivity)getActivity());
    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity mainactivity) {
            weakReference = new WeakReference<MainActivity>(mainactivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainactivity = weakReference.get();
            if (mainactivity != null) {
                switch (msg.what) {
                    case LOAD_SUCCESS:
                        String jsonString = (String) msg.obj;
                        Log.d("DB", msg.toString());
                        break;
                }
            }
        }
    }



    public void  getJSON(final String mUrl, final String connMethod) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String result;
                reportlist = new ArrayList<>();
                try {
                    java.net.URL url = new URL(mUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod(connMethod);
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setUseCaches(false);

                    httpURLConnection.connect();
                    Log.d("DB SUCCESS", "성공1");

                    int responseStatusCode = httpURLConnection.getResponseCode();

                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = httpURLConnection.getInputStream();
                    } else {
                        inputStream = httpURLConnection.getErrorStream();
                        Log.d("DB ERROR", inputStream.toString());
                    }

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder sb = new StringBuilder();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    Log.d("DB SUCCESS", "성공2");



                    bufferedReader.close();
                    httpURLConnection.disconnect();
                    result = sb.toString().trim();

                    JSONArray jsonArray = new JSONArray(result);
                    Log.d("JSONARRAY", Integer.toString(jsonArray.length()));
                    for(int i = 0; i < jsonArray.length(); i++){
                        ArrayList<String> report = new ArrayList<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String rptid = jsonObject.getString("RPTID");
                        String commentrpt = jsonObject.getString("COMMENT_RPT");
                        String ttl_amt = jsonObject.getString("TTL_AMT");
                        String avg_amt = jsonObject.getString("AVG_AMT");
                        String maxctg = jsonObject.getString("MAXCTG");
                        String maxctg_amt= jsonObject.getString("MAXCTG_AMT");
                        String maxtime = jsonObject.getString("MAXTIME");
                        String grp_name= jsonObject.getString("GRP_NAME");
                        String grp_comment= jsonObject.getString("GRP_COMMENT");
                        String readed = jsonObject.getString("READED");
                        report.add(rptid);
                        report.add(commentrpt);
                        report.add(ttl_amt);
                        report.add(avg_amt);
                        report.add(maxctg);
                        report.add(maxctg_amt);
                        report.add(maxtime);
                        report.add(grp_name);
                        report.add(grp_comment);
                        report.add(readed);
                        reportlist.add(report);
                    }
                } catch (Exception e) {
                    result = e.toString();
                    Log.d("DB ERROR", result);
                }
                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                Log.d("DB", result);
                Log.d("List", reportlist.get(0).get(0));
                mHandler.sendMessage(message);

            }
        });
        thread.start();
    }
}
