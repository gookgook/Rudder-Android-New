package com.rudder.src.host.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.databinding.FragmentPartySettingBinding
import com.rudder.src.host.viewmodel.PartySettingViewModel
import com.rudder.src.main.view.PartyDetailFragmentArgs

class PartySettingFragment: Fragment() {

    private  lateinit var binding: FragmentPartySettingBinding

    private val viewModel: PartySettingViewModel by viewModels()

    private val args: PartySettingFragmentArgs by navArgs()

    var partyThumbnailUrl: String = "" //여따 넣으셈
    var partyId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val hostParty = args.hostParty
        partyId = hostParty.partyId
        partyThumbnailUrl = hostParty.partyDetail.partyThumbnailUrl
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_party_setting, container, false)
        binding.main = this

        setBinding()
        setThumbnailImage()

        return binding.root
    }

    private fun setBinding() {
        viewModel.cancelPartyFlag.observe(viewLifecycleOwner, Observer { status ->
            status?.let{
                when (status) {
                    1 -> Toast.makeText(this.activity, "Party Canceled", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this.activity, "You can't cancel the party if there is one or more Participants", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this.activity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.stopRecruitFlag.observe(viewLifecycleOwner, Observer { status ->
            status?.let{
                when (status) {
                    1 -> Toast.makeText(this.activity, "Party Canceled", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this.activity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.fixMemberFlag.observe(viewLifecycleOwner, Observer { status ->
            status?.let{
                when (status) {
                    1 -> Toast.makeText(this.activity, "Party Canceled", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(this.activity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun cancelParty() {
        viewModel.cancelParty(partyId)
    }
    fun stopRecruit() {
        viewModel.stopRecruit(partyId)
    }
    fun fixMember(){
        viewModel.fixMembers(partyId)
    }

    private fun setThumbnailImage() {
        Glide.with(binding.partyThumbnailIV.context)
            .load(partyThumbnailUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.partyThumbnailIV)
    }

}