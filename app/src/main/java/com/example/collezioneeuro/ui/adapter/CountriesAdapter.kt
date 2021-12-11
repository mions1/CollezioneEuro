package com.example.collezioneeuro.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.collezioneeuro.databinding.HolderCountryCardBinding
import com.example.collezioneeuro.model.CECountry
import java.util.*
import kotlin.collections.ArrayList

class CountriesAdapter(
    private val ceCountries: ArrayList<CECountry>,
    private val onClickListener: OnClickListener? = null
) :
    RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>(), Filterable {

    private val adapterCountries = ArrayList(ceCountries)
    private val mFilter = ItemFilter()

    interface OnClickListener {
        fun onClick(clicked: CECountry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder =
        CountriesViewHolder(
            HolderCountryCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onClickListener
        )

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        holder.bind(adapterCountries[position])
    }

    override fun getItemCount(): Int = adapterCountries.size


    override fun getFilter(): Filter {
        return mFilter
    }

    inner class CountriesViewHolder(
        private val binding: HolderCountryCardBinding,
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
            binding.ivFlag.setImageDrawable(ceCountry.drawableId?.let {
                AppCompatResources.getDrawable(
                    binding.root.context,
                    it
                )
            })
            binding.tvCount.text = "${ceCountry.ownedCount()}/${ceCountry.coins.size}"
        }

    }

    /**
     * Quando viene cercato qualcosa, questa effettua un filtro dei dati mostrati per i nomi dei paesi
     */
    inner class ItemFilter : Filter() {

        /**
         * Controlla se il testo cercato è contenuto in quale nome di paese
         * cosi che mostro solo quelli
         */
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val textToSearch = constraint.toString().lowercase(Locale.getDefault())
            val results = FilterResults()
            val originalList = ArrayList(ceCountries)
            val filteredList = ArrayList<CECountry>(originalList.size)
            var countryNameToCheck: String

            for (country in originalList) {
                countryNameToCheck = country.country
                if (countryNameToCheck.lowercase(Locale.getDefault()).contains(textToSearch)) {
                    filteredList.add(country)
                }
            }
            results.values = filteredList
            results.count = filteredList.size
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            adapterCountries.clear()
            adapterCountries.addAll(results.values as ArrayList<CECountry>)
            notifyDataSetChanged()
        }
    }

}