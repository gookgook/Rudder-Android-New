package com.rudder.src.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        return binding.root
    }

    fun setBinding(){}

}