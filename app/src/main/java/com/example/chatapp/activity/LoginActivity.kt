package com.example.chatapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth=FirebaseAuth.getInstance()
    }

    fun goSingUp(view: View){
        val intent=Intent(this, SingUpActivity::class.java)
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
        val userEmail=findViewById<EditText>(R.id.editTextUserMail)
        val userPassword=findViewById<EditText>(R.id.editTextUserPassword)

        if(userEmail.text.toString().isNullOrEmpty() || userPassword.text.toString().isNullOrEmpty()){
            Toast.makeText(this,"Lütfen tüm alanları doldurunuz!",Toast.LENGTH_SHORT).show()
        }
        else{
            auth.signInWithEmailAndPassword(userEmail.text.toString(),userPassword.text.toString())
                .addOnCompleteListener(this){
                    if (it.isSuccessful){
                        Toast.makeText(this,"Giriş başarılı!",Toast.LENGTH_SHORT).show()
                        userEmail.setText("")
                        userPassword.setText("")
                        val intent=Intent(this, UsersActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"Hatalı giriş bilgileri!",Toast.LENGTH_SHORT).show()
                        findViewById<EditText>(R.id.editTextUserPassword).setText("")
                    }
                }
        }


    }
}