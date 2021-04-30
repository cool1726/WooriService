package com.example.wooriservice.calendars;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wooriservice.MainActivity;
import com.example.wooriservice.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* FragmentDays.java 참고 */
public class Calendars extends Fragment {
    private View view;
    ViewPager2 viewPager2;
    RecyclerView recyclerView2;

    // DB
    private static final String TAG = "basic REST API";
    private String connMethod = "GET";
    private String mURL = "";
    private String API_KEY = "";
    private String bodyJson;
    private static final int LOAD_SUCCESS = 101;

    private List<List<String>> datalist;
    private ArrayList<ArrayList<String>> translist = new ArrayList<>();

    private static ArrayList<ArrayList<String>> colorList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.days, container, false);

//        ArrayList<String> lists = new ArrayList<>();
//        String getTime ="이** 씨는 나에게";
//        for (int i=0; i<2; i++) {
//            lists.add(getTime + " ") ;
//        }
        getJSON(mURL, connMethod);

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView2 = view.findViewById(R.id.caltransview) ;
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext())) ;


        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
//        CalendarRecylerAdapter adapter = new CalendarRecylerAdapter(lists) ;
//        recyclerView2.setAdapter(adapter);
//
        CalendarAdapter.setRecyler(recyclerView2);


        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager2 = view.findViewById(R.id.pager);

        /* handler 기능 */
//        colorList.add(new Pair("210430", "blue"));
//        colorList.add(new Pair("210220", "blue"));
        ArrayList<CalendarView> list = new ArrayList<>();
        // 2020년 1월 ~ 2020년 12월까지
        for(int i = 0; i < 12; i++) {
            list.add(new CalendarView(getContext()));
        }

        viewPager2.setAdapter(new ViewPagerAdapter(getContext(), list, translist));
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        viewPager2.setCurrentItem(3, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();


        /* handler 기능 */
        ArrayList<CalendarView> list = new ArrayList<>();
        // 2020년 1월 ~ 2020년 12월까지
        for(int i = 0; i < 12; i++) {
            list.add(new CalendarView(getContext()));
        }

        viewPager2.setAdapter(new ViewPagerAdapter(getContext(), list, translist));
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        viewPager2.setCurrentItem(5, false);

    }

    private void readData() { /* RDS에서 데이터 읽어오는 함수 */
//        if(item != null) {
//                    diarylist = item.getDiaryData();
//                    Message msg = handler.obtainMessage();
//                    handler.sendMessage(msg);
//                } else {
//                    Message msg = handler.obtainMessage();
//                    handler.sendMessage(msg);
//                }
    }
    // DB 호출
    private final Calendars.MyHandler mHandler = new Calendars.MyHandler((MainActivity)getActivity());
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
//                translist = new ArrayList<>();
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
                        ArrayList<String> trans = new ArrayList<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String date = jsonObject.getString("TRN_DT");
                        String time = jsonObject.getString("TRN_TM");
                        String rcvam = jsonObject.getString("RCV_AM");
                        String payam = jsonObject.getString("PAY_AM");
                        String bal = jsonObject.getString("DPS_BAL");
                        String trntext = jsonObject.getString("TRN_TXT");
                        String category = jsonObject.getString("CATEGORY");
                        trans.add(date);
                        trans.add(time);
                        trans.add(rcvam);
                        trans.add(payam);
                        trans.add(bal);
                        trans.add(trntext);
                        trans.add(category);
                        translist.add(trans);
                        Log.d("Lg", trans.get(0));
                    }


                } catch (Exception e) {
                    result = e.toString();
                    Log.d("DB ERROR", result);
                }
                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                Log.d("DB", result);
//                CalendarAdapter.setTransList(translist);
                mHandler.sendMessage(message);

            }
        });
        thread.start();
//        return translist;
    }
}
