package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.model.User
import com.google.firebase.database.core.Context
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val context: android.content.Context,private val userList: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.user_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user=userList[position]
        holder.textUserName.text=user.userName
        Glide.with(context).load(user.pimage).placeholder(R.drawable.profile_image).into(holder.userImage)
    }

    class ViewHolder(view:View): RecyclerView.ViewHolder(view){
        val textUserName:TextView=view.findViewById(R.id.userName)
        val userImage:CircleImageView=view.findViewById(R.id.userProfileImage)
        val textMessage:TextView=view.findViewById(R.id.userMessage)
    }
}


