package com.rudder.src.host.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.rudder.R
import com.rudder.databinding.FragmentCreatePartyBinding
import com.rudder.databinding.FragmentPartyEnquiryBinding
import com.rudder.src.host.viewmodel.PartyEnquiryViewModel

class PartyEnquiryFragment: Fragment() {
    private lateinit var binding: FragmentPartyEnquiryBinding

    private val viewModel: PartyEnquiryViewModel by viewModels()

    private var partyId = -1
    private var enquiryType = ""

    private val args: PartyEnquiryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        partyId = args.partyId
        enquiryType = args.enquiryType
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPartyEnquiryBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.main = this

        if (enquiryType == "enquiry") {
            binding.titleTV.text = "Enquiry"
            binding.EnquiryBodyET.hint = "Tell us if you have any questions about hosting a pre"
        }

        return binding.root
    }

    fun setBinding() {
        viewModel.enquiryResultFlag.observe(viewLifecycleOwner, Observer { status ->
            status?.let{
                when (status) {
                    1 -> Toast.makeText(this.activity, "Thanks for the Feedback. We will take it from here", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this.activity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun sendEnquiry() {
        Log.d("sendEnquirty","touched")
        viewModel.sendEnquiry(partyId,enquiryType)
    }
}