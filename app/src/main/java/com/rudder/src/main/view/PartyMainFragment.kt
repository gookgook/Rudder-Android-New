package com.rudder.src.main.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rudder.MainActivity
import com.rudder.R
import com.rudder.config.App
import com.rudder.databinding.FragmentPartyMainBinding
import com.rudder.model.dto.PartyDto
import com.rudder.src.main.viewmodel.PartyMainViewModel
import com.rudder.util.StompManager

class PartyMainFragment : Fragment() {

    private val parentActivity by lazy {
        activity as MainActivity
    }
    private lateinit var binding: FragmentPartyMainBinding


    private val partyPreviewListAdapter by lazy {

        val onPartyPreviewClickListener = { partyId: Int ->
            val action =
                PartyMainFragmentDirections.actionFragmentPartyMainToPartyDetailFragment( partyId)
            findNavController().navigate(action)
        }

        PartyPreviewListAdapter(onPartyPreviewClickListener = onPartyPreviewClickListener)
    }

    private lateinit var mContext: Context

    private val partyMainViewModel: PartyMainViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userInfoId = App.prefs.getValue("userInfoId")
        if (!userInfoId.isNullOrEmpty()){
            StompManager.connectSocket(userInfoId!!.toInt())
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = FragmentPartyMainBinding.inflate(inflater, container, false)
        binding.partyMainFragment = this
        binding.partyPreviewListRV.also {
            it.layoutManager = LinearLayoutManager(parentActivity)
            it.setHasFixedSize(false)
            it.adapter = partyPreviewListAdapter
            it.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if(!it.canScrollVertically(1) ){
                        Log.d("PartyMain Refresh","refresh")
                        partyMainViewModel.getParties(isMore = true)
                    }
                }
            })
        }

        partyMainViewModel.newNoticationFlag.observe(viewLifecycleOwner, Observer { status ->


            if (status) {

                Glide.with(this.activity)
                    .load(R.drawable.bell_with_point)
                    .fitCenter()
                    .into(binding.notificationIV)
            } else {
                Glide.with(this.activity)
                    .load(R.drawable.bell)
                    .fitCenter()
                    .into(binding.notificationIV)
            }
        })

        partyMainViewModel.isLoadingFlag.observe(viewLifecycleOwner, Observer { status ->
            if (status) (activity as MainActivity).dialog.show()
            else {(activity as MainActivity).dialog.hide() }
        })

        partyMainViewModel.partyPreviewList.observe(viewLifecycleOwner, Observer {
            partyPreviewListAdapter.submitList(it.toList())
        })

        binding.partyPreviewListSRL.setOnRefreshListener {
            partyMainViewModel.refreshParties()
            binding.partyPreviewListSRL.isRefreshing = false
        }


        partyMainViewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(mContext,it,Toast.LENGTH_SHORT).show()
            }
        })


        return binding.root
    }

    fun goNotificationFragment() {
        partyMainViewModel.newNoticationFlag.value = false
        findNavController().navigate(R.id.action_fragment_party_main_to_notificationFragment)
    }

    fun goCreatePartyFragment() {
        findNavController().navigate(R.id.action_fragment_party_main_to_createPartyFragment)
    }

    fun goMyProfileFragment(){
        findNavController().navigate(R.id.action_fragment_party_main_to_myProfileFragment)
    }

}