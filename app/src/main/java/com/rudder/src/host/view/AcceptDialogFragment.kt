package com.rudder.src.host.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.rudder.MainActivity
import com.rudder.databinding.FragmentAcceptDialogBinding

class AcceptDialogFragment(): DialogFragment() {
    private lateinit var binding: FragmentAcceptDialogBinding

    private lateinit var mContext: Context

    override fun onAttach(context: Context){
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding = FragmentAcceptDialogBinding.inflate(inflater, container,false)


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height
        var lp = binding.partyAcceptCL.layoutParams
        lp.width = (displayDpValue[0] * 0.8).toInt()
        binding.partyAcceptCL.layoutParams = lp
    }
}