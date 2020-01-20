package com.bt_121shoppe.motorbike.loan.child;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bt_121shoppe.motorbike.Api.api.AllResponse;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.User_Detail;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.loan.Create_Load;
import com.bt_121shoppe.motorbike.loan.LoanSectionOneFragment;
import com.bt_121shoppe.motorbike.loan.model.Province;
import com.bt_121shoppe.motorbike.loan.model.item_one;
import com.bt_121shoppe.motorbike.loan.model.loan_item;
import com.bt_121shoppe.motorbike.loan.model.province_Item;

import java.util.*;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString;

public class one extends Fragment{
    private static final String ARG_NUMBER = "arg_number";
    private static final String PRICE = "price";
    private static final String LOANID = "loanid";
    private static final String FROMLOAN = "fromloan";

    private Toolbar mToolbar;
    private TextView mTvName;
    private Button mBtnNext, mBtnNextWithFinish;
    private RelativeLayout relative_conspirator,relati_Contributors,relativeTime_Practicing1;
    private TextView tv_conspirator,tv_Contributors;
    private CardView carview_conspirator,carview_Contributors;
    private View view1,view2,view_3;
    private RadioButton radio1,radio2,radio3;
    private RadioGroup mCo_borrower;
    private EditText mName,mPhone_Number,mAddress,mJob,mRelationship,mCo_borrower_Job,mTotal_Income,mTotal_Expense,mNet_Income,
                     mJob_Period,mCo_Job_Period,mDistrict,mCommune,mVillage;
    private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img10,img11,img12;
    private int mProductID;
    String mPrice;
    private Create_Load createLoad;
    private item_one itemOne;
    int index=3,indextJom,indexRela,indexCoborow_job,mProvinceID,mLoanID;
    Button next;
    boolean radioCheck = false,Co_borrower,mFromLoan;

    private SharedPreferences preferences;
    private String username,password,Encode;
    private int pk;
    loan_item loanItem;
    String basicEncode,currentLanguage;
    private List<province_Item> listData;
    private String[] provine = new String[28];
    final Handler handler = new Handler();
    AlertDialog dialog;
    String[] Job = {"seller","state staff","private company staff","service provider","other",""};
    String[] Rela = {"husband", "wife", "father", "mother", "son","daugther","brother","sister","other",""};
    String[] rJob ;
    String[] rRela;
    boolean bname,bphone,baddress,bJob,bJob_Period,bRelationship,bco_Relationship,bCo_borrower_Job,bCo_Job_Period,bTotal_Income,bmTotal_Expense;

    public static one newInstance(int number,String price,int loanid,boolean fromLoan) {
        one fragment = new one();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER, number);
        args.putString(PRICE,price);
        args.putInt(LOANID,loanid);
        args.putBoolean(FROMLOAN,fromLoan);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mProductID = args.getInt(ARG_NUMBER);
            mPrice = args.getString(PRICE);
            mLoanID = args.getInt(LOANID);
            mFromLoan = args.getBoolean(FROMLOAN);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create__load_one, container, false);
        createLoad = (Create_Load)getActivity();
        rJob = getResources().getStringArray(R.array.job);
        rRela = getResources().getStringArray(R.array.relationship);

        initView(view);

        preferences= getContext().getSharedPreferences("Register",MODE_PRIVATE);
        username=preferences.getString("name","");
        password=preferences.getString("pass","");
        Encode = getEncodedString(username,password);
        basicEncode = "Basic "+Encode;
        if (preferences.contains("token")) {
            pk = preferences.getInt("Pk",0);
        }else if (preferences.contains("id")) {
            pk = preferences.getInt("id", 0);
        }
        Log.d("Pk",""+ pk + basicEncode+"  user "+ username+"  pass  "+password);


