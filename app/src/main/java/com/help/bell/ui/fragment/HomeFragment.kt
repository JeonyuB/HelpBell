package com.help.bell.ui.fragment

import HashtagAdapter
import android.R
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.help.bell.databinding.DialogAddHashtagBinding
import com.help.bell.databinding.FragmentHomeBinding
import com.help.bell.model.HashtagData
import com.help.bell.util.SharedManager

class HomeFragment : Fragment() {
    // 뷰 바인딩 변수 선언
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    var hashtagList = mutableListOf<HashtagData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 뷰 바인딩 초기화
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        fetchHashtagDataForDate(binding.recyclerView, "")

        binding.btnAdd.setOnClickListener { showAddHashtagDialog() }
        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()
            fetchHashtagDataForDate(binding.recyclerView, query)

        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
            reverseLayout = true
            stackFromEnd = true
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 뷰 바인딩 해제
        _binding = null
    }

    private fun showAddHashtagDialog() {
        val dialogView = DialogAddHashtagBinding.inflate(LayoutInflater.from(requireContext()))

        val locations = arrayOf(
            "서울특별시",
            "인천광역시",
            "경기도",
            "강원도",
            "충청남도",
            "충청북도",
            "전라북도",
            "전라남도",
            "경상북도",
            "경상남도",
            "대구광역시",
            "대전광역시",
            "광주광역시",
            "부산광역시",
            "울산광역시",
            "세종특별자치도",
            "제주특별자치도"
        )

        val adapter = ArrayAdapter(dialogView.root.context, R.layout.simple_spinner_item, locations)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        dialogView.spLoc.adapter = adapter

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView.root)
            .setTitle("해시태그 추가")
            .setPositiveButton("추가") { dialog, which ->
                val name = dialogView.etName.text.toString()
                val time = dialogView.etTime.text.toString()
                val location = dialogView.spLoc.selectedItem.toString()
                val hashtags = dialogView.etEtc.text.toString()

                // Firebase Realtime Database에 데이터 저장
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    val uid = currentUser.uid

                    // 데이터베이스 경로 생성
                    val databasePath = "hashtags/"

                    // 데이터 저장
                    val databaseReference =
                        FirebaseDatabase.getInstance().getReference(databasePath)
                    val hashtagData =
                        HashtagData(name, time, location, hashtags, System.currentTimeMillis(), SharedManager.getString(requireContext(), "uid", ""))
                    databaseReference.push().setValue(hashtagData)

                    // 다이얼로그 닫기
                    dialog.dismiss()
                }
            }
            .setNegativeButton("취소") { dialog, which ->
                dialog.dismiss()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun fetchHashtagDataForDate(recyclerView: RecyclerView, query: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val databasePath = "hashtags/"

            val databaseReference = FirebaseDatabase.getInstance().getReference(databasePath)

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try{
                        hashtagList.clear()
                        for (childSnapshot in snapshot.children) {
                            val hashtagData = childSnapshot.getValue(HashtagData::class.java)
                            hashtagData?.let {
                                hashtagData.key = childSnapshot.key.toString()
                                hashtagList.add(it)
                            }
                        }
                        if (query.isNotEmpty()) {
                            hashtagList = hashtagList.filter { it.hashtags.contains(query) }.toMutableList()
                            binding.recyclerView.adapter?.notifyDataSetChanged()
                        }
                        recyclerView.adapter = HashtagAdapter(hashtagList)
                    }
                    catch (e: Exception){
                        e.printStackTrace()
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    // 데이터 가져오기에 실패한 경우 처리
                }
            })
        }
    }
}