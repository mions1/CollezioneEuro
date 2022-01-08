package com.example.collezioneeuro.ui.activity

import com.example.collezioneeuro.model.CECountry

interface ActivityInterface {

    fun setWhichItemIsSelectedBottomNavigationMenu(item: MainActivity.BottomNavigationItem)

    fun replaceFragmentToHomeFragment()
    fun replaceFragmentToCoinsFragment(ceCountry: CECountry)

}