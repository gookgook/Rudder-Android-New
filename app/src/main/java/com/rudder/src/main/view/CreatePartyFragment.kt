package com.rudder.src.main.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.rudder.R
import com.rudder.databinding.FragmentCreatePartyBinding
import com.rudder.databinding.FragmentPartyDetailBinding
import com.rudder.src.main.viewmodel.CreatePartyViewModel
import com.rudder.src.main.viewmodel.PartyApplyViewModel
import java.util.*

class CreatePartyFragment : Fragment() {
    private lateinit var binding: FragmentCreatePartyBinding

    private val viewModel: CreatePartyViewModel by viewModels()
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreatePartyBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val partyMemberCountList = listOf(5,6,7,8,9,10)
        val departmentASpinnerAdapter = ArrayAdapter(mContext, R.layout.custom_spinner_layout, partyMemberCountList)
        binding.partyMemberCountS.adapter = departmentASpinnerAdapter

        binding.partyDateDP.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            viewModel.partyCalendar.set(year,monthOfYear,dayOfMonth)
        }
        binding.partyDateTP.setOnTimeChangedListener { view, hourOfDay, minute ->
            viewModel.partyCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
            viewModel.partyCalendar.set(Calendar.MINUTE,minute)
            Log.d("cal",viewModel.partyCalendar.toString())
        }
        return binding.root
    }

    fun onSpinnerSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        viewModel.partyMemberCount = (parent.selectedItem as Int)
    }
}