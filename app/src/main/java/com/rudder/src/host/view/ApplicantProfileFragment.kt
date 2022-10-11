package com.rudder.src.host.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.databinding.FragmentApplicantProfileBinding
import com.rudder.databinding.FragmentHostBinding
import com.rudder.src.host.viewmodel.ApplicantProfileViewModel
import com.rudder.src.host.viewmodel.HostViewModel
import com.rudder.src.main.view.PartyDetailFragmentArgs
import com.rudder.util.LinePagerIndicatorDecoration


class ApplicantProfileFragment : Fragment() {
    private val viewModel: ApplicantProfileViewModel by viewModels()
    private lateinit var mContext: Context
    private lateinit var binding: FragmentApplicantProfileBinding
    private val args: ApplicantProfileFragmentArgs by navArgs()
    private val applicantProfileImageListAdapter by lazy {
        ApplicantProfileImageListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val applicantProfileRequest = args.applicantProfileRequest
        viewModel.setApplicantProfileRequest(applicantProfileRequest)
        viewModel.getHostParties()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApplicantProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.applicantProfileImagesRV.also {
            it.layoutManager = LinearLayoutManager(requireActivity(),
                LinearLayoutManager.HORIZONTAL,false)
            it.setHasFixedSize(false)
            it.adapter = applicantProfileImageListAdapter
            val pagerSnapHelper = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(it)
        }

        viewModel.partyProfile.observe(viewLifecycleOwner, Observer {
            it?.let {
                applicantProfileImageListAdapter.submitList(it.partyProfileImages.toList())
                binding.partyProfile = it
                Glide.with(binding.applicantUniversityLogoIV.context)
                    .load(it.schoolImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.applicantUniversityLogoIV)
            }
        })


        return binding.root
    }


}