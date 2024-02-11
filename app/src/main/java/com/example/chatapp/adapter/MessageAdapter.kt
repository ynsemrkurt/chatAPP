package com.example.chatapp.adapter

import android.content.Intent
import android.net.Uri
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.activity.ChatActivity
import com.example.chatapp.model.Chat
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.core.Context
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.contracts.Returns

class MessageAdapter(private val context: android.content.Context, private val messageList: ArrayList<Chat>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>(){

    private val friendMessageType=0
    private val userMessageType=1
    var firebaseUser:FirebaseUser?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType==userMessageType){
            val view=LayoutInflater.from(parent.context).inflate(R.layout.user_message,parent,false)
            return ViewHolder(view)
        }
        else{
            val view=LayoutInflater.from(parent.context).inflate(R.layout.friend_message,parent,false)
            return ViewHolder(view)
        }


    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat=messageList[position]
        holder.textUserMessage.text=chat.message
    }

    class ViewHolder(view:View): RecyclerView.ViewHolder(view){
        val textUserMessage:TextView=view.findViewById(R.id.userMessage)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser=FirebaseAuth.getInstance().currentUser
        if (messageList[position].senderId==firebaseUser!!.uid){
            return userMessageType
        }
        else{
            return friendMessageType
        }
    }
}


