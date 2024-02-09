package com.example.chatapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


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
                Toast.makeText(applicationContext,"Please enter your name!",Toast.LENGTH_SHORT).show()
                return
            }

            if (userPassword.length<6){
                showToast("The password should not be shorter than 6 digits!")
                return
            }

            if(userPassword!=confirmPassword){
                showToast("Registration is unsuccessful. The passwords don't match!")
                findViewById<EditText>(R.id.editTextUserPasswordConfirm).setText("")
                return
            }

            if (userEmail.isNullOrEmpty() || !controlEmail(userEmail)){
                showToast("Please enter a valid e-mail!")
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
                                showToast("Registration is successful!")
                                val intent=Intent(this, UsersActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                showToast("Registration failed, please check the information!")
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

