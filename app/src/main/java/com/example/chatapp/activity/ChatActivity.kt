package com.example.chatapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.adapter.MessageAdapter
import com.example.chatapp.model.Chat
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

import de.hdodenhof.circleimageview.CircleImageView

class ChatActivity : AppCompatActivity() {
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var reference: DatabaseReference? = null
    private var chatList = ArrayList<Chat>()
    private lateinit var recyclerViewMessages: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages)
        recyclerViewMessages.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val intent = intent
        val friendUserId = intent.getStringExtra("userId").toString()
        reference = FirebaseDatabase.getInstance().getReference("users").child(friendUserId)

        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user?.pimage.isNullOrEmpty()) {
                    findViewById<CircleImageView>(R.id.imageFriendProfileImage)
                        .setImageResource(R.drawable.profile_image)
                } else {
                    Glide.with(this@ChatActivity).load(user?.pimage)
                        .into(findViewById(R.id.imageFriendProfileImage))
                }
                findViewById<TextView>(R.id.textFriendName).text = user?.userName
            }

            override fun onCancelled(error: DatabaseError) {
                
            }
        })

        findViewById<ImageButton>(R.id.buttonSend).setOnClickListener {
            if (findViewById<EditText>(R.id.textMessage).text.isNotEmpty()) {
                reference = FirebaseDatabase.getInstance().getReference().child("Chat")

                val hashMap: HashMap<String, String> = HashMap()
                hashMap["senderId"] = firebaseUser?.uid.toString()
                hashMap["recipientId"] = friendUserId
                hashMap["message"] = findViewById<EditText>(R.id.textMessage).text.toString()

                reference!!.push().setValue(hashMap)
                findViewById<EditText>(R.id.textMessage).setText("")
            }
        }

        messageList(friendUserId)
    }

    private fun messageList(friendId: String) {
        chatList.clear()
        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Chat")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val chat = dataSnapShot.getValue(Chat::class.java)
                    if ((chat?.senderId == firebaseUser?.uid && chat?.recipientId == friendId) ||
                        (chat?.senderId == friendId && chat.recipientId == firebaseUser?.uid)
                    ) {
                        chatList.add(chat)
                    }
                }
                val chatAdapter = MessageAdapter(this@ChatActivity, chatList)
                recyclerViewMessages.adapter = chatAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
