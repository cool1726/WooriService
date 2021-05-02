package com.example.wooriservice.calendars;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarAdapter extends ArrayAdapter<Date> implements View.OnClickListener {
    private LayoutInflater inflater;
    private int inputMonth;
    private ArrayList<Date> days;
    boolean isRecyclerViewVisible = false;
    static RecyclerView recyclerView2;
    static ArrayList<ArrayList<String>> translists;
    Context mcontext;

    ConstraintLayout container;
    static TextView datetext;
    static TextView titlemonth;
    static TextView datecount;
    static TextView datesum;
    static View popupAsk;

    View view;

    String mColor = "";

    private ArrayList<ArrayList<String>> colorList;

    // 날짜 클릭을 위한 Listener 인터페이스 정의
    public interface CalClickListener {
        void onCalClickListener(View view, int position);
    }

    private CalClickListener calClickListener;

    public CalendarAdapter(Context context, ArrayList<ArrayList<String>> colorList, ArrayList<Date> days, int inputMonth, int position, ViewGroup parent) {
        super(context, R.layout.days_datenum, days);
        this.mcontext = context;
        this.inputMonth = inputMonth;
        this.days = days;

        this.colorList = colorList;
        inflater = LayoutInflater.from(context);
    }

    public static void setRecyler(RecyclerView recyclerView3) {
        recyclerView2 = recyclerView3;

    }
    public static void setTransList(ArrayList<ArrayList<String>> translist) {
        translists = translist;

    }
    public static void setText1(TextView txt1, TextView txt2, TextView txt3, TextView txt4, View popup) {
        datetext = txt1;
        titlemonth = txt2;
        datecount = txt3;
        datesum = txt4;
        popupAsk = popup;
    }

    public View getView(int position, View view, ViewGroup parent) {
        // 매일의 날짜 레이아웃 만들어둔 days_datenum을 inflate
        if(view == null)
            view = inflater.inflate(R.layout.days_dater, parent, false);

        this.view = view;

//        LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.days, container, true);

//        TextView datetext = container.findViewById(R.id.datetext);

        Calendar calendar = Calendar.getInstance();
        Date date = getItem(position);

        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // 오늘에 해당하는 캘린더 가져오기
        Date today = new Date();
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(today);

        TextView txtDate = view.findViewById(R.id.txt_date);
        ImageView imgColor = view.findViewById(R.id.img_date);
        //txtDate.setTypeface(null, Typeface.NORMAL);

        txtDate.setText(String.valueOf(calendar.get(Calendar.DATE)));



        // inputMonth는 ViewPager의 해당 페이지에 출력하는 Month를 표시
        if(month != inputMonth || year != calendarToday.get(Calendar.YEAR)) {
            // 해당월이 아닌경우에는 GridView에 표시되지 않도록 함
            view.setVisibility(View.INVISIBLE);
        }

        // 오늘 날짜에 표시할 것
        if(month == calendarToday.get(Calendar.MONTH) && year == calendarToday.get(Calendar.YEAR) && day == calendarToday.get(Calendar.DATE)) {
//            ((TextView)view).setTextColor(Color.GREEN);
//            ((TextView)view).setGravity(Gravity.CENTER);
            //view.setBackgroundResource(R.drawable.round_textView);
        }

        // 일기 데이터 저장할 날짜 key 값
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String intentDate = dateFormat.format(calendar.getTime());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy년 MM월");
        String intentMonth = dateFormat2.format(calendar.getTime());
        titlemonth.setText(toString().valueOf(year) + "년 " + toString().valueOf(month) + "월");

        SimpleDateFormat dateFormat3 = new SimpleDateFormat("E",Locale.KOREAN);
        String weekt = dateFormat3.format(calendarToday.getTime());
        datetext.setText(toString().valueOf(calendarToday.get(Calendar.DATE))+"일 "+weekt+"요일");


//
//        imgColor.setColorFilter(Color.BLUE);
        for(ArrayList<String> list : colorList){
            if(intentDate.equals(list.get(0))){
                imgColor.setImageResource(R.drawable.hexagonyellow);
                txtDate.setTextColor(getContext().getColor(R.color.black));
                break;
            }
        }
        translists = colorList;



        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                SimpleDateFormat dateFormat3 = new SimpleDateFormat("E",Locale.KOREAN);
                String weekd = dateFormat3.format(calendar.getTime());

                String day = intentDate.substring(6, 8);
                datetext.setText(day+"일 "+weekd+"요일");
                Log.d("VIVIEIW", view.toString());
                if(intentDate.equals(colorList.get(0).get(0))){
                    Log.d("date", intentDate);
                }else{
                    Log.d("trnas", colorList.get(0).get(0)+" "+intentDate);
                }
//                isRecyclerViewVisible = !isRecyclerViewVisible;
//                if(isRecyclerViewVisible) {
//                    recyclerView2.setVisibility(View.VISIBLE);
//                } else {
//                    recyclerView2.setVisibility(View.INVISIBLE);
//                }



                ArrayList<ArrayList<String>> lists = new ArrayList<>();
                int temp = 0;
                for (ArrayList<String> list : colorList) {
                    ArrayList<String> tdata = new ArrayList<>();
                    if (intentDate.equals(list.get(0))) {
                        tdata.add(list.get(0));
                        String time = list.get(1);
                        String timef = time.substring(0, 2) + ":" + time.substring(2, 4);
                        tdata.add(timef);
                        tdata.add(list.get(2));
                        tdata.add(list.get(3));
                        tdata.add(list.get(4));
                        tdata.add(list.get(5));
                        tdata.add(list.get(6));
                        tdata.add(list.get(7));
                        lists.add(tdata);

                        int rcvmoney = Integer.parseInt(list.get(2));
                        int paymoney = Integer.parseInt(list.get(3));
                        if(rcvmoney > paymoney){
                            temp += rcvmoney;
                        } else {
                            temp -=paymoney;
                        }

                    }
                }
//


                CalendarRecylerAdapter adapter = new CalendarRecylerAdapter(lists, popupAsk) ;
                recyclerView2.setAdapter(adapter);


                datecount.setText("총 "+toString().valueOf(lists.size())+"건");
                DecimalFormat myFormatter = new DecimalFormat("###,###");
                String formattedStringPrice = myFormatter.format(temp) + "원";
                datesum.setText(formattedStringPrice);

                CalendarAdapter.setRecyler(recyclerView2);


                if (imgColor.getColorFilter() == null) {
//                    Intent intent = new Intent(parent.getContext(), addDiary.class);
//                    intent.putExtra("datekey", intentDate);
//                    parent.getContext().startActivity(intent);
                } else {
//                    Intent intent = new Intent(parent.getContext(), ActivityD.class);
//                    intent.putExtra("sendDate", intentDate);
//                    parent.getContext().startActivity(intent);
                }
            }
        });
        return view;
    }

    // 날짜가 클릭되었을 때 실행되는 onClick 함수
    public void onClick(View v){
        if(this.calClickListener != null) {
            // !!
            Log.w("cal", "click");
        }
    }



}