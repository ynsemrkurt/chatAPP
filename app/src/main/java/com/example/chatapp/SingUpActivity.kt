package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SingUpActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_up)

        auth=FirebaseAuth.getInstance()

        }

         fun userSave(view: View){

            val userName = findViewById<EditText>(R.id.editTextUserName).text.toString()
            val userEmail = findViewById<EditText>(R.id.editTextUserMail).text.toString()
            val userPassword = findViewById<EditText>(R.id.editTextUserPassword).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.editTextUserPasswordConfirm).text.toString()

            if (userName.isNullOrEmpty()){
                Toast.makeText(applicationContext,"Lütfen Adınızı Giriniz",Toast.LENGTH_SHORT).show()
                return
            }

            if (userPassword.length<6){
                showToast("Şifre 6 haneden kısa olmamalı.")
                return
            }

            if(userPassword!=confirmPassword){
                showToast("Kayıt başarısız. Şifreler uyuşmuyor.")
                findViewById<EditText>(R.id.editTextUserPasswordConfirm).setText("")
                return
            }

            if (userEmail.isNullOrEmpty() ){
                showToast("Lütfen geçerli bir e-posta giriniz.")
                return
            }

            auth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this){
                    if (it.isSuccessful){
                        val user: FirebaseUser? =auth.currentUser
                        val userId:String=user!!.uid
                        databaseReference=FirebaseDatabase.getInstance().getReference("users").child(userId)

                        val hashMap:HashMap<String,String> = HashMap()
                        hashMap.put("userId",userId)
                        hashMap.put("userName",userName)
                        hashMap.put("pimage","")

                        databaseReference.setValue(hashMap).addOnCompleteListener(this){
                            if (it.isSuccessful){
                                val intent=Intent(this,MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }

        }

        private fun controlEmail(email: String): Boolean {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        private fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

    }



data class User(val userName: String, val userMail: String, val userPassword: String)
