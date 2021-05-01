package com.example.wooriservice.myacc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wooriservice.R;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class AccTransAdapter  extends RecyclerView.Adapter<AccTransAdapter.ViewHolder>{
    private ArrayList<ArrayList<String>> mData = null;
    ViewGroup p;

    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton cate_bt;
        TextView trn_tm;
        TextView pay_am;
        TextView trn_txt;
        TextView bal;

        ViewHolder(View itemView) {
            super(itemView) ;
            context = itemView.getContext();
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
            cate_bt = itemView.findViewById(R.id.acctransbt);
            trn_tm = itemView.findViewById(R.id.time) ;
            pay_am = itemView.findViewById(R.id.money);
            trn_txt = itemView.findViewById(R.id.txt_memo);
            bal = itemView.findViewById(R.id.balance);
        }
    }

    AccTransAdapter(ArrayList<ArrayList<String>> list) {
        mData = list ;
    }
    @NonNull
    @Override
    public AccTransAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        p=parent;
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.acctrans_item, parent, false) ;
        AccTransAdapter.ViewHolder vh = new AccTransAdapter.ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(@NonNull AccTransAdapter.ViewHolder holder, int position) {

        String text0 = mData.get(position).get(0);
        String date= text0.substring(0, 4) + "." + text0.substring(4, 6)+"." + text0.substring(6, 8);
        String text = mData.get(position).get(1);
        String timef = text.substring(0, 2) + ":" + text.substring(2, 4);
        holder.trn_tm.setText(date+ " " +timef) ;
        int rcvmoney = Integer.parseInt(mData.get(position).get(2));
        int paymoney = Integer.parseInt(mData.get(position).get(3));
        DecimalFormat myFormatter = new DecimalFormat("###,###");
        String temp;
        if(rcvmoney > paymoney){
            String formattedStringPrice = "+" + myFormatter.format(rcvmoney) + "원";
            temp = formattedStringPrice;
        }else{
            String formattedStringPrice = "-" + myFormatter.format(paymoney) + "원";
            temp = formattedStringPrice;
        }

        String cate = mData.get(position).get(6);
        cate.replace(" ", "");

        switch (cate) {
            case "식비":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.식비));
                break;
            case "배달":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.배달));
                break;
            case "생활":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.생활));
                break;
            case "카페/디저트":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.카페디저트));
                break;
            case "술/유흥":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.술유흥));
                break;
            case "교통":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.교통));
                break;
            case "택시":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.택시));
                break;
            case "대중교통":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.대중교통));
                break;
            case "운동":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.운동));
                break;
            case "편의점":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.편의점));
                break;
            case "자동차":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.자동차));
                break;
            case "의료/건강":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.의료건강));
                break;
            case "교육/학습":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.교육학습));
                break;
            case "자녀/육아":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.자녀육아));
                break;
            case "반려동물":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.반려동물));
                break;
            case "문화/여가":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.문화여가));
                break;
            case "여행/숙박":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.여행숙박));
                break;
            case "온라인쇼핑":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.온라인쇼핑));
                break;
            case "오프라인쇼핑":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.오프라인쇼핑));
                break;
            case "뷰티/미용":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.뷰티미용));
                break;
            case "서비스구독":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.서비스구독));
                break;
            case "금융":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.금융));
                break;
            case "경조선물":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.경조선물));
                break;
            case "주거통신":
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.주거통신));
                break;
            default:
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.기타));
        }
        holder.pay_am.setText(temp);
        int bal = Integer.parseInt(mData.get(position).get(4));
        String Bal = myFormatter.format(bal) + "원";
        holder.trn_txt.setText(mData.get(position).get(5));
        holder.bal.setText(Bal);
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
