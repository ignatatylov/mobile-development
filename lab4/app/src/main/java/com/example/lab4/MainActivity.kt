package com.example.lab4

import ClickerViewModel
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {
    private val viewModel: ClickerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, ClickerFragment())
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_clicker -> {
                    supportFragmentManager.commit {
                        replace(R.id.fragment_container, ClickerFragment())
                    }
                    true
                }
                R.id.nav_shop -> {
                    supportFragmentManager.commit {
                        replace(R.id.fragment_container, ShopFragment())
                    }
                    true
                }
                else -> false
            }
        }
    }
}

