package com.example.collezioneeuro.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.collezioneeuro.R
import com.example.collezioneeuro.ui.fragment.HomeFragment

class MainActivity : AppCompatActivity(), ActivityInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragmentToHomeFragment()
    }

    override fun replaceFragmentToHomeFragment() {
        val fragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment, HomeFragment.TAG)
            .commit()
    }

}