package com.example.wooriservice.calendars;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wooriservice.R;

public class CalendarView extends LinearLayout {

    // calendarView의 component 정의

    ImageView arrowUp;
    ImageView arrowDown;
    TextView txtMonth;
    LinearLayout header;
    GridView gridView;

    public CalendarView(Context context) {
        super(context);
        initControl(context);
    }


    private void assignUIElements() {
        // layout is inflated, assign local variables to components
        txtMonth = findViewById(R.id.title_month);
        header = findViewById(R.id.days_header);
        gridView = findViewById(R.id.days_grid);
    }

    private void initControl(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.days_calendar, this);
        assignUIElements();
    }

}