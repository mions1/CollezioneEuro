package com.example.collezioneeuro.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collezioneeuro.databinding.HolderCountryBinding
import com.example.collezioneeuro.model.CECountry

class CountriesAdapter(ceCountries: ArrayList<CECountry>) :
    RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {

    private val adapterCountries = ArrayList(ceCountries)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder =
        CountriesViewHolder(
            HolderCountryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(adapterCountries[position])
    }

    override fun getItemCount(): Int = adapterCountries.size

    inner class CountriesViewHolder(private val binding: HolderCountryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ceCountry: CECountry) {
            binding.tvCountry.text = ceCountry.country
        }

    }

}