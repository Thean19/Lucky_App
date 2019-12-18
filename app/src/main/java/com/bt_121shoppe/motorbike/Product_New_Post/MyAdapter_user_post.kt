package com.bt_121shoppe.motorbike.Product_New_Post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.motorbike.activities.Camera

import com.bt_121shoppe.motorbike.R
import java.io.ByteArrayOutputStream
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
//import javax.swing.text.StyleConstants.setIcon
import androidx.appcompat.app.AlertDialog
import com.bt_121shoppe.motorbike.activities.Account
import com.bt_121shoppe.motorbike.Api.ConsumeAPI
import com.bt_121shoppe.motorbike.Api.api.TabA1_api
import com.bt_121shoppe.motorbike.firebases.FBPostCommonFunction
import com.bt_121shoppe.motorbike.utils.CommonFunction
import com.bumptech.glide.Glide
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.Instant


class MyAdapter_user_post(private val itemList: ArrayList<TabA1_api>, val type: String?) : RecyclerView.Adapter<MyAdapter_user_post.ViewHolder>() {

    //internal var loadMoreListener: ViewHolder.OnLoadMoreListener? = null
    internal var isLoading = false
    internal var isMoreDataAvailable = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (type.equals("List")) {
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_list1, parent, false)
            Log.d("Type ", type.toString())
            return ViewHolder(layout)
        } else if (type.equals("Grid")) {
            Log.d("Type ", type.toString())
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
            return ViewHolder(layout)
        } else {
            Log.d("Type ", type.toString())
            val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
            return ViewHolder(layout)
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val post_type = itemView.findViewById<ImageView>(R.id.post_type)
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val title = itemView.findViewById<TextView>(R.id.title)
        val cost = itemView.findViewById<TextView>(R.id.tv_price)
        val btn_edit = itemView.findViewById<Button>(R.id.btnedit_post)
        val btn_delete = itemView.findViewById<Button>(R.id.btndelete)
        val btn_renewal = itemView.findViewById<Button>(R.id.btn_renew)
        val time = itemView.findViewById<TextView>(R.id.location)
        val count_view = itemView.findViewById<TextView>(R.id.user_view)
        val tv_user_view = itemView.findViewById<TextView>(R.id.user_view1)

        fun bindItems(item: TabA1_api) {
            Glide.with(itemView.context).load(item.image).placeholder(R.drawable.no_image_available).thumbnail(0.1f).into(imageView)
            title.text = item.title
            cost.text = "$" + item.cost.toString()
            time.text = item.location_duration
            count_view.text = "" + item.count_view

            var lang: String = tv_user_view.text as String
            if (lang == "View:") {
                if (item.postType.equals("sell")) {
                    post_type.setImageResource(R.drawable.sell)
                } else if (item.postType.equals("buy")) {
                    post_type.setImageResource(R.drawable.buy)
                } else
                    post_type.setImageResource(R.drawable.rent)
            } else {
                if (item.postType.equals("sell")) {
                    post_type.setImageResource(R.drawable.sell_kh)
                } else if (item.postType.equals("buy")) {
                    post_type.setImageResource(R.drawable.buy_kh)
                } else
                    post_type.setImageResource(R.drawable.rent_kh)
            }

            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
                val intent = Intent(itemView.context, Detail_New_Post::class.java)
                intent.putExtra("Price", item.cost)
                intent.putExtra("postt", 1)
                intent.putExtra("ID", item.id)
                itemView.context.startActivity(intent)
            }

            btn_edit.setOnClickListener {
                val intent = Intent(itemView.context, Camera::class.java)
                intent.putExtra("id_product", item.id)
                itemView.context.startActivity(intent)
            }
            btn_delete.setOnClickListener {
                //Toast.makeText(it.context,"Hello"+item.title,Toast.LENGTH_SHORT).show()
                val prefer = it.context.getSharedPreferences("Settings", Activity.MODE_PRIVATE)
                val language = prefer.getString("My_Lang", "")
                lateinit var sharedPref: SharedPreferences
                var name = ""
                var pass = ""
                var Encode = ""
                var pk = 0

                sharedPref = it.context.getSharedPreferences("RegisterActivity", Context.MODE_PRIVATE)
                if (sharedPref.contains("token") || sharedPref.contains("id")) {
                    name = sharedPref.getString("name", "")
                    pass = sharedPref.getString("pass", "")
                    Encode = "Basic " + CommonFunction.getEncodedString(name, pass)
                    if (sharedPref.contains("token")) {
                        pk = sharedPref.getInt("Pk", 0)
                    } else if (sharedPref.contains("id")) {
                        pk = sharedPref.getInt("id", 0)
                    }
                }

                val items = arrayOf<CharSequence>("This product has been sold", "Suspend this ads", "Delete to post new ads", "Cancel")
                val itemkh = arrayOf<CharSequence>("ផលិតផលនេះត្រូវបានលក់ចេញ", "ផ្អាកការប្រកាសនេះ", "លុបដើម្បីប្រកាសជាថ្មី", "បោះបង់")
                val builder = AlertDialog.Builder(it.context)
                if (language.equals("km")) {
                    builder.setItems(itemkh) { dialog, ite ->
                        if (itemkh[ite] == "បោះបង់") {
                            dialog.dismiss()
                        } else {
                            val reason = itemkh[ite].toString()
                            val URL_ENDCODE = ConsumeAPI.BASE_URL + "api/v1/renewaldelete/" + item.id.toInt() + "/"
                            val media = MediaType.parse("application/json")
                            val client = OkHttpClient()
                            val data = JSONObject()
                            try {
                                data.put("status", 2)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    data.put("modified", Instant.now().toString())
                                }
                                data.put("modified_by", pk)
                                data.put("rejected_comments", reason)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            val body = RequestBody.create(media, data.toString())
                            val request = Request.Builder()
                                    .url(URL_ENDCODE)
                                    .put(body)
                                    .header("Accept", "application/json")
                                    .header("Content-Type", "application/json")
                                    .header("Authorization", Encode)
                                    .build()

                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    val message = e.message.toString()
                                    Log.d("failure Response", message)
                                }

                                @Throws(IOException::class)
                                override fun onResponse(call: Call, response: Response) {
                                    val message = response.body()!!.string()
                                    FBPostCommonFunction.deletePost(item.id.toString())
                                    val intent = Intent(it.context, Account::class.java)
                                    it.context.startActivity(intent)
                                }
                            })
                        }
                    }
                    builder.show()
                } else if (language.equals("en")) {
                    builder.setItems(items) { dialog, ite ->
                        if (items[ite] == "Cancel") {
                            dialog.dismiss()

                        }
                        else {
                            val reason = items[ite].toString()
                            val URL_ENDCODE = ConsumeAPI.BASE_URL + "api/v1/renewaldelete/" + item.id.toInt() + "/"
                            val media = MediaType.parse("application/json")
                            val client = OkHttpClient()
                            val data = JSONObject()
                            try {
                                //data.put("id",60)
                                data.put("status", 2)
                                //ata.put("description","Test")
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    data.put("modified", Instant.now().toString())
                                }
                                data.put("modified_by", pk)
                                data.put("rejected_comments", reason)
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            Log.d("TAG", URL_ENDCODE + " " + data)
                            val body = RequestBody.create(media, data.toString())
                            val request = Request.Builder()
                                    .url(URL_ENDCODE)
                                    .put(body)
                                    .header("Accept", "application/json")
                                    .header("Content-Type", "application/json")
                                    .header("Authorization", Encode)
                                    .build()

                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    val message = e.message.toString()
                                    Log.d("failure Response", message)
                                }

                                @Throws(IOException::class)
                                override fun onResponse(call: Call, response: Response) {
                                    val message = response.body()!!.string()
                                    Log.d("Response Renewal", message)
                                    val intent = Intent(it.context, Account::class.java)
                                    it.context.startActivity(intent)
                                }
                            })
                        }
                    }
                    builder.show()
                }

       } // delete
                if (item.status_id == "3") {
                    btn_renewal.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    btn_renewal.setTextColor(Color.parseColor("#FF9400"))
                    btn_renewal.setText(R.string.pending)
                    btn_renewal.setOnClickListener { }

                }
                if (item.status_id == "4") {
                    btn_renewal.setCompoundDrawablesWithIntrinsicBounds(R.drawable.refresh, 0, 0, 0)
                    btn_renewal.setTextColor(Color.parseColor("#0A0909"))
                    btn_renewal.setText(R.string.renew)

                    btn_renewal.setOnClickListener {
                        lateinit var sharedPref: SharedPreferences
                        var name = ""
                        var pass = ""
                        var Encode = ""
                        var pk = 0

                        sharedPref = it.context.getSharedPreferences("RegisterActivity", Context.MODE_PRIVATE)
                        if (sharedPref.contains("token") || sharedPref.contains("id")) {
                            name = sharedPref.getString("name", "")
                            pass = sharedPref.getString("pass", "")
                            Encode = "Basic " + CommonFunction.getEncodedString(name, pass)
                            if (sharedPref.contains("token")) {
                                pk = sharedPref.getInt("Pk", 0)
                            } else if (sharedPref.contains("id")) {
                                pk = sharedPref.getInt("id", 0)
                            }
                        }
                        AlertDialog.Builder(it.context)
                                .setTitle(R.string.Post_Renewal)
                                .setMessage(R.string.renew_post)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, whichButton ->
                                    val URL_ENDCODE = ConsumeAPI.BASE_URL + "api/v1/renewaldelete/" + item.id.toInt() + "/"
                                    val media = MediaType.parse("application/json")
                                    val client = OkHttpClient()
                                    val data = JSONObject()
                                    try {
                                        data.put("status", 4)
                                        //ata.put("description","Test")
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            data.put("modified", Instant.now().toString())
                                        }
                                        data.put("modified_by", pk)
                                        data.put("rejected_comments", "")
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    //Log.d("TAG", URL_ENDCODE + " " + data)
                                    val body = RequestBody.create(media, data.toString())
                                    val request = Request.Builder()
                                            .url(URL_ENDCODE)
                                            .put(body)
                                            .header("Accept", "application/json")
                                            .header("Content-Type", "application/json")
                                            .header("Authorization", Encode)
                                            .build()

                                    client.newCall(request).enqueue(object : Callback {
                                        override fun onFailure(call: Call, e: IOException) {
                                            val message = e.message.toString()
                                            Log.d("failure Response", message)
                                        }

                                        @Throws(IOException::class)
                                        override fun onResponse(call: Call, response: Response) {
                                            val message = response.body()!!.string()
                                            FBPostCommonFunction.renewalPost(item.id.toString())
                                            val intent = Intent(it.context, Account::class.java)
                                            it.context.startActivity(intent)
                                        }
                                    })
                                })
                                .setNegativeButton(android.R.string.no, null).show()
                    }
                } //status id
            fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
                val bytes = ByteArrayOutputStream()
                inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
                return Uri.parse(path)
            }

            fun BitMapToString(bitmap: Bitmap): String {
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val arr = baos.toByteArray()
                return Base64.encodeToString(arr, Base64.DEFAULT)
            }
        }
        internal class LoadHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}
