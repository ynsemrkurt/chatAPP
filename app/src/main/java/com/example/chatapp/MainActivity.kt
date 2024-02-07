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

        auth=FirebaseAuth.getInstance()
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

    fun userLogin(view: View){
        val userEmail=findViewById<EditText>(R.id.editTextUserMail).text.toString()
        val userPassword=findViewById<EditText>(R.id.editTextUserPassword).text.toString()

        if(userEmail.isNullOrEmpty() || userPassword.isNullOrEmpty()){
            Toast.makeText(this,"Lütfen tüm alanları doldurunuz!",Toast.LENGTH_SHORT).show()
        }
        else{
            auth.signInWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this){
                    if (it.isSuccessful){
                        Toast.makeText(this,"Giriş Başarılı!",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this,"Hatalı giriş bilgileri!",Toast.LENGTH_SHORT).show()
                        findViewById<EditText>(R.id.editTextUserPassword).setText("")
                    }
                }
        }


    }
}