package com.example.myapplication.launch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentFeaturesBinding
import com.google.android.gms.ads.AdRequest

class FeaturesFragment : Fragment() {

    private var _binding: FragmentFeaturesBinding? = null
    private val binding get() = _binding!!

    private var listener: OnNextClickListener? = null

    interface OnNextClickListener {
        fun onFeaturesNextClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnNextClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeaturesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.btnNext.setOnClickListener {
            listener?.onFeaturesNextClicked()

        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
