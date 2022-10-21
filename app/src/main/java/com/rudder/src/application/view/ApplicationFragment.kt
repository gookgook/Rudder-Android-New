package com.rudder.src.application.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.databinding.FragmentApplicationBinding
import com.rudder.model.dto.PartyDto
import com.rudder.src.application.viewmodel.ApplicationViewModel


class ApplicationFragment : Fragment() {

    private val approvedPartyListAdapter by lazy {
        val onApprovedPartyClickListener = { chatRoomId: Int ->
            val action =
                ApplicationFragmentDirections.actionFragmentApplicationToChatFragment(chatRoomId = chatRoomId)
            findNavController().navigate(action)
        }
        ApprovedPartyListAdapter(onApprovedPartyClickListener)
    }

    private val appliedPartyListAdapter by lazy {
        val onAppliedPartyClickListener = { appliedPartyItem: PartyDto.Companion.AppliedPartyItem->
            if (appliedPartyItem.partyOneToOneChatRoom.partyId.equals(-1)){
                val action =
                    ApplicationFragmentDirections.actionFragmentApplicationToPartyDetailFragment(partyId = appliedPartyItem.party.partyId)
                findNavController().navigate(action)
            }else{

            }

        }
        AppliedPartyListAdapter(onAppliedPartyClickListener)
    }
    private lateinit var binding: FragmentApplicationBinding
    private val viewModel: ApplicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.registerEvent()
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.unregisterEvent()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApplicationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.acceptedPreListRV.also {
            it.layoutManager = object : LinearLayoutManager(requireActivity()){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            it.setHasFixedSize(false)
            it.adapter = approvedPartyListAdapter
        }

        binding.appliedPreListRV.also {
            it.layoutManager = object : LinearLayoutManager(requireActivity()){
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            it.setHasFixedSize(false)
            it.adapter = appliedPartyListAdapter
        }

        viewModel.approvedPartyItems.observe(viewLifecycleOwner, Observer {
            it?.let {


                approvedPartyListAdapter.submitList(it.toList().map { it.second } )

            }
        })

        viewModel.appliedPartyItems.observe(viewLifecycleOwner, Observer {
            it?.let {


                appliedPartyListAdapter.submitList(it.toList().map { it.second } )

            }
        })


        return binding.root
    }

}