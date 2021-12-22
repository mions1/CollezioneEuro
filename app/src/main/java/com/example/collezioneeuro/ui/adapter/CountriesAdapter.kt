package com.example.collezioneeuro.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.collezioneeuro.R
import com.example.collezioneeuro.databinding.HolderCountryCardBinding
import com.example.collezioneeuro.model.CECountry
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class CountriesAdapter(
    private val ceCountries: ArrayList<CECountry>,
    private val onClickListener: OnClickListener? = null
) :
    RecyclerView.Adapter<CountriesAdapter.CountriesViewHolder>(), Filterable {

    private val adapterCountries = ArrayList(ceCountries.sortedBy { it.country })
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
         * Imposta il testo dell'holder al nome dello stato, l'immagine della bandiera, e il conteggio
         * delle monete possedute.
         * Inizia la shimmer animation fino a che i dati non vengono recuperati.
         *
         * @param ceCountry il paese da impostare
         */
        fun bind(ceCountry: CECountry) {
            binding.tvCountry.text = ceCountry.country
            startshimmer()
            // imposta l'immagine della bandiera. Se c'è un url funzionante, imposta quella, altrimenti
            // se c'è un'immagine scaricata imposta quella, altrimenti imposta la default
            when {
                ceCountry.drawableUrl != null -> setDrawableUrlIntoImageView(ceCountry)
                ceCountry.drawableId != null -> setDrawableIdIntoImageView(ceCountry.drawableId)
                else -> setDrawableIdIntoImageView(R.drawable.flag_default)
            }
            binding.tvCount.text = "${ceCountry.ownedCount()}/${ceCountry.coins.size}"
        }

        /**
         * Inzia la shimmer:
         *  1. Visualizza il layout shimmered
         *  2. Nascondi il layout originale
         *  3. Inizia l'animazione
         */
        private fun startshimmer() {
            val shimmeredFrameLayout = binding.shimmerFrameLayout
            val noshimmeredLayout = binding.llCountryCard

            shimmeredFrameLayout.visibility = View.VISIBLE
            noshimmeredLayout.visibility = View.GONE
            shimmeredFrameLayout.startShimmerAnimation()
        }

        /**
         * Ferma l'animazione shimmer:
         *  1. Stoppa l'animazione
         *  2. Nascondi il layout shimmered
         *  3. Visualizza il layout originale
         */
        private fun stopshimmer() {
            val shimmeredFrameLayout = binding.shimmerFrameLayout
            val noshimmeredLayout = binding.llCountryCard

            shimmeredFrameLayout.stopShimmerAnimation()
            shimmeredFrameLayout.visibility = View.GONE
            noshimmeredLayout.visibility = View.VISIBLE
        }

        /**
         * Imposta l'immagine della bandiera da url.
         * Se non funziona, prova prima ad impostarla da id preso dalla country, altrimenti la default.
         * Quando la bandiera viene impostata, lo shimmer viene fermato.
         */
        private fun setDrawableUrlIntoImageView(ceCountry: CECountry) {
            Picasso.get().load(ceCountry.drawableUrl).into(
                binding.ivFlag,
                object : Callback {
                    override fun onSuccess() {
                        stopshimmer()
                    }

                    override fun onError(e: Exception?) {
                        when {
                            ceCountry.drawableId != null -> {
                                setDrawableIdIntoImageView(ceCountry.drawableId)
                                stopshimmer()
                            }
                            else -> {
                                setDrawableIdIntoImageView(R.drawable.flag_default)
                                stopshimmer()
                            }
                        }
                    }
                })
        }

        /**
         * Imposta l'immagine della bandiera dall'id del drawable passato
         */
        private fun setDrawableIdIntoImageView(id: Int) {
            binding.ivFlag.setImageDrawable(
                AppCompatResources.getDrawable(
                    binding.root.context,
                    id
                )
            )
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
            adapterCountries.addAll((results.values as ArrayList<CECountry>).sortedBy { it.country })
            notifyDataSetChanged()
        }
    }

}