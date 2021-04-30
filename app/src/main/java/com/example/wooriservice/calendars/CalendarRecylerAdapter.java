package com.example.wooriservice.calendars;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wooriservice.R;
import com.example.wooriservice.report.Report_Content;

import java.util.ArrayList;

public class CalendarRecylerAdapter extends RecyclerView.Adapter<CalendarRecylerAdapter.ViewHolder>{
    private ArrayList<ArrayList<String>> mData = null;
    ViewGroup p;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView trn_tm;
        TextView pay_am;
        TextView trn_txt;

        ViewHolder(View itemView) {
            super(itemView) ;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
//                        mData.set(pos, "디자인 예시를 주지 않았어") ;
//                        notifyItemChanged(pos) ;
                    }
                }
            });
            // 뷰 객체에 대한 참조. (hold strong reference)
            trn_tm = itemView.findViewById(R.id.time) ;
            pay_am = itemView.findViewById(R.id.money);
            trn_txt = itemView.findViewById(R.id.txt_memo);
        }
    }

    CalendarRecylerAdapter(ArrayList<ArrayList<String>> list) {
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

        String text = mData.get(position).get(1);
        holder.trn_tm.setText(text) ;
        holder.pay_am.setText(mData.get(position).get(3));
        holder.trn_txt.setText(mData.get(position).get(5));

    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
