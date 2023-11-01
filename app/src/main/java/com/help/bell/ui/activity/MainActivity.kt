package com.help.bell.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationBarView
import com.help.bell.R
import com.help.bell.databinding.ActivityMainBinding
import com.help.bell.ui.fragment.BoardFragment
import com.help.bell.ui.fragment.HomeFragment
import com.help.bell.ui.fragment.ProfileFragment

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener, NavigationBarView.OnItemReselectedListener,
    View.OnClickListener {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var currentUID = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setInitialize()

    }


    private fun setInitialize() {
        supportFragmentManager.beginTransaction().add(R.id.frame_main, HomeFragment()).commit()

        binding.bottomNavigation.apply {
            setOnItemSelectedListener (this@MainActivity)
            setOnItemReselectedListener(this@MainActivity)
        }

        binding.btnNoti.setOnClickListener {
            startActivity(Intent(this@MainActivity, NotiActivity::class.java))
        }

    }

    override fun onClick(view: View?) {
        when (view?.id) {
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_home -> supportFragmentManager.beginTransaction().replace(R.id.frame_main, HomeFragment()).commit()
            R.id.menu_board ->  supportFragmentManager.beginTransaction().replace(R.id.frame_main, BoardFragment()).commit()
            R.id.menu_profile -> supportFragmentManager.beginTransaction().replace(R.id.frame_main, ProfileFragment()).commit()
        }
        return true
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        // 중복클릭 방지
        when (item.itemId) {
            R.id.menu_home -> supportFragmentManager.beginTransaction().replace(R.id.frame_main, HomeFragment()).commit()
            R.id.menu_board ->  supportFragmentManager.beginTransaction().replace(R.id.frame_main, BoardFragment()).commit()
            R.id.menu_profile -> supportFragmentManager.beginTransaction().replace(R.id.frame_main, ProfileFragment()).commit()
        }
    }
}
