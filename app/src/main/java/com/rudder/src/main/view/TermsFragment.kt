package com.rudder.src.main.view

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rudder.MainActivity
import com.rudder.R
import com.rudder.databinding.FragmentTermsBinding

class TermsFragment: Fragment() {

    private lateinit var binding: FragmentTermsBinding

    inner class MyWebViewClient: WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

            (activity as MainActivity).dialog.show()
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            (activity as MainActivity).dialog.hide()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_terms, container, false)
        binding.termsWV.apply {
            webViewClient = MyWebViewClient()
            settings.javaScriptEnabled = true
        }

        binding.termsWV.loadUrl("https://sites.google.com/view/rudderuseragreement")


        return binding.root
    }



    fun onlickBack(){
        Log.d("pressed","pressed")
    }

}