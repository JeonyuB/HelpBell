package com.help.bell.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.help.bell.databinding.DialogEditInfoBinding
import com.help.bell.databinding.FragmentProfileBinding
import com.help.bell.model.User
import com.help.bell.util.SharedManager

class ProfileFragment : Fragment() {
    // 뷰 바인딩 변수 선언
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val uid = FirebaseAuth.getInstance().currentUser!!.uid
    val userInfoRef = FirebaseDatabase.getInstance().reference.child("users").child(uid)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 뷰 바인딩 초기화
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnLogout.setOnClickListener { logout() }

        // 현재 사용자 정보 가져오기
        userInfoRef.addValueEventListener(object : ValueEventListener {
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

        binding.btnEdit.setOnClickListener { openEditUserInfoDialog() }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 뷰 바인딩 해제
        _binding = null
    }

    private fun openEditUserInfoDialog() {
        val dialogView = DialogEditInfoBinding.inflate(LayoutInflater.from(requireContext()))


        // 현재 사용자 정보 가져오기
        userInfoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        dialogView.etName.setText(user.name)
                        dialogView.etOp1.setText(user.op1)
                        dialogView.etOp2.setText(user.op2)
                        dialogView.etOp3.setText(user.op3)
                        dialogView.etOp4.setText(user.op4)
                        dialogView.etOp5.setText(user.op5)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView.root)
            .setTitle("사용자 정보 편집")
            .setPositiveButton("저장") { dialog, _ ->
                // 변경된 정보 저장
                val newName = dialogView.etName.text.toString()
                val newOp1 = dialogView.etOp1.text.toString()
                val newOp2 = dialogView.etOp2.text.toString()
                val newOp3 = dialogView.etOp3.text.toString()
                val newOp4 = dialogView.etOp4.text.toString()
                val newOp5 = dialogView.etOp5.text.toString()

                SharedManager.putString(requireContext(), "name", newName)

                // Firebase Realtime Database에 업데이트
                userInfoRef.child("name").setValue(newName)
                userInfoRef.child("op1").setValue(newOp1)
                userInfoRef.child("op2").setValue(newOp2)
                userInfoRef.child("op3").setValue(newOp3)
                userInfoRef.child("op4").setValue(newOp4)
                userInfoRef.child("op5").setValue(newOp5)

                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun logout() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("로그아웃 확인")
        builder.setMessage("로그아웃하시겠습니까?")
        builder.setPositiveButton("로그아웃") { _, _ ->
            FirebaseAuth.getInstance().signOut()
            requireActivity().finish()
        }
        builder.setNegativeButton("취소") { _, _ -> }
        builder.show()
    }
}