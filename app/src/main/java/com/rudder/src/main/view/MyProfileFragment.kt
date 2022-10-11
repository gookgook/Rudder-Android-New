package com.rudder.src.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.R
import com.rudder.databinding.FragmentMyProfileBinding
import com.rudder.src.main.viewmodel.MyProfileViewModel

class MyProfileFragment: Fragment() {
    private lateinit var binding: FragmentMyProfileBinding

    private val viewModel: MyProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        setBinding()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        binding.main = this
        binding.viewModel = viewModel
        viewModel.getProfile()
        return binding.root
    }

    fun setBinding(){
        viewModel.partyProfile.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.partyProfile = it
                Glide.with(binding.profileImageIV.context)
                    .load(it.partyProfileImages[0])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.profileImageIV)

                Glide.with(binding.universityLogoIV.context)
                    .load(it.schoolImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.universityLogoIV)
            }
        })
    }

    fun goChat() {
        view?.let { Navigation.findNavController(it).navigate(R.id.action_fragment_myProfile_to_chat) }
    }


}