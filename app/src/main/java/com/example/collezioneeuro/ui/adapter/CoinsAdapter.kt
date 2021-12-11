package com.example.collezioneeuro.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.collezioneeuro.R
import com.example.collezioneeuro.databinding.HolderCoinCardBinding
import com.example.collezioneeuro.model.CECoin

/**
 * Adapter per le monete da visualizzare del paese selezionato.
 */
class CoinsAdapter(
    ceCoins: ArrayList<CECoin>,
    private val onClickListener: OnClickListener? = null
) :
    RecyclerView.Adapter<CoinsAdapter.CoinsViewHolder>() {

    private val adapterCoins = ArrayList(ceCoins)

    interface OnClickListener {
        fun onClick(clicked: CECoin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsViewHolder =
        CoinsViewHolder(
            HolderCoinCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onClickListener
        )

    override fun onBindViewHolder(holder: CoinsViewHolder, position: Int) {
        holder.bind(adapterCoins[position])
    }

    override fun getItemCount(): Int = adapterCoins.size

    inner class CoinsViewHolder(
        private val binding: HolderCoinCardBinding,
        onClickListener: OnClickListener? = null
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            onClickListener?.let { onClickIt ->
                binding.holder.setOnClickListener {
                    onClickIt.onClick(
                        adapterCoins[adapterPosition]
                    )
                    notifyItemChanged(adapterPosition)
                }
            }
        }

        /**
         * Imposta l'holder con:
         *     1. il valore della monete
         *     2. l'immagine della moneta
         *     3. il colore che mostra lo stato di owned
         *
         * @param ceCoin la moneta da impostare
         */
        fun bind(ceCoin: CECoin) {
            binding.tvValue.text = "${ceCoin.value} €"
            binding.ivCoin.setImageDrawable(ceCoin.drawableId?.let {
                AppCompatResources.getDrawable(
                    binding.root.context,
                    it
                )
            })
            if (ceCoin.owned)
                binding.dOwned.background =
                    AppCompatResources.getDrawable(binding.root.context, R.color.coin_owned)
            else
                binding.dOwned.background =
                    AppCompatResources.getDrawable(binding.root.context, R.color.coin_no_owned)
        }

    }

}