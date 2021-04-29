package com.example.wooriservice.calendars;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wooriservice.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarAdapter extends ArrayAdapter<Date> implements View.OnClickListener {
    private LayoutInflater inflater;
    private int inputMonth;
    private ArrayList<Date> days;
    boolean isRecyclerViewVisible = false;
    static RecyclerView recyclerView2;
    Context mcontext;

    View view;

    String mColor = "";

    private ArrayList<Pair<String, String>> colorList;

    // 날짜 클릭을 위한 Listener 인터페이스 정의
    public interface CalClickListener {
        void onCalClickListener(View view, int position);
    }

    private CalClickListener calClickListener;

    public CalendarAdapter(Context context, ArrayList<Pair<String, String>> colorList, ArrayList<Date> days, int inputMonth, int position, ViewGroup parent) {
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

    public View getView(int position, View view, ViewGroup parent) {
        // 매일의 날짜 레이아웃 만들어둔 days_datenum을 inflate
        if(view == null)
            view = inflater.inflate(R.layout.days_dater, parent, false);

        this.view = view;

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
        txtDate.setTextColor(Color.BLACK);

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String intentDate = dateFormat.format(calendar.getTime());

        for(Pair<String, String> color : colorList) {
            if(intentDate.equals(color.first)){
                imgColor.setColorFilter(Color.parseColor(color.second));
                break;
            }
        }

        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                isRecyclerViewVisible = !isRecyclerViewVisible;
                if(isRecyclerViewVisible) {
                    recyclerView2.setVisibility(View.VISIBLE);
                } else {
                    recyclerView2.setVisibility(View.INVISIBLE);
                }


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
