package com.example.wooriservice.report;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wooriservice.R;

import java.util.ArrayList;

public class reportadapter extends RecyclerView.Adapter<reportadapter.ViewHolder> {
    private ArrayList<String> mData = null;
    ViewGroup p;
    ArrayList<String> subs = new ArrayList<>();


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        ImageButton checks;


        ViewHolder(View itemView) {
            super(itemView) ;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(p.getContext(), Report_Content.class);
                    p.getContext().startActivity(intent);
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        checks.setBackgroundResource(R.drawable.ovalgray);
                        notifyItemChanged(pos) ;
                    }
                }
            });
            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.title) ;
            textView2 = itemView.findViewById(R.id.term) ;
            checks = itemView.findViewById(R.id.checks);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    reportadapter(ArrayList<String> list) {
        mData = list ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        p=parent;
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.reportview_item, parent, false) ;
        reportadapter.ViewHolder vh = new reportadapter.ViewHolder(view) ;

        subs.add("04.05~04.11");
        subs.add("03.01~03.31");
        subs.add("03.29~04.04");
        subs.add("03.22~03.28");
        subs.add("03.15~03.21");
        subs.add("03.08~04.14");
        subs.add("02.01~02.28");

        return vh ;
    }


    public void onBindViewHolder(@NonNull reportadapter.ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.textView1.setText(text) ;
        holder.textView2.setText(subs.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
