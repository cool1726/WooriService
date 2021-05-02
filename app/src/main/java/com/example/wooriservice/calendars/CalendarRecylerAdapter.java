package com.example.wooriservice.calendars;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wooriservice.HTTP.HttpUrl;
import com.example.wooriservice.MainActivity;
import com.example.wooriservice.R;
import com.example.wooriservice.RequestEntity.AccBasicInfoBodyReq;
import com.example.wooriservice.RequestEntity.AccBasicInfoDataReq;
import com.example.wooriservice.RequestEntity.AccBasicInfoHeaderReq;
import com.example.wooriservice.RequestEntity.TransDataUpdateBodyReq;
import com.example.wooriservice.RequestEntity.TransDataUpdateDataReq;
import com.example.wooriservice.RequestEntity.TransDataUpdateHeaderReq;
import com.example.wooriservice.myacc.AccTrans;
import com.example.wooriservice.myacc.Myacc;
import com.example.wooriservice.report.Report_Content;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class CalendarRecylerAdapter extends RecyclerView.Adapter<CalendarRecylerAdapter.ViewHolder>{
    private ArrayList<ArrayList<String>> mData = null;
    ViewGroup p;
    View popupAsk;

    Context context;
    String pospos;

    // DB
    private static final String TAG = "basic REST API";
    private String connMethod;
    private String mURL = "https://2gue7sszi6.execute-api.us-west-2.amazonaws.com/beta/update";
    private String API_KEY = "";
    private String bodyJson;
    private static final int LOAD_SUCCESS = 101;
    ArrayList<ArrayList<String>> translist = new ArrayList<>();

    String spinnerStr = "";

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton cate_bt;
        TextView trn_tm;
        TextView pay_am;
        TextView trn_txt;
        TextView category;



        ViewHolder(View itemView) {
            super(itemView) ;
            context = itemView.getContext();

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(popupAsk);

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside( false );


            TextView spinnerResult = popupAsk.findViewById(R.id.spinnerResult);
            Spinner spinner = (Spinner) popupAsk.findViewById(R.id.spinnercate);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerResult.setText("선택하신 카테고리는 " + parent.getItemAtPosition(position) + "입니다");
                    spinnerStr = parent.getItemAtPosition(position).toString();
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Button btnOk = popupAsk.findViewById(R.id.editBtn);
            btnOk.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    TransDataUpdateDataReq request = new TransDataUpdateDataReq();
                    request.setId(Integer.parseInt(pospos));
                    request.setCategory(spinnerStr);

                    String data = new Gson().toJson(request);
//                    response = new HttpUrl().sendREST(uri, data);
                    sendJSON(mURL, "PUT", data);
                    alertDialog.dismiss();
                }
            });
            ImageButton close = popupAsk.findViewById(R.id.btnClose);
            close.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
