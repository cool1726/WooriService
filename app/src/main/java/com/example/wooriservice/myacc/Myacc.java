package com.example.wooriservice.myacc;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wooriservice.HTTP.HttpUrl;
import com.example.wooriservice.MainActivity;
import com.example.wooriservice.R;
import com.example.wooriservice.RequestEntity.AccBasicInfoBodyReq;
import com.example.wooriservice.RequestEntity.AccBasicInfoDataReq;
import com.example.wooriservice.RequestEntity.AccBasicInfoHeaderReq;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoBodyReq;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoDataReq;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoHeaderReq;
import com.example.wooriservice.ResponseEntity.AccBasicInfoData;
import com.example.wooriservice.ResponseEntity.IndivAllAccInfoBodyGRID;
import com.example.wooriservice.ResponseEntity.IndivAllAccInfoData;
import com.example.wooriservice.report.Report_Content;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

import static android.content.Context.NOTIFICATION_SERVICE;

public class Myacc extends Fragment {
    private View view;

    private ImageButton btnGet, btnPost;
    private static TextView accName, accNum, accBal;
    private static TextView acno, ct_bal, pay_avl_am, new_dt, xpr_dt, mm_pid_am;
    private static ImageButton btnClose;

    private IndivAllAccInfoTask task = new IndivAllAccInfoTask();
    private static IndivAllAccInfoData list;

    private AccBasicInfoTask accBasicInfoTask = new AccBasicInfoTask();
    private AccBasicInfoData accBasicInfoData;

    public static String URL = "https://openapi.wooribank.com:444";
    public static String getAccTransList = "/oai/wb/v1/finance/getAccTransList";            //거래내역조회
    public static String getIndivAllAccInfo = "/oai/wb/v1/finance/getIndivAllAccInfo";        //전계좌조회
    public static String getAccBasicInfo = "/oai/wb/v1/finance/getAccBasicInfo";            //계좌기본조회



    // DB
    private static final String TAG = "basic REST API";
    private String connMethod;
    private String mURL = "https://2gue7sszi6.execute-api.us-west-2.amazonaws.com/2021-04-30/transdata";
    private String API_KEY = "";
    private String bodyJson;
    private static final int LOAD_SUCCESS = 101;
    ArrayList<ArrayList<String>> translist = new ArrayList<>();

    // Channel에 대한 id 생성
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    // Channel을 생성 및 전달해 줄 수 있는 Manager 생성
    private NotificationManager mNotificationManager;

    // Notification에 대한 ID 생성
    private static final int NOTIFICATION_ID = 0;

