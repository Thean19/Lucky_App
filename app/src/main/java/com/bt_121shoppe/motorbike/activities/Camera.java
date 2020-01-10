package com.bt_121shoppe.motorbike.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.bt_121shoppe.motorbike.Api.api.Active_user;
import com.bt_121shoppe.motorbike.adapters.CustomView;
import com.bt_121shoppe.motorbike.adapters.ColorAdapter;
import com.bt_121shoppe.motorbike.Api.api.Client;
import com.bt_121shoppe.motorbike.Api.api.Service;
import com.bt_121shoppe.motorbike.Api.api.model.UserResponseModel;
import com.bt_121shoppe.motorbike.Api.api.model.dealershop;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseCondition;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseYear;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseType;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseCategory;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseTypeCate;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseBrand;
import com.bt_121shoppe.motorbike.BottomSheetDialog.BottomChooseModel;
import com.bt_121shoppe.motorbike.Login_Register.Register;
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction;
import com.bt_121shoppe.motorbike.fragments.FragmentMap;
import com.bt_121shoppe.motorbike.models.CreatePostModel;
import com.bt_121shoppe.motorbike.models.PostDealerShopViewModel;
import com.bt_121shoppe.motorbike.models.ShopViewModel;
import com.bt_121shoppe.motorbike.models.UserShopViewModel;
import com.bt_121shoppe.motorbike.utils.CommonFunction;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bt_121shoppe.motorbike.Api.ConsumeAPI;
import com.bt_121shoppe.motorbike.Api.User;
import com.bt_121shoppe.motorbike.R;
import com.bt_121shoppe.motorbike.utils.FileCompressor;
import com.bt_121shoppe.motorbike.utils.ImageUtil;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


//import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Camera extends AppCompatActivity implements BottomChooseCondition.ItemClickListener,
        BottomChooseYear.ItemClickListener,BottomChooseBrand.ItemClickListener,BottomChooseModel.ItemClickListener,
        BottomChooseCategory.ItemClickListener, BottomChooseType.ItemClickListener,BottomChooseTypeCate.ItemClickListener{
    private static final int REQUEST_LOCATION = 1;
    public static final int[] itemcolor = new int[]{
            R.drawable.white,
            R.drawable.blue,
            R.drawable.ligth_black,
            R.drawable.red,
            R.drawable.yellow,
            R.drawable.pink,
            R.drawable.purple,
            R.drawable.oriange,
            R.drawable.dark_blue,
            R.drawable.dark_gray,
            R.drawable.dark_green,
            R.drawable.dark_red,
            R.drawable.light_blue_sky,
            R.drawable.light_red,
            R.drawable.ligth_green,
            R.drawable.ligth_gray,
            R.drawable.green,
            R.drawable.blue_sky
    };
    LocationManager locationManager;
    double latitude,longtitude;
    private GoogleMap mMap;
    private String latlng;
    SupportMapFragment mapFragment;
    Constraints Layout_call_chat_like_loan;
    private static final String TAG = Camera.class.getSimpleName();
    static final int REQUEST_TAKE_PHOTO_1=1;
    static final int REQUEST_TAKE_PHOTO_2=2;
    static final int REQUEST_TAKE_PHOTO_3=3;
    static final int REQUEST_TAKE_PHOTO_4=4;
    static final int REQUEST_TAKE_PHOTO_5=9;
    static final int REQUEST_TAKE_PHOTO_6=10;
    static final int REQUEST_GALLERY_PHOTO_1=5;
    static final int REQUEST_GALLERY_PHOTO_2=6;
    static final int REQUEST_GALLERY_PHOTO_3=7;
    static final int REQUEST_GALLERY_PHOTO_4=8;
    static final int REQUEST_GALLERY_PHOTO_5=11;
    static final int REQUEST_GALLERY_PHOTO_6=12;
    private int REQUEST_TAKE_PHOTO_NUM=0;
    private int REQUEST_CHOOSE_PHOTO_NUM=0;
    private File mPhotoFile;
    private FileCompressor mCompressor;
    private ScrollView scrollView;
    private EditText etDescription,etPrice,etDiscount_amount,etPhone1,etPhone2,etPhone3,etEmail,etAddress,etMap,etName;
    private ImageButton btremove_pic1,btremove_pic2,btremove_pic3,btremove_pic4,btremove_pic5,btremove_pic6;
    private SearchView tvAddress;
    private Button submit_post,bt_update;
    private EditText tvPostType,tvCondition,tvYear,tvCategory,tvType_cate,tvBrand,tvModel;
    private ImageView imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageMap,imgAdd_color,cancel_color;
    private String user_name,user_email,user_phone,user_address,user_address_name;
    private String edit_name,edit_email,edit_phone,edit_address,edit_address_name;
    private String name,pass,Encode,road,name_post;
    private int pk,id_typeother=0;
    private ArrayAdapter<String> brands,models;
    private ArrayAdapter<Integer> ID_category,ID_brands,ID_type,ID_year,ID_model,id_brand_Model;
    private List<String> list_category = new ArrayList<>();
    private List<String> list_type = new ArrayList<>();
    private List<String> list_brand = new ArrayList<>();
    private List<String> list_model = new ArrayList<>();
    private List<String> list_year= new ArrayList<>();
    private List<Integer> list_id_category = new ArrayList<>();
    private List<Integer> list_id_type = new ArrayList<>();
    private List<Integer> list_id_brands = new ArrayList<>();
    private List<Integer> list_id_model = new ArrayList<>();
    private List<Integer> list_id_year = new ArrayList<>();
    private List<Integer> list_brand_model = new ArrayList<>();
    private int seekbar_price = 0,seekbar_rearr = 0,seekbar_screww = 0, seekbar_engine = 0, seekbar_head = 0,assembly = 0,seekbar_accessorie = 0,seekbar_consolee = 0,seekbar_pump = 0,whole_ink=0;
    private String yearr,discount_price,price,modell,brandd,categoryy,post_typee,condition,email_post,address_post,phone_number1_post,phone_number2_post,phone_number3_post,description;
    String id_cate, id_brand,id_model,id_year,id_type, login_verify,register_intent,strPostType,strCondition,strColor,strColorKH="";
    String num_used="0",color;
    int num_used1 = 0;
    int num_use = 100;

    int idYear=0,process_type=0,post_type=0,category=0;
    int cate=0,brand=0,model=0,year=0,type=0;
    SharedPreferences prefer,pre_id;
    ProgressDialog mProgress;
    private Bitmap bitmapImage1,bitmapImage2,bitmapImage3,bitmapImage4,bitmapImage5,bitmapImage6,default_bitmap;

    int edit_id,status;
    Bundle bundle;
    int mmodel=1;
    Boolean my_name = true;
    RelativeLayout layout_color;
    LinearLayout layout_estimate;

    List<dealershop>list_shop = new ArrayList<>();
    List<Integer>listid_shop = new ArrayList<>();
    List<Integer> idshop = new ArrayList<>();
    private SeekBar seekbar_pri_per,seekbar_rear,seekbar_screw,seekbar_pumps,seekbar_rigt_engine,seekbar_engine_head,seekbar_assmebly,seekbar_console,seekbar_accessories,seekbar_whole;

    //end
    private RelativeLayout layout_phone1,layout_phone2;
    private int mDealerShopId1=0,mDealerShopId2=0,mDealerShopId3=0;
    private Bitmap bitmapImage,bitmapImage10,bitmapImage20;
    private List<UserShopViewModel> userShops;
    private Button btnPos;
    private Button btnNag;
    private TextView delete_massage,tv_add,tv_add1,tv_cancel,tvType_elec,tv_name,title_dicount;
    private CircleImageView btnlogo;
    private Button Cancel,Submit;
    private String[] photoChooseOption;
    private static final int REQUEST_GALLARY_PHOTO=2;
    private static final int REQUEST_TAKE_PHOTO=1;
    private Uri imageUri;

    private String[] postTypeListItems,conditionListItems,modelListItemkh,discountTypeListItems,brandListItemkh,typeListItemkh,categoryListItemkh,colorListItems,yearListItems,categoryListItems,typeListItems,brandListItem,modelListItems,shopListItems;
    private int[] yearIdListItems,categoryIdListItems,typeIdListItems,brandIdListItems,modelIdListItems,shopIdListItems,postShopListItems;
    private List<PostDealerShopViewModel> postShops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);

        // end
        scrollView = findViewById(R.id.scroll_post);
        prefer = getSharedPreferences("Register",MODE_PRIVATE);
        name = prefer.getString("name","");
        pass = prefer.getString("pass","");
        Encode = getEncodedString(name,pass);
        if (prefer.contains("token")) {
            pk = prefer.getInt("Pk",0);
        }else if (prefer.contains("id")) {
            pk = prefer.getInt("id", 0);
        }
        Log.d("Pk",""+pk);
        //ButterKnife.bind(this);
        //check active and deactive account by samang 2/09/19
        Active_user activeUser = new Active_user();
        String active;
        active = activeUser.isUserActive(pk,this);
        if (active.equals("false")){
            activeUser.clear_session(this);
        }
        // end
        mCompressor = new FileCompressor(this);
        //     Log.d(TAG,"time"+Instant.now().toString());

        TextView back = (TextView) findViewById(R.id.tv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog builder = new android.app.AlertDialog.Builder(Camera.this).create();
                builder.setMessage(getString(R.string.back_message));
                builder.setCancelable(false);
                builder.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.back_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (register_intent!=null || login_verify!=null){
                            startActivity(new Intent(Camera.this,Home.class));
                        }else finish();
                    }
                });
                builder.setButton(Dialog.BUTTON_NEGATIVE,getString(R.string.back_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.dismiss();
                    }
                });
                builder.show();
            }
        });

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");

        Toolbar toolbar=findViewById(R.id.toolbar);
        pre_id = getSharedPreferences("id",MODE_PRIVATE);
        Variable_Field();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_post);
        DropDown();
        mSeekbar();

        initialUserInformation(pk,Encode);

        bundle = getIntent().getExtras();
        if (bundle!=null) {
            road = bundle.getString("road");
            latlng = bundle.getString("location");
            login_verify = bundle.getString("Login_verify");
            register_intent = bundle.getString("Register_verify");
            process_type=bundle.getInt("process_type",0);
            if(process_type==0){
                submit_post.setVisibility(View.VISIBLE);
                bt_update.setVisibility(View.GONE);
                category = bundle.getInt("category_post",0);
                strPostType = tvPostType.getText().toString();
                if(strPostType.equals("sell")){
                    tvPostType.setText(R.string.sel);
                    toolbar.setBackgroundColor(getColor(R.color.logo_green));
                }else if(strPostType.equals("rent")){
                    tvPostType.setText(R.string.ren);
                    toolbar.setBackgroundColor(getColor(R.color.logo_red));
                }

//                getCategegoryName(Encode,cate);

                if(category==1){
                    tvType_elec.setVisibility(View.VISIBLE);
                    tvType_cate.setVisibility(View.VISIBLE);
                }else {
                    tvType_elec.setVisibility(View.GONE);
                    tvType_cate.setVisibility(View.GONE);
                }
            }else {
                bt_update.setVisibility(View.VISIBLE);
                submit_post.setVisibility(View.GONE);
                tv_name.setVisibility(View.VISIBLE);
                etName.setVisibility(View.VISIBLE);
                edit_id = bundle.getInt("id_product", 0);
                getData_Post(Encode,edit_id);
//                Log.e("Edit_id:", String.valueOf(edit_id));
            }
//            Log.e("Type ID", String.valueOf(type));
            price = bundle.getString("price");
            modell = bundle.getString("model");
            brandd = bundle.getString("brand");
            condition = bundle.getString("condition");
            description = bundle.getString("description");
            categoryy = bundle.getString("category");
            post_typee = bundle.getString("post_type");
            email_post = bundle.getString("email_post");
            address_post = bundle.getString("address_post");
            phone_number1_post = bundle.getString("phone_number1_post");
            phone_number2_post = bundle.getString("phone_number2_post");
            phone_number3_post = bundle.getString("phone_number3_post");
            discount_price = bundle.getString("discount_amount");
            seekbar_price = bundle.getInt("discount_percent",0);
            yearr = bundle.getString("year");
            whole_ink = bundle.getInt("whole_ink",0);
            seekbar_rearr = bundle.getInt("rear",0);
            seekbar_screww = bundle.getInt("screw",0);
            seekbar_pump = bundle.getInt("pumps",0);
            seekbar_engine = bundle.getInt("right_engine",0);
            seekbar_head = bundle.getInt("engine_head",0);
            assembly = bundle.getInt("assembly",0);
            seekbar_consolee = bundle.getInt("console",0);
            seekbar_accessorie = bundle.getInt("accessories",0);
            name_post = bundle.getString("name_post");
            etName.setText(name_post);
            etPrice.setText(price);
            tvYear.setText(yearr);
            tvModel.setText(modell);
            tvBrand.setText(brandd);
            tvCondition.setText(condition);
            etEmail.setText(email_post);
            etDescription.setText(description);
            tvCategory.setText(categoryy);
            tvPostType.setText(post_typee);
            etAddress.setText(address_post);
            etPhone1.setText(phone_number1_post);
            etPhone2.setText(phone_number2_post);
            etPhone3.setText(phone_number3_post);
            etDiscount_amount.setText(discount_price);
            seekbar_pri_per.setProgress(seekbar_price);
            seekbar_whole.setProgress(whole_ink);
            seekbar_engine_head.setProgress(seekbar_head);
            seekbar_rigt_engine.setProgress(seekbar_engine);
            seekbar_assmebly.setProgress(assembly);
            seekbar_console.setProgress(seekbar_consolee);
            seekbar_accessories.setProgress(seekbar_accessorie);
            seekbar_pumps.setProgress(seekbar_pump);
            seekbar_screw.setProgress(seekbar_screww);
            seekbar_rear.setProgress(seekbar_rearr);
            etMap.setText(road);
        }

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_1;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_1;
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_2;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_2;
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_3;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_3;
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_4;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_4;
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_5;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_5;
            }
        });

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_6;
                REQUEST_CHOOSE_PHOTO_NUM=REQUEST_GALLERY_PHOTO_6;
            }
        });
        //dd by Raksmey
        btremove_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView1 != null){
                    imageView1.setImageResource(R.drawable.icon_camera);
                    bitmapImage1=null;
                    btremove_pic1.setVisibility(View.GONE);
                }
            }
        });
        btremove_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView2 !=null){
                    imageView2.setImageResource(R.drawable.icon_camera);
                    bitmapImage2=null;
                    btremove_pic2.setVisibility(View.GONE);
                }
            }
        });
        btremove_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView3 !=null){
                    imageView3.setImageResource(R.drawable.icon_camera);
                    bitmapImage3=null;
                    btremove_pic3.setVisibility(View.GONE);
                }
            }
        });
        btremove_pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView4 !=null){
                    imageView4.setImageResource(R.drawable.icon_camera);
                    bitmapImage4=null;
                    btremove_pic4.setVisibility(View.GONE);
                }
            }
        });
        btremove_pic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView5 !=null){
                    imageView5.setImageResource(R.drawable.icon_camera);
                    bitmapImage5=null;
                    btremove_pic5.setVisibility(View.GONE);
                }
            }
        });
        btremove_pic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageView6 !=null){
                    imageView6.setImageResource(R.drawable.icon_camera);
                    bitmapImage6=null;
                    btremove_pic6.setVisibility(View.GONE);
                }
            }
        });
        tv_add.setVisibility(View.VISIBLE);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_phone1.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.VISIBLE);
            }
        });
        tv_add1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_phone2.setVisibility(View.VISIBLE);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_phone1.setVisibility(View.GONE);
                layout_phone2.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
            }
        });

