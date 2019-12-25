package com.bt_121shoppe.motorbike.Fragment_details_post;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Product_New_Post.MyAdapter_list_grid_image;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.activities.Item_API;
import com.bt_121shoppe.motorbike.models.BuyViewModel;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.models.RentViewModel;
import com.bt_121shoppe.motorbike.models.SaleViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Detail_1 extends Fragment {
    public static final String TAG = "1 Fragement";
    private TextView postCode,brand,type,model,year,color,condition;
    private TextView whole_ink,wheel_sets,the_whole_screw,pumps,engine_counter,engine_head,assembly,console,accessories;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView no_result;
    private TextView description;
    PostViewModel postDetail = new PostViewModel();

    private int pt=0;
    private int postId = 0;

    SharedPreferences prefer;
    private String name,pass,Encode;
    String basic_Encode;
    private String con;

    private int inBrand;
    View line_rela;
    RelativeLayout rela_eta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detail_1, container, false);
        postCode = view.findViewById(R.id.tvPostCode);
        brand = view.findViewById(R.id.tvBrand);
        type = view.findViewById(R.id.type);
        model = view.findViewById(R.id.tv_Model);
        year = view.findViewById(R.id.tv_Year);
        color = view.findViewById(R.id.tv_Color);
        condition = view.findViewById(R.id.tv_Condition);

        //basic
        prefer = getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = CommonFunction.getEncodedString(name,pass);
        basic_Encode = "Basic "+getEncodedString(name,pass);
        pt = getActivity().getIntent().getIntExtra("postt",0);
        postId = getActivity().getIntent().getIntExtra("ID",0);
        detail_fragment_1(Encode);

        //relate_post

        recyclerView = view.findViewById(R.id.list_rela);
        progressBar = view.findViewById(R.id.mprogressbar);
        no_result = view.findViewById(R.id.txt_noresult);

        //motor machin and other
        whole_ink = view.findViewById(R.id.tvViewWholeInk);
        wheel_sets = view.findViewById(R.id.tvViewFrontReal);
        the_whole_screw = view.findViewById(R.id.tvViewWholeScrew);
        pumps = view.findViewById(R.id.tvPumps);
        engine_counter = view.findViewById(R.id.tvCounter);
        engine_head = view.findViewById(R.id.tvEngine);
        assembly = view.findViewById(R.id.tvAssembly);
        console = view.findViewById(R.id.tvConsole);
        accessories = view.findViewById(R.id.tvAccessories);

        //Description
        description = view.findViewById(R.id.tv_Description);
        //ban
        line_rela = view.findViewById(R.id.line_rela);
        rela_eta = view.findViewById(R.id.relative_eta);
        line_rela.setVisibility(View.GONE);
        rela_eta.setVisibility(View.GONE);
        return view;

    }

