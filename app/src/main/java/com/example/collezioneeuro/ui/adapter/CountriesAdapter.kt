package com.example.collezioneeuro.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collezioneeuro.databinding.HolderCountryBinding
import com.example.collezioneeuro.model.CECountry

class CountriesAdapter(
    ceCountries: ArrayList<CECountry>,
    private val onClickListener: OnClickListener? = null
) :
    RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>() {

    private val adapterCountries = ArrayList(ceCountries)

    interface OnClickListener {
        fun onClick(clicked: CECountry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder =
        CountriesViewHolder(
            HolderCountryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onClickListener
        )

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(adapterCountries[position])
    }

    override fun getItemCount(): Int = adapterCountries.size

    inner class CountriesViewHolder(
        private val binding: HolderCountryBinding,
        onClickListener: OnClickListener? = null
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
                onClickListener?.let { onClickIt ->
                    binding.holder.setOnClickListener {
                        onClickIt.onClick(
                            adapterCountries[adapterPosition]
                        )
                    }
                }
        }

        /**
         * Imposta il testo dell'holder al nome dello stato
         *
         * @param ceCountry il paese da impostare
         */
        fun bind(ceCountry: CECountry) {
            binding.tvCountry.text = ceCountry.country
        }

    }

}