//End
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stDis_amount,stDis_percent,stPrice;
                double dbDis_amount = 0 , dbDis_percent = 0, dbPrice ;
                double ko = 0.0;
                stPrice = etPrice.getText().toString();

                if (stPrice == null || stPrice.isEmpty()){
                    dbPrice = 1;
                }else {
                    dbPrice = Double.parseDouble(stPrice);
                }
                stDis_amount = etDiscount_amount.getText().toString();
                if (stDis_amount == null || stDis_amount.isEmpty()){
                    dbDis_percent = 0;
                    dbDis_amount = 0;
                }
                int image_value ;
                String postType = tvPostType.getText().toString();
                if (postType.equals("Buy") || postType.equals("ទិញ")){
                    image_value = 1;
                }else if (bitmapImage1==null||bitmapImage2==null||bitmapImage3==null||bitmapImage4==null){
                    image_value = 0;
                }else image_value = 1;

                if (tvPostType.getText().toString().length()==0||tvCategory.getText().toString().length()==0
                        || tvBrand.getText().toString().length()==0 || tvModel.getText().toString().length()==0 || tvYear.getText().toString().length()==0
                        || etPrice.getText().toString().length()==0 || etPhone1.getText().toString().length() < 9 || dbDis_percent >=100|| dbDis_amount >= dbPrice
                        ||  image_value == 0) {
                    if (etPhone1.getText().toString().length()<9){
                        etPhone1.requestFocus();
                    }
                    if (dbDis_percent >= 100 || dbDis_amount >= dbPrice){
                        etDiscount_amount.requestFocus();
                    }
                    if (etPrice.getText().toString().length()==0) {
                        etPrice.requestFocus();
                    }
                    if (tvYear.getText().toString().length()==0) {
                        tvYear.requestFocus();
                    }
                    if (tvModel.getText().toString().length()==0) {
                        tvModel.requestFocus();
                    }
                    if (tvBrand.getText().toString().length()==0) {
                        tvBrand.requestFocus();
                    }

                    if (tvCategory.getText().toString().length()==0) {
                        tvCategory.requestFocus();
                    }
                    if (tvPostType.getText().toString().length()==0){
                        tvPostType.requestFocus();
                    }
                    if (tvPostType.getText().toString().length()==0){
                        tvPostType.requestFocus();
                    }
                    if (strColor.isEmpty() || strColor == null ){
                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                        alertDialog.setMessage(Camera.this.getString(R.string.missing_color));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    if (dbDis_percent >= 100){
                        etDiscount_amount.requestFocus();
                    }

                    if (bitmapImage1==null||bitmapImage2==null||bitmapImage3==null||bitmapImage4==null){
                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                        alertDialog.setMessage(Camera.this.getString(R.string.missing_image));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }

                } else if (category == 1 && tvType_cate.getText().toString().length()==0){
                    tvType_cate.requestFocus();
                } else if (bundle!=null) {
                    mProgress.show();
                    EditPost_Approve(Encode, edit_id);
                } else  {
                    mProgress.show();
                    EditPost_Approve(Encode, edit_id);
                }
            }
        });
        submit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stDis_amount,stDis_percent,stPrice;
                double dbDis_amount = 0 , dbDis_percent = 0, dbPrice ;
                double ko = 0.0;
                stPrice = etPrice.getText().toString();

                if (stPrice == null || stPrice.isEmpty()){
                    dbPrice = 1;
                }else {
                    dbPrice = Double.parseDouble(stPrice);
                }
                stDis_amount = etDiscount_amount.getText().toString();
                if (stDis_amount == null || stDis_amount.isEmpty()){
                    dbDis_percent = 0;
                    dbDis_amount = 0;
                }
                int image_value ;
                String postType = tvPostType.getText().toString();
                if (postType.equals("Buy") || postType.equals("ទិញ")){
                    image_value = 1;
                }else if (bitmapImage1==null||bitmapImage2==null||bitmapImage3==null||bitmapImage4==null){
                    image_value = 0;
                }else image_value = 1;

                if (tvPostType.getText().toString().length()==0||tvCategory.getText().toString().length()==0
                        || tvBrand.getText().toString().length()==0 || tvModel.getText().toString().length()==0 || tvYear.getText().toString().length()==0
                        || etPrice.getText().toString().length()==0 || etPhone1.getText().toString().length() < 9 || dbDis_percent >=100|| dbDis_amount >= dbPrice
                        ||  image_value == 0) {
                    if (etPhone1.getText().toString().length()<9){
                        etPhone1.requestFocus();
                    }
                    if (dbDis_percent >= 100 || dbDis_amount >= dbPrice){
                        etDiscount_amount.requestFocus();
                    }
                    if (etPrice.getText().toString().length()==0) {
                        etPrice.requestFocus();
                    }
                    if (tvYear.getText().toString().length()==0) {
                        tvYear.requestFocus();
                    }
                    if (tvModel.getText().toString().length()==0) {
                        tvModel.requestFocus();
                    }
                    if (tvBrand.getText().toString().length()==0) {
                        tvBrand.requestFocus();
                    }

                    if (tvCategory.getText().toString().length()==0) {
                        tvCategory.requestFocus();
                    }
                    if (tvPostType.getText().toString().length()==0){
                        tvPostType.requestFocus();
                    }
                    if (tvPostType.getText().toString().length()==0){
                        tvPostType.requestFocus();
                    }
                    if (color == null ){
                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                        alertDialog.setMessage(Camera.this.getString(R.string.missing_color));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    if (dbDis_percent >= 100){
                        etDiscount_amount.requestFocus();
                    }

                    if (bitmapImage1==null||bitmapImage2==null||bitmapImage3==null||bitmapImage4==null){
                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                        alertDialog.setMessage(Camera.this.getString(R.string.missing_image));
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }

                } else if (category == 1 && tvType_cate.getText().toString().length()==0){
                    tvType_cate.requestFocus();
                } else if (bundle!=null) {
                    mProgress.show();
                    if(process_type==0){
                        if (cate == 2){
                            type = Integer.parseInt(tvType_cate.getText().toString());
                        }
                        Log.d("Type id brs", String.valueOf(type));
                        PostData(Encode);
                    }
                } else  {
                    mProgress.show();
                    PostData(Encode);
                }
            }
        });

    } // create

    private void dialog_dealer(){
        AlertDialog dialog = new AlertDialog.Builder(Camera.this).create();
        dialog.setTitle(R.string.for_loan_title);
        dialog.setMessage(getString(R.string.already_select_shop));
        dialog.setCancelable(false);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog builder = new android.app.AlertDialog.Builder(Camera.this).create();
        builder.setMessage(getString(R.string.back_message));
        builder.setCancelable(false);
        builder.setButton(Dialog.BUTTON_POSITIVE,getString(R.string.back_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (register_intent!=null || login_verify!=null){
                    startActivity(new Intent(Camera.this,Home.class));
                }
                else {
                    finish();
                }
            }
        });
        builder.setButton(Dialog.BUTTON_NEGATIVE,getString(R.string.back_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.dismiss();
            }
        });
        builder.show();
    }

    private void getData_Post(String encode, int id){
        if (bundle!=null) {
            final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL, "postbyuser/", id);
            Log.d("Url", url);
            OkHttpClient client = new OkHttpClient();
            String auth = "Basic " + encode;
            Request request = new Request.Builder()
                    .url(url)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", auth)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String respon = response.body().string();
                    Log.d("Edit Response:", respon);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject object = new JSONObject(respon);
                                String description = object.getString("description");
                                int price = object.getInt("cost");
                                int id = object.getInt("user");
                                String search_title = object.getString("vin_code");
                                String name = object.getString("machine_code");
                                String discount = object.getString("discount");
                                String email = object.getString("contact_email");
                                String post_sub_title = object.getString("post_sub_title");
                                strPostType = object.getString("post_type");
                                cate = object.getInt("category");
                                type = object.getInt("type");

                                int eta1 = object.getInt("used_eta1");
                                int eta2 = object.getInt("used_eta2");
                                int eta3 = object.getInt("used_eta3");
                                int eta4 = object.getInt("used_eta4");
                                int machine1 = object.getInt("used_machine1");
                                int machine2 = object.getInt("used_machine2");
                                int machine3 = object.getInt("used_machine3");
                                int machine4 = object.getInt("used_machine4");
                                int other1 = object.getInt("used_other1");

                                seekbar_whole.setProgress(eta1);
                                seekbar_rear.setProgress(eta2);
                                seekbar_screw.setProgress(eta3);
                                seekbar_pumps.setProgress(eta4);
                                seekbar_rigt_engine.setProgress(machine1);
                                seekbar_engine_head.setProgress(machine2);
                                seekbar_assmebly.setProgress(machine3);
                                seekbar_console.setProgress(machine4);
                                seekbar_accessories.setProgress(other1);

                                String phone = object.getString("contact_phone");

                                if (cate == 2) {
                                    type = 3;
                                    tvType_elec.setVisibility(View.GONE);
                                    tvType_cate.setVisibility(View.GONE);
                                } else {
                                    tvType_elec.setVisibility(View.VISIBLE);
                                    tvType_cate.setVisibility(View.VISIBLE);
                                }

                                model = object.getInt("modeling");
                                mmodel = object.getInt("modeling");
                                year = object.getInt("year");

                                strCondition = object.getString("condition");
                                strColor = object.getString("color");


                                Geocoder geocoder;
                                List<Address> addresses;
                                geocoder = new Geocoder(getApplication(), Locale.getDefault());
                                String locat = object.getString("contact_address");
                                if (!locat.isEmpty()) {
                                    String add[] = locat.split(",");
                                    Double latetitude = Double.parseDouble(add[0]);
                                    Double longtitude = Double.parseDouble(add[1]);
                                    try {
                                        addresses = geocoder.getFromLocation(latetitude, longtitude, 1);
                                        String road = addresses.get(0).getAddressLine(0);
                                        if (road.length() > 30) {
                                            String loca = road.substring(0,30) + "...";
                                            if (road != null){
                                                if (road.length() > 30) {
                                                    String locate = road.substring(0,30) + "...";
                                                    etMap.setText(locate);
                                                }
                                            }else {
                                                etMap.setText(loca);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if(!search_title.isEmpty()) {
                                    etAddress.setText(search_title);
                                }

                                String fron = object.getString("front_image_path");
                                String back = object.getString("back_image_path");
                                String left = object.getString("left_image_path");
                                String right = object.getString("right_image_path");
                                String extra1 = object.getString("extra_image1");
                                String extra2 = object.getString("extra_image2");

                                List<String> list = new ArrayList<>();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        etDescription.setText(description);
                                        etPrice.setText(String.valueOf(price));
                                        etEmail.setText(email);
                                        etDiscount_amount.setText(discount);

                                        String[] splitPhone = phone.split(",");
                                        if (splitPhone.length == 1){
                                            etPhone1.setText(String.valueOf(splitPhone[0]));
                                        }else if (splitPhone.length == 2){
                                            etPhone1.setText(String.valueOf(splitPhone[0]));
                                            etPhone2.setText(String.valueOf(splitPhone[1]));
                                        }else if (splitPhone.length == 3){
                                            etPhone1.setText(String.valueOf(splitPhone[0]));
                                            etPhone2.setText(String.valueOf(splitPhone[1]));
                                            etPhone3.setText(String.valueOf(splitPhone[2]));
                                        }

                                        String postType = strPostType.substring(0,1).toUpperCase() + strPostType.substring(1);
                                        String condition= strCondition.substring(0,1).toUpperCase() + strCondition.substring(1);

                                        String color= strColor.substring(0,1).toLowerCase() + strColor.substring(1);

                                        if (postType.equals("Sell")){
                                            tvPostType.setText(R.string.sel);
                                        }else if (postType.equals("Rent")){
                                            tvPostType.setText(R.string.ren);
                                        }else if (postType.equals("Buy")){
                                            tvPostType.setText(R.string.bu);
                                        }

                                        if (condition.equals("New")){
                                            tvCondition.setText(R.string.newl);
                                        }else if (condition.equals("Used")){
                                            tvCondition.setText(R.string.used);
                                            layout_estimate.setVisibility(View.VISIBLE);
                                        }

                                        //Call_Model(Encode,brand);
                                        getCategegoryName(Encode,cate);
                                        getTypeName(Encode,type);
                                        getYearName(Encode,year);
                                        getModelName(Encode,model);
                                        getUsername(id);
                                        //Log.d(TAG,"Brand_id"+ brand);

                                        Glide.with(Camera.this).asBitmap().load(fron).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView1.setImageBitmap(resource);
                                                bitmapImage1 = resource;
                                                btremove_pic1.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(back).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView2.setImageBitmap(resource);
                                                bitmapImage2 = resource;
                                                Log.d("BITMAP","2:"+bitmapImage2);
                                                btremove_pic2.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(left).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView3.setImageBitmap(resource);
                                                bitmapImage3 = resource;
                                                btremove_pic3.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(right).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView4.setImageBitmap(resource);
                                                bitmapImage4 = resource;
                                                btremove_pic4.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(extra1).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView5.setImageBitmap(resource);
                                                bitmapImage5 = resource;
                                                btremove_pic5.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });
                                        Glide.with(Camera.this).asBitmap().load(extra2).into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                imageView6.setImageBitmap(resource);
                                                bitmapImage6 = resource;
                                                btremove_pic6.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {

                                            }
                                        });


                                    }
                                });
                                model=object.getInt("modeling");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } //
                        }
                    });
                    //Log.d("Edit", respon);
                }
            });
            model=mmodel;
        }  //getextra
    }
    private void getUsername(int pk) {
        Service apiService = Client.getClient().create(Service.class);
        retrofit2.Call<UserResponseModel> call = apiService.getUserProfile(pk);
        call.enqueue(new retrofit2.Callback<UserResponseModel>() {
            @Override
            public void onResponse(retrofit2.Call<UserResponseModel> call, retrofit2.Response<UserResponseModel> response) {
                if (response.isSuccessful()) {
                    etName.setText(response.body().getFirst_name());
                }
            }
            @Override
            public void onFailure(retrofit2.Call<UserResponseModel> call, Throwable t) {

            }
        });
    }

    private void initialUserInformation(int pk, String encode) {
//        if (bundle==null || bundle.isEmpty()) {
        final String url = String.format("%s%s%s/", ConsumeAPI.BASE_URL, "api/v1/users/", pk);
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        //Log.d(TAG,"tt"+url);
        OkHttpClient client = new OkHttpClient();

        String auth = "Basic " + encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                Gson gson = new Gson();
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                                User converJsonJava = new User();
                            if (bundle == null || bundle.isEmpty()) {
                                User converJsonJava = gson.fromJson(respon, User.class);
                                int g = converJsonJava.getProfile().getGroup();
                                id_typeother = g;
                                etEmail.setText(converJsonJava.getEmail());

                                if (converJsonJava.getProfile().getTelephone() != null) {
                                    user_phone = converJsonJava.getProfile().getTelephone();
                                    String[] splitPhone = user_phone.split(",");
                                    if (splitPhone.length == 1) {
                                        etPhone1.setText(String.valueOf(splitPhone[0]));
                                    }
                                    else if (splitPhone.length == 2){
                                        etPhone1.setText(String.valueOf(splitPhone[0]));
                                        etPhone2.setText(String.valueOf(splitPhone[1]));
                                    } else if (splitPhone.length == 3) {
                                        etPhone1.setText(String.valueOf(splitPhone[0]));
                                        etPhone2.setText(String.valueOf(splitPhone[1]));
                                        etPhone3.setText(String.valueOf(splitPhone[2]));
                                    }
                                }
                                user_address = converJsonJava.getProfile().getAddress();
                                if(user_address.isEmpty()){

                                }else {
//                                    String[] splitAddr = user_address.split(",");
//                                    latitude = Double.valueOf(splitAddr[0]);
//                                    longtitude = Double.valueOf(splitAddr[1]);
//                                    get_location(false);

//                                    if (converJsonJava.getProfile().getResponsible_officer()!=null){
//                                        user_address_name = converJsonJava.getProfile().getResponsible_officer();
//                                        tvAddress.setQuery(user_address_name,false);
//                                    }
//                                    mapFragment.getMapAsync(Camera.this::onMapReady);

                                }
                                //dealer shop section
                                if (g == 3) {
//                                        txtOther_main.setVisibility(View.VISIBLE);
//                                        relative_othermain.setVisibility(View.VISIBLE);

                                    /* dealer shop */
                                    //CommonFunction.initialDealer(converJsonJava.getShops());

                                    List<String> shopname = new ArrayList<>();
                                    if (converJsonJava.getShops().size() > 0) {

                                        shopListItems=new String[converJsonJava.getShops().size()];
                                        shopIdListItems=new int[converJsonJava.getShops().size()];

                                        for(int i=0;i<converJsonJava.getShops().size();i++){
                                            ShopViewModel shopViewModel = converJsonJava.getShops().get(i);
                                            shopListItems[i]=shopViewModel.getShop_name();
                                            shopIdListItems[i]=shopViewModel.getId();
                                        }

                                        mDealerShopId1 = converJsonJava.getShops().get(0).getId();

                                    }

                                    Log.e("TAG",shopListItems.toString());
                                }
                            }
                        }
                    });
                } catch (JsonParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private void PostData(String encode) {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "";
        OkHttpClient client = new OkHttpClient();
        JSONObject post = new JSONObject();
        JSONObject sale = new JSONObject();
        try {

            post.put("category", category);
            post.put("status", 3);
            post.put("condition",tvCondition.getText().toString());
            post.put("used_eta1", seekbar_whole.getProgress());
            post.put("used_eta2", seekbar_rear.getProgress());
            post.put("used_eta3", seekbar_screw.getProgress());
            post.put("used_eta4", seekbar_pumps.getProgress());
            post.put("used_machine1", seekbar_rigt_engine.getProgress());
            post.put("used_machine2", seekbar_engine_head.getProgress());
            post.put("used_machine3", seekbar_assmebly.getProgress());
            post.put("used_machine4", seekbar_console.getProgress());
            post.put("used_other1", seekbar_accessories.getProgress());

            post.put("user",pk );

            strPostType = tvPostType.getText().toString();
            Log.e("post type",""+strPostType);
            if (strPostType == "buy") {
                post.put("discount", "0");
                post.put("discount_type","amount");
// set default image for Buy when no select image by samang 5/9/19
                default_bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_121);
                if (bitmapImage1 == null) {
                    post.put("front_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("front_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage1)));
                }
                if (bitmapImage2 == null) {
                    post.put("right_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("right_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage2)));
                }
                if (bitmapImage3 == null) {
                    post.put("left_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("left_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage3)));
                }
                if (bitmapImage4 == null) {
                    post.put("back_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("back_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage4)));
                }
                if (bitmapImage5 == null) {
                    post.put("extra_image1", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("extra_image1", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage5)));
                }
                if (bitmapImage6 == null) {
                    post.put("extra_image2", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, default_bitmap)));
                } else {
                    post.put("extra_image2", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage6)));
                }

            }else {
                post.put("discount_type", null);
                post.put("discount", String.valueOf(seekbar_pri_per.getProgress()));

                if (bitmapImage1 == null) {
                    post.put("front_image_path", "");
                    post.put("front_image_base64", "");
                } else {
                    post.put("front_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage1)));
                    post.put("front_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage1)));
                }
                if (bitmapImage2 == null) {
                    post.put("right_image_path", "");
                    post.put("right_image_base64", "");
                } else {
                    post.put("right_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage2)));
                    post.put("right_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage2)));
                }
                if (bitmapImage3 == null) {
                    post.put("left_image_path", "");
                    post.put("left_image_base64", "");
                } else {
                    post.put("left_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage3)));
                    post.put("left_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage3)));
                }
                if (bitmapImage4 == null) {
                    post.put("back_image_path", "");
                    post.put("back_image_base64", "");
                } else {
                    post.put("back_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage4)));
                    post.put("back_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage4)));
                }
                // add 2 image by samang 26/08
                if (bitmapImage5 == null) {
                    post.put("extra_image1", null);
                    post.put("extra_image1_base64", "");
                } else {
                    post.put("extra_image1", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage5)));
                    post.put("extra_image1_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage5)));
                }
                if (bitmapImage6 == null) {
                    post.put("extra_image2", null);
                    post.put("extra_image2_base64", "");
                } else {
                    post.put("extra_image2", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage6)));
                    post.put("extra_image2_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage6)));
                }
            }
            //end add image
            //Instant.now().toString()
            post.put("created", "");
            post.put("created_by", pk);
            //         post.put("modified", Instant.now().toString());
            post.put("modified_by", null);
            post.put("approved_date", null);
            post.put("approved_by", null);
            post.put("rejected_date", null);
            post.put("rejected_by",null);
            post.put("rejected_comments", "");
            post.put("year", year); //year
            post.put("modeling",model);
            post.put("description", etDescription.getText().toString());
            post.put("cost",etPrice.getText().toString());
            post.put("post_type",strPostType);
            post.put("type", type);
//check empty field user for detail by samang 28/08
            if (etAddress.getText().toString().isEmpty() || etAddress.getText().toString() == null){
                post.put("vin_code", user_address_name);
            }else {
                post.put("vin_code", etAddress.getText().toString());
            }
            if (etPhone1.getText().toString().isEmpty() || etPhone1.getText().toString() == null){
                post.put("contact_phone",user_phone);
            }else {
                post.put("contact_phone", etPhone1.getText().toString()+","+etPhone2.getText().toString()+","+etPhone3.getText().toString());
            }
            if (etEmail.getText().toString().isEmpty() || etEmail.getText().toString() == null){
                post.put("contact_email", user_email);
            }else {
                post.put("contact_email", etEmail.getText().toString());
            }

            post.put("contact_address", latlng);
// end check
            if (color.equals("blue")){
                strColor = color;
            }else if (color.equals("red")){
                strColor = color;
            }else if (color.equals("yellow")){
                strColor = color;
            }else if (color.equals("light blue sky")){
                strColor = color;
            }else if (color.equals("green")){
                strColor = color;
            }else if (color.equals("pink")){
                strColor = color;
            }else if (color.equals("purple")){
                strColor = color;
            }else if (color.equals("blue sky")){
                strColor = color;
            }else if (color.equals("dark blue")){
                strColor = color;
            }else if (color.equals("dark gray")){
                strColor = color;
            }else if (color.equals("light gray")){
                strColor = color;
            }else if (color.equals("light green")){
                strColor = color;
            }else if (color.equals("light red")){
                strColor = color;
            }else if (color.equals("dark red")){
                strColor = color;
            }else if (color.equals("dark green")){
                strColor = color;
            }else if (color.equals("white")){
                strColor = color;
            }else if (color.equals("black")){
                strColor = color;
            }else if (color.equals("orange")){
                strColor = color;
            }
            Log.e("color",strColor);
            post.put("color", strColor);

            Log.e("item",""+year+category+model+brand+type);

            //enhance sep 19 2019
            post.put("post_code", CommonFunction.generateRandomDigits(9));
            post.put("post_sub_title",CommonFunction.generatePostSubTitle(brand,model,year,strColor,strColorKH));
            switch (strPostType){
                case "លក់":
                case "sell":
                    url=ConsumeAPI.BASE_URL+"postsale/";
                    sale.put("sale_status", 3);
                    sale.put("record_status",1);
                    sale.put("sold_date", null);
                    sale.put("price", etPrice.getText().toString());
                    sale.put("total_price",etPrice.getText().toString());
                    post.put("sale_post",new JSONArray("["+sale+"]"));
                    break;

                case "ជួល":
                case "rent":
                    url = ConsumeAPI.BASE_URL+"postrent/";
                    JSONObject rent=new JSONObject();
                    rent.put("rent_status",3);
                    rent.put("record_status",1);
                    rent.put("rent_type","month");
                    rent.put("price",etPrice.getText().toString());
                    rent.put("total_price",etPrice.getText().toString());
                    rent.put("rent_date",null);
                    rent.put("return_date",null);
                    rent.put("rent_count_number",0);
                    post.put("rent_post",new JSONArray("["+rent+"]"));
                    break;

                case "ទិញ":
                case "buy":
                    url = ConsumeAPI.BASE_URL+"api/v1/postbuys/";
                    JSONObject buy=new JSONObject();
                    buy.put("buy_status",3);
                    buy.put("record_status",1);
                    post.put("buy_post",new JSONArray("["+buy+"]"));
                    break;
            }

            Log.d(TAG,post.toString());

            RequestBody body = RequestBody.create(MEDIA_TYPE, post.toString());
            String auth = "Basic " + encode;
            Request request = new Request.Builder()
                    .url(url)

                    .post(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization",auth)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String respon = response.body().string();
                    Log.d(TAG, "Post TTTT" + respon);
                    Gson gson = new Gson();
                    CreatePostModel createPostModel = new CreatePostModel();
                    try{
                        createPostModel = gson.fromJson(respon,CreatePostModel.class);
                        if (createPostModel!=null){
                            int id = createPostModel.getId();
                            if (id!=0){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject obj = new JSONObject(respon);
                                            int pID = obj.getInt("id");
                                            String pTitle=obj.getString("title");
                                            String pType=obj.getString("post_type");
                                            String pCoverURL=obj.getString("front_image_path");
                                            String price=obj.getString("cost");
                                            String dicountPrice=obj.getString("discount");
                                            String dicountType=obj.getString("discount_type");
                                            String location=obj.getString("contact_address");
                                            String createdAt=obj.getString("created");
                                            String postCode=obj.getString("post_code");
                                            String postSubTitle=obj.getString("post_sub_title");
                                            String fcolor=obj.getString("color");

                                            String eta1 = obj.getString("used_eta1");
                                            String eta2 = obj.getString("used_eta2");
                                            String eta3 = obj.getString("used_eta3");
                                            String eta4 = obj.getString("used_eta4");
                                            String machine1 = obj.getString("used_machine1");
                                            String machine2 = obj.getString("used_machine2");
                                            String machine3 = obj.getString("used_machine3");
                                            String machine4 = obj.getString("used_machine4");
                                            String other1 = obj.getString("used_other1");

                                            int pStatus=obj.getInt("status");
                                            FBPostCommonFunction.SubmitPost(String.valueOf(pID),pTitle,pType,pCoverURL,price,dicountPrice,dicountType,location,createdAt,pStatus,pk,postSubTitle,postCode,fcolor);

                                            Service api = Client.getClient().create(Service.class);
                                            dealershop ds;

//                                            for (int i=0;i<listid_shop.size();i++){
//                                                //Log.d("Listid Shop1212 ",listid_shop.get(i)+" indext: "+i);
//                                                ds = new dealershop(id,listid_shop.get(i),1);
//                                                list_shop.add(ds);
//                                            }
//                                            dealershop ds = new dealershop(id,2,1);
//                                            getUser_dealShop(pk,Encode,id);
//                                            Log.d("Size 1212121212","323"+list_shop.size());

                                            if(mDealerShopId1>0)
                                                list_shop.add(new dealershop(id,mDealerShopId1,1));
                                            if(mDealerShopId2>0)
                                                list_shop.add(new dealershop(id,mDealerShopId2,1));
                                            if(mDealerShopId3>0)
                                                list_shop.add(new dealershop(id,mDealerShopId3,1));

                                            for (int i=0;i<list_shop.size();i++){
                                                retrofit2.Call<dealershop> call1 = api.pushdealershop(list_shop.get(i));
                                                call1.enqueue(new retrofit2.Callback<dealershop>() {
                                                    @Override
                                                    public void onResponse(retrofit2.Call<dealershop> call, retrofit2.Response<dealershop> response) {
                                                        if (!response.isSuccessful()){
                                                            Log.d("444444444444","42"+response.code());
                                                            try {
                                                                Log.d("1212121121121","21"+response.errorBody().string());
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(retrofit2.Call<dealershop> call, Throwable t) {

                                                    }
                                                });
                                            }


                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }

                                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                        alertDialog.setTitle(getString(R.string.title_post));
                                        alertDialog.setMessage(getString(R.string.post_message));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mProgress.dismiss();
                                                        Intent intent = new Intent(Camera.this,Home.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                        //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                        alertDialog.setTitle(getString(R.string.title_post));
                                        alertDialog.setMessage(getString(R.string.post_fail_message));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                        mProgress.dismiss();
                                        //Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    }catch (JsonParseException e){
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                alertDialog.setTitle(getString(R.string.title_post));
                                alertDialog.setMessage(getString(R.string.post_fail_message));
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });

                        mProgress.dismiss();
                    }

                }//
                @Override
                public void onFailure(Call call, IOException e) {
                    String mMessage = e.getMessage().toString();
                    Log.d("Failure:",mMessage );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Failure:",mMessage );
                            AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                            alertDialog.setTitle(getString(R.string.title_post));
                            alertDialog.setMessage(getString(R.string.post_fail_message));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });

                    mProgress.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                    alertDialog.setTitle(getString(R.string.title_post));
                    alertDialog.setMessage(getString(R.string.post_fail_message));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
            mProgress.dismiss();
        }

    } //postdata

    private void EditPost_Approve(String encode,int edit_id) {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "";
        OkHttpClient client = new OkHttpClient();
        JSONObject post = new JSONObject();
        JSONObject sale = new JSONObject();

        String str_dis=etDiscount_amount.getText().toString();
        if(str_dis==null || str_dis.isEmpty())
            str_dis="0";
        if(model==0)
            model=mmodel;
        try {
            post.put("category", category );
            post.put("status", 3);
            post.put("condition",tvCondition.getText().toString());

            post.put("used_eta1", seekbar_whole.getProgress());
            post.put("used_eta2", seekbar_rear.getProgress());
            post.put("used_eta3", seekbar_screw.getProgress());
            post.put("used_eta4", seekbar_pumps.getProgress());
            post.put("used_machine1", seekbar_rigt_engine.getProgress());
            post.put("used_machine2", seekbar_engine_head.getProgress());
            post.put("used_machine3", seekbar_assmebly.getProgress());
            post.put("used_machine4", seekbar_console.getProgress());
            post.put("used_other1", seekbar_accessories.getProgress());

            if (strPostType.equals("buy")) {
                post.put("discount", "0");
                post.put("discount_type","amount");
            }else {
                post.put("discount_type", null);
                post.put("discount",str_dis);
            }
            post.put("user",pk );
            if(bitmapImage1==null) {
                post.put("front_image_path", "");
                post.put("front_image_base64", "");
            }
            else {
                post.put("front_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage1)));
                post.put("front_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this, bitmapImage1)));
            }
            if(bitmapImage2==null){
                post.put("right_image_path", "");
                post.put("right_image_base64", "");
            }else{
                post.put("right_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage2)));
                post.put("right_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage2)));
            }
            if(bitmapImage3==null){
                post.put("left_image_path", "");
                post.put("left_image_base64", "");
            }else{
                post.put("left_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage3)));
                post.put("left_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage3)));
            }
            if(bitmapImage4==null){
                post.put("back_image_path", "");
                post.put("back_image_base64", "");
            }else{
                post.put("back_image_path", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage4)));
                post.put("back_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage4)));
            }
// add 2 image by samang 26/08
            if(bitmapImage5==null){
                post.put("extra_image1", null);
//                post.put("other_image_base64", "");
            }else{
                post.put("extra_image1", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage5)));
//                post.put("other_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage5)));
            }
            if(bitmapImage6==null){
                post.put("extra_image2", null);
//                post.put("others_image_base64", "");
            }else{
                post.put("extra_image2", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage6)));
//                post.put("others_image_base64", ImageUtil.encodeFileToBase64Binary(ImageUtil.createTempFile(this,bitmapImage6)));
            }
            // end add image
            //Instant.now().toString()
            //post.put("created", "");
            post.put("created_by", pk);
            if (android.os.Build.VERSION.SDK_INT >= 26){
                post.put("modified", Instant.now().toString());
            } else{
//                fix DateTime with sdk < 26 by samang (26/08/2019)
//                  String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String currentDateTimeString = formatter.format(date);
                post.put("modified", currentDateTimeString);
                // do something for phones running an SDK before lollipop
            }
            post.put("modified_by", pk);
            post.put("approved_date", null);
            post.put("approved_by", null);
            post.put("rejected_date", null);
            post.put("rejected_by",null);
            post.put("rejected_comments", "");
            post.put("year", year); //year
            post.put("modeling", model);
            post.put("description", etDescription.getText().toString());
            post.put("cost",etPrice.getText().toString());
            post.put("post_type",strPostType);
            post.put("vin_code", etAddress.getText().toString());
            post.put("type", type);
//check empty field user for detail by samang 28/08
            if (etAddress.getText().toString().isEmpty() ||etAddress.getText().toString() == null){
                post.put("vin_code", edit_address_name);
            }else {
                post.put("vin_code", etAddress.getText().toString());
            }
            if (etPhone1.getText().toString().isEmpty() || etPhone1.getText().toString() == null){
                post.put("contact_phone",edit_phone);
            }else {
                post.put("contact_phone", etPhone1.getText().toString() + "," + etPhone2.getText().toString() + "," + etPhone3.getText().toString());
            }
            if (etEmail.getText().toString().isEmpty() || etEmail.getText().toString() ==null){
                post.put("contact_email", edit_email);
            }else {
                post.put("contact_email", etEmail.getText().toString());
            }

            post.put("contact_address", latlng);
            //end check
            if (color.equals("blue")){
                strColor = color;
            }else if (color.equals("red")){
                strColor = color;
            }else if (color.equals("yellow")){
                strColor = color;
            }else if (color.equals("light blue sky")){
                strColor = color;
            }else if (color.equals("green")){
                strColor = color;
            }else if (color.equals("pink")){
                strColor = color;
            }else if (color.equals("purple")){
                strColor = color;
            }else if (color.equals("blue sky")){
                strColor = color;
            }else if (color.equals("dark blue")){
                strColor = color;
            }else if (color.equals("dark gray")){
                strColor = color;
            }else if (color.equals("light gray")){
                strColor = color;
            }else if (color.equals("light green")){
                strColor = color;
            }else if (color.equals("light red")){
                strColor = color;
            }else if (color.equals("dark red")){
                strColor = color;
            }else if (color.equals("dark green")){
                strColor = color;
            }else if (color.equals("white")){
                strColor = color;
            }else if (color.equals("black")){
                strColor = color;
            }else if (color.equals("orange")){
                strColor = color;
            }
            Log.e("color",strColor);
            post.put("color", strColor);
            post.put("post_sub_title",CommonFunction.generatePostSubTitle(brand,model,year,strColor,strColorKH));

            switch (strPostType){
                case "លក់":
                case "sell":
                    url=ConsumeAPI.BASE_URL+"postsale/"+edit_id+"/";
                    //Log.d("URL","URL"+url);
                    sale.put("sale_status", 3);
                    sale.put("record_status",1);
                    sale.put("sold_date", null);
                    sale.put("price", etPrice.getText().toString());
                    sale.put("total_price",etPrice.getText().toString());
                    post.put("sale_post",new JSONArray("["+sale+"]"));
                    //post.put("rent_post",new JSONArray("[]"));
                    //post.put("buy_post",new JSONArray("[]"));
                    break;

                case "ជួល":
                case "rent":
                    url = ConsumeAPI.BASE_URL+"postrent/"+edit_id+"/";
                    JSONObject rent=new JSONObject();
                    rent.put("rent_status",3);
                    rent.put("record_status",1);
                    rent.put("rent_type","month");
                    rent.put("price",etPrice.getText().toString().toLowerCase());
                    rent.put("total_price",etPrice.getText().toString().toLowerCase());
                    rent.put("rent_date",null);
                    rent.put("return_date",null);
                    rent.put("rent_count_number",0);
                    post.put("rent_post",new JSONArray("["+rent+"]"));
                    break;

//                case "ទិញ":
//                case "buy":
//                    url = ConsumeAPI.BASE_URL+"api/v1/postbuys/"+ edit_id + "/";
//                    JSONObject buy=new JSONObject();
//                    buy.put("buy_status",3);
//                    buy.put("record_status",1);
//                    post.put("buy_post",new JSONArray("["+buy+"]"));
//                    break;
            }

            RequestBody body = RequestBody.create(MEDIA_TYPE, post.toString());
            String auth = "Basic " + encode;
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization",auth)
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String respon = response.body().string();
                    Log.d(TAG, "Edit TTTT" + respon);

                    Gson gson = new Gson();
                    CreatePostModel createPostModel = new CreatePostModel();
                    try{
                        createPostModel = gson.fromJson(respon,CreatePostModel.class);
                        if (createPostModel!=null){
                            int id = createPostModel.getId();

                            if (id!=0 ){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //update to firebase
                                        try {
                                            JSONObject obj = new JSONObject(respon);
                                            int pID = obj.getInt("id");
                                            String pTitle = obj.getString("title");
                                            String pCoverURL = obj.getString("front_image_path");
                                            String price = obj.getString("cost");
                                            String dicountPrice = obj.getString("discount");
                                            String dicountType = obj.getString("discount_type");
                                            String location = obj.getString("contact_address");
                                            String createdAt = obj.getString("created");
                                            String postSubTitle=obj.getString("post_sub_title");

                                            String fcolor=obj.getString("color");
                                            String eta1 = obj.getString("used_eta1");
                                            String eta2 = obj.getString("used_eta2");
                                            String eta3 = obj.getString("used_eta3");
                                            String eta4 = obj.getString("used_eta4");
                                            String machine1 = obj.getString("used_machine1");
                                            String machine2 = obj.getString("used_machine2");
                                            String machine3 = obj.getString("used_machine3");
                                            String machine4 = obj.getString("used_machine4");
                                            String other1 = obj.getString("used_other1");
                                            FBPostCommonFunction.modifiedPost(String.valueOf(pID), pTitle, pCoverURL, price, dicountPrice, dicountType, location, createdAt,postSubTitle,eta1,eta2,eta3,eta4,machine1,machine2,machine3,machine4,other1,fcolor);

                                            //submit dealer
                                            Service api = Client.getClient().create(Service.class);
                                            if(mDealerShopId1>0)
                                                list_shop.add(new dealershop(id,mDealerShopId1,1));
                                            if(mDealerShopId2>0)
                                                list_shop.add(new dealershop(id,mDealerShopId2,1));
                                            if(mDealerShopId3>0)
                                                list_shop.add(new dealershop(id,mDealerShopId3,1));

                                            for (int i=0;i<list_shop.size();i++){
                                                retrofit2.Call<dealershop> call1 = api.pushdealershop(list_shop.get(i));
                                                call1.enqueue(new retrofit2.Callback<dealershop>() {
                                                    @Override
                                                    public void onResponse(retrofit2.Call<dealershop> call, retrofit2.Response<dealershop> response) {
                                                        if (!response.isSuccessful()){
                                                            Log.d("444444444444","42"+response.code());
                                                            try {
                                                                Log.d("1212121121121","21"+response.errorBody().string());
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(retrofit2.Call<dealershop> call, Throwable t) {

                                                    }
                                                });
                                            }

                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                        alertDialog.setTitle(getString(R.string.title_post));
                                        alertDialog.setMessage(getString(R.string.waiting_approval));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mProgress.dismiss();
                                                        Intent intent = new Intent(Camera.this,Account.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
//                                                        finish();
//                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                    }
                                });

                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                        alertDialog.setTitle(getString(R.string.title_post));
                                        alertDialog.setMessage(getString(R.string.post_fail_message));
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        alertDialog.show();
                                        mProgress.dismiss();
                                    }
                                });
                            }
                        }
                    }catch (JsonParseException e){
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                                alertDialog.setTitle(getString(R.string.title_post));
                                alertDialog.setMessage(getString(R.string.post_fail_message));
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });
                        mProgress.dismiss();
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    String mMessage = e.getMessage().toString();
                    Log.d("Failure:",mMessage );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                            alertDialog.setTitle(getString(R.string.title_post));
                            alertDialog.setMessage(getString(R.string.post_fail_message));
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    });
                    mProgress.dismiss();

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(Camera.this).create();
                    alertDialog.setTitle(getString(R.string.title_post));
                    alertDialog.setMessage(getString(R.string.post_fail_message));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
            mProgress.dismiss();
        }
    } //edit post approve

    private void get_model(String encode , int model_id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/models/"+model_id);
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon_model = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject jsonObject = new JSONObject(respon_model);
                            int brand_id = jsonObject.getInt("brand");
                            String name = jsonObject.getString("modeling_name");
                            List<String> listMN = new ArrayList<>();
                            listMN.add(name);
                            Log.d("Brand",brand_id+" Model: "+name );
                            ArrayAdapter<Integer> adapterM_id = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,model_id);
                            ArrayAdapter<String> adapterM_name = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,listMN);
                            //tvModel.setAdapter(adapterM_name);
                            //tvModel.setSelection(0);

                            final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/brands/"+brand_id);
                            Log.d("Url brand",url);
                            OkHttpClient client = new OkHttpClient();
                            String auth = "Basic "+ encode;
                            Request request = new Request.Builder()
                                    .url(url)
                                    .header("Accept","application/json")
                                    .header("Content-Type","application/json")
                                    .header("Authorization",auth)
                                    .build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String respon_brand = response.body().string();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                JSONObject object = new JSONObject(respon_brand);
                                                String brand_name = object.getString("brand_name");
                                                Log.d("brand name",brand_name);
                                                List<String> ListBn = new ArrayList<>();
                                                ListBn.add(brand_name);
                                                ArrayAdapter<Integer> adapterB_id = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,brand_id);
                                                ArrayAdapter<String> adapterB_name = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,ListBn);
                                                //tvBrand.setAdapter(adapterB_name);
                                                //tvBrand.setSelection(0);



                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            });
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void getCategegoryName(String encode,int id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/categories/"+id+"/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                try{
                    SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                    String language = preferences.getString("My_Lang", "");
                    JSONObject jsonObject = new JSONObject(respon);
                    String catName = jsonObject.getString("cat_name");
                    String catNamekh=jsonObject.getString("cat_name_kh");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (language.equals("km")){
                                tvCategory.setText(catNamekh);
                            }else if (language.equals("en")) {
                                tvCategory.setText(catName);
                            }
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTypeName(String encode,int id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/types/"+id+"/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                            String language = preferences.getString("My_Lang", "");
                            JSONObject jsonObject = new JSONObject(respon);
                            String typename=jsonObject.getString("type");
                            String typenamekh=jsonObject.getString("type_kh");
                            if (language.equals("km")){
                                tvType_cate.setText(typenamekh);
                            }else if (language.equals("en")){
                                tvType_cate.setText(typename);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void getBrandName(String encode, int id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/brands/"+id+"/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Failure Error",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                            String language = preferences.getString("My_Lang", "");
                            JSONObject jsonObject = new JSONObject(respon);
                            String brandname=jsonObject.getString("brand_name");
                            String brandnamekh=jsonObject.getString("brand_name_as_kh");
                            if (language.equals("km")){
                                tvBrand.setText(brandnamekh);
                            }else if (language.equals("en")){
                                tvBrand.setText(brandname);
                            }
                            //Call_Model(Encode,id,);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    private void getModelName(String encode,int id){

        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/models/"+id+"/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
                            String language = preferences.getString("My_Lang", "");
                            JSONObject jsonObject = new JSONObject(respon);
                            int brandId=jsonObject.getInt("brand");
                            String name=jsonObject.getString("modeling_name");
                            String namekh=jsonObject.getString("modeling_name_kh");
                            if (language.equals("km")){
                                brand=brandId;
                                tvModel.setText(name);
                                getBrandName(Encode,brand);
                            }else if (language.equals("en")){
                                brand=brandId;
                                tvModel.setText(name);
                                getBrandName(Encode,brand);
                            }
                            //tvModel.setText(name);
                            Log.d(TAG,"Brand from model "+name);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.d("Exception",e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void getYearName(String encode,int id){
        final String url = String.format("%s%s", ConsumeAPI.BASE_URL,"api/v1/years/"+id+"/");
        OkHttpClient client = new OkHttpClient();
        String auth = "Basic "+ encode;
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",auth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respon = response.body().string();
                try{
                    JSONObject jsonObject = new JSONObject(respon);
                    String yearname=jsonObject.getString("year");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvYear.setText(yearname);
                        }
                    });

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void DropDown() {

        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        final ColorAdapter adapter = new ColorAdapter(itemcolor,getApplication());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int selectedIndex = adapter.selectedPositions.indexOf(position);
                long id_color = adapter.getItemId(position);
                if (selectedIndex > -1) {
                    adapter.selectedPositions.remove(selectedIndex);
                    ((CustomView)v).display(false);
                    if (id_color == 0){
                        color = null;
                    }else if (id_color == 1){
                        color = null;
                    }else if (id_color == 2){
                        color = null;
                    }else if (id_color == 3){
                        color = null;
                    }else if (id_color == 4){
                        color = null;
                    }else if (id_color == 5){
                        color = null;
                    }else if (id_color == 6){
                        color = null;
                    }else if (id_color == 7){
                        color = null;
                    }else if (id_color == 8){
                        color = null;
                    }else if (id_color == 9){
                        color = null;
                    }else if (id_color == 10){
                        color = null;
                    }else if (id_color == 11){
                        color = null;
                    }else if (id_color == 12){
                        color = null;
                    }else if (id_color == 13){
                        color = null;
                    }else if (id_color == 14){
                        color = null;
                    }else if (id_color == 15){
                        color = null;
                    }else if (id_color == 16){
                        color = null;
                    }else if (id_color == 17){
                        color = null;
                    }
                } else {
                    adapter.selectedPositions.add(position);
                    ((CustomView)v).display(true);
                    if (id_color == 0){
                        color = "white";
                    }else if (id_color == 1){
                        color = "blue"+"black";
                    }else if (id_color == 2){
                        color = "black"+"red";
                    }else if (id_color == 3){
                        color = "red"+"yellow";
                    }else if (id_color == 4){
                        color = "yellow";
                    }else if (id_color == 5){
                        color = "pink";
                    }else if (id_color == 6){
                        color = "purple";
                    }else if (id_color == 7){
                        color = "orange";
                    }else if (id_color == 8){
                        color = "dark blue";
                    }else if (id_color == 9){
                        color = "gray";
                    }else if (id_color == 10){
                        color = "dark green";
                    }else if (id_color == 11){
                        color = "dark red";
                    }else if (id_color == 12){
                        color = "light blue sky";
                    }else if (id_color == 13){
                        color = "light red";
                    }else if (id_color == 14){
                        color = "green";
                    }else if (id_color == 15){
                        color = "silver";
                    }else if (id_color == 16){
                        color = "light green";
                    }else if (id_color == 17){
                        color = "blue sky";
                    }
                }

                Log.e("color",""+color);
            }
        });

        imgAdd_color.setVisibility(View.VISIBLE);
        imgAdd_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_color.setBackground(getDrawable(R.color.light_gray));
                layout_color.setVisibility(View.VISIBLE);
                cancel_color.setVisibility(View.VISIBLE);
                imgAdd_color.setVisibility(View.GONE);
            }
        });
        cancel_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_color.setVisibility(View.GONE);
                imgAdd_color.setVisibility(View.VISIBLE);
                cancel_color.setVisibility(View.GONE);
            }
        });

        tvCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomCondition(view);
            }
        });
        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomYear(view);
            }
        });
        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomCategory(view);
            }
        });
        tvType_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomTypeCate(view);
            }
        });

        tvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomBrand(view);
            }
        });

        tvModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomModel(view);
            }
        });
        tvPostType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomType(view);
            }
        });
        imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                        (getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                    ActivityCompat.requestPermissions(Camera.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
                }else {
                    Intent intent = new Intent(Camera.this, FragmentMap.class);
                    intent.putExtra("post","post");
                    intent.putExtra("name_post",etName.getText().toString());
                    intent.putExtra("process_type",process_type);
                    intent.putExtra("price",etPrice.getText().toString());
                    intent.putExtra("post_type",tvPostType.getText().toString());
                    intent.putExtra("category",tvCategory.getText().toString());
                    intent.putExtra("brand",tvBrand.getText().toString());
                    intent.putExtra("model",tvModel.getText().toString());
                    intent.putExtra("year",tvYear.getText().toString());
                    intent.putExtra("condition",tvCondition.getText().toString());
                    intent.putExtra("description",etDescription.getText().toString());
                    intent.putExtra("email_post",etEmail.getText().toString());
                    intent.putExtra("address_post",etAddress.getText().toString());
                    intent.putExtra("phone_number1_post",etPhone1.getText().toString());
                    intent.putExtra("phone_number2_post",etPhone2.getText().toString());
                    intent.putExtra("phone_number3_post",etPhone3.getText().toString());
                    intent.putExtra("discount_percent",seekbar_price);
                    intent.putExtra("discount_amount",etDiscount_amount.getText().toString());
                    intent.putExtra("whole_ink",whole_ink);
                    intent.putExtra("rear",seekbar_rearr);
                    intent.putExtra("screw",seekbar_screww);
                    intent.putExtra("pumps",seekbar_pump);
                    intent.putExtra("right_engine",seekbar_engine);
                    intent.putExtra("engine_head",seekbar_head);
                    intent.putExtra("assembly",assembly);
                    intent.putExtra("console",seekbar_consolee);
                    intent.putExtra("accessories",seekbar_accessorie);
                    intent.putExtra("category_post",category);
                    intent.putExtra("color",strColor);
                    Log.e("color",""+strColor);
                    startActivity(intent);
                }
            }
        });
    }

    private String getEncodedString(String username, String password) {
        final String userpass = username+":"+password;
        return Base64.encodeToString(userpass.getBytes(),Base64.NO_WRAP);
    }

    private void Variable_Field() {

        tvPostType = findViewById(R.id.tvPostType);
        tvCategory = (EditText) findViewById(R.id.tvCategory);
        tvBrand    = (EditText) findViewById(R.id.tvBrand);
        tvModel    = (EditText) findViewById(R.id.tvModel);
        tvYear     = (EditText) findViewById(R.id.tvYears);
        tvCondition= (EditText) findViewById(R.id.tvCondition);
        tvType_elec = findViewById(R.id.title_cate);
        tvType_cate = findViewById(R.id.tvType);
        tv_name = findViewById(R.id.title_name);
        etName = findViewById(R.id.etName);
        bt_update = findViewById(R.id.btnupdate);
        submit_post = (Button) findViewById(R.id.btnSubmitPost);
        layout_color = findViewById(R.id.add_color);
        cancel_color = findViewById(R.id.cancel_color);
        layout_estimate = findViewById(R.id.layout_estimate);
        title_dicount = findViewById(R.id.title_dis_pri);


        etMap = findViewById(R.id.et_map);
        imageMap = findViewById(R.id.map);
        etAddress  = (EditText) findViewById(R.id.et_address);

        etDescription     = (EditText)findViewById(R.id.etDescription );
        etPrice           = (EditText)findViewById(R.id.etPrice );
        etDiscount_amount = (EditText)findViewById(R.id.etDisAmount );
        tv_add = findViewById(R.id.tv_add);
        tv_add1 = findViewById(R.id.tv_add1);
        tv_cancel = findViewById(R.id.tv_cancel);
        layout_phone1 = findViewById(R.id.layout_phone1);
        layout_phone2 = findViewById(R.id.layout_phone2);
        etPhone1          = (EditText)findViewById(R.id.etphone1 );
        etPhone2          = (EditText)findViewById(R.id.etphone2);
        etPhone3          = (EditText)findViewById(R.id.etphone3);
        etEmail           = (EditText)findViewById(R.id.etEmail );

        btremove_pic1    = (ImageButton)findViewById(R.id.remove_pic1);
        btremove_pic2    = (ImageButton)findViewById(R.id.remove_pic2);
        btremove_pic3    = (ImageButton)findViewById(R.id.remove_pic3);
        btremove_pic4    = (ImageButton)findViewById(R.id.remove_pic4);
        btremove_pic5    = (ImageButton)findViewById(R.id.remove_pic5);
        btremove_pic6    = (ImageButton)findViewById(R.id.remove_pic6);

        imageView1 =(ImageView) findViewById(R.id.Picture1);
        imageView2 =(ImageView) findViewById(R.id.Picture2);
        imageView3 =(ImageView) findViewById(R.id.Picture3);
        imageView4 =(ImageView) findViewById(R.id.Picture4);
        imageView5 =(ImageView) findViewById(R.id.Picture5);
        imageView6 =(ImageView) findViewById(R.id.Picture6);

        seekbar_whole = findViewById(R.id.seekbar_whole);
        seekbar_pri_per = findViewById(R.id.seekbar_pri_per);
        seekbar_rear = findViewById(R.id.seekbar_rear);
        seekbar_screw = findViewById(R.id.seekbar_screw);
        seekbar_rigt_engine = findViewById(R.id.seekbar_right_engine);
        seekbar_pumps = findViewById(R.id.seekbar_pumps);
        seekbar_assmebly = findViewById(R.id.seekbar_assembly);
        seekbar_accessories = findViewById(R.id.seekbar_accessories);
        seekbar_console = findViewById(R.id.seekbar_console);
        seekbar_engine_head = findViewById(R.id.seekbar_engine_head);

        imgAdd_color = findViewById(R.id.add_more_color);

    }

    private void selectImage() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(Camera.this);
        View sheetview = getLayoutInflater().inflate(R.layout.choose_image,null,false);
        bottomSheet.setContentView(sheetview);
        bottomSheet.show();
        TextView tv_camera = sheetview.findViewById(R.id.tv_camera);
        TextView tv_gallery = sheetview.findViewById(R.id.tv_gallery);
        Button bt_cancel = sheetview.findViewById(R.id.btn_cancel);
        TextView tv_clear = sheetview.findViewById(R.id.bt_clear);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission(true);
                bottomSheet.dismiss();
            }
        });
        tv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestStoragePermission(false);
                bottomSheet.dismiss();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet.dismiss();
            }
        });
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet.dismiss();
            }
        });

    }
    /**
     * Requesting multiple permissions (storage and camera) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.e("TAG","It's allow permiision");
//                            if (isCamera) {
//                                dispatchTakePictureIntent();
////                                dispatchTakePictureIntentdealer();
//                            } else {
//                                dispatchGalleryIntent();
//                            }
                        }else {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        switch (REQUEST_TAKE_PHOTO_NUM){
            case REQUEST_TAKE_PHOTO_1:
                Toast toast=Toast.makeText(getApplicationContext(),"ថតពីមុខ",Toast.LENGTH_SHORT);
                //toast.setMargin(50,50);
                toast.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group = (ViewGroup) toast.getView();
                TextView messageTextView = (TextView) group.getChildAt(0);
                messageTextView.setTextSize(25);
                toast.show();
                break;
            case REQUEST_TAKE_PHOTO_2:
                Toast toast1=Toast.makeText(getApplicationContext(),"ថតផ្នែកខាងស្ដាំ",Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group1 = (ViewGroup) toast1.getView();
                TextView messageTextView1 = (TextView) group1.getChildAt(0);
                messageTextView1.setTextSize(25);
                toast1.show();
                break;
            case REQUEST_TAKE_PHOTO_3:
                Toast toast2=Toast.makeText(getApplicationContext(),"ថតផ្នែកខាងឆ្វេង",Toast.LENGTH_SHORT);
                toast2.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group2 = (ViewGroup) toast2.getView();
                TextView messageTextView2 = (TextView) group2.getChildAt(0);
                messageTextView2.setTextSize(25);
                toast2.show();
                break;
            case REQUEST_TAKE_PHOTO_4:
                Toast toast3= Toast.makeText(getApplicationContext(),"ថតពីក្រោយ",Toast.LENGTH_SHORT);
                toast3.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group3 = (ViewGroup) toast3.getView();
                TextView messageTextView3 = (TextView) group3.getChildAt(0);
                messageTextView3.setTextSize(25);
                toast3.show();
                break;
// add 2 image by samang 26/08
            case REQUEST_TAKE_PHOTO_5:
                Toast toast4= Toast.makeText(getApplicationContext(),"ផ្នែកផ្សេងទៀត",Toast.LENGTH_SHORT);
                toast4.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group4 = (ViewGroup) toast4.getView();
                TextView messageTextView4 = (TextView) group4.getChildAt(0);
                messageTextView4.setTextSize(25);
                toast4.show();
                break;

            case REQUEST_TAKE_PHOTO_6:
                Toast toast5= Toast.makeText(getApplicationContext(),"ផ្នែកផ្សេងទៀត",Toast.LENGTH_SHORT);
                toast5.setGravity(Gravity.TOP, 100,80);
                //toast.show();
                ViewGroup group5 = (ViewGroup) toast5.getView();
                TextView messageTextView5 = (TextView) group5.getChildAt(0);
                messageTextView5.setTextSize(25);
                toast5.show();
                break;
            // end
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        this.getPackageName() + ".provider",
                        photoFile);
                //BuildConfig.APPLICATION_ID
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_NUM);
            }
        }
    }

    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_CHOOSE_PHOTO_NUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            //other dealer section
//            if (requestCode==REQUEST_GALLARY_PHOTO){
//                try {
//                    imageUri = data.getData();
//                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(imageUri)));
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }else if (requestCode==REQUEST_TAKE_PHOTO){
//                try {
//                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//                imageUri=Uri.fromFile(mPhotoFile);
//            }
////            bitmapImage = BitmapFactory.decodeFile(mPhotoFile.getPath());
//            Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.default_profile_pic)).into(btnlogo);
//            //end

            if (requestCode == REQUEST_TAKE_PHOTO_1) {
                try {
                    //Uri filePath=data.getData();
                    //bitmapImage1=MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage1= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView1);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_2;
                btremove_pic1.setVisibility(View.VISIBLE);
                requestStoragePermission(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO_2) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage2= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView2);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_3;
                btremove_pic2.setVisibility(View.VISIBLE);
                requestStoragePermission(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO_3) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage3= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView3);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_4;
                btremove_pic3.setVisibility(View.VISIBLE);
                requestStoragePermission(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO_4) {
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage4= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView4);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_5;
                btremove_pic4.setVisibility(View.VISIBLE);
                requestStoragePermission(true);
            }
            // add 2 image by samang 26/08
            else if (requestCode == REQUEST_TAKE_PHOTO_5){
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage5= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView5);
                REQUEST_TAKE_PHOTO_NUM=REQUEST_TAKE_PHOTO_6;
                btremove_pic5.setVisibility(View.VISIBLE);
                requestStoragePermission(true);
            }
            else if (requestCode == REQUEST_TAKE_PHOTO_6){
                try {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    bitmapImage6= BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView6);
                btremove_pic6.setVisibility(View.VISIBLE);
            }
//
            else if (requestCode == REQUEST_GALLERY_PHOTO_1) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage1=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView1);
                btremove_pic1.setVisibility(View.VISIBLE);
            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_2) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage2=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView2);
                btremove_pic2.setVisibility(View.VISIBLE);
            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_3) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage3=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView3);
                btremove_pic3.setVisibility(View.VISIBLE);
            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_4) {
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage4=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView4);
                btremove_pic4.setVisibility(View.VISIBLE);

            }
            // add 2 image by samang
            else if (requestCode == REQUEST_GALLERY_PHOTO_5){
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage5=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView5);
                btremove_pic5.setVisibility(View.VISIBLE);

            }
            else if (requestCode == REQUEST_GALLERY_PHOTO_6){
                Uri selectedImage = data.getData();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    bitmapImage6=BitmapFactory.decodeFile(mPhotoFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(Camera.this).load(mPhotoFile).apply(new RequestOptions().centerCrop().centerCrop().placeholder(R.drawable.group_2293)).into(imageView6);
                btremove_pic6.setVisibility(View.VISIBLE);
            }
            // end
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.permission));
        builder.setMessage(getString(R.string.setting_permission));
        builder.setPositiveButton(getString(R.string.go_setting), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        //Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Intent intent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**
     * Create file with current timestamp name
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private void mSeekbar(){
        seekbar_pri_per.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_price = progress;
                @SuppressLint("DefaultLocale")
                String title = String.format("Discount Price(%d%%)", seekbar_price);
                title_dicount.setText(title);
                try {
                    double str_dis = 0, price1 = 0;
                    String cost = "";
                    cost = etPrice.getText().toString();
                    price1 = Double.valueOf(cost);
                    str_dis = (price1 * seekbar_price) / 100;
                    etDiscount_amount.setText(String.valueOf(str_dis));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_whole.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                whole_ink = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_rear.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_rearr = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_screw.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_screww = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_rigt_engine.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_engine = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_engine_head.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_head = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_pumps.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_pump = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_console.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_consolee = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekbar_accessories.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                seekbar_accessorie = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekbar_assmebly.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                assembly = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void showBottomCondition(View view) {
        BottomChooseCondition addPhotoBottomDialogFragment = BottomChooseCondition.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseCondition.TAG);
    }
    public void showBottomYear(View view) {
        BottomChooseYear addPhotoBottomDialogFragment = BottomChooseYear.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseYear.TAG);
    }
    public void showBottomBrand(View view) {
        BottomChooseBrand addPhotoBottomDialogFragment = BottomChooseBrand.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseBrand.TAG);
    }
    public void showBottomModel(View view) {
        BottomChooseModel addPhotoBottomDialogFragment = BottomChooseModel.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseModel.TAG);
    }
    public void showBottomCategory(View view) {
        BottomChooseCategory addPhotoBottomDialogFragment = BottomChooseCategory.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseCategory.TAG);
    }
    public void showBottomType(View view) {
        BottomChooseType addPhotoBottomDialogFragment = BottomChooseType.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseType.TAG);
    }
    public void showBottomTypeCate(View view) {
        BottomChooseTypeCate addPhotoBottomDialogFragment = BottomChooseTypeCate.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(), BottomChooseTypeCate.TAG);
    }

    @Override
    public void onItemClick(String item) {
        tvCondition.setText(item);
        if (item.equals("used")){
            layout_estimate.setVisibility(View.VISIBLE);
        }else {
            layout_estimate.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickItem(String item) {
        tvYear.setText(item);
    }

    @Override
    public void AddIDyear(int id) {
        year = id;
        Log.e("year",""+year);
    }

    @Override
    public void onItem(String item) {
        tvBrand.setText(item);
    }

    @Override
    public void onItemTypeCate(String item) {
        tvType_cate.setText(item);
    }

    @Override
    public void AddIdTypeCate(int id) {
        type = id;
        Log.e("type",""+type);
    }

    @Override
    public void AddID(int id) {
       brand = id;
       Log.e("brand",""+brand);
    }

    @Override
    public void onClickItemModel(String item) {
        tvModel.setText(item);
    }

    @Override
    public void AddModelID(int id) {
        model = id;
        Log.e("model",""+model);
    }

    @Override
    public void onCLickItemType(String item) {
        tvCategory.setText(item);
    }

    @Override
    public void AddIdCategory(int id) {
        category = id;
        if (id == 1){
            tvType_elec.setVisibility(View.VISIBLE);
            tvType_cate.setVisibility(View.VISIBLE);
        }else {
            tvType_elec.setVisibility(View.GONE);
            tvType_cate.setVisibility(View.GONE);
        }
        Log.e("category",""+category);
    }

    @Override
    public void onItemType(String item) {
        tvPostType.setText(item);
    }
}


