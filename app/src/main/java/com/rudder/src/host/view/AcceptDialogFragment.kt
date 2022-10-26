package com.rudder.src.host.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rudder.MainActivity
import com.rudder.databinding.FragmentAcceptDialogBinding
import com.rudder.src.host.viewmodel.AcceptDialogVIewModel

class AcceptDialogFragment(): DialogFragment() {
    private lateinit var binding: FragmentAcceptDialogBinding

    private lateinit var mContext: Context

    private val viewModel: AcceptDialogVIewModel by viewModels()

   // private val args: AcceptDialogFragmentArgs by navArgs()

    private var partyId: Int = -1
    private var partyMemberId: Int = -1

    override fun onAttach(context: Context){
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // partyId = args.partyId
       // partyMemberId = args.partyMemberId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding = FragmentAcceptDialogBinding.inflate(inflater, container,false)

        setBinding()

        return binding.root
    }

    fun setBinding() {
        viewModel.acceptResultFlag.observe(viewLifecycleOwner, Observer { status ->
            status?.let {
                when(status) {
                    1 -> requireActivity().onBackPressed()
                    else -> Toast.makeText(this.activity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height
        var lp = binding.partyAcceptCL.layoutParams
        lp.width = (displayDpValue[0] * 0.8).toInt()
        binding.partyAcceptCL.layoutParams = lp
    }
}