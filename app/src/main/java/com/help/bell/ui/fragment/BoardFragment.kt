package com.help.bell.ui.fragment

import BoardAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.help.bell.databinding.DialogAddBoardBinding
import com.help.bell.databinding.FragmentBoardBinding
import com.help.bell.model.BoardData
import com.help.bell.util.SharedManager

class BoardFragment : Fragment() {
    // 뷰 바인딩 변수 선언
    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!
    var boardList = mutableListOf<BoardData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 뷰 바인딩 초기화
        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
            reverseLayout = true
            stackFromEnd = true
        }

        binding.btnAdd.setOnClickListener { showAddBoardDialog() }
        fetchBoardDataForDate(binding.recyclerView)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 뷰 바인딩 해제
        _binding = null
    }

    private fun showAddBoardDialog() {
        val dialogView = DialogAddBoardBinding.inflate(LayoutInflater.from(requireContext()))


        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView.root)
            .setTitle("게시글 추가")
            .setPositiveButton("추가") { dialog, which ->
                val name = dialogView.etName.text.toString()
                val boards = dialogView.etEtc.text.toString()

                // Firebase Realtime Database에 데이터 저장
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    val uid = currentUser.uid

                    // 데이터베이스 경로 생성
                    val databasePath = "boards/"

                    // 데이터 저장
                    val databaseReference =
                        FirebaseDatabase.getInstance().getReference(databasePath)
                    val boardData =
                        BoardData(name, boards, SharedManager.getString(requireContext(), "name", ""),System.currentTimeMillis(), SharedManager.getString(requireContext(), "uid", ""))
                    databaseReference.push().setValue(boardData)

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

    private fun fetchBoardDataForDate(recyclerView: RecyclerView) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            val databasePath = "boards/"

            val databaseReference = FirebaseDatabase.getInstance().getReference(databasePath)

            databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try{
                        boardList.clear()
                        for (childSnapshot in snapshot.children) {
                            val boardData = childSnapshot.getValue(BoardData::class.java)
                            boardData?.let {
                                boardList.add(it)
                            }
                        }
                        recyclerView.adapter = BoardAdapter(boardList).apply { notifyDataSetChanged() }
                    } catch (e: Exception){
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