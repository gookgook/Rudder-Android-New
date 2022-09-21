package com.rudder.src.main.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rudder.MainActivity
import com.rudder.R
import com.rudder.databinding.FragmentPartyApplyDialogBinding
import com.rudder.src.main.viewmodel.PartyApplyViewModel


class PartyApplyDialogFragment(val partyId: Int) : DialogFragment() {

    private lateinit var binding: FragmentPartyApplyDialogBinding

    private lateinit var mContext: Context
    private val partyApplyViewModel: PartyApplyViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        partyApplyViewModel.setPartyId(partyId)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        binding = FragmentPartyApplyDialogBinding.inflate(inflater, container, false)
        binding.viewModel= partyApplyViewModel
        binding.partyApplyDialogFragment=this

        val partyMemberCountList = listOf(0, 1, 2, 3, 4)
        val departmentASpinnerAdapter = ArrayAdapter(mContext, R.layout.custom_spinner_layout, partyMemberCountList)
        binding.partyApplyMemberCountS.adapter = departmentASpinnerAdapter



        partyApplyViewModel.isApplySuccess.observe(viewLifecycleOwner, Observer {
            dismiss()
        })

        partyApplyViewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(mContext,it,Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val displayDpValue = (activity as MainActivity).getDisplaySize() // [0] == width, [1] == height
        var lp = binding.partyApplyCL.layoutParams
        lp.width = (displayDpValue[0] * 0.8).toInt()
        binding.partyApplyCL.layoutParams = lp
    }

    fun onSpinnerSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        partyApplyViewModel.setNumberApplicants(parent.selectedItem as Int)
    }

}