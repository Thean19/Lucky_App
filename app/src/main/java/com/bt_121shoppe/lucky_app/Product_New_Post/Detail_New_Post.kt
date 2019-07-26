package com.bt_121shoppe.lucky_app.Product_New_Post

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateUtils
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.custom.sliderimage.logic.SliderImage
import com.bt_121shoppe.lucky_app.Activity.Item_API
import com.bt_121shoppe.lucky_app.Api.ConsumeAPI
import com.bt_121shoppe.lucky_app.Api.User
import com.bt_121shoppe.lucky_app.Login_Register.UserAccount
import com.bt_121shoppe.lucky_app.Product_dicount.Detail_Discount
import com.bt_121shoppe.lucky_app.useraccount.User_post
import com.bt_121shoppe.lucky_app.R
import com.bt_121shoppe.lucky_app.loan.LoanCreateActivity
import com.bt_121shoppe.lucky_app.models.PostViewModel
import com.bt_121shoppe.lucky_app.utils.LoanCalculator

import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.JsonParseException
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_detail_new_post.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Detail_New_Post : AppCompatActivity(){//, OnMapReadyCallback{
    private val TAG = Detail_Discount::class.java.simpleName
    private lateinit var mMap: GoogleMap
    private var list_rela: RecyclerView? = null

 //   private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted: Boolean = false
    private var mLastKnownLocation: Location? = null
    private var postId:Int=0
    private var pk=0
    private var name=""
    private var pass=""
    private var Encode=""
    private var p=0
    private var pt=0
    internal lateinit var txt_detail_new: TextView

    private lateinit var tvPostTitle:TextView
    private lateinit var tvPrice:TextView
    private lateinit var tvBrand:TextView
    private lateinit var tvModel:TextView
    private lateinit var tvYear:TextView
    private lateinit var tvCondition:TextView
    private lateinit var tvColor:TextView
    private lateinit var tvDescription:TextView
    private lateinit var tvMonthlyPayment:TextView
    private lateinit var edLoanPrice:EditText
    private lateinit var edLoanInterestRate:EditText
    private lateinit var edLoanDeposit:EditText
    private lateinit var edLoanTerm:EditText
    private lateinit var sliderImage:SliderImage
    private lateinit var img_user:CircleImageView
    private lateinit var user_name:TextView
    private lateinit var user_telephone:TextView
    private lateinit var user_email:TextView
    private lateinit var user_location:TextView
    private lateinit var tv_count_view:TextView
    private lateinit var tv_location_duration:TextView
    private lateinit var tex_noresult:TextView
    private lateinit var mprocessBar:ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_new_post)

        postId = intent.getIntExtra("ID",0)
        Log.d("ID Detail New :",postId.toString())

        val sharedPref: SharedPreferences = getSharedPreferences("Register", Context.MODE_PRIVATE)
        name = sharedPref.getString("name", "")
        pass = sharedPref.getString("pass", "")
        Encode = getEncodedString(name,pass)
        Log.d("adfjlsfjsldk",Encode)
        if (sharedPref.contains("token")) {
           pk = sharedPref.getInt("Pk",0)
        } else if (sharedPref.contains("id")) {
           pk = sharedPref.getInt("id", 0)
        }

        Log.d("Response pk:",pk.toString())
        p = intent.getIntExtra("ID",0)
        pt=intent.getIntExtra("postt",0)

        initialProductPostDetail(Encode)
        submitCountView(Encode)
        countPostView(Encode)


//        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
//
//        txt_detail_new = findViewById(R.id.txt_show_place_detail_new_post) as TextView
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        val mapFragment = supportFragmentManager
//                .findFragmentById(R.id.map_detail_newpost) as SupportMapFragment
//        mapFragment.getMapAsync(this)
        /*
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)

        txt_detail_new = findViewById(R.id.txt_show_place_detail_new_post) as TextView
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map_detail_newpost) as SupportMapFragment
        mapFragment.getMapAsync(this)
        */

        tex_noresult = findViewById(R.id.txt_noresult)
        list_rela = findViewById(R.id.list_rela)
        mprocessBar = findViewById(R.id.mprogressbar)
        mprocessBar.visibility = View.VISIBLE

        //Back
        val back = findViewById<TextView>(R.id.tv_back)
        back.setOnClickListener { finish() }
        //Slider
        sliderImage = findViewById<SliderImage>(R.id.slider)