//        next = view.findViewById(R.id.next);
        relative_conspirator = view.findViewById(R.id.relative_conspirator);
        relati_Contributors = view.findViewById(R.id.relati_Contributors);
        relativeTime_Practicing1 = view.findViewById(R.id.relativeTime_Practicing1);
        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        currentLanguage = preferences.getString("My_Lang", "");

        if (mFromLoan){
            GetLoan();
            }else {
            getDetailUser();
            mTotal_Income.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty()) {
                        mTotal_Income.setFilters(new InputFilter[]{new InputFilterMinMax(0, 50000)});
                        if (mTotal_Expense.getText().toString().isEmpty()) {
                            mNet_Income.setText(s.toString());
                        } else {
//                        if (Double.parseDouble(s.toString())<Double.parseDouble(mTotal_Expense.getText().toString())){
//                            mTotal_Expense.setText(null);
//                        }else {
                            mNet_Income.setText(Double.parseDouble(mTotal_Income.getText().toString()) - Double.parseDouble(mTotal_Expense.getText().toString()) + "");
//                        }
                        }
                    } else {
                        mNet_Income.setText(null);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            mTotal_Expense.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty() && !mTotal_Income.getText().toString().isEmpty()) {
//                    mTotal_Expense.setFilters(new InputFilter[]{new InputFilterMinMax(0, Integer.parseInt(mTotal_Income.getText().toString()))});
                        mNet_Income.setText(Double.parseDouble(mTotal_Income.getText().toString()) - Double.parseDouble(s.toString()) + "");
                    } else {
                        if (!mTotal_Income.getText().toString().isEmpty())
                            mNet_Income.setText(Double.parseDouble(mTotal_Income.getText().toString()) + "");
                        else
                            mTotal_Expense.setFilters(new InputFilter[]{new InputFilterMinMax(0, 50000)});
                    }

//                int incom;
//                if (s.length() == 0||mTotal_Income.getText().length() == 0){
//                    s = "0";
//                    incom = 0;
////                    mTotal_Income.setText("0");
//                }
//                incom = Integer.parseInt(mTotal_Income.getText().toString());
//                int borrow = Integer.parseInt(s.toString());
//                mNet_Income.setText(""+(incom-borrow));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        getprovince();
        view1 = view.findViewById(R.id.view_1);
        view2 = view.findViewById(R.id.view_2);
        view_3 = view.findViewById(R.id.view_3);
        radio1 = view.findViewById(R.id.radio1);
        radio2 = view.findViewById(R.id.radio2);
////        next.setOnClickListener(v -> frm_on.setViewPager(1));


