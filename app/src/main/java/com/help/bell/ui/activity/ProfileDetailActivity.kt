package com.help.bell.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.help.bell.databinding.ActivityProfileDetailBinding
import com.help.bell.model.User

class ProfileDetailActivity : AppCompatActivity() {
    private val binding: ActivityProfileDetailBinding by lazy {
        ActivityProfileDetailBinding.inflate(layoutInflater)
    }

    private var currentUID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        currentUID = intent.getStringExtra("uid").toString()
        if(currentUID.isEmpty()) finish()

        val userInfoRef = FirebaseDatabase.getInstance().reference.child("users").child(currentUID)
        userInfoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try{
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            binding.textViewUserName.text = user.name
                            binding.textViewOption1.text = user.op1
                            binding.textViewOption2.text = user.op2
                            binding.textViewOption3.text = user.op3
                            binding.textViewOption4.text = user.op4
                            binding.textViewOption5.text = user.op5
                        }
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}
