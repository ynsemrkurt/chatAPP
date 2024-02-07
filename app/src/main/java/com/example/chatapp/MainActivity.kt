package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database



class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goSingUp(view: View){
        val intent=Intent(this,SingUpActivity::class.java)
        startActivity(intent)
    }

    fun hiddenPassword(view: View) {
        val editTextPassword = findViewById<EditText>(R.id.editTextUserPassword)
        val currentInputType = editTextPassword.inputType

        if (currentInputType == InputType.TYPE_CLASS_TEXT) {
            editTextPassword.inputType =
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        } else {
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT
        }

        editTextPassword.setSelection(editTextPassword.text.length)
    }
}