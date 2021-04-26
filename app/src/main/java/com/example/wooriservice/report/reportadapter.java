package com.example.wooriservice.report;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wooriservice.R;

import java.util.ArrayList;

public class reportadapter extends RecyclerView.Adapter<reportadapter.ViewHolder> {
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
                    Intent intent = new Intent(p.getContext(), Report_Content.class);
                    p.getContext().startActivity(intent);
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        mData.set(pos, "item clicked. pos=" + pos) ;

                        notifyItemChanged(pos) ;
                    }
                }
            });
            // 뷰 객체에 대한 참조. (hold strong reference)
            textView1 = itemView.findViewById(R.id.title) ;
            textView2 = itemView.findViewById(R.id.term) ;
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

        return vh ;
    }


    public void onBindViewHolder(@NonNull reportadapter.ViewHolder holder, int position) {
        String text = mData.get(position) ;
        holder.textView1.setText(text) ;
        holder.textView2.setText("2021-04-26");
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
