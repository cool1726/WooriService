package com.example.wooriservice.calendars;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.wooriservice.R;

import java.util.ArrayList;
import java.util.List;

/* FragmentDays.java 참고 */
public class Calendars extends Fragment {
    private View view;
    ViewPager2 viewPager2;

    private List<List<String>> datalist;
    private ArrayList<Pair<String, String>> colorList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.days, container, false);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager2 = view.findViewById(R.id.pager);

        /* handler 기능 */
        colorList.add(new Pair("210430", "blue"));
        colorList.add(new Pair("210220", "blue"));
        ArrayList<CalendarView> list = new ArrayList<>();
        // 2020년 1월 ~ 2020년 12월까지
        for(int i = 0; i < 12; i++) {
            list.add(new CalendarView(getContext()));
        }

        viewPager2.setAdapter(new ViewPagerAdapter(getContext(), list, colorList));
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        viewPager2.setCurrentItem(5, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        /* handler 기능 */
        colorList.add(new Pair("red", "blue"));
        ArrayList<CalendarView> list = new ArrayList<>();
        // 2020년 1월 ~ 2020년 12월까지
        for(int i = 0; i < 12; i++) {
            list.add(new CalendarView(getContext()));
        }

        viewPager2.setAdapter(new ViewPagerAdapter(getContext(), list, colorList));
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
}
