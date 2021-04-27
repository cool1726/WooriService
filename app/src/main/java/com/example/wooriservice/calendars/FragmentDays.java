package com.example.wooriservice.calendars;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wooriservice.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentDays extends Fragment {

    View fragmentView;
    ViewPager2 viewPager2;

    private List<List<String>> diarylist;
    private ArrayList<Pair<String, String>> colorList = new ArrayList<>();
//    private String user = MainActivity.username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.days, container, false);

        return fragmentView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager2 = view.findViewById(R.id.pager);

//        AWSMobileClient.getInstance().initialize(getContext(), new AWSStartupHandler() {
//            @Override
//            public void onComplete(AWSStartupResult awsStartupResult) {
//                // Add code to instantiate a AmazonDynamoDBClient
//                AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
//                dynamoDBMapper = DynamoDBMapper.builder().dynamoDBClient(dynamoDBClient).awsConfiguration(AWSMobileClient.getInstance().getConfiguration()).build();
//                readData();
//                //createData();
//            }
//        }).execute();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

//        AWSMobileClient.getInstance().initialize(getContext(), new AWSStartupHandler() {
//            @Override
//            public void onComplete(AWSStartupResult awsStartupResult) {
//                // Add code to instantiate a AmazonDynamoDBClient
//                AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
//                dynamoDBMapper = DynamoDBMapper.builder().dynamoDBClient(dynamoDBClient).awsConfiguration(AWSMobileClient.getInstance().getConfiguration()).build();
//                readData();
//                //createData();
//            }
//        }).execute();

    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {

        public void handleMessage(Message msg)
        {
            for(List<String> diary: diarylist) {
                colorList.add(new Pair<>(diary.get(0), diary.get(3)));
            }
            ArrayList<CalendarView> list = new ArrayList<>();
            // 2020년 1월 ~ 2020년 12월까지
            for(int i = 0; i < 12; i++) {
                list.add(new CalendarView(getContext()));
            }

            viewPager2.setAdapter(new ViewPagerAdapter(getContext(), list, colorList));
            viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

            viewPager2.setCurrentItem(5, false);
        }

    };


    private void readData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                colorList.clear();
                diarylist = new ArrayList<List<String>>();
//                UserDB item = dynamoDBMapper.load(UserDB.class, user);
//                if(item != null) {
//                    diarylist = item.getDiaryData();
//                    Message msg = handler.obtainMessage();
//                    handler.sendMessage(msg);
//                } else {
//                    Message msg = handler.obtainMessage();
//                    handler.sendMessage(msg);
//                }
            }
        }).start();
    }
}