package com.example.wooriservice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.wooriservice.HTTP.HttpUrl;
import com.example.wooriservice.RequestEntity.AccBasicInfoBodyReq;
import com.example.wooriservice.RequestEntity.AccBasicInfoDataReq;
import com.example.wooriservice.RequestEntity.AccBasicInfoHeaderReq;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoBodyReq;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoDataReq;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoHeaderReq;
import com.example.wooriservice.ResponseEntity.AccBasicInfoData;
import com.example.wooriservice.ResponseEntity.IndivAllAccInfoBodyGRID;
import com.example.wooriservice.ResponseEntity.IndivAllAccInfoData;
import com.example.wooriservice.calendars.Calendars;
import com.example.wooriservice.myacc.Myacc;
import com.example.wooriservice.report.reports;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;
    private ViewPager mViewPager;
    private FragmentManager fm;

    public static String URL = "https://openapi.wooribank.com:444";
    public static String getAccTransList = "/oai/wb/v1/finance/getAccTransList";            //거래내역조회
    public static String getIndivAllAccInfo = "/oai/wb/v1/finance/getIndivAllAccInfo";        //전계좌조회
    public static String getAccBasicInfo = "/oai/wb/v1/finance/getAccBasicInfo";            //계좌기본조회
    private IndivAllAccInfoTask task = new IndivAllAccInfoTask();
    private IndivAllAccInfoData list;
    private AccBasicInfoTask accBasicInfoTask = new AccBasicInfoTask();
    private AccBasicInfoData accBasicInfoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNV = findViewById(R.id.bottomNavi);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelecte
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());


                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.MyAcc);
        //xml 컴포넌트 배치하는데 계속 불러오길래 끈다 api
         //Init();
    }

    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (id == R.id.MyAcc) {
                fragment = new Myacc();

            } else if (id == R.id.calendars){

                fragment = new Calendars();
            }else {
                fragment = new reports();
            }

            fragmentTransaction.add(R.id.main_frame, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }



    private void Init(){
//        task.execute();
        accBasicInfoTask.execute();
    }


    /* 통신 */
    public class IndivAllAccInfoTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
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
                Log.d("success1", "성공1" + response);
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
            if (true) { // !s.equals("")
                try {
                    list = new Gson().fromJson(s, IndivAllAccInfoData.class);
                    if(list.getDataBody().getGRID().size() > 0 ){

                    }
                }
                catch (Exception e) {
                    Log.d("ERROR2", "e.getMessage()");
                    ErrorNotify(s + "\n" + e.getMessage());
                }
            }
            else {
                Log.d("ERROR3", s);
                ErrorNotify(s);
            }
        }
    }

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
                requestBody.setINQ_BAS_DT("20190101");

                AccBasicInfoDataReq request = new AccBasicInfoDataReq();
                request.setDataHeader(requestHeader);
                request.setDataBody(requestBody);

                String data = new Gson().toJson(request);
                Log.d("요청 데이터", data);
                String uri = URL + getAccBasicInfo;
                response = new HttpUrl().sendREST(uri, data);
                Log.d("success1", "성공1" + response);
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
            if (true) { // !s.equals("")
                try {
                    accBasicInfoData = new Gson().fromJson(s, AccBasicInfoData.class);
                    if(accBasicInfoData != null ){
                        Log.d("onPostExecute", accBasicInfoData.getDataBody().toString());
//                        Acno.setText(accBasicInfoData.getDataBody().getACNO());
//                        Actsts.setText(accBasicInfoData.getDataBody().getACT_STS());
//                        Cucd.setText(accBasicInfoData.getDataBody().getCUCD());
//                        Pdcd.setText(accBasicInfoData.getDataBody().getPDCD());
//                        Ctbal.setText(accBasicInfoData.getDataBody().getCT_BAL());
//                        Newdt.setText(accBasicInfoData.getDataBody().getNEW_DT());
//                        Xprdt.setText(accBasicInfoData.getDataBody().getXPR_DT());
//                        Txprpdcd.setText(accBasicInfoData.getDataBody().getTXPR_PDCD());
//                        Mmpidam.setText(accBasicInfoData.getDataBody().getMM_PID_AM());
//                        Tdybspr.setText(accBasicInfoData.getDataBody().getTDY_BSPR());
//                        Tdyevlam.setText(accBasicInfoData.getDataBody().getTDY_EVL_AM());
//                        Ivstprn.setText(accBasicInfoData.getDataBody().getIVST_PRN());
//                        Smplprftrt.setText(accBasicInfoData.getDataBody().getSMPL_PRFT_RT());
//                        Ctatcntbal.setText(accBasicInfoData.getDataBody().getCT_ATCNT_BAL());
//                        Lstlnpcsam.setText(accBasicInfoData.getDataBody().getLST_LN_PCS_AM());
                    }
                }
                catch (Exception e) {
                    Log.d("ERROR2", "e.getMessage()");
                    ErrorNotify(s + "\n" + e.getMessage());
                }
            }
            else {
                Log.d("ERROR3", s);
                ErrorNotify(s);
            }
        }
    }

    private void ErrorNotify(String errorMessage)
    {
        String title = "통신장애";
        String message = "통신이 월활하지 않습니다. 다시 시도하여 주세요.";

        if (!errorMessage.equals("")) {
            title = "예외발생";
            message = errorMessage;
        }

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}