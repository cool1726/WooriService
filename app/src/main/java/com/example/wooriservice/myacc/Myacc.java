package com.example.wooriservice.myacc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wooriservice.HTTP.HttpUrl;
import com.example.wooriservice.MainActivity;
import com.example.wooriservice.R;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoBodyReq;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoDataReq;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoHeaderReq;
import com.example.wooriservice.ResponseEntity.IndivAllAccInfoBodyGRID;
import com.example.wooriservice.ResponseEntity.IndivAllAccInfoData;
import com.google.gson.Gson;

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

import static com.example.wooriservice.MainActivity.getIndivAllAccInfo;

public class Myacc extends Fragment {
    private View view;

    private ImageButton btnGet, btnPost;
    private static TextView accName, accNum;

    private IndivAllAccInfoTask task = new IndivAllAccInfoTask();
    private static IndivAllAccInfoData list;

    public static String URL = "https://openapi.wooribank.com:444";
    public static String getAccTransList = "/oai/wb/v1/finance/getAccTransList";            //거래내역조회
    public static String getIndivAllAccInfo = "/oai/wb/v1/finance/getIndivAllAccInfo";        //전계좌조회
    public static String getAccBasicInfo = "/oai/wb/v1/finance/getAccBasicInfo";            //계좌기본조회

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
        accName = view.findViewById(R.id.myaccountname);
        accNum = view.findViewById(R.id.myaccountnum);
        btnGet = view.findViewById(R.id.viewhistory);
        btnPost = view.findViewById(R.id.viewinform);
        Init();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button.OnClickListener ButtonListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (v == btnGet){
//                    connMethod = "GET";
//                    getJSON(mURL, connMethod);
//                } else if (v == btnPost) {
//                    connMethod = "POST";
//                    getJSON(mURL, connMethod);
//                }
            }
        };
        btnGet.setOnClickListener(ButtonListener);
        btnPost.setOnClickListener(ButtonListener);
    }

    private void Init(){
        task.execute();
    }

    //우리은행 전계좌조회
    /* 통신 */
    public static class IndivAllAccInfoTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;

            try {
                IndivAllAccInfoHeaderReq requestHeader = new IndivAllAccInfoHeaderReq();
                requestHeader.setUTZPE_CNCT_IPAD("127.0.0.1");
                requestHeader.setUTZPE_CNCT_MCHR_UNQ_ID("3B5E6E7B");
                requestHeader.setUTZPE_CNCT_TEL_NO_TXT("01012341234");
                requestHeader.setUTZPE_CNCT_MCHR_IDF_SRNO("IMEI");
                requestHeader.setUTZ_MCHR_OS_DSCD("1");
                requestHeader.setUTZ_MCHR_OS_VER_NM("8.0.0");
                requestHeader.setUTZ_MCHR_MDL_NM("SM-G930S");
                requestHeader.setUTZ_MCHR_APP_VER_NM("1.0.0");
                IndivAllAccInfoBodyReq requestBody = new IndivAllAccInfoBodyReq();

                IndivAllAccInfoDataReq request = new IndivAllAccInfoDataReq();
                request.setDataHeader(requestHeader);
                request.setDataBody(requestBody);

                String data = new Gson().toJson(request);
                Log.d("요청 데이터", data);
                String uri = URL + getIndivAllAccInfo;
                response = new HttpUrl().sendREST(uri, data);
//                Log.d("success1", response);
            } catch (Exception e) {
                response = e.getMessage();
                Log.d("ERROR1", response);
            }
            return response;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (true) { // !s.equals("")
                try {
                    list = new Gson().fromJson(s, IndivAllAccInfoData.class);
                    if (list.getDataBody().getGRID().size() > 0) {
                        ArrayList<IndivAllAccInfoBodyGRID> grid = list.getDataBody().getGRID();
                        Log.d("GRID", grid.get(0).getACNO());
                        accNum.setText(grid.get(0).getACNO());
                        accName.setText(grid.get(0).getPRD_NM());
                    }
                } catch (Exception e) {
                    Log.d("ERROR2", "e.getMessage()");
                }
            } else {
                Log.d("ERROR3", s);
            }
        }
    }

//    // DB 호출
//    private final MyHandler mHandler = new MyHandler((MainActivity)getActivity());
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
//                ArrayList<String> results = new ArrayList<String>();
//                String result;
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
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String date = jsonObject.getString("TRN_DT");
//                        Log.d("jsonarray", date);
//                        results.add(jsonArray.getString(i));
//                    }
//                } catch (Exception e) {
//                    result = e.toString();
//                    Log.d("DB ERROR", result);
//                }
//                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
//                Log.d("DB", result);
//                Log.d("List", results.get(0));
//                mHandler.sendMessage(message);
//
//            }
//        });
//        thread.start();
//    }

}
