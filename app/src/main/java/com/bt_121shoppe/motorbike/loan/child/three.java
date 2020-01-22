package com.bt_121shoppe.motorbike.loan.child;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Product_New_Post.Detail_New_Post;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.loan.Create_Load;
import com.bt_121shoppe.motorbike.loan.model.item_two;
import com.bt_121shoppe.motorbike.loan.model.loan_item;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.bt_121shoppe.motorbike.utils.CommonFunction.getEncodedString;

public class three extends Fragment {
    private static final String ARG_NUMBER = "arg_number";

    private Toolbar mToolbar;
    private TextView mTvName;
    private Button mBtnSubmit, mBtnNextWithFinish,mBtnback;

    private Button btn_positive;
    private Button btn_negative;

    private int mNumber;
    private AlertDialog dialog;
    private TextView etID_card,etFamily_book,etPhotos,etEmployment_card,etID_card1,etFamily_book1,etPhotos1,etEmployment_card1;
    private Create_Load createLoad;
    private item_two itemTwo;

    private SharedPreferences preferences;
    private String username,password,Encode,language;
    private int pk;
    private int i = -1;
    private loan_item loanItem;
    private String basicEncode;
    boolean ischeck;
    boolean mCard_ID,mFamily_Book,mPhoto,mCard_Work,mCard_ID1=false,mFamily_Book1=false,mPhoto1=false,mCard_Work1=false;
    boolean bID_Card,bFramily_Book,bPhotos,bEmployment_card,bID_Card1,bFramily_Book1,bPhotos1,bEmployment_card1;
    private ProgressDialog mProgress;
    public static three newInstance(item_two itemTwo) {
        Bundle args = new Bundle();
//        args.putParcelable(ARG_NUMBER,itemOne);
        args.putParcelable(ARG_NUMBER, itemTwo);
        three fragment = new three();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null) {
            itemTwo =  args.getParcelable(ARG_NUMBER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create__load_three, container, false);
        initView(view);
        createLoad = (Create_Load)this.getActivity();
        mBtnback = view.findViewById(R.id.btn_back);
        mBtnSubmit = view.findViewById(R.id.btn_submit);
        btn_positive = view.findViewById(R.id.button_positive);
        btn_negative = view.findViewById(R.id.button_negative);
        mProgress = new ProgressDialog(getContext());
        mProgress.setMessage(getString(R.string.update));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        Paper.init(getContext());
        language = Paper.book().read("language");

        checkEd();
        mBtnback.setOnClickListener(view1 -> { createLoad.setBack(); });
        mBtnSubmit.setOnClickListener(v -> {
            checkEd();
            if (itemTwo.getItemOne().getIndex()==1){
//                createLoad.requstFocus(bID_Card,img1,null);
//                createLoad.requstFocus(bFramily_Book,img2,null);
//                createLoad.requstFocus(bPhotos,img3,null);
//                createLoad.requstFocus(bEmployment_card,img4,null);
            }else {
//                createLoad.requstFocus(bID_Card,img1,null);
//                createLoad.requstFocus(bFramily_Book,img2,null);
//                createLoad.requstFocus(bPhotos,img3,null);
//                createLoad.requstFocus(bEmployment_card,img4,null);
//
//                createLoad.requstFocus(bID_Card1,img5,null);
//                createLoad.requstFocus(bFramily_Book1,img6,null);
//                createLoad.requstFocus(bPhotos1,img7,null);
//                createLoad.requstFocus(bEmployment_card1,img8,null);
            }

            if (checkEd()){
                Log.e("FromeLoan",String.valueOf(itemTwo.getItemOne().isFromLoan()));

                if (itemTwo.getItemOne().isFromLoan()){
//                    Editloan();
                    dialog_Editloan();
//                    Log.d("909090909090909",loanItem.getLoan_amount()+"");
                }else {
                    Log.d("Pk",""+ pk + Encode+"  user "+ username+"  pass  "+password);
                    putapi();
                }
            }
        });

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

//        loanItem = new loan_item(itemTwo.getLoan_Amount(),0,1,itemTwo.getItemOne().getTotal_Income(),itemTwo.getItemOne().getTotal_Expense(),pk,202,1,202,null,null,itemTwo.getItemOne().getName(),null,null,itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getPhone_Number(),itemTwo.getItemOne().getAddress(),createLoad.Checked_Radio(),);


        return view;
    }

