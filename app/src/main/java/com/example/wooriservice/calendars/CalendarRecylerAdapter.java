package com.example.wooriservice.calendars;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wooriservice.R;
import com.example.wooriservice.report.Report_Content;

import java.util.ArrayList;

public class CalendarRecylerAdapter extends RecyclerView.Adapter<CalendarRecylerAdapter.ViewHolder>{
    private ArrayList<String> mData = null;
    ViewGroup p;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;

        ViewHolder(View itemView) {
            super(itemView) ;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        mData.set(pos, "디자인 예시를 주지 않았어") ;
                        notifyItemChanged(pos) ;
                    }
                }
            });
            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.txt_memo);
            textView2 = itemView.findViewById(R.id.category) ;
        }
    }

    CalendarRecylerAdapter(ArrayList<String> list) {
        mData = list ;
    }
    @NonNull
    @Override
    public CalendarRecylerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        p=parent;
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.calendartrans_item, parent, false) ;
        CalendarRecylerAdapter.ViewHolder vh = new CalendarRecylerAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarRecylerAdapter.ViewHolder holder, int position) {

        String text = mData.get(position) ;
        holder.textView1.setText(text) ;
        holder.textView2.setText("2021-04-26");

    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
