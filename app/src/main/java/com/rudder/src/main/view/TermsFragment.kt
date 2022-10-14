package com.rudder.src.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rudder.R
import com.rudder.databinding.FragmentTermsBinding

class TermsFragment: Fragment() {

    private lateinit var binding: FragmentTermsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_terms, container, false)
        binding.termsWV.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }
        binding.termsWV.loadUrl("https://sites.google.com/view/rudderuseragreement")

        return binding.root
    }

}