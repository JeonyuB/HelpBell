package com.help.bell.ui.activity

import ReplyAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.help.bell.databinding.ActivityHashtagDetailBinding
import com.help.bell.model.HashtagData
import com.help.bell.model.ReplyData
import com.help.bell.util.SharedManager

class HashtagDetailActivity : AppCompatActivity() {

    private val binding: ActivityHashtagDetailBinding by lazy {
        ActivityHashtagDetailBinding.inflate(layoutInflater)
    }
    var replyList = mutableListOf<ReplyData>()
    val currentUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val data = intent.getSerializableExtra("data") as HashtagData

        binding.textViewName.text = data.name
        binding.textViewEtc.text = data.hashtags
        binding.textViewTime.text = data.time
        binding.textViewLoc.text = data.location
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext).apply {
            orientation = LinearLayoutManager.VERTICAL
            reverseLayout = true
            stackFromEnd = true
        }
        fetchHashtagDataForDate(data.key, binding.recyclerView, "")

        binding.btnReply.setOnClickListener {
            val reply = binding.etReply.text.toString()
            val replydata = ReplyData(
                SharedManager.getString(applicationContext, "name", ""),
                System.currentTimeMillis(),
                reply,
                currentUser?.uid ?: ""
            )
            addReply(data.key, replydata)
        }
    }

    private fun addReply(key: String, data: ReplyData) {
        if (currentUser != null) {
            // 데이터베이스 경로 생성
            val databasePath = "hashtags/$key/reply"

            // 데이터 저장
            binding.etReply.setText("")
            val databaseReference =
                FirebaseDatabase.getInstance().getReference(databasePath)
            databaseReference.push().setValue(data)
        }
    }

    private fun fetchHashtagDataForDate(key: String, recyclerView: RecyclerView, query: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val databasePath = "hashtags/$key/reply"

            val databaseReference = FirebaseDatabase.getInstance().getReference(databasePath)

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    replyList.clear()

                    for (childSnapshot in snapshot.children) {
                        val hashtagData = childSnapshot.getValue(ReplyData::class.java)
                        hashtagData?.let {
                            replyList.add(it)
                        }
                    }

                    recyclerView.adapter = ReplyAdapter(replyList)

                }

                override fun onCancelled(error: DatabaseError) {
                    // 데이터 가져오기에 실패한 경우 처리
                }
            })
        }
    }
}