//                        mData.set(pos, "디자인 예시를 주지 않았어") ;
//                        notifyItemChanged(pos) ;
                        if (popupAsk.getParent() != null)
                            ((ViewGroup) popupAsk.getParent()).removeView(popupAsk);
                        alertDialog.show();
                    }
                }
            });
            // 뷰 객체에 대한 참조. (hold strong reference)
            cate_bt = itemView.findViewById(R.id.transbutton);
            trn_tm = itemView.findViewById(R.id.time) ;
            pay_am = itemView.findViewById(R.id.money);
            trn_txt = itemView.findViewById(R.id.txt_memo);
            category = itemView.findViewById(R.id.category);
        }
    }

    CalendarRecylerAdapter(ArrayList<ArrayList<String>> list, View popupask) {
        mData = list ;
        popupAsk = popupask;
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CalendarRecylerAdapter.ViewHolder holder, int position) {
        pospos = mData.get(position).get(7);
        String text = mData.get(position).get(1);
        holder.trn_tm.setText(text) ;
        int rcvmoney = Integer.parseInt(mData.get(position).get(2));
        int paymoney = Integer.parseInt(mData.get(position).get(3));
        String temp;
        if(rcvmoney > paymoney){
            DecimalFormat myFormatter = new DecimalFormat("###,###");
            String formattedStringPrice = "+" + myFormatter.format(rcvmoney) + "원";
            temp = formattedStringPrice;
        } else {
            DecimalFormat myFormatter = new DecimalFormat("###,###");
            String formattedStringPrice = "-" + myFormatter.format(paymoney) + "원";
            temp = formattedStringPrice;
        }

        String cate = mData.get(position).get(6);
        cate.replace(" ", "");

        switch (cate) {
            case "식비":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.식비));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.식비));
                break;
            case "배달":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.배달));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.배달));
                break;
            case "생활":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.생활));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.생활));
                break;
            case "카페/디저트":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.카페디저트));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.카페디저트));
                break;
            case "술/유흥":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.술유흥));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.술유흥));
                break;
            case "교통":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.교통));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.교통));
                break;
            case "택시":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.택시));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.택시));
                break;
            case "대중교통":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.대중교통));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.대중교통));
                break;
            case "운동":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.운동));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.운동));
                break;
            case "편의점":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.편의점));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.편의점));
                break;
            case "자동차":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.자동차));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.자동차));
                break;
            case "의료/건강":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.의료건강));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.의료건강));
                break;
            case "교육/학습":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.교육학습));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.교육학습));
                break;
            case "자녀/육아":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.자녀육아));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.자녀육아));
                break;
            case "반려동물":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.반려동물));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.반려동물));
                break;
            case "문화/여가":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.문화여가));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.문화여가));
                break;
            case "여행/숙박":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.여행숙박));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.여행숙박));
                break;
            case "온라인쇼핑":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.온라인쇼핑));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.온라인쇼핑));
                break;
            case "오프라인쇼핑":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.오프라인쇼핑));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.오프라인쇼핑));
                break;
            case "뷰티/미용":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.뷰티미용));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.뷰티미용));
                break;
            case "서비스구독":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.서비스구독));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.서비스구독));
                break;
            case "금융":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.금융));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.금융));
                break;
            case "경조선물":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.경조선물));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.경조선물));
                break;
            case "주거통신":
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.주거통신));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.주거통신));
                break;
            default:
                holder.category.setTextColor(ContextCompat.getColor(context, R.color.기타));
                holder.cate_bt.setBackgroundColor(ContextCompat.getColor(context, R.color.기타));
        }

        holder.pay_am.setText(temp);
        holder.trn_txt.setText(mData.get(position).get(5));
        holder.category.setText(mData.get(position).get(6));
    }

    @Override
    public int getItemCount() {
        return mData.size() ;
    }




    public void  sendJSON(final String mUrl, final String connMethod, final String jsonValue) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                ArrayList<String> results = new ArrayList<String>();
                String result;
                HttpURLConnection httpURLConnection = null;
                try {
                    // urlConnection 설정
                    java.net.URL url = new URL(mUrl);
                    httpURLConnection = (HttpsURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestMethod(connMethod);
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setUseCaches(false);

                    httpURLConnection.connect();
                    Log.d("EDIT SUCCESS", "성공1");

                    // parameter 전달 및 데이터 읽어오기
//                    StringBuffer sbParams = new StringBuffer();
//                    sbParams.append("id="+1+"&category="+"android");

                    OutputStream os = httpURLConnection.getOutputStream();
                    os.write(jsonValue.getBytes("UTF-8"));
                    os.flush();
                    os.close();


                    // 연결 요청 확인
                    // 실패 시 DB ERROR 로그 출력
//                    int responseStatusCode = httpURLConnection.getResponseCode();
//                    InputStream inputStream;
                    if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                        Log.d("EDIT ERROR1", httpURLConnection.getInputStream().toString());

//                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {
//                        inputStream = httpURLConnection.getInputStream();
//                    } else {
//                        inputStream = httpURLConnection.getErrorStream();
//                        Log.d("EDIT ERROR1", inputStream.toString());
//                    }

//                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF8");
//                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                    StringBuilder sb = new StringBuilder();
//                    String line;
//
//                    while ((line = bufferedReader.readLine()) != null) {
//                        sb.append(line);
//                    }
                    Log.d("EDIT", "성공2");



//                    bufferedReader.close();
                    httpURLConnection.disconnect();
//                    result = sb.toString().trim();

//                    JSONArray jsonArray = new JSONArray(result);
//                    Log.d("JSONARRAY", Integer.toString(jsonArray.length()));
//                    for(int i = 0; i < jsonArray.length(); i++){
//                        ArrayList<String> trans = new ArrayList<>();
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        String date = jsonObject.getString("TRN_DT");
//                        String time = jsonObject.getString("TRN_TM");
//                        String rcvam = jsonObject.getString("RCV_AM");
//                        String payam = jsonObject.getString("PAY_AM");
//                        String bal = jsonObject.getString("DPS_BAL");
//                        String trntext = jsonObject.getString("TRN_TXT");
//                        String category = jsonObject.getString("CATEGORY");
//                        trans.add(date);
//                        trans.add(time);
//                        trans.add(rcvam);
//                        trans.add(payam);
//                        trans.add(bal);
//                        trans.add(trntext);
//                        trans.add(category);
//                        translist.add(trans);
//                    }
                } catch (Exception e) {
                    result = e.toString();
                    Log.d("EDIT ERROR2", result);
                }
//                Message message = mHandler.obtainMessage(LOAD_SUCCESS, result);
//                Log.d("DB", result);
//                mHandler.sendMessage(message);

            }
        });
        thread.start();
    }

}