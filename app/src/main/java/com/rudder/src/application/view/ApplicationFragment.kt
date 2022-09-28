package com.rudder.src.application.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rudder.databinding.FragmentApplicationBinding
import com.rudder.src.application.viewmodel.ApplicationViewModel
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.streams.toList


class ApplicationFragment : Fragment() {

    private val approvedPartyListAdapter by lazy {
        ApprovedPartyListAdapter()
    }
    private lateinit var binding: FragmentApplicationBinding
    private val viewModel: ApplicationViewModel by viewModels()
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

        viewModel.approvedPartyItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("start", "start$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
                it?.forEach { m->
                    run {
                        Log.d("map", m.toString())
                    }
                }

                Log.d("end", "end$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")

                approvedPartyListAdapter.submitList(it.toList().map { it.second } )

            }
        })


        return binding.root
    }

}