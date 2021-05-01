package com.example.wooriservice.myacc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wooriservice.MainActivity;
import com.example.wooriservice.R;
import com.example.wooriservice.calendars.Calendars;
import com.example.wooriservice.report.reportadapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class AccTrans extends BottomSheetDialogFragment {
    // DB
    private static final String TAG = "basic REST API";
    private static ArrayList<ArrayList<String>> translist = new ArrayList<>();
    private String connMethod = "GET";
    private String mURL = "https://2gue7sszi6.execute-api.us-west-2.amazonaws.com/2021-04-30/transdata";
    private String API_KEY = "";
    private String bodyJson;
    private static final int LOAD_SUCCESS = 101;

    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_acc_trans, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.trans_itemview) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        System.out.println(translist.size());
        AccTransAdapter adapter = new AccTransAdapter(translist) ;
        recyclerView.setAdapter(adapter) ;
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public static void setTransList(ArrayList<ArrayList<String>> translists) {
        translist = translists;

    }
//    // DB 호출
//    private final AccTrans.MyHandler mHandler = new AccTrans.MyHandler((MainActivity)getActivity());
//    private static class MyHandler extends Handler {
//        private final WeakReference<MainActivity> weakReference;
//
//        public MyHandler(MainActivity mainactivity) {
//            weakReference = new WeakReference<MainActivity>(mainactivity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            MainActivity mainactivity = weakReference.get();
//            if (mainactivity != null) {
//                switch (msg.what) {
//                    case LOAD_SUCCESS:
//                        String jsonString = (String) msg.obj;
//                        Log.d("DB", msg.toString());
//                        break;
//                }
//            }
//        }
//    }
//
//
//
//    public void  getJSON(final String mUrl, final String connMethod) {
//        Thread thread = new Thread(new Runnable() {
//            public void run() {
//                String result;
////                translist = new ArrayList<>();
//                try {
//                    java.net.URL url = new URL(mUrl);
//                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                    httpURLConnection.setReadTimeout(3000);
//                    httpURLConnection.setConnectTimeout(3000);
//                    httpURLConnection.setDoInput(true);
//                    httpURLConnection.setRequestMethod(connMethod);
//                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
//                    httpURLConnection.setUseCaches(false);
//
//                    httpURLConnection.connect();
//                    Log.d("DB SUCCESS", "성공1");
//
//                    int responseStatusCode = httpURLConnection.getResponseCode();
//
//                    InputStream inputStream;
//                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {
//                        inputStream = httpURLConnection.getInputStream();
//                    } else {
//                        inputStream = httpURLConnection.getErrorStream();
//                        Log.d("DB ERROR", inputStream.toString());
//                    }
//
//                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF8");
//                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                    StringBuilder sb = new StringBuilder();
//                    String line;
//
//                    while ((line = bufferedReader.readLine()) != null) {
//                        sb.append(line);
//                    }
//                    Log.d("DB SUCCESS", "성공2");
//
//
//
//                    bufferedReader.close();
//                    httpURLConnection.disconnect();
//                    result = sb.toString().trim();
//
//                    JSONArray jsonArray = new JSONArray(result);
//                    Log.d("JSONARRAY", Integer.toString(jsonArray.length()));
//                    for(int i = 0; i < jsonArray.length(); i++){
//                        ArrayList<String> trans = new ArrayList<>();
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String date = jsonObject.getString("TRN_DT");
//                        String time = jsonObject.getString("TRN_TM");
//                        String rcvam = jsonObject.getString("RCV_AM");
//                        String payam = jsonObject.getString("PAY_AM");
//                        String bal = jsonObject.getString("DPS_BAL");
//                        String trntext = jsonObject.getString("TRN_TXT");
//                        String category = jsonObject.getString("CATEGORY");
//                        trans.add(date);
//                        trans.add(time);
//                        trans.add(rcvam);
//                        trans.add(payam);
//                        trans.add(bal);
//                        trans.add(trntext);
//                        trans.add(category);
//                        translist.add(trans);
//                    }
//
//
//                } catch (Exception e) {
//                    result = e.toString();
//                    Log.d("DB ERROR", result);
//                }
//                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
//                Log.d("DB", result);
////                CalendarAdapter.setTransList(translist);
//                mHandler.sendMessage(message);
//
//            }
//        });
//        RecyclerView recyclerView = view.findViewById(R.id.trans_itemview) ;
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())) ;
//
//        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
//        AccTransAdapter adapter = new AccTransAdapter(translist) ;
//        recyclerView.setAdapter(adapter) ;
//
//        thread.start();
////        return translist;
//    }

}