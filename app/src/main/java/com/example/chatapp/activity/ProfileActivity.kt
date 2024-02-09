@file:Suppress("DEPRECATION")

package com.example.chatapp.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.model.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.util.UUID

class ProfileActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    private var filePath:Uri?=null
    private var request:Int=2020

    private lateinit var storage:FirebaseStorage
    private lateinit var strgRef:StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        firebaseUser= FirebaseAuth.getInstance().currentUser!!
        databaseReference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.uid)

        storage=Firebase.storage
        strgRef=storage.reference

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user?.userId == firebaseUser.uid) {
                    if (user.pimage.isEmpty()) {
                        findViewById<CircleImageView>(R.id.imageProfile).setImageResource(R.drawable.profile_image)
                    } else {
                        try {
                            val imageUri = Uri.parse(user.pimage)
                            Glide.with(this@ProfileActivity).load(imageUri).placeholder(R.drawable.profile_image)
                                .into(findViewById<CircleImageView>(R.id.imageProfile))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            showToast("Geçersiz uri")
                        }
                    }
                    findViewById<EditText>(R.id.editTextUserName).setText(user.userName)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity,"ERROR",Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun openImageFolder(view: View){
        val intent=Intent()
        intent.type="image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"),request)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == request && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                findViewById<CircleImageView>(R.id.imageProfile).setImageBitmap(bitmap)
                if (filePath!=null){
                    showToast("Please wait, image is loading!")
                    val ref:StorageReference=strgRef.child("image/"+firebaseUser.uid)
                    ref.putFile(filePath!!)
                        .addOnSuccessListener {
                            showToast("Uploaded image!")
                        }
                        .addOnFailureListener{
                            showToast("Failed, please try again!")
                        }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun uploadData(view: View){
        val user: FirebaseUser? =FirebaseAuth.getInstance().currentUser
        val userId:String=user!!.uid

        databaseReference=FirebaseDatabase.getInstance().getReference("users").child(userId)

        val hashMap:HashMap<String,String> = HashMap()
        hashMap.put("userId",userId)
        hashMap.put("userName",findViewById<EditText>(R.id.editTextUserName).text.toString())
        if (filePath!=null){
            hashMap.put("pimage",filePath.toString())
        }
        databaseReference.setValue(hashMap).addOnCompleteListener(this){
            if (it.isSuccessful){
                showToast("Registration Is Successful!")
            }
            else{
                showToast("Registration failed, please check the information!")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

