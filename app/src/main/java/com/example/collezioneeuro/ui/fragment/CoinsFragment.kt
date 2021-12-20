package com.example.collezioneeuro.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collezioneeuro.contract.CEContract
import com.example.collezioneeuro.databinding.FragmentCoinsBinding
import com.example.collezioneeuro.model.CECoin
import com.example.collezioneeuro.model.CECountry
import com.example.collezioneeuro.model.repository.CEFakeRepository
import com.example.collezioneeuro.model.repository.CERepositoryInterface
import com.example.collezioneeuro.presenter.CEPresenter
import com.example.collezioneeuro.presenter.RuntimeDispatcherProvider
import com.example.collezioneeuro.ui.activity.ActivityInterface
import com.example.collezioneeuro.ui.activity.MainActivity
import com.example.collezioneeuro.ui.adapter.CoinsAdapter
import com.example.collezioneeuro.ui.animation.CEAnimation

/**
 * CoinsFragment mostra la lista di monete del paese selezionato.
 * Al click sulla moneta, consente di cambiarne lo stato di "owned".
 */
class CoinsFragment : Fragment(), CEContract.View {

    private lateinit var animator: CEAnimation
    private var shortAnimationDuration: Int = 0
    private var isZoomed: Boolean = false

    private lateinit var binding: FragmentCoinsBinding
    private lateinit var activityParent: ActivityInterface

    private lateinit var repository: CERepositoryInterface
    private lateinit var presenter: CEContract.Presenter

    private var ceCountry: CECountry? = null

    companion object {
        const val TAG = "CoinsFragment"

        private const val ARG_COUNTRY = "ARG_COUNTRY"

        fun newInstance(ceCountry: CECountry): CoinsFragment {
            val fragment = CoinsFragment()
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
        ceCountry = arguments?.getParcelable(ARG_COUNTRY)

        repository = CEFakeRepository.SingleInstance
        presenter = CEPresenter(RuntimeDispatcherProvider(), repository)
        presenter.bindView(this)

        setAnimator()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoinsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun setAnimator() {
        animator = CEAnimation(object : CEAnimation.AnimationListener {
            override fun onAnimationStart() {
                isZoomed = true
            }

            override fun onAnimationStop() {
                isZoomed = false
            }

            override fun onAnimationCancel() {
                isZoomed = false
            }

        })
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    private fun initRecyclerView() {
        // imposta le monete su 3 colonne
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 3)
        binding.rvCoins.layoutManager = mLayoutManager
        setRecyclerViewAdapter()
    }

    private fun setRecyclerViewAdapter() {
        binding.rvCoins.adapter =
            ceCountry?.coins?.let {
                CoinsAdapter(it, object : CoinsAdapter.OnClickListener {
                    override fun onClick(clickedView: View, clickedCoin: CECoin) {
                        // imposta la moneta come owned se non è in corso l'ingrandimento
                        if (!isZoomed)
                            setOwned(clickedCoin)
                        else
                            animator.finishZoomImage(
                                clickedView,
                                binding.expandedImage,
                                shortAnimationDuration.toLong()
                            )
                    }

                    // Ingrandisci la moneta tenuta premuta avviando un'animazione
                    override fun onLongClick(clickedView: View, clickedCoin: CECoin) {
                        clickedCoin.drawableUrl?.let { it1 ->
                            animator.startZoomImage(
                                clickedView,
                                binding.expandedImage,
                                it1,
                                shortAnimationDuration.toLong()
                            )
                        }
                    }
                })
            }
    }

    /**
     * Cambia lo stato di owned della moneta chiamando il repository
     */
    private fun setOwned(coin: CECoin) {
        val owned = !coin.owned
        coin.owned = owned
        ceCountry?.let { presenter.setOwned(it, coin, owned) }
    }

    override fun onGetCountries(countries: ArrayList<CECountry>) {}

}