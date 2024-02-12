package com.example.chatapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MessageAdapter(private val context: android.content.Context, private val messageList: ArrayList<Chat>) :
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private val friendMessageType = 0
    private val userMessageType = 1
    private var firebaseUser: FirebaseUser? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutId = if (viewType == userMessageType) R.layout.user_message else R.layout.friend_message
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = messageList[position]
        holder.textUserMessage.text = chat.message
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textUserMessage: TextView = view.findViewById(R.id.userMessage)
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (messageList[position].senderId == firebaseUser?.uid) userMessageType else friendMessageType
    }
}