    private void AlertDialog(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mCard_ID = true;
            else mCard_ID = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog1(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mFamily_Book = true;
            else mFamily_Book = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog2(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mPhoto = true;
            else mPhoto = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog3(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mCard_Work = true;
            else mCard_Work = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog4(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mCard_ID1 = true;
            else mCard_ID1 = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog5(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mFamily_Book1 = true;
            else mFamily_Book1 = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog6(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mPhoto1 = true;
            else mPhoto1 = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog7(String[] items, EditText editText){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.ThemeOverlay_AppCompat_Dialog_Alert);
        builder.setTitle(getString(R.string.choose_item));
        int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(items, checkedItem, (dialog, which) -> {
            if (which == 0)
                mCard_Work1 = true;
            else mCard_Work1 = false;
//            Toast.makeText(this, "Position: " + which + " Value: " + items[which], Toast.LENGTH_LONG).show();
            editText.setText(items[which]);
            dialog.dismiss();
        });
//        builder.setPositiveButton("Done", (dialog, which) -> dialog.dismiss());
        dialog = builder.create();
        dialog.show();
    }
    private void putapi(){
        Service api1 = Client.getClient().create(Service.class);
        loanItem = new loan_item(itemTwo.getLoan_Amount(),0,Integer.parseInt(itemTwo.getLoan_Term()),itemTwo.getItemOne().getTotal_Income(),itemTwo.getItemOne().getTotal_Expense(),9,1,pk,itemTwo.getItemOne().getmProductId(),pk,pk,null,itemTwo.getItemOne().getName(),null,0,itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getPhone_Number(),itemTwo.getItemOne().getProvince(),itemTwo.getItemOne().getDistrict(),itemTwo.getItemOne().getCommune(),itemTwo.getItemOne().getVillage(),mCard_ID,mFamily_Book,mCard_Work,mPhoto,itemTwo.getItemOne().getmProvinceID(),itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getJob_Period(),itemTwo.getLoan_RepaymentType(),itemTwo.getDeposit_Amount(),itemTwo.getBuying_InsuranceProduct(),itemTwo.getAllow_visito_home(),Integer.parseInt(itemTwo.getNumber_institution()),mCard_ID1,mFamily_Book1,mPhoto1,mCard_Work1,itemTwo.getItemOne().getRelationship(),itemTwo.getItemOne().getCo_borrower_Job(),itemTwo.getItemOne().getCo_Job_Period(),itemTwo.getItemOne().isCo_borrower(),Float.parseFloat(itemTwo.getMonthly_AmountPaid_Institurion()));
//        loanItem = new loan_item(158,1200,0,3,"Test",1,1,"Thou","male",19,"Student",600,300,"1234567","st 273",true,false,true,false,2,"185","null",185,null,null,null,null,202,7,null,null,null,"seller","2","1",false,true,4,"1234",false,false,true,false,true);
        Call<loan_item> call = api1.setCreateLoan(loanItem,basicEncode);
        call.enqueue(new Callback<loan_item>() {
            @Override
            public void onResponse(Call<loan_item> call, Response<loan_item> response) {
//                try {
//                    Log.d("Bodybody", response.errorBody().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                if (response.isSuccessful()){
                    MaterialDialog();
                }
            }

            @Override
            public void onFailure(Call<loan_item> call, Throwable t) {
                Log.d("ErroronFailure121212", t.getMessage());
            }
        });
    }
    private void Editloan(){
        Service api = Client.getClient().create(Service.class);
        loanItem = new loan_item(itemTwo.getLoan_Amount(),0,Integer.parseInt(itemTwo.getLoan_Term()),itemTwo.getItemOne().getTotal_Income(),itemTwo.getItemOne().getTotal_Expense(),9,1,pk,itemTwo.getItemOne().getmProductId(),pk,pk,null,itemTwo.getItemOne().getName(),null,0,itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getPhone_Number(),itemTwo.getItemOne().getProvince(),itemTwo.getItemOne().getDistrict(),itemTwo.getItemOne().getCommune(),itemTwo.getItemOne().getVillage(),mCard_ID,mFamily_Book,mCard_Work,mPhoto,itemTwo.getItemOne().getmProvinceID(),itemTwo.getItemOne().getJob(),itemTwo.getItemOne().getJob_Period(),itemTwo.getLoan_RepaymentType(),itemTwo.getDeposit_Amount(),itemTwo.getBuying_InsuranceProduct(),itemTwo.getAllow_visito_home(),Integer.parseInt(itemTwo.getNumber_institution()),mCard_ID1,mFamily_Book1,mPhoto1,mCard_Work1,itemTwo.getItemOne().getRelationship(),itemTwo.getItemOne().getCo_borrower_Job(),itemTwo.getItemOne().getCo_Job_Period(),itemTwo.getItemOne().isCo_borrower(),Float.parseFloat(itemTwo.getMonthly_AmountPaid_Institurion()));
        Call<loan_item> call = api.getEditLoan(itemTwo.getItemOne().getmLoanID(),loanItem,basicEncode);
        call.enqueue(new Callback<loan_item>() {
            @Override
            public void onResponse(Call<loan_item> call, Response<loan_item> response) {
                if (!response.isSuccessful()){
                    Log.e("EditLoan Error",response.code()+"");
                    try {
                        Log.d("121212",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                try {
//                    Log.e("EditLoan",response.errorBody().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
            @Override
            public void onFailure(Call<loan_item> call, Throwable t) {
            }
        });
    }
    private void dialog_Editloan() {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View clearDialogView = factory.inflate(R.layout.layout_alert_dialog, null);
        final AlertDialog clearDialog = new AlertDialog.Builder(getContext()).create();
        clearDialog.setView(clearDialogView);
        TextView Mssloan = clearDialogView.findViewById(R.id.textView_message);
        Mssloan.setText(R.string.loan_edit_m);
        Button btnYes =  clearDialogView.findViewById(R.id.button_positive);
        btnYes.setText(R.string.yes_leave);
        Button btnNo = clearDialogView.findViewById(R.id.button_negative);
        btnNo.setText(R.string.no_leave);
        clearDialogView.findViewById(R.id.button_negative).setOnClickListener(v -> {
            clearDialog.dismiss();
        });
        clearDialogView.findViewById(R.id.button_positive).setOnClickListener(v -> {
            Editloan();
//            clearDialog.dismiss();
            mProgress.show();
            getActivity().finish();
        });
        mProgress.dismiss();
        clearDialog.show();
    }

    private void initView(View view) {
        String[] values = getResources().getStringArray(R.array.choose);
//        etID_card = view.findViewById(R.id.etID_card);
//        etID_card.setOnClickListener(v -> {
//            AlertDialog(values,etID_card);
//        });
//        etFamily_book = view.findViewById(R.id.etFamily_book);
//        etFamily_book.setOnClickListener(v -> {
//            AlertDialog1(values,etFamily_book);
//        });
//        etPhotos = view.findViewById(R.id.etPhotos);
//        etPhotos.setOnClickListener(v -> {
//            AlertDialog2(values,etPhotos);
//        });
//        etEmployment_card = view.findViewById(R.id.etEmployment_card);
//        etEmployment_card.setOnClickListener(v -> {
//            AlertDialog3(values,etEmployment_card);
//        });
//
//        etID_card1 = view.findViewById(R.id.etID_card1);
//        etID_card1.setOnClickListener(v -> {
//            AlertDialog4(values,etID_card1);
//        });
//        etFamily_book1 = view.findViewById(R.id.etFamily_book1);
//        etFamily_book1.setOnClickListener(v -> {
//            AlertDialog5(values,etFamily_book1);
//        });
//        etPhotos1 = view.findViewById(R.id.etPhotos1);
//        etPhotos1.setOnClickListener(v -> {
//            AlertDialog6(values,etPhotos1);
//        });
//        etEmployment_card1 = view.findViewById(R.id.etEmployment_card1);
//        etEmployment_card1.setOnClickListener(v -> {
//            AlertDialog7(values,etEmployment_card1);
//        });
//        if (itemTwo.getItemOne().getIndex()==1){
//            linearLayout.setVisibility(View.GONE);
//        }else {
//            linearLayout.setVisibility(View.VISIBLE);
//        }
//        if (itemTwo.getItemOne().isFromLoan()){
//            GetLoan();
//        }

    }
    private boolean checkEd(){

//        bID_Card = createLoad.CheckedYear(etID_card);
//        bFramily_Book = createLoad.CheckedYear(etFamily_book);
//        bPhotos = createLoad.CheckedYear(etPhotos);
//        bEmployment_card = createLoad.CheckedYear(etEmployment_card);
//
//        bID_Card1 = createLoad.CheckedYear(etID_card1);
//        bFramily_Book1 = createLoad.CheckedYear(etFamily_book1);
//        bPhotos1 = createLoad.CheckedYear(etPhotos1);
//        bEmployment_card1 = createLoad.CheckedYear(etEmployment_card1);


//        bNumber_institution = createLoad.CheckedYear(mNumber_institution);
//        bMonthly_Amount_Paid = createLoad.CheckedYear(mMonthly_Amount_Paid);

//        createLoad.ConditionYear(img1,etID_card);
//        createLoad.ConditionYear(img2,etFamily_book);
//        createLoad.ConditionYear(img3,etPhotos);
//        createLoad.ConditionYear(img4,etEmployment_card);
//
//        createLoad.ConditionYear(img5,etID_card1);
//        createLoad.ConditionYear(img6,etFamily_book1);
//        createLoad.ConditionYear(img7,etPhotos1);
//        createLoad.ConditionYear(img8,etEmployment_card1);

//        if (itemTwo.getItemOne().getIndex()==1){
//            return bID_Card&&bFramily_Book&&bPhotos&&bEmployment_card;
//        }else {
//            return bID_Card&&bFramily_Book&&bPhotos&&bEmployment_card&&bID_Card1&&bFramily_Book1&&bPhotos1&&bEmployment_card1;
//        }
        return bID_Card&&bFramily_Book&&bPhotos&&bEmployment_card&&bID_Card1&&bFramily_Book1&&bPhotos1&&bEmployment_card1;
    }

    private void MaterialDialog(){
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.title_create_loan));
        alertDialog.setMessage(getString(R.string.loan_message));
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog, which) -> {
            Intent intent = new Intent(getContext(), Detail_New_Post.class);
            intent.putExtra("ID", itemTwo.getItemOne().getmProductId());
            startActivity(intent);
            mProgress.show();
            getActivity().finish();
            dialog.dismiss();
        });
        alertDialog.show();
    }
    private void GetLoan(){
        Service api = Client.getClient().create(Service.class);
        Call<loan_item> call = api.getDeailLoan(itemTwo.getItemOne().getmLoanID(),basicEncode);
        call.enqueue(new Callback<loan_item>() {
            @Override
            public void onResponse(Call<loan_item> call, Response<loan_item> response) {
                if (!response.isSuccessful()){
                    Log.e("ONRESPONSE ERROR", String.valueOf(response.code()));
                }
               if (response.body().isState_id()){
                   etID_card.setText(getString(R.string.yes));
                   mCard_ID = true;
               } else {etID_card.setText(getString(R.string.no)); mCard_ID = false;}

                if (response.body().isFamily_book()){
                    etFamily_book.setText(getString(R.string.yes)); mFamily_Book = true;
                } else{ etFamily_book.setText(getString(R.string.no));mFamily_Book =false;}

                if (response.body().isIs_borrower_photo()){
                    etPhotos.setText(getString(R.string.yes)); mPhoto = true;
                } else {etPhotos.setText(getString(R.string.no));mPhoto = false;}

                if (response.body().isStaff_id()){
                    etEmployment_card.setText(getString(R.string.yes)); mCard_Work = true;
                } else{ etEmployment_card.setText(getString(R.string.no)); mCard_Work = false;}

                //co-borrow
                if (response.body().isIs_coborrower_idcard()){
                    etID_card1.setText(getString(R.string.yes)); mCard_ID1 = true;
                }else {  etID_card1.setText(getString(R.string.no)); mCard_ID1 = false;}
                if (response.body().isIs_coborrower_familybook()){
                    etFamily_book1.setText(getString(R.string.yes)); mFamily_Book1 = true;
                }else {etFamily_book1.setText(getString(R.string.no)); mFamily_Book1 = false;}
                if (response.body().isIs_coborrower_photo()){
                    etPhotos1.setText(getString(R.string.yes)); mPhoto1= true;
                }else {etPhotos1.setText(getString(R.string.no)); mPhoto1 = false;}
                if (response.body().isIs_coborrower_payslip()){
                    etEmployment_card1.setText(getString(R.string.yes)); mCard_Work1= true;
                }else {etEmployment_card1.setText(getString(R.string.no)); mCard_Work1= false;}
            }
            @Override
            public void onFailure(Call<loan_item> call, Throwable t) { }
        });
    }
}
