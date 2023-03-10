package com.rudder.src.auth.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.rudder.MainActivity
//import androidx.navigation.Navigation.*
import com.rudder.R
import com.rudder.databinding.FragmentLoginBinding
import com.rudder.src.auth.viewmodel.LoginViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private  lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        setBinding()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.main = this
        binding.viewModel = viewModel
        binding.root.setOnClickListener { (requireActivity() as MainActivity).hideKeyboard() }
        return binding.root
    }

    fun setBinding() {
        viewModel.loginResultFlag.observe(viewLifecycleOwner, Observer { status ->
            status?.let{
                when (status) {
                    -2 -> Toast.makeText(this.activity, "Something Empty", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(this.activity, "Wrong", Toast.LENGTH_SHORT).show()
                    1 -> view?.let {
                        if (!findNavController().popBackStack(R.id.action_fragment_login_to_fragment_party_main,false)){
                            findNavController().navigate(R.id.action_fragment_login_to_fragment_party_main)
                        }
                    }
                    else -> Toast.makeText(this.activity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.isLoadingFlag.observe(viewLifecycleOwner, Observer { status ->
            if (status) (activity as MainActivity).dialog.show()
            else {(activity as MainActivity).dialog.hide() }
        })
    }

    fun printResult(resultBody: String) {
        Toast.makeText(this.activity, resultBody, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}
