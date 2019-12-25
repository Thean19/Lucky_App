package com.bt_121shoppe.motorbike.Api.api.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bt_121shoppe.motorbike.Activity.Detail_new_post_java;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Item;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jianghejie on 23/08/2019.
 */
public class Adapter_historybyuser extends RecyclerView.Adapter<Adapter_historybyuser.ViewHolder> {

    private List<Item> datas;
    private Context mContext;
    SharedPreferences prefer;
    String name,pass,basic_Encode;
    private int pk=0;

    public Adapter_historybyuser(List<Item> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list2,viewGroup,false);

        prefer = mContext.getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");

        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk", 0);
        } else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        basic_Encode = "Basic "+getEncodedString(name,pass);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder view, final int position) {
        final Item model = datas.get(position);
        //String iditem = String.valueOf(model.getId()).substring(0, String.valueOf(model.getId()).indexOf("."));
//        view.linearLayout.setOnClickListener(v -> {
//            Intent intent = new Intent(mContext, Detail_New_Post.class);
//            intent.putExtra("Price", model.getCost());
//            intent.putExtra("postt", 1);
//            intent.putExtra("ID",Integer.parseInt(iditem));
//            mContext.startActivity(intent);
//        });
        String iditem=String.valueOf((int)model.getId());
        view.btn_unlike.setVisibility(View.GONE);

        Double rs_price = 0.0;
 //Close by Raksmey
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        long date = 0;
//        Log.d("344343444","4545"+model.getModified());
//        try {
//            if (model.getModified() == null){
//                date = sdf.parse(model.getCreated()).getTime();
//            }else {
//                date = sdf.parse(model.getModified()).getTime();
//            }
//
//            Long now = System.currentTimeMillis();
//            CharSequence ago = DateUtils.getRelativeTimeSpanString(date, now, DateUtils.MINUTE_IN_MILLIS);
//            view.date.setText(ago);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//End
//dd by Raksmey
        String strPostTitle="";
        String lang = view.strView.getText().toString();
        int year =Integer.valueOf(model.getYear());
        if(model.getPost_sub_title()== null){
//            String fullTitle= CommonFunction.generatePostSubTitle(model.getModeling(),year,model.getColor());
//            if(lang.equals("View:"))
//                strPostTitle=fullTitle.split(",")[0];
//            else
//                strPostTitle=fullTitle.split(",")[1];
        }else {
            if (lang.equals("View:")) {
                strPostTitle = model.getPost_sub_title().split(",")[0];
            } else {
                strPostTitle = model.getPost_sub_title().split(",")[1];
            }
        }
        view.title.setText(strPostTitle);

//End
        if (model.getDiscount().equals("0.00")){
            view.cost.setText("$"+model.getCost());
//            rs_price = Double.parseDouble(model.getCost());
            view.txt_discount.setVisibility(View.INVISIBLE);
        }else {
            rs_price = Double.parseDouble(model.getCost());
            if (model.getDiscount_type().equals("amount")){
                rs_price = rs_price - Double.parseDouble(model.getDiscount());
            }else if (model.getDiscount_type().equals("percent")){
                Double per = Double.parseDouble(model.getCost()) *( Double.parseDouble(model.getDiscount())/100);
                rs_price = rs_price - per;
            }
            view.cost.setText("$"+rs_price);
            view.txt_discount.setVisibility(View.VISIBLE);
            Double co_price = Double.parseDouble(model.getCost());
            view.txt_discount.setText("$"+co_price);
            view.txt_discount.setPaintFlags(view.txt_discount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        Double finalRs_price = rs_price;
        view.linearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, Detail_new_post_java.class);
            intent.putExtra("Price", model.getCost());
            intent.putExtra("Discount", finalRs_price);
            if (model.getStatus() == 2){
                intent.putExtra("postt", 2);
            }
            intent.putExtra("ID",Integer.parseInt(iditem));
            mContext.startActivity(intent);
        });
        Glide.with(mContext).load(model.getFront_image_path()).apply(new RequestOptions().placeholder(R.drawable.no_image_available)).into(view.imageView);
        if (model.getPost_type().equals("sell")){
            view.item_type.setText(R.string.sell_t);
//            view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_sell));
            view.item_type.setBackgroundResource(R.drawable.roundimage);
        }else if (model.getPost_type().equals("buy")){
            view.item_type.setText(R.string.buy_t);
            view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_buy));
        }else {
            view.item_type.setText(R.string.rent);
//            view.item_type.setBackgroundColor(mContext.getResources().getColor(R.color.color_rent));
            view.item_type.setBackgroundResource(R.drawable.roundimage_rent);
        }
        try{
            Service apiServiece = Client.getClient().create(Service.class);
            Call<AllResponse> call = apiServiece.getCount(iditem,basic_Encode);
            call.enqueue(new Callback<AllResponse>() {
                @Override
                public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                    view.txtview.setText(String.valueOf(response.body().getCount()));
                }

                @Override
                public void onFailure(Call<AllResponse> call, Throwable t) { Log.d("Error",t.getMessage()); }
            });
        }catch (Exception e){Log.d("Error e",e.getMessage());}
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }
    private String getEncodedString(String username,String password){
        String userpass = username+":"+password;
        return Base64.encodeToString(userpass.trim().getBytes(), Base64.NO_WRAP);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,cost,item_type,txtview,date,txt_discount,strView;
        ImageView imageView;
        ImageButton btn_unlike;
        LinearLayout linearLayout;
        ViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
            cost = view.findViewById(R.id.tv_price);
//            date = view.findViewById(R.id.date);
            item_type = view.findViewById(R.id.item_type);
            txtview = view.findViewById(R.id.user_view);
            strView = view.findViewById(R.id.user_view1);
            btn_unlike = view.findViewById(R.id.imgbtn_unlike);
            txt_discount = view.findViewById(R.id.tv_discount);
//            btn_delete = view.findViewById(R.id.btndelete);
            linearLayout = view.findViewById(R.id.linearLayout);
        }
    }
}