package com.example.collezioneeuro.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.collezioneeuro.databinding.FragmentHomeBinding
import com.example.collezioneeuro.ui.activity.ActivityInterface
import com.example.collezioneeuro.ui.activity.MainActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var activityParent: ActivityInterface

    companion object {
        const val TAG = "HomeFragment"
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityParent = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}