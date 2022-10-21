package com.rudder.src.host.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.MainActivity
import com.rudder.R
import com.rudder.databinding.FragmentHostBinding
import com.rudder.model.dto.ApplicantProfileRequest
import com.rudder.model.dto.HostParty
import com.rudder.model.dto.PartyDto
import com.rudder.src.host.viewmodel.HostViewModel
import java.sql.Timestamp


class HostFragment : Fragment() {

    private val viewModel: HostViewModel by viewModels()
    private lateinit var mContext: Context
    private lateinit var binding: FragmentHostBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
     private lateinit var partyDates : List<PartyDto.Companion.PartyOnlyDate>
     private lateinit var partyDatesSpinnerAdapter : ArrayAdapter<PartyDto.Companion.PartyOnlyDate>


    fun onSettingClickListener (hostParty: HostParty){
        val action =
            HostFragmentDirections.actionHostFragmentToPartySettingFragment(hostParty)
        findNavController().navigate(action)
    }

    fun onPartyGroupChatRoomClickListener(hostParty: HostParty){
        val action =
            HostFragmentDirections.actionHostFragmentToChatFragment(chatRoomId = hostParty.partyGroupChatRoom.chatRoomId)
        findNavController().navigate(action)
    }

    private val partyApplicantListAdapter by lazy {
        val onPartyApplicantClickListener = { partyMemberId:Int, userInfoId: Int, isChatExist: Boolean ->

            kotlin.run {
                val applicantProfileRequest = ApplicantProfileRequest(
                    partyId = viewModel.selectedHostParty.value?.partyId ?: return@run,
                    partyMemberId = partyMemberId,
                    userInfoId = userInfoId,
                    isOneToOneChatExist = isChatExist
                )
                val action =
                    HostFragmentDirections.actionHostFragmentToApplicantProfileFragment(applicantProfileRequest=applicantProfileRequest)
                findNavController().navigate(action)
            }

        }

        PartyApplicantListAdapter(onPartyApplicantClickListener)
    }

    private val partyHostOneToOneChatRoomListAdapter by lazy {
        PartyHostOneToOneChatRoomListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        partyDates = arrayListOf(PartyDto.Companion.PartyOnlyDate(-1, DEFAULT_TIMESTAMP))
        partyDatesSpinnerAdapter = ArrayAdapter(mContext, R.layout.custom_spinner_layout, partyDates)
        viewModel.registerEvent()
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.unregisterEvent()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHostBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.hostFragment = this



        binding.partyDatesS.adapter = partyDatesSpinnerAdapter

        viewModel.hostParties.observe(viewLifecycleOwner, Observer {
            it?.let {
                partyDatesSpinnerAdapter.clear()
                partyDatesSpinnerAdapter.addAll(it)
                if (!partyDatesSpinnerAdapter.isEmpty){
                    viewModel.setSelectedParty(partyDatesSpinnerAdapter.getItem(0)?.partyId?:return@let)

                }
            }
        })

        viewModel.isLoadingFlag.observe(viewLifecycleOwner, Observer { status ->
            if (status) (activity as MainActivity).dialog.show()
            else {(activity as MainActivity).dialog.hide() }
        })

        viewModel.selectedHostParty.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.hostParty=it
                Glide.with(binding.hostPartyImageIV.context)
                    .load(it.partyDetail.partyThumbnailUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.hostPartyImageIV)
                partyApplicantListAdapter.submitList(it.partyApplicants.toList())
                partyHostOneToOneChatRoomListAdapter.submitList(it.partyOneToOneChatRooms.toList())
            }
        })

        binding.partyApplicantsRV.also {
            it.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
            it.setHasFixedSize(false)
            it.adapter = partyApplicantListAdapter
        }


        binding.partyOneToOneChatRoomRV.also {
            it.layoutManager = LinearLayoutManager(requireActivity())
            it.setHasFixedSize(false)
            it.adapter = partyHostOneToOneChatRoomListAdapter
        }

        return binding.root

    }

    fun onSpinnerSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val selectedParty = parent.selectedItem as PartyDto.Companion.PartyOnlyDate


        if(selectedParty.partyId.equals(-1)||selectedParty.partyId.equals(viewModel.selectedHostParty.value?.partyId)) return
        viewModel.setSelectedParty(selectedParty.partyId)
    }

    fun goSetting() {
        view?.let { Navigation.findNavController(it).navigate(R.id.action_hostFragment_to_partySettingFragment) }
    }

    companion object{
        val DEFAULT_TIMESTAMP = Timestamp.valueOf("0001-01-01 00:00:00")
    }

}