//    private void updateView(String language){
//        context = LocaleHapler.setLocale(getContext(),language);
//        resources = context.getResources();
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        postCode.setText("1");
        brand.setText("2");
        type.setText("3");
        model.setText("4");
        year.setText("5");
        color.setText("6");
        condition.setText("7");

        whole_ink.setText("A");
        wheel_sets.setText("B");
        the_whole_screw.setText("C");
        pumps.setText("D");
        engine_head.setText("E");
        engine_counter.setText("F");
        assembly.setText("G");
        console.setText("H");
        accessories.setText("I");

        description.setText("Zilini");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void detail_fragment_1(String encode){
        String url;
        Request request;
        String auth = "Basic" + encode;
        if (pt==1){
            url = ConsumeAPI.BASE_URL + "postbyuser/" + postId;
            request = new  Request.Builder()
                    .url(url)
                    .header("Accept","application/json")
                    .header("Content-Type","application/json")
                    .header("Authorization",auth)
                    .build();
        }
        else {
            url = ConsumeAPI.BASE_URL + "detailposts/" + postId;
            request = new  Request.Builder()
                    .url(url)
                    .header("Accept","application/json")
                    .header("Content-Type", "application/json")
                    .build();
        }

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Request",mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.d(TAG+"3333",mMessage);
                Gson json = new Gson();
                try {
                    getActivity().runOnUiThread(() -> {
                        postDetail = json.fromJson(mMessage,PostViewModel.class);
                        Log.e(TAG,"D"+mMessage);
                        description.setText(postDetail.getDescription().toString());

                        postCode.setText(postDetail.getPost_code().toString());
                        color.setText(postDetail.getColor().toString());
                        con=postDetail.getCondition().toString();
                        if (con.equals("new")) {
                            condition.setText(R.string.newl);
                        } else if (con.equals("used")) {
                            condition.setText(R.string.used);
                        }
                        //type
                        int inType = postDetail.getCategory();
                        if (inType==1) {
                            type.setText(R.string.electronic);
                        }else if (inType==2){
                            type.setText(R.string.motor);
                            if (con.equals("used")){
                                line_rela.setVisibility(View.VISIBLE);
                                rela_eta.setVisibility(View.VISIBLE);
                            }
                        }
                        //brand
                        brand.setText(String.valueOf(postDetail.getModeling()));
                        //year
                        year.setText(String.valueOf(postDetail.getYear()));
                        //model
                        model.setText(String.valueOf(postDetail.getModeling()));

                        //for section

                        whole_ink.setText(postDetail.getUsed_eta1()+"%");
                        wheel_sets.setText(postDetail.getUsed_eta2()+"%");
                        the_whole_screw.setText(postDetail.getUsed_eta3()+"%");
                        pumps.setText(postDetail.getUsed_eta4()+"%");

                        engine_counter.setText(postDetail.getUsed_machine1()+"%");
                        engine_head.setText(postDetail.getUsed_machine2()+"%");
                        assembly.setText(postDetail.getUsed_machine3()+"%");
                        console.setText(postDetail.getUsed_machine4()+"%");

                        accessories.setText(postDetail.getUsed_other1()+"%");

                        String postType="";
                        RentViewModel[] rent=postDetail.getRents();
                        SaleViewModel[] sale=postDetail.getSales();
//                        BuyViewModel[] buy=postDetail.getBuys();
                        if(rent.length>0) {
                            postType = "rent";
                        }
                        if(sale.length>0) {
                            postType = "sell";
                        }
//                        if(buy.length>0) {
//                            postType = "buy";
//
//                        }
                        initialRelatedPost(encode,postType,postDetail.getCategory(),postDetail.getModeling(), Float.parseFloat(postDetail.getCost()));
                    });
                } catch (JsonParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initialRelatedPost(String encode,String postType,int category,int modeling,float cost){
        ArrayList itemApi =  new ArrayList<Item_API>();
        String URL_ENDPOINT=ConsumeAPI.BASE_URL+"relatedpost/?post_type="+postType+"&category="+category+"&modeling="+modeling+"&min_price="+(cost-500)+"&max_price="+(cost+500);
        Log.d("URL123",URL_ENDPOINT);
        OkHttpClient client= new  OkHttpClient();
        Request request= new Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encode)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                    try {
                        JSONObject object = new JSONObject(mMessage);
                        JSONArray jsonArray = object.getJSONArray("results");
                        int jsonCount = object.getInt("count");
                        Log.w("Relate",mMessage);
                        progressBar.setVisibility(View.GONE);
                        if (jsonCount==0) {
                            no_result.setVisibility(View.VISIBLE);
                        }else {
                            no_result.setVisibility(View.GONE);
                        }
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String title = obj.getString("title");
                            int id = obj.getInt("id");
                            int user_id = obj.getInt("created_by");
                            String condition = obj.getString("condition");
                            double cost = obj.getDouble("cost");
                            String image = obj.getString("front_image_path");
                            String img_user = obj.getString("right_image_path");
                            String postType = obj.getString("post_type");
                            String phoneNumber = obj.getString("contact_phone");
                            String discount_type = obj.getString("discount_type");
                            double discount = obj.getDouble("discount");
                            String postsubtitle = obj.getString("post_sub_title");
                            String color = obj.getString("color");
                            int model = obj.getInt("modeling");
                            int year = obj.getInt("year");

                            String ago="";
                            if(postId != id) {
                                itemApi.add(new Item_API(id,user_id,img_user, image, title, cost, condition, postType, ago, String.valueOf(jsonCount),color,model,year,discount_type,discount,postsubtitle));
//                                itemApi.add(Modeling(id,userId,img_user,image,title,cost,condition,postType,location_duration,jsonCount.toString(),discount_type,discount))
                                no_result.setVisibility(View.GONE);
                                recyclerView.setAdapter( new MyAdapter_list_grid_image(itemApi,"Grid",getActivity()));
                                recyclerView.setLayoutManager( new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                            }
                        }
                    } catch (JsonParseException | JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }


    private String getEncodedString(String username,String password){
        String userpass = username+":"+password;
        return Base64.encodeToString(userpass.trim().getBytes(), Base64.NO_WRAP);
    }
}