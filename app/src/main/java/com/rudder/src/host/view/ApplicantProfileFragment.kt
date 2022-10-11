package com.rudder.src.host.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rudder.R
import com.rudder.databinding.FragmentApplicantProfileBinding
import com.rudder.databinding.FragmentHostBinding


class ApplicantProfileFragment : Fragment() {

    private lateinit var mContext: Context
    private lateinit var binding: FragmentApplicantProfileBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApplicantProfileBinding.inflate(inflater, container, false)



        return binding.root
    }


}