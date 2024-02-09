package com.example.chatapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.adapter.UserAdapter
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersActivity : AppCompatActivity() {

    private lateinit var userRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        userRecyclerView=findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager=LinearLayoutManager(this, RecyclerView.VERTICAL ,false)

        var userList=ArrayList<User>()

        var firebase:FirebaseUser= FirebaseAuth.getInstance().currentUser!!
        var databaseReference:DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (dataSnapShot:DataSnapshot in snapshot.children){
                    val user: User? =dataSnapShot.getValue(User::class.java)
                    if (user!!.userId != firebase.uid){
                        userList.add(user)
                    }
                }
                val userAdapter=UserAdapter(this@UsersActivity,userList)
                userRecyclerView.adapter=userAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UsersActivity,"ERROR",Toast.LENGTH_SHORT).show()
            }

        })

    }
    fun openProfile(view: View){
        val intent=Intent(this,ProfileActivity::class.java)
        startActivity(intent)
    }
}