package com.example.collezioneeuro.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.collezioneeuro.R
import com.example.collezioneeuro.model.CECountry
import com.example.collezioneeuro.ui.fragment.CountryFragment
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

    override fun replaceFragmentToCountryFragment(ceCountry: CECountry) {
        val fragment = CountryFragment.newInstance(ceCountry)
        supportFragmentManager.beginTransaction()
            .addToBackStack(CountryFragment.TAG)
            .replace(R.id.fragment, fragment, CountryFragment.TAG)
            .commit()
    }

}