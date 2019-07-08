package com.example.lucky_app.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.lucky_app.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lucky_app.Startup.Item

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [account_user_list]interface.
 */
class account_user_list: Fragment() {

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.user_post_list, container, false)

        val recyclrview = view.findViewById<RecyclerView>(R.id.recyclerView)

        val item = ArrayList<Item>()
        item.addAll(Item.getUser_Post(2))

        recyclrview.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        recyclrview.adapter = MyAdapter_user(item,null)

        return view
    }
}
