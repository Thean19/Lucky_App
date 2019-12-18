package com.example.lucky_app.Product_New_Post

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bt_121shoppe.motorbike.activities.Item_API
import com.bt_121shoppe.motorbike.R

import java.io.ByteArrayOutputStream


class MyAdapter_user_post(private val itemList: ArrayList<Item_API>, val type: String?) : RecyclerView.Adapter<MyAdapter_user_post.ViewHolder>() {

//    internal var loadMoreListener: OnLoadMoreListener? = null
 //   internal var loadMoreListener: com.bt_121shoppe.motorbike.Product_New_Post.MyAdapter_user_post.OnLoadMoreListener? = null
//    internal var loadMoreListener: MyAdapter_list_grid_image.OnLoadMoreListener? = null
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
        val btn_delete = itemView.findViewById<Button>(R.id.btndelete)
        val btn_edit = itemView.findViewById<Button>(R.id.btnedit_post)
        val btn_renew = itemView.findViewById<Button>(R.id.btn_renew)

        fun bindItems(item: Item_API) {
//            imageView.setImageResource(item.image)

            val decodedString = Base64.decode(item.img_user, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            imageView.setImageBitmap(decodedByte)
//            Log.d("String = ",)
            title.text = item.title
            cost.text = item.cost.toString()

            if (item.postType.equals("sell")) {
                post_type.setImageResource(R.drawable.sell)
            } else if (item.postType.equals("buy")) {
                post_type.setImageResource(R.drawable.buy)
            } else
                post_type.setImageResource(R.drawable.rent)

            itemView.findViewById<LinearLayout>(R.id.linearLayout).setOnClickListener {
//                val intent = Intent(itemView.context, Detail_New_Post::class.java)
//                intent.putExtra("Image",decodedByte)
//                intent.putExtra("Image_user",decodedByte)
//                intent.putExtra("Title",item.title)
//                  intent.putExtra("Price",item.cost)
//////                intent.putExtra("Name",item.name)
//                  intent.putExtra("ID",item.id)
//                itemView.context.startActivity(intent)
//                intent.putExtra("Price", item.cost)
//////                intent.putExtra("Name",item.name)
//                intent.putExtra("ID", item.id)
//                itemView.context.startActivity(intent)
//            }

            btn_renew.setOnClickListener {
                Toast.makeText(it.context, "Re_new:" + item.title, Toast.LENGTH_SHORT).show()
            }
            itemView.findViewById<Button>(R.id.btnedit_post).setOnClickListener {
                Toast.makeText(it.context,"BTN Edit",Toast.LENGTH_SHORT).show()
            }
            btn_delete.setOnClickListener {
                Toast.makeText(it.context, "Delete" + item.title, Toast.LENGTH_SHORT).show()
            }
        }

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

    //

}
    internal class LoadHolder(itemView: View) : RecyclerView.ViewHolder(itemView);
}
