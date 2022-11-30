package com.rudder.src.main.view

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rudder.MainActivity
import com.rudder.R
import com.rudder.config.App
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
        viewModel.isLoadingFlag.observe(viewLifecycleOwner, Observer { status ->
            if (status) (activity as MainActivity).dialog.show()
            else {(activity as MainActivity).dialog.hide() }
        })
    }

    fun goChat() {
        view?.let { Navigation.findNavController(it).navigate(R.id.action_fragment_myProfile_to_chat) }
    }

    fun waitForTheNextUpdate() {
        Toast.makeText(requireContext(),"Wait for the next Update!",Toast.LENGTH_SHORT).show()
    }

    fun goTerms() {
        view?.let { Navigation.findNavController(it).navigate(R.id.action_fragment_myProfile_to_terms) }
    }

    fun logout() {

        val builder = AlertDialog.Builder(this.context!!)
        builder.setTitle("Do you want to LOGOUT?")
            .setMessage("")
            .setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id ->
                    App.prefs.removeValue("authToken")
                    findNavController().navigate(R.id.action_myProfileFragment_to_fragment_start)
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                })
        // 다이얼로그를 띄워주기
        builder.show()

    }


}