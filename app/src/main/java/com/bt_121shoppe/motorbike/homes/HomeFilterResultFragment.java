package com.bt_121shoppe.motorbike.homes;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.Breand;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.classes.APIResponse;
import com.bt_121shoppe.motorbike.models.BrandViewModel;
import com.bt_121shoppe.motorbike.models.CategoryViewModel;
import com.bt_121shoppe.motorbike.models.PostViewModel;
import com.bt_121shoppe.motorbike.models.YearViewModel;
import com.bt_121shoppe.motorbike.searches.SearchTypeAdapter;
import com.bt_121shoppe.motorbike.utils.CommomAPIFunction;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFilterResultFragment extends android.app.Fragment {
    private static final String TAG= HomeFilterResultFragment.class.getSimpleName();

    private View view;
    private Bundle bundle;
    private int mPostTypeId=0,mCategoryId=0,mBrandId=0,mYearId=0;
    private double mMinPrice=0,mMaxPrice=0;
    private String mViewType="",mCurrentLanguage;
    private ArrayList<PostViewModel> mPosts;
    private int[] modelIdListItems;
    private int countresult=0;

    private ImageView mGridView,mListView,mGallaryView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProgressDialog mProgress;
    private SearchTypeAdapter mAdapter;
    private ProgressBar mProgressbar;
    private TextInputEditText mFilterCategory,mFilterBrand,mFilterYear,mFilterPriceRange,mFilterPostType;
    private TextView mCountResultTextView,mNoResultTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home_search, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        mCurrentLanguage = preferences.getString("My_Lang", "");

        bundle=getArguments();
        mPostTypeId=bundle.getInt("postTypeId",0);
        mCategoryId=bundle.getInt("categoryId",0);
        mBrandId=bundle.getInt("brandId",0);
        mYearId=bundle.getInt("yearId",0);
        mMinPrice=bundle.getDouble("minPrice",0);
        mMaxPrice=bundle.getDouble("maxPrice",0);

        Log.d(TAG,"post="+mPostTypeId+" cat= "+mCategoryId+" brand="+mBrandId+" year="+mYearId+"  current language is"+mCurrentLanguage);

        mProgress=new ProgressDialog(getContext());
        if(isAdded())
            mProgress.setMessage(getString(R.string.please_wait));
        mProgress.show();

        mRecyclerView=view.findViewById(R.id.RecylerView);
        mListView=view.findViewById(R.id.img_list);
        mGridView=view.findViewById(R.id.grid);
        mGallaryView=view.findViewById(R.id.btn_image);
        mProgressbar=view.findViewById(R.id.progress_bar);
        mFilterCategory=view.findViewById(R.id.editTextCategory);
        mFilterBrand=view.findViewById(R.id.editTextBrand);
        mFilterYear=view.findViewById(R.id.editTextYear);
        mFilterPriceRange=view.findViewById(R.id.editTextPriceRange);
        mFilterPostType=view.findViewById(R.id.editTextPostType);
        mCountResultTextView=view.findViewById(R.id.countResult);
        mNoResultTextView=view.findViewById(R.id.noResult);

        mFilterPostType.setFocusable(false);
        mFilterCategory.setFocusable(false);
        mFilterBrand.setFocusable(false);
        mFilterYear.setFocusable(false);
        mFilterPriceRange.setFocusable(false);

        modelIdListItems=new int[0];
        /* initial value to filter control */
        //Post Type
        switch (mPostTypeId){
            case 0:
                if(isAdded())
                    mFilterPostType.setText(getString(R.string.all));
                break;
            case 1:
                if(isAdded())
                    mFilterPostType.setText(getString(R.string.sell));
                break;
            case 2:
                if(isAdded())
                    mFilterPostType.setText(getString(R.string.rent));
                break;
        }
        //Category
        if(mCategoryId==0){
            if(isAdded())
                mFilterCategory.setText(getString(R.string.all));
        }else {
            Service apiService=Client.getClient().create(Service.class);
            Call<CategoryViewModel> call=apiService.getCategoryDetail(mCategoryId);
            call.enqueue(new Callback<CategoryViewModel>() {
                @Override
                public void onResponse(Call<CategoryViewModel> call, Response<CategoryViewModel> response) {
                    if(response.isSuccessful()) {
                        CategoryViewModel mResponse = response.body();
                        if(mCurrentLanguage.equals("km"))
                            mFilterCategory.setText(mResponse.getCat_name_kh());
                        else
                            mFilterCategory.setText(mResponse.getCat_name());
                    }
                }

                @Override
                public void onFailure(Call<CategoryViewModel> call, Throwable t) {

                }
            });
        }
        //Brand
        if(mBrandId==0){
            if(isAdded())
                mFilterBrand.setText(getString(R.string.all));
        }else{
            Service apiService=Client.getClient().create(Service.class);
            Call<BrandViewModel> call=apiService.getBrandDetail(mBrandId);
            call.enqueue(new Callback<BrandViewModel>() {
                @Override
                public void onResponse(Call<BrandViewModel> call, Response<BrandViewModel> response) {
                    if(response.isSuccessful()){
                        if(mCurrentLanguage.equals("km"))
                            mFilterBrand.setText(response.body().getBrand_name_kh());
                        else
                            mFilterBrand.setText(response.body().getBrand_name());
                    }
                }
                @Override
                public void onFailure(Call<BrandViewModel> call, Throwable t) { }
            });
        }
        //Year
        if(mYearId==0){
            if(isAdded())
                mFilterYear.setText(getString(R.string.all));
        }else{
            Service apiService=Client.getClient().create(Service.class);
            Call<YearViewModel> call=apiService.getYearDetail(mYearId);
            call.enqueue(new Callback<YearViewModel>() {
                @Override
                public void onResponse(Call<YearViewModel> call, Response<YearViewModel> response) {
                    if(response.isSuccessful()){
                        mFilterYear.setText(response.body().getYear());
                    }
                }
                @Override
                public void onFailure(Call<YearViewModel> call, Throwable t) {

                }
            });
        }
        //price range
        if(mMinPrice>1||mMaxPrice>1){
            if(mMinPrice>1&&mMaxPrice>1)
                mFilterPriceRange.setText("$"+mMinPrice+" - $"+mMaxPrice);
            else if(mMinPrice>1&&mMaxPrice<1)
                mFilterPriceRange.setText("$"+mMinPrice+" - $");
            else if(mMinPrice<1&&mMaxPrice>1)
                mFilterPriceRange.setText("$0 - $"+mMaxPrice);
        }else
            if(isAdded())
                mFilterPriceRange.setText(getString(R.string.all));

        /* end initial value to filter control */

        /* start action event listener */
        mFilterPostType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("filterType", CommonFunction.FILTERPOSTTYPE);
                bundle.putInt("postTypeId",mPostTypeId);
                bundle.putInt("categoryId",mCategoryId);
                bundle.putInt("brandId",mBrandId);
                bundle.putInt("yearId",mYearId);
                bundle.putDouble("minPrice",mMinPrice);
                bundle.putDouble("maxPrice",mMaxPrice);
                HomeFilterConditionFragment fragment=new HomeFilterConditionFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        mFilterCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("filterType",CommonFunction.FILTERCATEGORY);
                bundle.putInt("postTypeId",mPostTypeId);
                bundle.putInt("categoryId",mCategoryId);
                bundle.putInt("brandId",mBrandId);
                bundle.putInt("yearId",mYearId);
                bundle.putDouble("minPrice",mMinPrice);
                bundle.putDouble("maxPrice",mMaxPrice);
                HomeFilterConditionFragment fragment=new HomeFilterConditionFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        mFilterBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                Log.d("212121212 thou",CommonFunction.FILTERBRAND+"posttype"+mPostTypeId+"categortyID"+mCategoryId+"brandid"+mBrandId+"year"+mYearId+"minprice"+mMinPrice+"max"+mMaxPrice);
                bundle.putInt("filterType",CommonFunction.FILTERBRAND);
                bundle.putInt("postTypeId",mPostTypeId);
                bundle.putInt("categoryId",mCategoryId);
                bundle.putInt("brandId",mBrandId);
                bundle.putInt("yearId",mYearId);
                bundle.putDouble("minPrice",mMinPrice);
                bundle.putDouble("maxPrice",mMaxPrice);
                HomeFilterConditionFragment fragment=new HomeFilterConditionFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        mFilterYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("filterType",CommonFunction.FILTERYEAR);
                bundle.putInt("postTypeId",mPostTypeId);
                bundle.putInt("categoryId",mCategoryId);
                bundle.putInt("brandId",mBrandId);
                bundle.putInt("yearId",mYearId);
                bundle.putDouble("minPrice",mMinPrice);
                bundle.putDouble("maxPrice",mMaxPrice);
                HomeFilterConditionFragment fragment=new HomeFilterConditionFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });

        mFilterPriceRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("filterType",CommonFunction.FILTERPRICERANGE);
                bundle.putInt("postTypeId",mPostTypeId);
                bundle.putInt("categoryId",mCategoryId);
                bundle.putInt("brandId",mBrandId);
                bundle.putInt("yearId",mYearId);
                bundle.putDouble("minPrice",mMinPrice);
                bundle.putDouble("maxPrice",mMaxPrice);
                HomeFilterConditionFragment fragment=new HomeFilterConditionFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });
        mListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewType="List";
                mListView.setImageResource(R.drawable.path_3930);
                mGridView.setImageResource(R.drawable.path_16);
                mGallaryView.setImageResource(R.drawable.path_18);
                mRecyclerView.setAdapter(new SearchTypeAdapter(mPosts,"List"));
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            }
        });

        mGridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewType="Grid";
                mListView.setImageResource(R.drawable.path_17);
                mGridView.setImageResource(R.drawable.path_3928);
                mGallaryView.setImageResource(R.drawable.path_18);
                mRecyclerView.setAdapter(new SearchTypeAdapter(mPosts,"Grid"));
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
            }
        });

        mGallaryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewType="Image";
                mListView.setImageResource(R.drawable.path_17);
                mGridView.setImageResource(R.drawable.path_16);
                mGallaryView.setImageResource(R.drawable.path_3929);
                mRecyclerView.setAdapter(new SearchTypeAdapter(mPosts,"Image"));
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
            }
        });
        /* end action event lister */
        if(mBrandId>0) {
            modelIdListItems = getModelIdList(mBrandId);
            Log.e(TAG,"brand id "+mBrandId+" "+modelIdListItems.length);
        }
        setupFilterResults(String.valueOf(mPostTypeId),"List",mCategoryId,modelIdListItems,mYearId,mMinPrice,mMaxPrice);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        //move your code from onViewCreated() here
        /* initial value to filter control */
        //Post Type
        switch (mPostTypeId){
            case 0:
                if(isAdded())
                    mFilterPostType.setText(getString(R.string.all));
                break;
            case 1:
                if(isAdded())
                    mFilterPostType.setText(getString(R.string.sell));
                break;
            case 2:
                if(isAdded())
                    mFilterPostType.setText(getString(R.string.rent));
                break;
        }
        //Category
        if(mCategoryId==0){
            if(isAdded())
                mFilterCategory.setText(getString(R.string.all));
        }else {
            Service apiService=Client.getClient().create(Service.class);
            Call<CategoryViewModel> call=apiService.getCategoryDetail(mCategoryId);
            call.enqueue(new Callback<CategoryViewModel>() {
                @Override
                public void onResponse(Call<CategoryViewModel> call, Response<CategoryViewModel> response) {
                    if(response.isSuccessful()) {
                        CategoryViewModel mResponse = response.body();
                        if(mCurrentLanguage.equals("km"))
                            mFilterCategory.setText(mResponse.getCat_name_kh());
                        else
                            mFilterCategory.setText(mResponse.getCat_name());
                    }
                }

                @Override
                public void onFailure(Call<CategoryViewModel> call, Throwable t) {

                }
            });
        }
        //Brand
        if(mBrandId==0){
            if(isAdded())
                mFilterBrand.setText(getString(R.string.all));
        }else{
            Service apiService=Client.getClient().create(Service.class);
            Call<BrandViewModel> call=apiService.getBrandDetail(mBrandId);
            call.enqueue(new Callback<BrandViewModel>() {
                @Override
                public void onResponse(Call<BrandViewModel> call, Response<BrandViewModel> response) {
                    if(response.isSuccessful()){
                        if(mCurrentLanguage.equals("km"))
                            mFilterBrand.setText(response.body().getBrand_name_kh());
                        else
                            mFilterBrand.setText(response.body().getBrand_name());
                    }
                }

                @Override
                public void onFailure(Call<BrandViewModel> call, Throwable t) {

                }
            });
        }
        //Year
        if(mYearId==0){
            if(isAdded())
                mFilterYear.setText(getString(R.string.all));
        }else{
            Service apiService=Client.getClient().create(Service.class);
            Call<YearViewModel> call=apiService.getYearDetail(mYearId);
            call.enqueue(new Callback<YearViewModel>() {
                @Override
                public void onResponse(Call<YearViewModel> call, Response<YearViewModel> response) {
                    if(response.isSuccessful()){
                        mFilterYear.setText(response.body().getYear());
                    }
                }

                @Override
                public void onFailure(Call<YearViewModel> call, Throwable t) {

                }
            });
        }
        //price range
        if(mMinPrice>1||mMaxPrice>1){
            if(mMinPrice>1&&mMaxPrice>1)
                mFilterPriceRange.setText("$"+mMinPrice+" - $"+mMaxPrice);
            else if(mMinPrice>1&&mMaxPrice<1)
                mFilterPriceRange.setText("$"+mMinPrice+" - $");
            else if(mMinPrice<1&&mMaxPrice>1)
                mFilterPriceRange.setText("$0 - $"+mMaxPrice);
        }else
            if(isAdded())
                mFilterPriceRange.setText(getString(R.string.all));

        /* end initial value to filter control */

    }

    private void setupFilterResults(String postType,String viewType,int categoryId,int[] modelsId,int yearId,double minPrice,double maxPrice){
//        mProgress.show();
        mPosts=new ArrayList<>();
        mLayoutManager=new GridLayoutManager(getContext(),1);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_drawable));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter=new SearchTypeAdapter(new ArrayList<>(),viewType);
        for (int i=0;i<modelsId.length;i++){
            Log.d("sljdflasdjfal",modelsId[i]+"go");
        }
        readPosts(postType,categoryId,modelsId,yearId,minPrice,maxPrice);
        mProgressbar.setVisibility(View.GONE);

    }

    private void readPosts(String postType,int categoryId,int[] modelIdList,int yearId,double minPrice,double maxPrice){
        String category=categoryId==0?"":String.valueOf(categoryId);
        String year=yearId==0?"":String.valueOf(yearId);
        String strMinPrice=minPrice<1?"":String.valueOf(minPrice);
        String strMaxPrice=maxPrice<1?"":String.valueOf(maxPrice);
        String type=postType.equals("1")?"sell":postType.equals("2")?"rent":"";

        //Log.d(TAG,"model id "+modelIdList.length);
        if(modelIdList.length==0 && mBrandId==0) {
            mPosts = new ArrayList<>();
            //old process
            /*
            String url = ConsumeAPI.BASE_URL + "relatedpost/?post_type=" + type + "&category=" + category + "&modeling=&min_price="+strMinPrice+"&max_price="+strMaxPrice+"&year=" + year;
            String response = "";
            try {
                response = CommonFunction.doGetRequest(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "response " + response);
            APIResponse APIResponse = new APIResponse();
            Gson gson = new Gson();
            APIResponse = gson.fromJson(response, APIResponse.class);
            mPosts = APIResponse.getresults();
            mAdapter.addItems(mPosts);
            countresult=mPosts.size();
            */
            //new process
            Service apiService= Client.getClient().create(Service.class);
            Call<APIResponse> call=apiService.getFilterResult(type,category,strMinPrice,strMaxPrice,year);
            call.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    mProgress.dismiss();
                    if(!response.isSuccessful()){
                        Log.e(TAG,"Get Filter Result failure:"+response.code());
                    }else{
                        mPosts=response.body().getresults();
                        mAdapter.addItems(mPosts);
                        countresult =mPosts.size();
                        if(isAdded())
                            mCountResultTextView.setText(mPosts.size()+" "+getString(R.string.result_name));
                        if(countresult ==0)
                            mNoResultTextView.setVisibility(View.VISIBLE);
                        else
                            mNoResultTextView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    Log.e(TAG,"Get Filter Result failure:"+t.getMessage());
                }
            });



        }else if(modelIdList.length>0){
            for(int i=0;i<modelIdList.length;i++){
                //String modelId=String.valueOf(modelIdList[i]);
                int modelId=modelIdList[i];
                mPosts = new ArrayList<>();
                /*
                String url = ConsumeAPI.BASE_URL + "relatedpost/?post_type=" + type + "&category=" + category + "&modeling="+modelId+"&min_price="+strMinPrice+"&max_price="+strMaxPrice+"&year=" + year;
                String response = "";
                try {
                    response = CommonFunction.doGetRequest(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Log.d(TAG, "response with model " + response);
                APIResponse APIResponse = new APIResponse();
                Gson gson = new Gson();
                APIResponse = gson.fromJson(response, APIResponse.class);
                mPosts = APIResponse.getresults();
                mAdapter.addItems(mPosts);
                countresult = countresult +mPosts.size();
                */
                //new process
                Service apiService= Client.getClient().create(Service.class);
                Call<APIResponse> call=apiService.getFilterResultwithModel(type,category,modelId,strMinPrice,strMaxPrice,year);
                call.enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        mProgress.dismiss();
                        if(!response.isSuccessful()){
                            Log.e(TAG,"Get Filter Result failure:"+response.code());
                        }else{
                            mPosts=response.body().getresults();
                            mAdapter.addItems(mPosts);
                            countresult =countresult+mPosts.size();
                            if(isAdded())
                                mCountResultTextView.setText(countresult+" "+getString(R.string.result_name));

                            if(countresult ==0)
                                mNoResultTextView.setVisibility(View.VISIBLE);
                            else
                                mNoResultTextView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        Log.e(TAG,"Get Filter Result failure:"+t.getMessage());
                    }
                });

            }
        }else{
            mProgress.dismiss();
            mPosts = new ArrayList<>();
            mAdapter.addItems(mPosts);
            mNoResultTextView.setVisibility(View.VISIBLE);
        }
        //mCountResultTextView.setText(countresult+" "+getString(R.string.result_name));
        mRecyclerView.setAdapter(mAdapter);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView,false);
        mAdapter.notifyDataSetChanged();
//        mProgress.dismiss();

//        if(countresult ==0)
//            mNoResultTextView.setVisibility(View.VISIBLE);
//        else
//            mNoResultTextView.setVisibility(View.GONE);


    }

    private int[] getModelIdList(int brandId){
        int[] modelsId=new int[0];
        String response="";
        int count=0,ccount=0;
        try{
            response=CommonFunction.doGetRequest(ConsumeAPI.BASE_URL+"api/v1/models/");
            Log.e(TAG,response);
        }catch (IOException e){ e.printStackTrace(); }
        try{
            JSONObject obj=new JSONObject(response);
            JSONArray results=obj.getJSONArray("results");
            for(int i=0;i<results.length();i++){
                JSONObject oobj=results.getJSONObject(i);
                if(brandId==oobj.getInt("brand"))
                    count++;
            }
            modelsId=new int[count];
            for(int i=0;i<results.length();i++){
                JSONObject oobj=results.getJSONObject(i);
                if(brandId==oobj.getInt("brand")){
                    modelsId[ccount]=oobj.getInt("id");
                    ccount++;
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.e(TAG,"Inline "+modelsId.length);
        return modelsId;
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fm=getFragmentManager();
        FragmentTransaction fragmentTransaction=fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

}
