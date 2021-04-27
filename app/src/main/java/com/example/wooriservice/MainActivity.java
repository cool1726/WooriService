package com.example.wooriservice;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.wooriservice.HTTP.HttpUrl;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoBodyReq;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoDataReq;
import com.example.wooriservice.RequestEntity.IndivAllAccInfoHeaderReq;
import com.example.wooriservice.ResponseEntity.IndivAllAccInfoBodyGRID;
import com.example.wooriservice.ResponseEntity.IndivAllAccInfoData;
import com.example.wooriservice.calendars.Calendars;
import com.example.wooriservice.myacc.Myacc;
import com.example.wooriservice.report.reports;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;
    private ViewPager mViewPager;
    private FragmentManager fm;

    private String key = "l7xxe83OafPFgLMmAfqFA2cBWkeG3HKl03sz";
    public static String URL = "https://openapi.wooribank.com:444";
    public static String getAccTransList = "/oai/wb/v1/finance/getAccTransList";            //거래내역조회
    public static String getIndivAllAccInfo = "/oai/wb/v1/finance/getIndivAllAccInfo";        //전계좌조회
    public static String getAccBasicInfo = "/oai/wb/v1/finance/getAccBasicInfo";            //계좌기본조회
    private IndivAllAccInfoTask task = new IndivAllAccInfoTask();
    private IndivAllAccInfoData list;

    private Async dbasync = new Async();


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
        // Init();
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
        task.execute();
    }

    public class Async extends AsyncTask<Void, Void, Void> {
        String records = "", error = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.1.2:3306/android", "andro", "andro");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM test");
                while(resultSet.next()) {
                    records += resultSet.getString(1) + " " + resultSet.getString(2) + "\n";
                }
            } catch(Exception e) {
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(error != "") {
                Log.e("DB-records", records);
                Log.e("DB-error", error);
            }
            super.onPostExecute(aVoid);
        }
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

    public class ListViewAdapter extends BaseAdapter {
        private ArrayList<IndivAllAccInfoBodyGRID> listViewItemList = new ArrayList<IndivAllAccInfoBodyGRID>() ;

        public ListViewAdapter(ArrayList<IndivAllAccInfoBodyGRID> grid) {
            listViewItemList = grid;
        }
        @Override
        public int getCount() {
            return listViewItemList.size() ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            if (convertView == null) {

            }
//            Button button1 = (Button) convertView.findViewById(R.id.button1);
//            button1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //TODO : 거래내역 조회
//                    Intent intent = new Intent(MainActivity.this, AccTransListActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//            Button button2 = (Button) convertView.findViewById(R.id.button2);
//            button2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //TODO : 기본 정보 조회
//                    Intent intent = new Intent(MainActivity.this, AccBasicInfoActivity.class);
//                    startActivity(intent);
//                }
//            });

            IndivAllAccInfoBodyGRID listViewItem = listViewItemList.get(position);

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position ;
        }

        @Override
        public Object getItem(int position) {
            return listViewItemList.get(position) ;
        }
    }

    private void ErrorNotify(String errorMessage)
    {
        String title = "에러";
        String message = "통신이 월활하지 않습니다. 다시 시도하여 주세요.";
        if (!errorMessage.equals("")) {
            title = "예외발생";
            message = errorMessage;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
//        builder.setPositiveButton("확인",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                });
        builder.show();
    }
}