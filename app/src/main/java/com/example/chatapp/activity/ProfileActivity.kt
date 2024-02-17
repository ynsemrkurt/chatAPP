package com.example.chatapp.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.IOException

class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    private var filePath: Uri? = null
    private var request: Int = 2020

    private lateinit var storage: FirebaseStorage
    private lateinit var strgRef: StorageReference

    private var imageUrl: String = ""

    private lateinit var getContent: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        databaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.uid)

        storage = Firebase.storage
        strgRef = storage.reference

        getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                onActivityResult(request, result.resultCode, result.data)
            }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user?.userId == firebaseUser.uid) {
                    if (user.pimage.isEmpty()) {
                        findViewById<CircleImageView>(R.id.imageProfile).setImageResource(R.drawable.profile_image)
                    } else {
                        try {
                            val imageUri = Uri.parse(user.pimage)
                            Glide.with(this@ProfileActivity).load(imageUri)
                                .placeholder(R.drawable.profile_image)
                                .into(findViewById<CircleImageView>(R.id.imageProfile))
                            imageUrl = user.pimage
                        } catch (e: Exception) {
                            findViewById<CircleImageView>(R.id.imageProfile).setImageResource(R.drawable.login_image)
                        }
                    }
                    findViewById<EditText>(R.id.editTextUserName).setText(user.userName)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("ERROR")
            }
        })


        findViewById<ImageButton>(R.id.buttonLogout).setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Logout")
            alertDialogBuilder.setMessage("Are you sure you want to logout?")
            alertDialogBuilder.setPositiveButton("Logout") { _, _ ->
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                showToast("Has been logged out.")
            }
            alertDialogBuilder.setNegativeButton("Cancel") { _, _ -> }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }


        findViewById<ImageButton>(R.id.changeImage).setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            getContent.launch(Intent.createChooser(intent, "Select Profile Image"))
        }



        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            if (findViewById<EditText>(R.id.editTextUserName).text.toString().isNotEmpty()) {
                val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                val userId: String = user!!.uid

                databaseReference =
                    FirebaseDatabase.getInstance().getReference("users").child(userId)

                val hashMap: HashMap<String, String> = HashMap()
                hashMap["userId"] = userId
                hashMap["userName"] = findViewById<EditText>(R.id.editTextUserName).text.toString()
                hashMap["pimage"] = imageUrl
                databaseReference.setValue(hashMap).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        showToast("Registration Is Successful!")
                    } else {
                        showToast("Registration failed, please check the information!")
                    }
                }
            } else {
                showToast("User name cannot be empty!")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                showToast("Image is uploading...")

                val originalBitmap: Bitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                val imageStream = ByteArrayOutputStream()
                originalBitmap.compress(Bitmap.CompressFormat.JPEG, 18, imageStream)
                val imageArray = imageStream.toByteArray()

                val ref: StorageReference = strgRef.child("image/" + firebaseUser.uid)
                ref.putBytes(imageArray)
                    .addOnSuccessListener {
                        showToast("Uploaded image!")
                    }
                    .addOnFailureListener {
                        showToast("Failed, please try again!")
                    }
                ref.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                }
                findViewById<CircleImageView>(R.id.imageProfile).setImageBitmap(originalBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