//        val id = intent.getIntExtra("ID",0)
//        val phone = findViewById<TextView>(R.id.tv_phone)
//
        tvPostTitle = findViewById<TextView>(R.id.title)
        tvPrice = findViewById<TextView>(R.id.tv_price)
        tvBrand=findViewById<TextView>(R.id.tvBrand)
        tvModel=findViewById<TextView>(R.id.tv_Model)
        tvYear=findViewById<TextView>(R.id.tv_Year)
        tvCondition=findViewById<TextView>(R.id.tv_Condition)
        tvColor=findViewById<TextView>(R.id.tv_Color)
        tvDescription=findViewById<TextView>(R.id.tv_Description)
        tvMonthlyPayment=findViewById<TextView>(R.id.tvMonthlyPayment)
        edLoanPrice=findViewById<EditText>(R.id.ed_loan_price)
        edLoanInterestRate=findViewById<EditText>(R.id.ed_loan_interest_rate)
        edLoanDeposit=findViewById<EditText>(R.id.ed_loan_deposit)
        edLoanTerm=findViewById<EditText>(R.id.ed_loan_term)

        user_name = findViewById<TextView>(R.id.tv_name)
        img_user = findViewById<CircleImageView>(R.id.cr_img)
        user_telephone=findViewById<TextView>(R.id.tv_iconphone)
        user_email=findViewById<TextView>(R.id.tv_phone)
        tv_count_view=findViewById<TextView>(R.id.count_view)
        tv_location_duration=findViewById<TextView>(R.id.tv_location_duration)

//Button Share
        val share = findViewById<ImageButton>(R.id.btn_share)
        share.setOnClickListener{
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            startActivity(Intent.createChooser(shareIntent,getString(R.string.title_activity_account)))
        }
//Button Call

