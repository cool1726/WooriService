package com.example.wooriservice.myacc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wooriservice.MainActivity;
import com.example.wooriservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Myacc extends Fragment {
    private View view;

    private Button btnGet, btnPost;

    // DB
    private static final String TAG = "basic REST API";
    private String connMethod;
    private String mURL = "";
    private String API_KEY = "";
    private String bodyJson;
    private static final int LOAD_SUCCESS = 101;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myacc, container, false);
        btnGet = view.findViewById(R.id.viewhistory);
        btnPost = view.findViewById(R.id.viewinform);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button.OnClickListener ButtonListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnGet){
                    connMethod = "GET";
                    getJSON(mURL, connMethod);
                } else if (v == btnPost) {
                    connMethod = "POST";
                    getJSON(mURL, connMethod);
                }
            }
        };
        btnGet.setOnClickListener(ButtonListener);
        btnPost.setOnClickListener(ButtonListener);
    }

    // DB 호출
    private final MyHandler mHandler = new MyHandler((MainActivity)getActivity());
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
                ArrayList<String> results = new ArrayList<String>();
                String result;
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
                        results.add(jsonArray.getString(i));
                    }
                } catch (Exception e) {
                    result = e.toString();
                    Log.d("DB ERROR", result);
                }
                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                Log.d("DB", result);
                Log.d("List", results.get(0));
                mHandler.sendMessage(message);

            }
        });
        thread.start();
    }



}