    private ViewPager2 sliderViewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.myacc, container, false);
        accName = view.findViewById(R.id.myaccountname);
        accNum = view.findViewById(R.id.myaccountnum);
        accBal = view.findViewById(R.id.myaccountBal);
        btnGet = view.findViewById(R.id.viewhistory);
        btnPost = view.findViewById(R.id.viewinform);
        connMethod = "GET";
        getJSON(mURL, connMethod);
        Init();
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View popupView = getLayoutInflater().inflate(R.layout.popup_acc, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(popupView);

        AlertDialog alertDialog = builder.create();

        sliderViewPager = view.findViewById(R.id.accimage);

        sliderViewPager.setOffscreenPageLimit(1);
        sliderViewPager.setAdapter(new ImageSliderAdapter(getContext()));

        sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sendNotification();
            }
        });


        acno = popupView.findViewById(R.id.acno);
        ct_bal = popupView.findViewById(R.id.ctbal);
        pay_avl_am = popupView.findViewById(R.id.payavlam);
        new_dt = popupView.findViewById(R.id.newdt);
        xpr_dt = popupView.findViewById(R.id.xprdt);
        mm_pid_am = popupView.findViewById(R.id.mmpidam);
        btnClose = popupView.findViewById(R.id.btnClose);


        btnClose.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Button.OnClickListener ButtonListener = new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnGet) {

                    AccTrans bottomsheet = new AccTrans();
                    bottomsheet.show(getParentFragmentManager(), "bottomsheet");

                } else if (v == btnPost) {
//                    sendNotification();
                    alertDialog.show();

                }
            }
        };
        createNotificationChannel();
        btnGet.setOnClickListener(ButtonListener);
        btnPost.setOnClickListener(ButtonListener);
    }

    private void Init(){
        task.execute();
        accBasicInfoTask.execute();
    }

    //우리은행 계좌정보조회
    public class AccBasicInfoTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;

            try {
                AccBasicInfoHeaderReq requestHeader = new AccBasicInfoHeaderReq();
                requestHeader.setUTZPE_CNCT_IPAD("127.0.0.1");
                requestHeader.setUTZPE_CNCT_MCHR_UNQ_ID("3B5E6E7B");
                requestHeader.setUTZPE_CNCT_TEL_NO_TXT("01012341234");
                requestHeader.setUTZPE_CNCT_MCHR_IDF_SRNO("IMEI");
                requestHeader.setUTZ_MCHR_OS_DSCD("1");
                requestHeader.setUTZ_MCHR_OS_VER_NM("8.0.0");
                requestHeader.setUTZ_MCHR_MDL_NM("SM-G930S");
                requestHeader.setUTZ_MCHR_APP_VER_NM("1.0.0");

                AccBasicInfoBodyReq requestBody = new AccBasicInfoBodyReq();
                requestBody.setINQ_ACNO("1002146788200");
                requestBody.setINQ_BAS_DT("20201228");
                requestBody.setINQ_CUCD("USD");
                requestBody.setACCT_KND("P");

                AccBasicInfoDataReq request = new AccBasicInfoDataReq();
                request.setDataHeader(requestHeader);
                request.setDataBody(requestBody);

                String data = new Gson().toJson(request);
                String uri = URL + getAccBasicInfo;
                response = new HttpUrl().sendREST(uri, data);
            }
            catch (Exception e) {
                response = e.getMessage();
                Log.d("ERROR1", response);
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s){
            super.onPostExecute(s);
            if (!s.equals("")) {
                try {
                    accBasicInfoData = new Gson().fromJson(s, AccBasicInfoData.class);

                    if (accBasicInfoData != null) {
                        ArrayList<IndivAllAccInfoBodyGRID> grid = list.getDataBody().getGRID();
                        acno.setText(accBasicInfoData.getDataBody().getACNO());
                        ct_bal.setText(accBasicInfoData.getDataBody().getCT_BAL());
                        pay_avl_am.setText(accBasicInfoData.getDataBody().getPAY_AVL_AM());
                        new_dt.setText(accBasicInfoData.getDataBody().getNEW_DT());
                        xpr_dt.setText(accBasicInfoData.getDataBody().getXPR_DT());
                        mm_pid_am.setText(accBasicInfoData.getDataBody().getMM_PID_AM());
                        Log.d("ACCBASIC", accBasicInfoData.getDataBody().getACNO());
                    }
                }
                catch (Exception e) {
                    Log.d("ERROR2", e.getMessage());
                }
            }
            else {
                Log.d("ERROR3", s);
            }
        }
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
                        accNum.setText(grid.get(2).getACNO());
                        accName.setText(grid.get(5).getPRD_NM());
                        if(grid.get(5).getPDOK_BAL() == "")
                            accBal.setText(grid.get(5).getPDOK_BAL());
                    }
                } catch (Exception e) {
                    Log.d("ERROR2", e.getMessage());
                }
            } else {
                Log.d("ERROR3", s);
            }
        }
    }

    // DB 호출
    private final Myacc.MyHandler mHandler = new Myacc.MyHandler((MainActivity)getActivity());
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
                        ArrayList<String> trans = new ArrayList<>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String date = jsonObject.getString("TRN_DT");
                        String time = jsonObject.getString("TRN_TM");
                        String rcvam = jsonObject.getString("RCV_AM");
                        String payam = jsonObject.getString("PAY_AM");
                        String bal = jsonObject.getString("DPS_BAL");
                        String trntext = jsonObject.getString("TRN_TXT");
                        String category = jsonObject.getString("CATEGORY");
                        String week = jsonObject.getString("WEEK");
                        trans.add(date);
                        trans.add(time);
                        trans.add(rcvam);
                        trans.add(payam);
                        trans.add(bal);
                        trans.add(trntext);
                        trans.add(category);
                        trans.add(week);
                        translist.add(trans);

                    }
                } catch (Exception e) {
                    result = e.toString();
                    Log.d("DB ERROR", result);
                }
                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
                Log.d("DB", result);
                mHandler.sendMessage(message);

            }
        });
        AccTrans.setTransList(translist);
        Report_Content.setTransList(translist);
        thread.start();
    }

    //채널을 만드는 메소드
    public void createNotificationChannel()
    {
        //notification manager 생성
        mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        // 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)
        if(android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.O){
            //Channel 정의 생성자( construct 이용 )
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID
                    ,"Test Notification",mNotificationManager.IMPORTANCE_HIGH);
            //Channel에 대한 기본 설정
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            // Manager을 이용하여 Channel 생성
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

    }

    // Notification Builder를 만드는 메소드
    private NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(getContext(), PRIMARY_CHANNEL_ID)
                .setContentTitle("잔액이 100,000원 이하입니다!")
                .setContentText("사용할 계좌의 잔액을 확인하세요.")
                .setSmallIcon(R.drawable.ic_launcher_foreground);
        return notifyBuilder;
    }

    // Notification을 보내는 메소드
    public void sendNotification(){
        // Builder 생성
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        // Manager를 통해 notification 디바이스로 전달
        mNotificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());
    }



}
