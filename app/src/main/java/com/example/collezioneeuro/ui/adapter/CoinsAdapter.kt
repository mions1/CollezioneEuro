package com.example.collezioneeuro.ui.adapter

import android.view.LayoutInflater
import android.view.View
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

    private val adapterCoins = ArrayList(ceCoins.sortedBy { it.value })

    interface OnClickListener {
        fun onClick(clickedView: View, clickedCoin: CECoin)
        fun onLongClick(clickedView: View, clickedCoin: CECoin)
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
                        binding.holder,
                        adapterCoins[adapterPosition]
                    )
                    notifyItemChanged(adapterPosition)
                }
                binding.holder.setOnLongClickListener {
                    onClickIt.onLongClick(binding.holder, adapterCoins[adapterPosition])
                    false
                }
            }
        }

        /**
         * Imposta l'holder con:
         *     1. il valore della monete
         *     2. l'immagine della moneta
         *     3. il colore che mostra lo stato di owned
         *
         * Inoltre, inizia la shimmer animation fino a che non vengono recuperati i dati
         * @param ceCoin la moneta da impostare
         */
        fun bind(ceCoin: CECoin) {
            binding.tvValue.text = "${ceCoin.value} €"
            startShimmer()
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
         * Inzia la shimmer:
         *  1. Visualizza il layout shimmered
         *  2. Nascondi il layout originale
         *  3. Inizia l'animazione
         */
        private fun startShimmer() {
            val shimmeredFrameLayout = binding.shimmerFrameLayout
            val noShimmeredLayout = binding.llCoinCard

            shimmeredFrameLayout.visibility = View.VISIBLE
            noShimmeredLayout.visibility = View.GONE
            shimmeredFrameLayout.startShimmerAnimation()
        }

        /**
         * Ferma l'animazione shimmer:
         *  1. Stoppa l'animazione
         *  2. Nascondi il layout shimmered
         *  3. Visualizza il layout originale
         */
        private fun stopShimmer() {
            val shimmeredFrameLayout = binding.shimmerFrameLayout
            val noshimmeredLayout = binding.llCoinCard

            shimmeredFrameLayout.stopShimmerAnimation()
            shimmeredFrameLayout.visibility = View.GONE
            noshimmeredLayout.visibility = View.VISIBLE
        }

        /**
         * Imposta l'immagine della moneta da url.
         * Se non funziona, prova prima ad impostarla da id preso dalla country, altrimenti la default.
         * Quando la moneta è stata impostata, lo shimmer viene fermato.
         */
        private fun setDrawableUrlIntoImageView(ceCoin: CECoin) {
            Picasso.get().load(ceCoin.drawableUrl).into(
                binding.ivCoin,
                object : Callback {
                    override fun onSuccess() {
                        stopShimmer()
                    }

                    override fun onError(e: Exception?) {
                        when {
                            ceCoin.drawableId != null -> {
                                setDrawableIdIntoImageView(ceCoin.drawableId!!)
                                stopShimmer()
                            }
                            else -> {
                                setDrawableIdIntoImageView(R.drawable.coin_default)
                                stopShimmer()
                            }
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