//        Button btn = view.findViewById(R.id.btn);
//        btn.setOnClickListener(v -> {
//            Log.d("Pk",""+ pk + basicEncode+"  user "+ username+"  pass  "+password);
////            putapi();
//        });
        return view;
    }
    public String method(String str) {
        for (int i=0;i<str.length();i++){
            if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }
    private void getDetailUser(){
        Service api = Client.getClient().create(Service.class);
        Call<User_Detail> call = api.getDetailUser(pk,basicEncode);
        call.enqueue(new Callback<User_Detail>() {
            @Override
            public void onResponse(Call<User_Detail> call, Response<User_Detail> response) {
                if (!response.body().getFirst_name().isEmpty()){
                    mName.setText(response.body().getFirst_name());
                }
                String stphone = response.body().getProfile().getTelephone();

                mPhone_Number.setText(method(stphone));
//               mPhone_Number.setText(response.body().getProfile().getTelephone());
            }

            @Override
            public void onFailure(Call<User_Detail> call, Throwable t) {

            }
        });
    }
    private void getprovince(){
        Service api = Client.getClient().create(Service.class);
        Call<AllResponse> call = api.getProvince();
        call.enqueue(new Callback<AllResponse>() {
            @Override
            public void onResponse(Call<AllResponse> call, Response<AllResponse> response) {
                if (!response.isSuccessful()){
                    Log.d("Error121211",response.code()+" ");
                }
                listData = response.body().getresults();
                Log.d("333333333333", String.valueOf(listData.size()));
                for (int i=0;i<listData.size();i++){
                    if (currentLanguage.equals("en")){
                        provine[i] = listData.get(i).getProvince();
                        Log.d("Province",listData.get(i).getProvince()+listData.get(i).getId());
                        Log.e("Pk",""+ pk + Encode+" user "+ username+"  pass  "+password+ " List " +listData.size());
                    }else {
                        Log.d("Province",listData.get(i).getProvince()+listData.get(i).getId());
                        provine[i] = listData.get(i).getProvince_kh();
                    }
                }
//                Log.d("Pk",""+ pk + Encode+"  user "+ username+"  pass  "+password+ " List " +listData.size());
            }
            @Override
            public void onFailure(Call<AllResponse> call, Throwable t) {
                Log.d("OnFailure",t.getMessage());
            }
        });
    }

    private void initView(View view) {

        Paper.init(getContext());
        String language = Paper.book().read("language");
        Log.e("90909090909","Current language is "+language);

        mName = view.findViewById(R.id.etName);
        mPhone_Number = view.findViewById(R.id.etPhone);
        mAddress = view.findViewById(R.id.etcity);
        mDistrict = view.findViewById(R.id.edDistrict);
        mCommune = view.findViewById(R.id.edCommune);
        mVillage = view.findViewById(R.id.edvillage);

        mJob = view.findViewById(R.id.et_Personal_Occupation);
        mJob_Period = view.findViewById(R.id.etTime_Practicing);
        mCo_borrower = view.findViewById(R.id.radio_group);
        mRelationship = view.findViewById(R.id.et_conspirator);
        mCo_borrower_Job = view.findViewById(R.id.et_Contributors);
        mCo_Job_Period = view.findViewById(R.id.etTime_Practicing1);
        mTotal_Income = view.findViewById(R.id.etTotal_income_borrowers);
        mTotal_Expense = view.findViewById(R.id.etTotal_cost_borrowers);
        mNet_Income = view.findViewById(R.id.et_total);

        img1 = view.findViewById(R.id.img_1);
        img2 = view.findViewById(R.id.img_2);
        img3 = view.findViewById(R.id.img_3);
        img4 = view.findViewById(R.id.img_4);
        img5 = view.findViewById(R.id.img_5);
        img6 = view.findViewById(R.id.img_6);
        img7 = view.findViewById(R.id.img_7);
        img8 = view.findViewById(R.id.img_8);
        img9 = view.findViewById(R.id.img_9);
        img10 = view.findViewById(R.id.img_10);
        img11 = view.findViewById(R.id.img_11);
        img12 = view.findViewById(R.id.img_12);

        editext();
        mBtnNext = view.findViewById(R.id.btn_next);

//        int radioButtonID = mCo_borrower.getCheckedRadioButtonId();
//        RadioButton radioButton = mCo_borrower.findViewById(radioButtonID);
        mCo_borrower.setOnCheckedChangeListener((group, checkedId) -> {
            View radioButton = mCo_borrower.findViewById(checkedId);
           index = mCo_borrower.indexOfChild(radioButton);
            radio3 = mCo_borrower.findViewById(checkedId);
            img6.setImageResource(R.drawable.ic_check_circle_black_24dp);
            // dd logic here
            switch (index) {
                case 0: // first button
                    relative_conspirator.setVisibility(View.VISIBLE);
                    relati_Contributors.setVisibility(View.VISIBLE);
                    relativeTime_Practicing1.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);
                    view_3.setVisibility(View.VISIBLE);
                    radioCheck = true;
                    Co_borrower = true;
                    break;
                case 1: // secondbutton
                    relative_conspirator.setVisibility(View.GONE);
                    relati_Contributors.setVisibility(View.GONE);
                    relativeTime_Practicing1.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view_3.setVisibility(View.GONE);

                    mRelationship.setText(null);
                    img7.setImageResource(R.drawable.ic_error_black_24dp);
                    img8.setImageResource(R.drawable.ic_error_black_24dp);
                    mCo_borrower_Job.setText(null);
                    mCo_Job_Period.setText("0");
                    indexRela = 9;
                    indexCoborow_job = 5;
                    radioCheck = true;
                    Co_borrower = false;
                    break;
            }
        });
        mJob.setOnClickListener(v -> {
            createLoad.AlertDialog(rJob,mJob);
        });
        mRelationship.setOnClickListener(v -> {
            createLoad.AlertDialog(rRela,mRelationship);
        });
        mCo_borrower_Job.setOnClickListener(v -> {
            createLoad.AlertDialog(rJob,mCo_borrower_Job);
        });
        mAddress.setOnClickListener(v -> AlertDialog(provine,mAddress));
        mJob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i=0;i<rJob.length;i++){
                    if (mJob.getText().toString().equals(rJob[i])){
                        indextJom = i;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        mRelationship.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i=0;i<rRela.length;i++){
                    if (s.toString().toLowerCase().equals(rRela[i].toLowerCase())){
                        indexRela = i;
                        Log.d("1212121255555",Rela[indexRela]+"indext"+indexRela);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        mCo_borrower_Job.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i=0;i<rJob.length;i++){
                    if (s.toString().toLowerCase().equals(rJob[i].toLowerCase())){
                        indexCoborow_job = i;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        mBtnNext.setOnClickListener(view3 -> {
//            Bundle bundle=new Bundle();
//            boolean bCo_borrower = createLoad.RadioCondition(img6,mCo_borrower);
            Log.d("343434343",String.valueOf(editext())+"    "+index);
//            Log.d("111111111111111","1111"+radio3.getText().toString()+"  "+mProvinceID);
            if (index == 1){
                createLoad.requstFocus(bRelationship,img7,null);
                createLoad.requstFocus(bCo_borrower_Job,img8,null);
                createLoad.requstFocus(bCo_Job_Period,img9,mCo_Job_Period);
            }
            if (!(index == 0|| index == 1)){
                img6.setImageResource(R.drawable.ic_error_black_24dp);
            }
            createLoad.requstFocus(bname,img1,mName);
            createLoad.requstFocus(baddress,img3,mAddress);
            createLoad.requstFocus(bJob,img4,mJob);
            createLoad.requstFocus(bJob_Period,img5,mJob_Period);
//            createLoad.requstFocus(bRelationship,img6,null);
            createLoad.requstFocus(bTotal_Income,img10,mTotal_Income);
            createLoad.requstFocus(bmTotal_Expense,img11,mTotal_Expense);
            if (editext()){
                itemOne = new item_one(mName.getText().toString(),mPhone_Number.getText().toString(),mAddress.getText().toString(),mDistrict.getText().toString(),mCommune.getText().toString(),mVillage.getText().toString(),Job[indextJom],
                        Co_borrower,index,Rela[indexRela],Job[indexCoborow_job],Float.parseFloat(mTotal_Income.getText().toString()),Float.parseFloat(mTotal_Expense.getText().toString()),
                        mNet_Income.getText().toString(),Integer.parseInt(mJob_Period.getText().toString()),
                        Integer.parseInt(mCo_Job_Period.getText().toString()),mProductID,mProvinceID,mPrice,
                        mLoanID,mFromLoan);
                two fragment = two.newInstance(itemOne);
//            fragment.setArguments(bundle);
                createLoad.loadFragment(fragment);
            }
        });

    }
    private boolean editext(){
        bname = createLoad.Checked(mName);
        bphone = createLoad.Checked(mPhone_Number);
        baddress = createLoad.Checked(mAddress);
        bJob = createLoad.Checked(mJob);
        bJob_Period = createLoad.CheckedYear(mJob_Period);
        bRelationship = createLoad.Checked(mRelationship);
        bCo_borrower_Job = createLoad.CheckedYear(mCo_borrower_Job);
//        bRelationship = createLoad.RadioCondition(img6,mCo_borrower);
        bTotal_Income = createLoad.CheckedYear(mTotal_Income);
        bmTotal_Expense = createLoad.CheckedYear(mTotal_Expense);

        createLoad.Condition(img1,mName);
        createLoad.Condition(img2,mPhone_Number);
        createLoad.Condition(img3,mAddress);
        createLoad.Condition(img4,mJob);
        createLoad.ConditionYear(img5,mJob_Period);
        createLoad.Condition(img7,mRelationship);
        createLoad.Condition(img8,mCo_borrower_Job);
        createLoad.ConditionYear(img9,mCo_Job_Period);
        createLoad.ConditionYear(img10,mTotal_Income);
        createLoad.ConditionYear(img11,mTotal_Expense);
        createLoad.ConditionYear(img12,mNet_Income);
        if (index == 0){
            return bname&&bphone&&baddress&&bJob&&bJob_Period&&radioCheck&&bTotal_Income&&bmTotal_Expense&&bRelationship&&bCo_borrower_Job;
        }else {
            return bname&&bphone&&baddress&&bJob&&bJob_Period&&radioCheck&&bTotal_Income&&bmTotal_Expense;
        }
//           boolean bCo_borrower = createLoad.RadioCondition(img6,mCo_borrower);
    }
    public void AlertDialog(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            mProvinceID = which+1;
//            Toast.makeText(getContext(), "Position: " + mProvinceID + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
        dialog = builder.create();
        dialog.show();
    }
    public class InputFilterMinMax implements InputFilter {
        private int min;
        private int max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            //noinspection EmptyCatchBlock
            try {
                int input = Integer.parseInt(dest.subSequence(0, dstart).toString() + source + dest.subSequence(dend, dest.length()));
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) { }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
    private void GetLoan(){
        Service api = Client.getClient().create(Service.class);
        Call<loan_item> call = api.getDeailLoan(mLoanID,basicEncode);
        call.enqueue(new Callback<loan_item>() {
            @Override
            public void onResponse(Call<loan_item> call, Response<loan_item> response) {
                if (!response.isSuccessful()){
                    Log.d("5555555555555555",response.code()+"");
                }
//                mName.setText(response.body());
                mName.setText(response.body().getUsername());
                mPhone_Number.setText(response.body().getTelephone());
                mDistrict.setText(response.body().getDistrmict());
                mCommune.setText(response.body().getCommune());
                mVillage.setText(response.body().getVillage());
                for (int i=0;i<Job.length;i++){
                    if (response.body().getJob().equals(Job[i])){
                        mJob.setText(rJob[i]);
                        indextJom = i;
                    }
                }
                mJob_Period.setText(String.valueOf(response.body().getBorrower_job_period()));
                if (response.body().ismIs_Co_borrower()){
                    mCo_borrower.check(R.id.radio1);
                    radio1.toggle();
                    for (int i=0;i<rRela.length;i++){
                        if (response.body().getmRelationship().equals(Rela[i].toLowerCase())) {
                            mRelationship.setText(rRela[i]);
                            indexRela = i;
                        }
                    }
                    for (int i=0;i<rJob.length;i++){
                        if (response.body().getmCoborrower_job().equals(Job[i].toLowerCase())){
                            mCo_borrower_Job.setText(rJob[i]);
                            indexCoborow_job = i;
                        }
                    }
                    mCo_Job_Period.setText(String.valueOf(response.body().getmCoborrower_job_period()));
                }else {
                    mCo_borrower.check(R.id.radio2);
                    radio2.toggle();
//                    mRelationship.setText(null);
//                    mCo_Job_Period.setText("0");
                }

                mTotal_Income.setText(String.valueOf(response.body().getAverage_income()));
                mTotal_Expense.setText(String.valueOf(response.body().getAverage_expense()));
                mNet_Income.setText(response.body().getAverage_income()- response.body().getAverage_expense()+"");
                mProvinceID = response.body().getProvince_id();
                Call<Province> call1 = api.getProvince(response.body().getProvince_id());
                call1.enqueue(new Callback<Province>() {
                    @Override
                    public void onResponse(Call<Province> call, Response<Province> response) {
                        if (!response.isSuccessful()){
                            Log.e("ONRESPONSE Province", String.valueOf(response.code()));
                        }
                        if (currentLanguage.equals("en"))
                        mAddress.setText(response.body().getProvince());
                        else  mAddress.setText(response.body().getProvince_kh());
                    }
                    @Override
                    public void onFailure(Call<Province> call, Throwable t) { }
                });

            }

            @Override
            public void onFailure(Call<loan_item> call, Throwable t) {

            }
        });
    }
}
