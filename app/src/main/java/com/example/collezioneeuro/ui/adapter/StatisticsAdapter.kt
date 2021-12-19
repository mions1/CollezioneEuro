package com.example.collezioneeuro.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collezioneeuro.databinding.HolderStatisticsCardBinding
import com.example.collezioneeuro.ui.fragment.Statistics

/**
 * Adapter per le statistiche da visualizzare.
 */
class StatisticsAdapter(statistics: ArrayList<Statistics>) :

    RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>() {

    private val adapterStatistics = ArrayList(statistics)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder =
        StatisticsViewHolder(
            HolderStatisticsCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.bind(adapterStatistics[position])
    }

    override fun getItemCount(): Int = adapterStatistics.size

    inner class StatisticsViewHolder(
        private val binding: HolderStatisticsCardBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(statistics: Statistics) {
            binding.tvTitle.text = statistics.title
            binding.tvText.text = statistics.text
        }

    }

}