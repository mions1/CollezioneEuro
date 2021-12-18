package com.example.collezioneeuro.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.collezioneeuro.R
import com.example.collezioneeuro.databinding.HolderCoinCardBinding
import com.example.collezioneeuro.model.CECoin
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

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
            when {
                ceCoin.drawableUrl != null -> setDrawableUrlIntoImageView(ceCoin)
                ceCoin.drawableId != null -> setDrawableIdIntoImageView(ceCoin.drawableId!!)
                else -> setDrawableIdIntoImageView(R.drawable.coin_default)
            }
            if (ceCoin.owned)
                binding.dOwned.background =
                    AppCompatResources.getDrawable(binding.root.context, R.color.coin_owned)
            else
                binding.dOwned.background =
                    AppCompatResources.getDrawable(binding.root.context, R.color.coin_no_owned)
        }

        /**
         * Imposta l'immagine della moneta da url.
         * Se non funziona, prova prima ad impostarla da id preso dalla country, altrimenti la default
         */
        private fun setDrawableUrlIntoImageView(ceCoin: CECoin) {
            Picasso.get().load(ceCoin.drawableUrl).into(binding.ivCoin,
                object : Callback {
                    override fun onSuccess() {}
                    override fun onError(e: Exception?) {
                        when {
                            ceCoin.drawableId != null -> setDrawableIdIntoImageView(ceCoin.drawableId!!)
                            else -> setDrawableIdIntoImageView(R.drawable.coin_default)
                        }
                    }
                })
        }

        /**
         * Imposta l'immagine della moneta dall'id del drawable passato
         */
        private fun setDrawableIdIntoImageView(id: Int) {
            binding.ivCoin.setImageDrawable(
                AppCompatResources.getDrawable(
                    binding.root.context,
                    id
                )
            )
        }

    }

}