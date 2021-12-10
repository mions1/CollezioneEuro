package com.example.collezioneeuro.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.collezioneeuro.R
import com.example.collezioneeuro.contract.CEContract
import com.example.collezioneeuro.databinding.FragmentCountryBinding
import com.example.collezioneeuro.databinding.IncludeCoinBinding
import com.example.collezioneeuro.model.CECoin
import com.example.collezioneeuro.model.CECountry
import com.example.collezioneeuro.model.repository.CEFakeRepository
import com.example.collezioneeuro.model.repository.CERepositoryInterface
import com.example.collezioneeuro.presenter.CEPresenter
import com.example.collezioneeuro.presenter.RuntimeDispatcherProvider
import com.example.collezioneeuro.ui.activity.ActivityInterface
import com.example.collezioneeuro.ui.activity.MainActivity

class CountryFragment : Fragment(), CEContract.View {

    private lateinit var binding: FragmentCountryBinding
    private lateinit var activityParent: ActivityInterface

    private lateinit var repository: CERepositoryInterface
    private lateinit var presenter: CEContract.Presenter

    private lateinit var ceCountry: CECountry

    companion object {
        const val TAG = "CountryFragment"

        const val ARG_COUNTRY = "ARG_COUNTRY"

        fun newInstance(ceCountry: CECountry): CountryFragment {
            val fragment = CountryFragment()
            val args = Bundle()
            args.putParcelable(ARG_COUNTRY, ceCountry)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityParent = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<CECountry>(ARG_COUNTRY)?.let { ceCountry = it }

        repository = CEFakeRepository.SingleInstance
        presenter = CEPresenter(RuntimeDispatcherProvider(), repository)
        presenter.bindView(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCoins()
    }

    /**
     * Imposta una singola moneta nel layout
     *
     * vedi setCoins
     */
    private fun setCoin(coinBinding: IncludeCoinBinding, coin: CECoin) {
        coinBinding.tvValue.text =
            coin.value.toString()
        coinBinding.ivCoin.setImageDrawable(
            coin.drawableId?.let {
                ResourcesCompat.getDrawable(
                    resources,
                    it,
                    context?.theme
                )
            })
        coinBinding.coin.visibility = ConstraintLayout.VISIBLE
        setCoinOwnedColor(coinBinding, coin)
        coinBinding.coin.setOnClickListener {
            setOwned(coinBinding, coin)
        }
    }

    private fun setCoinOwnedColor(coinBinding: IncludeCoinBinding, coin: CECoin) {
        if (coin.owned) {
            coinBinding.dOwned.background =
                ResourcesCompat.getDrawable(resources, R.color.coin_owned, context?.theme)
        } else {
            coinBinding.dOwned.background =
                ResourcesCompat.getDrawable(resources, R.color.coin_no_owned, context?.theme)
        }
    }

    /**
     * Imposta ogni moneta di quella nazionalità nel layout.
     * 3 file da 4 monete l'una
     */
    private fun setCoins() {
        var coinToInsert = 0
        for (i in 1..3) {
            if (coinToInsert > ceCountry.coins.size - 1)
                break
            when (i) {
                1 ->
                    for (j in 1..4) {
                        if (coinToInsert > ceCountry.coins.size - 1)
                            break
                        when (j) {
                            1 -> setCoin(binding.coin1, ceCountry.coins[coinToInsert])
                            2 -> setCoin(binding.coin2, ceCountry.coins[coinToInsert])
                            3 -> setCoin(binding.coin3, ceCountry.coins[coinToInsert])
                            4 -> setCoin(binding.coin4, ceCountry.coins[coinToInsert])
                        }
                        coinToInsert++
                    }
                2 ->
                    for (j in 1..4) {
                        if (coinToInsert > ceCountry.coins.size - 1)
                            break
                        when (j) {
                            1 -> setCoin(binding.coin5, ceCountry.coins[coinToInsert])
                            2 -> setCoin(binding.coin6, ceCountry.coins[coinToInsert])
                            3 -> setCoin(binding.coin7, ceCountry.coins[coinToInsert])
                            4 -> setCoin(binding.coin8, ceCountry.coins[coinToInsert])
                        }
                        coinToInsert++
                    }
                3 ->
                    for (j in 1..4) {
                        if (coinToInsert > ceCountry.coins.size - 1)
                            break
                        when (j) {
                            1 -> setCoin(binding.coin9, ceCountry.coins[coinToInsert])
                            2 -> setCoin(binding.coin10, ceCountry.coins[coinToInsert])
                            3 -> setCoin(binding.coin11, ceCountry.coins[coinToInsert])
                            4 -> setCoin(binding.coin12, ceCountry.coins[coinToInsert])
                        }
                        coinToInsert++
                    }
            }
        }
    }

    private fun setOwned(coinBinding: IncludeCoinBinding, coin: CECoin) {
        val owned = !coin.owned
        coin.owned = owned
        setCoinOwnedColor(coinBinding, coin)
        presenter.setOwned(ceCountry, coin, owned)
    }

    override fun onGetCountries(countries: ArrayList<CECountry>) {}

}