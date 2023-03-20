package com.example.projetfinaldevmobile_lucasguenotmerlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.fragment.app.Fragment
import com.example.projetfinaldevmobile_lucasguenotmerlin.fragment.AccountFragment
import com.example.projetfinaldevmobile_lucasguenotmerlin.fragment.FavoriteFragment
import com.example.projetfinaldevmobile_lucasguenotmerlin.fragment.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_home_page.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_home_page)

        val pseudo = intent.getStringExtra("pseudo")
        val userKey = intent.getStringExtra("userKey")

        val homeFragment = HomeFragment()
        val favoriteFragment = FavoriteFragment()
        val accountFragment = AccountFragment()

        if (pseudo != null) {
            homeFragment.setPseudo(pseudo)
            favoriteFragment.setPseudo(pseudo)
            accountFragment.setPseudo(pseudo)
        }
        if (userKey != null) {
            accountFragment.setUserKey(userKey)
        }

        makeCurrentFragment(homeFragment)

        bottom_nav_bar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> makeCurrentFragment(homeFragment)
                R.id.nav_favorites -> makeCurrentFragment(favoriteFragment)
                R.id.nav_account -> makeCurrentFragment(accountFragment)
            }
            true
        }
    }

    private fun makeCurrentFragment(Fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, Fragment)
            commit()
        }
    }
}