package com.example.myapplication.launch

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentPrivacyTermsBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.AdError

class PrivacyTermsFragment : Fragment() {

    private var _binding: FragmentPrivacyTermsBinding? = null
    private val binding get() = _binding!!

    private var listener: OnFinishClickListener? = null

    interface OnFinishClickListener {
        fun onFinishClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnFinishClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacyTermsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.cbAgree.setOnCheckedChangeListener { _, isChecked ->
            binding.btnFinish.isEnabled = isChecked
        }

        binding.btnFinish.setOnClickListener {
            val ad = WelcomeFragment.interstitialAd
            if (ad != null) {
                ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        WelcomeFragment.interstitialAd = null
                        listener?.onFinishClicked()
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        WelcomeFragment.interstitialAd = null
                        listener?.onFinishClicked()
                    }
                }
                ad.show(requireActivity())
            } else {

                listener?.onFinishClicked()
            }
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
