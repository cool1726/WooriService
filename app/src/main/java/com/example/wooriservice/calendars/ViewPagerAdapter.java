package com.example.wooriservice.calendars;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wooriservice.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class ViewPagerAdapter extends RecyclerView.Adapter<com.example.wooriservice.calendars.ViewPagerAdapter.ViewHolderPage> {

    private ArrayList<CalendarView> calendarList;
    private Context mContext;
    private ArrayList<ArrayList<String>> colorList;

    int year = 2021;
    int month = 0;

    int pos = 0;

    ViewPagerAdapter(Context context, ArrayList<CalendarView> calendarList, ArrayList<ArrayList<String>> colorList) {
        this.calendarList = calendarList;
        this.mContext = context;
        this.colorList = colorList;
    }

    public class ViewHolderPage extends RecyclerView.ViewHolder {

        TextView txtMonth;
        GridView gridView;
        ConstraintLayout constraintLayout; //추후 삭제

        ViewGroup parent;
        Context context;

        ViewHolderPage(View itemView, Context context, ViewGroup parent) {
            super(itemView);
            txtMonth = itemView.findViewById(R.id.title_month);
            gridView = itemView.findViewById(R.id.days_grid);
            constraintLayout = itemView.findViewById(R.id.cal_layout); //추후 삭제

            this.context = context;
            this.parent = parent;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    pos = getBindingAdapterPosition();
                    Log.d("Days Click", Integer.toString(pos));
                    Toast.makeText(parent.getContext(), Integer.toString(pos) + "clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.days_calendar, parent, false);
        com.example.wooriservice.calendars.ViewPagerAdapter.ViewHolderPage viewHolderPage = new ViewHolderPage(view, context, parent);
        return viewHolderPage;
    }

    @Override
    public void onBindViewHolder(ViewHolderPage holder, int position) {
        if(holder != null) {

            long now = System.currentTimeMillis();
            Date mDate = new Date(now);

            ArrayList<Date> dateList = new ArrayList<>();
            dateList.add(mDate);

            HashSet<Date> dateHashSet = new HashSet<>();
            dateHashSet.add(mDate);

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월");
            holder.txtMonth.setText(sdf.format(mDate));

            year += position / 12;
            month = position % 12;

            // TODO: 안에 들어갈 gridview채울 어뎁터, 들어갈 내용, 적용
            CalendarView calendarView = new CalendarView(holder.context);
            //calendarView.updateCalendar(dateHashSet, calendar, position, holder.parent);
            //holder.gridView.setAdapter(calendarAdapter);

            //txtMonth.setText(sdf.format(currentDate.getTime()));

            //update part
            ArrayList<Date> cells = new ArrayList<>();

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, 1);

            int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;    // 한 주의 시작은 일요일 ( -1 )

            calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

            // fill cells
            while(cells.size() < Calendar.DAY_OF_MONTH + calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + 1) {
                cells.add(calendar.getTime());
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            calendar.set(Calendar.MONTH, month);
            if(calendar.get(Calendar.MONTH) == 0)
                calendar.set(Calendar.YEAR, year);

            // update grid
            holder.txtMonth.setText(String.valueOf(year) + "년 " + String.valueOf(month + 1) + "월");
            CalendarAdapter calendarAdapter = new CalendarAdapter(holder.context, colorList, cells, calendar.get(Calendar.MONTH), position, holder.parent);
            holder.gridView.setAdapter(calendarAdapter);

        }
    }

    /*
    public void instantiateItem(ViewGroup container, int position) {
        HashSet<Date> eventDays = new HashSet<Date>();
        eventDays.add(new Date());
    }*/

    @Override
    public int getItemCount() {
        return calendarList.size();
    }


}