////Button SMS
//        val sms = findViewById<Button>(R.id.btn_sms)
//        sms.setOnClickListener {
////                val phoneNumber = "0962363929"
////                val uri = Uri.parse("smsto:0962363929")
////                intent.putExtra("sms_body", "Here goes your message...")
////                val smsManager = SmsManager.getDefault() as SmsManager
//
//            val smsIntent = Intent(Intent.ACTION_VIEW)
//            smsIntent.type = "vnd.android-dir/mms-sms"
//            smsIntent.putExtra("address", "0962363929")
////            smsIntent.putExtra("sms_body", "Body of Message")
//            startActivity(smsIntent)
//        }
//Button Like
        val like = findViewById<ImageView>(R.id.btn_like)
        like.setOnClickListener {
            if (sharedPref.contains("token") || sharedPref.contains("id")) {
                Toast.makeText(this@Detail_New_Post,"This Product add to Your Liked",Toast.LENGTH_SHORT).show()
                Like_post(Encode)
            }else{
                val intent = Intent(this@Detail_New_Post, UserAccount::class.java)
                startActivity(intent)
            }
        }

        val loan= findViewById<ImageView>(R.id.btn_loan)
        loan.setOnClickListener{

            if (sharedPref.contains("token") || sharedPref.contains("id")) {
                val intent = Intent(this@Detail_New_Post, LoanCreateActivity::class.java)
                //put extra id from detail new post to loancreate activity
                intent.putExtra("PutIDLoan",postId)
                startActivity(intent)
            }else{
                val intent = Intent(this@Detail_New_Post, UserAccount::class.java)
                startActivity(intent)
            }
        }

        edLoanInterestRate.setText("1.5")
        edLoanTerm.setText("1")
        tvMonthlyPayment.setText("$ 0.00")

        edLoanPrice.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                //Perform Code
                Toast.makeText(this@Detail_New_Post, edLoanPrice.getText().toString(), Toast.LENGTH_SHORT).show()
                calculateLoanMonthlyPayment()
                return@OnKeyListener true
            }
            false
        })

        edLoanInterestRate.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                //Perform Code
                Toast.makeText(this@Detail_New_Post, edLoanPrice.getText(), Toast.LENGTH_SHORT).show()
                calculateLoanMonthlyPayment()
                return@OnKeyListener true
            }
            false
        })

        edLoanInterestRate.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                //Perform Code
                Toast.makeText(this@Detail_New_Post, edLoanPrice.getText(), Toast.LENGTH_SHORT).show()
                calculateLoanMonthlyPayment()
                return@OnKeyListener true
            }
            false
        })

    }  // oncreate
    fun dialContactPhone(phoneNumber:String) {
    startActivity( Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)))
    }
    fun sms(phoneNumber:String) {
//        startActivity( Intent(Intent.ACTION_SEND, Uri.fromParts("tel", phoneNumber, null)))
        val sendIntent =  Intent(Intent.ACTION_VIEW)
        sendIntent.putExtra("address"  ,phoneNumber)
        sendIntent.putExtra("sms_body", "")
        sendIntent.setType("vnd.android-dir/mms-sms")
        startActivity(sendIntent)
    }

    fun initialProductPostDetail(encode: String){
        var url:String
        var request:Request
        Log.d(TAG,"POST type: "+pt)
        val auth = "Basic $encode"
        if(pt==1) {
            url = ConsumeAPI.BASE_URL + "postbyuser/" + postId
            request=Request.Builder()
                    .url(url)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", auth)
                    .build()
        }
        else {
            url = ConsumeAPI.BASE_URL + "allposts/" + postId
            request = Request.Builder()
                    .url(url)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    //.header("Authorization", auth)
                    .build()
        }

        //val url=ConsumeAPI.BASE_URL+"allposts/"+postId
        val client=OkHttpClient()
        /*
        val request = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                //.header("Authorization", auth)
                .build()
                */
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                Log.d(TAG,mMessage)
                val gson = Gson()
                try {
                    runOnUiThread {
                        var postDetail = PostViewModel()
                        postDetail = gson.fromJson(mMessage, PostViewModel::class.java)
                        Log.e(TAG,"D"+ mMessage)

                        val url1=ConsumeAPI.BASE_URL+"api/v1/years/"+postDetail.year
                        var client1=OkHttpClient()
                        val request1 = Request.Builder()
                                .url(url1)
                                .header("Accept", "application/json")
                                .header("Content-Type", "application/json")
                                .header("Authorization", auth)
                                .build()
                        client1.newCall(request1).enqueue(object : Callback {
                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {
                                val mMessage = response.body()!!.string()
                                runOnUiThread {
                                    try {
                                        val jsonObject = JSONObject(mMessage)
                                        //Log.d(TAG,"Year "+jsonObject.getString("year"))
                                        tvYear.setText(jsonObject.getString("year"))
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                            }

                            override fun onFailure(call: Call, e: IOException) {

                            }

                        })

                        val url2=ConsumeAPI.BASE_URL+"api/v1/models/"+postDetail.modeling
                        val client2=OkHttpClient()
                        val request2 = Request.Builder()
                                .url(url2)
                                .header("Accept", "application/json")
                                .header("Content-Type", "application/json")
                                .header("Authorization", auth)
                                .build()
                        client2.newCall(request2).enqueue(object : Callback {
                            @Throws(IOException::class)
                            override fun onResponse(call: Call, response: Response) {
                                val mMessage = response.body()!!.string()
                                runOnUiThread {
                                    try {
                                        val jsonObject = JSONObject(mMessage)
                                        //Log.d(TAG,"Year "+jsonObject.getString("year"))
                                        tvModel.setText(jsonObject.getString("modeling_name"))

                                        val url3=ConsumeAPI.BASE_URL+"api/v1/brands/"+jsonObject.getString("brand")
                                        val client3=OkHttpClient()
                                        val request3 = Request.Builder()
                                                .url(url3)
                                                .header("Accept", "application/json")
                                                .header("Content-Type", "application/json")
                                                .header("Authorization", auth)
                                                .build()
                                        client3.newCall(request3).enqueue(object : Callback {
                                            @Throws(IOException::class)
                                            override fun onResponse(call: Call, response: Response) {
                                                val mMessage = response.body()!!.string()
                                                runOnUiThread {
                                                    try {
                                                        val jsonObject = JSONObject(mMessage)
                                                        //Log.d(TAG,"Year "+jsonObject.getString("year"))
                                                        tvBrand.setText(jsonObject.getString("brand_name"))
                                                    } catch (e: JSONException) {
                                                        e.printStackTrace()
                                                    }
                                                }
                                            }

                                            override fun onFailure(call: Call, e: IOException) {

                                            }

                                        })

                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                            }

                            override fun onFailure(call: Call, e: IOException) {

                            }

                        })

                        tvPostTitle.setText(postDetail.title.toString())
                        tvPrice.setText("$ "+postDetail.cost.toString())
                        tvCondition.setText(postDetail.condition.toString())
                        tvColor.setText(postDetail.color.toString())
                        tvDescription.setText(postDetail.description.toString())

                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        sdf.timeZone = TimeZone.getTimeZone("GMT")
                        val time:Long = sdf.parse(postDetail.created).time
                        val now:Long = System.currentTimeMillis()
                        val ago:CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
                        tv_location_duration.setText(ago)

                        val base64_front_image=postDetail.base64_front_image.toString()
                        val base64_right_image=postDetail.base64_right_image.toString()
                        val base64_left_image=postDetail.base64_left_image.toString()
                        val base64_back_image=postDetail.base64_back_image.toString()

                        var front_image:String=""
                        var right_image:String=""
                        var back_image:String=""
                        var left_image:String=""

                        if(!base64_front_image.isNullOrEmpty()){
                            val decodedFrontImageString = Base64.decode(base64_front_image, Base64.DEFAULT)
                            val decodedFrontByte = BitmapFactory.decodeByteArray(decodedFrontImageString, 0, decodedFrontImageString.size)
                            front_image=getImageUri(this@Detail_New_Post,decodedFrontByte).toString()
                        }

                        if(!base64_right_image.isNullOrEmpty()){
                            val decodedRightImageString = Base64.decode(base64_right_image, Base64.DEFAULT)
                            val decodedRightByte = BitmapFactory.decodeByteArray(decodedRightImageString, 0, decodedRightImageString.size)
                            right_image=getImageUri(this@Detail_New_Post,decodedRightByte).toString()
                        }
                        if(!base64_left_image.isNullOrEmpty()){
                            val decodedLeftImageString = Base64.decode(base64_left_image, Base64.DEFAULT)
                            val decodedLeftByte = BitmapFactory.decodeByteArray(decodedLeftImageString, 0, decodedLeftImageString.size)
                            left_image=getImageUri(this@Detail_New_Post,decodedLeftByte).toString()
                        }
                        if(!base64_back_image.isNullOrEmpty()){
                            val decodedBackImageString = Base64.decode(base64_back_image, Base64.DEFAULT)
                            val decodedBackByte = BitmapFactory.decodeByteArray(decodedBackImageString, 0, decodedBackImageString.size)
                            back_image=getImageUri(this@Detail_New_Post,decodedBackByte).toString()
                        }

                        val images = listOf(front_image,
                                right_image,
                                left_image,
                                back_image)
                        sliderImage.setItems(images)
                        sliderImage.addTimerToSlide(3000)
                        sliderImage.removeTimerSlide()
                        sliderImage.getIndicator()

                        val created_by:Int=postDetail.created_by.toInt()
                        getUserProfile(created_by,auth)

                        //Initial Related Post
                        var postType:String=""
                        val rent=postDetail.rents
                        val sale=postDetail.sales
                        val buy=postDetail.buys
                        if(rent.count()>0)
                            postType="rent"
                        if(sale.count()>0)
                            postType="sell"
                        if(buy.count()>0)
                            postType="buy"
                        Log.d(TAG,"credfafa"+ postType)
                        initialRelatedPost(encode,postType,postDetail.category,postDetail.modeling,postDetail.cost.toFloat())

                    }
                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun getUserProfile(id:Int,encode: String){
        var user1 = User()
        var URL_ENDPOINT=ConsumeAPI.BASE_URL+"api/v1/users/"+id
        var MEDIA_TYPE=MediaType.parse("application/json")
        var client= OkHttpClient()
        var request=Request.Builder()
                .url(URL_ENDPOINT) 
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                //.header("Authorization",encode)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                val gson = Gson()
                try {
                    user1= gson.fromJson(mMessage, User::class.java)
                    Log.d(TAG,"TAH"+mMessage)
                    runOnUiThread {

                        val profilepicture: String=if(user1.profile.profile_photo==null) " " else user1.profile.base64_profile_image
                        if(profilepicture==null){

                        }else
                        {
                            val decodedString = Base64.decode(profilepicture, Base64.DEFAULT)

                            var decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                            img_user.setImageBitmap(decodedByte)

                        }
                        user_name.setText(user1.username)
                        user_telephone.setText(user1.profile.telephone)
                        user_email.setText(user1.email)
                        findViewById<CircleImageView>(R.id.cr_img).setOnClickListener {
//                            Log.d(TAG,"Tdggggggggggggg"+user1.profile.telephone)
                            val intent = Intent(this@Detail_New_Post, User_post::class.java)
                            intent.putExtra("ID",user1.id.toString())
                            intent.putExtra("Phone",user1.profile.telephone)
                            intent.putExtra("Email",user1.profile.email)
                            //intent.putExtra("Phone",phone.text)
                            startActivity(intent)
                        }
                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun Like_post(encode: String) {
        var url=ConsumeAPI.BASE_URL+"like/"
        val MEDIA_TYPE = MediaType.parse("application/json")
        val post = JSONObject()
        try{
            post.put("post", p)
            post.put("like_by",pk)
            post.put("record_status",1)

        val client = OkHttpClient()
        val body = RequestBody.create(MEDIA_TYPE, post.toString())
        val auth = "Basic $encode"
        val request = Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", auth)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                var respon = response.body()!!.string()
            Log.d("Response",respon)
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.d("Error",call.toString())
            }
        })

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun submitCountView(encode: String) {
        var url=ConsumeAPI.BASE_URL+"countview/"
        val MEDIA_TYPE = MediaType.parse("application/json")
        val post = JSONObject()
        try{
            post.put("post", p)
            post.put("number",1)

            val client = OkHttpClient()
            val body = RequestBody.create(MEDIA_TYPE, post.toString())
            val auth = "Basic $encode"
            val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", auth)
                    .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    var respon = response.body()!!.string()
                    Log.d("Response",respon)
                }

                override fun onFailure(call: Call, e: IOException) {
                    Log.d("Error",call.toString())
                }
            })

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun initialRelatedPost(encode:String,postType:String,category:Int,modeling:Int,cost:Float){
        val itemApi = ArrayList<Item_API>()
        val URL_ENDPOINT=ConsumeAPI.BASE_URL+"relatedpost/?post_type="+postType+"&category="+category+"&modeling="+modeling+"&min_price="+(cost-500)+"&max_price="+(cost+500)
        val client= OkHttpClient()
        val request=Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encode)
                .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                val jsonObject = JSONObject(mMessage)
                val jsonCount=jsonObject.getInt("count")
                Log.d("Thou",jsonCount.toString())

                runOnUiThread {
                    try {
                        Log.d(TAG, "Related post " + mMessage)
                        val jsonArray = jsonObject.getJSONArray("results")
                        if (jsonCount <= 1 ){
                            mprocessBar.visibility = View.GONE
                            tex_noresult.visibility = View.VISIBLE
                        }
                        mprocessBar.visibility = View.GONE
                        for (i in 0 until jsonArray.length()) {
                            val obj = jsonArray.getJSONObject(i)

                            val title = obj.getString("title")
                            val id = obj.getInt("id")
                            val condition = obj.getString("condition")
                            val cost = obj.getDouble("cost")
                            val image = obj.getString("front_image_base64")
                            val img_user = obj.getString("right_image_base64")
                            val postType = obj.getString("post_type")
                            val phoneNumber = obj.getString("contact_phone")

                            var location_duration = ""
                            //var count_view=countPostView(Encode,id)

                            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                            sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
                            val time: Long = sdf.parse(obj.getString("created")).getTime()
                            val now: Long = System.currentTimeMillis()
                            val ago: CharSequence = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
//Call
                            val call = findViewById<ImageView>(R.id.btn_call)
                            call.setOnClickListener{
                                //   makePhoneCall("0962363929")
                                dialContactPhone(phoneNumber)
                                Log.d("Phone ",phoneNumber)
                            }
//SMS
                            val sms = findViewById<ImageView>(R.id.btn_sms)
                            sms.setOnClickListener {
                                sms(phoneNumber)
                                Log.d("SMS ",phoneNumber)
                            }
                            if(postId != id) {
                                Log.d("PostId ",postId.toString())
                                itemApi.add(Item_API(id, img_user, image, title, cost, condition, postType, ago.toString(), jsonCount.toString()))
                            }
                            list_rela!!.adapter = MyAdapter_list_grid_image(itemApi, "Grid")
                            list_rela!!.layoutManager = GridLayoutManager(this@Detail_New_Post,2)
                        }

                        //tv_count_view.setText("View: "+jsonCount)


                    } catch (e: JsonParseException) {
                        e.printStackTrace()
                    }
                }

            }
        })
    }

    fun countPostView(encode:String){
        val URL_ENDPOINT=ConsumeAPI.BASE_URL+"countview/?post="+postId
        var MEDIA_TYPE=MediaType.parse("application/json")
        val client= OkHttpClient()
        val request=Request.Builder()
                .url(URL_ENDPOINT)
                .header("Accept","application/json")
                .header("Content-Type","application/json")
                .header("Authorization",encode)
                .build()
        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
            }
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body()!!.string()
                val gson = Gson()
                try {
                    val jsonObject = JSONObject(mMessage)
                    val jsonCount=jsonObject.getInt("count")
                    runOnUiThread {
                        tv_count_view.setText("View: "+jsonCount)
                    }

                } catch (e: JsonParseException) {
                    e.printStackTrace()
                }

            }
        })
    }

    fun calculateLoanMonthlyPayment(){
        var loanPrice:String=edLoanPrice.text.toString()
        var loanInterestRate:String=edLoanInterestRate.text.toString()
        var loanTerm:String=edLoanTerm.text.toString()

        var aPrice: Double=0.0
        var aInterestRate:Double=0.0
        var aLoanTerm:Int=0

        if(loanPrice.isNullOrEmpty()) aPrice=0.0 else aPrice=loanPrice.toDouble()
        if(loanInterestRate.isNullOrEmpty()) aInterestRate=1.5 else aInterestRate=loanInterestRate.toDouble()
        if(loanTerm.isNullOrEmpty()) aLoanTerm=12 else aLoanTerm=loanTerm.toInt()*12

        val monthlyPayment=LoanCalculator.getLoanMonthPayment(aPrice,aInterestRate,aLoanTerm)
        Log.d(TAG,loanPrice+" "+loanInterestRate+" "+monthlyPayment.toString() +" "+aPrice+" "+aInterestRate+" "+aLoanTerm)
        val df = DecimalFormat("#.####")
        df.roundingMode = RoundingMode.CEILING
        tvMonthlyPayment.setText("$ "+ df.format(monthlyPayment).toString())
    }

    private fun getEncodedString(username: String, password: String): String {
        val userpass = "$username:$password"
        return Base64.encodeToString(userpass.toByteArray(),
                Base64.NO_WRAP)
    }
    fun makePhoneCall(number: String) : Boolean {
        try {
            val intent = Intent(Intent.ACTION_CALL)
            intent.setData(Uri.parse("tel:$number"))
            startActivity(intent)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),
                        42)
            }
        } else {
            // Permission has already been granted
            callPhone()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 42) {
            // If request is cancelled, the result arrays are empty.
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission was granted, yay!
                callPhone()
            } else {
                // permission denied, boo! Disable the
                // functionality
            }
            return
        }
    }

    fun callPhone(){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "0962363929"))
        startActivity(intent)
    }

    fun getImageLocal(filePath:String):Bitmap{
        return getImageLocal(filePath,BitmapUtil.REQUEST_WIDTH,BitmapUtil.REQUEST_HEIGHT)
    }

    fun getImageLocal(filePath:String,reqWidth:Int, reqHeight:Int):Bitmap{
        if(reqWidth==-1||reqHeight==-1){ // no subsample and no
            return BitmapFactory.decodeFile(filePath)
        }else {
            // First decode with inJustDecodeBounds=true to check dimensions
            val options:BitmapFactory.Options =BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filePath, options)

            // Calculate inSampleSize
            options.inSampleSize = BitmapUtil.calculateInSampleSize(options, reqWidth, reqHeight)
            Log.d(TAG, "options inSampleSize "+options.inSampleSize)
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeFile(filePath, options)
        }
    }


     class BitmapUtil {

         companion object{
             val REQUEST_WIDTH:Int = 100
             val REQUEST_HEIGHT:Int = 100

             fun calculateInSampleSize(options:BitmapFactory.Options,reqWidth:Int,reqHeight:Int):Int{

                 // Raw height and width of image
                 val height:Int = options.outHeight
                 val width:Int = options.outWidth
                 var inSampleSize:Int = 1

                 if (height > reqHeight || width > reqWidth) {
                     // Calculate ratios of height and width to requested height and
                     // width
                     val heightRatio:Int = Math.round(height.toFloat() /reqHeight.toFloat())
                     val widthRatio:Int = Math.round(width.toFloat() / reqWidth.toFloat())

                     // Choose the smallest ratio as inSampleSize value, this will
                     // guarantee
                     // a final image with both dimensions larger than or equal to
                     // the
                     // requested height and width.

                     if(heightRatio<widthRatio)
                         inSampleSize=heightRatio
                     else
                         inSampleSize=widthRatio
                 }

                 return inSampleSize
             }
             fun calculateInSampleSize(options:BitmapFactory.Options):Int{
                 return calculateInSampleSize(options, REQUEST_WIDTH, REQUEST_HEIGHT)
             }
         }

    }

    fun getImageUri(inContext:Context,inImage:Bitmap):Uri {
        val bytes:ByteArrayOutputStream = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path:String = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
        return Uri.parse(path)
    }
}
