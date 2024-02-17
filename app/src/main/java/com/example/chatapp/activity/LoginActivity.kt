package com.example.chatapp.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                val intent = Intent(this, UsersActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        findViewById<Button>(R.id.buttonSingUp).setOnClickListener {
            val intent = Intent(this, SingUpActivity::class.java)
            startActivity(intent)
        }



        findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            val userEmail = findViewById<EditText>(R.id.editTextUserMail)
            val userPassword = findViewById<EditText>(R.id.editTextUserPassword)

            if (userEmail.text.toString().isEmpty() || userPassword.text.toString()
                    .isEmpty()
            ) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(
                    userEmail.text.toString(),
                    userPassword.text.toString()
                )
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                            userEmail.setText("")
                            userPassword.setText("")
                            val intent = Intent(this, UsersActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Incorrect login information!", Toast.LENGTH_SHORT)
                                .show()
                            findViewById<EditText>(R.id.editTextUserPassword).setText("")
                        }
                    }
            }
        }



        findViewById<ImageButton>(R.id.hiddenButton).setOnClickListener {
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
}
