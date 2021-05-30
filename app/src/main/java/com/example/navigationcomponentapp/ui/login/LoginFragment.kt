package com.example.navigationcomponentapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentapp.R
import com.example.navigationcomponentapp.databinding.FragmentLoginBinding
import com.example.navigationcomponentapp.extensions.disMissError
import com.example.navigationcomponentapp.extensions.navigateWithAnimastions
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null
    private val viewModel: LoginViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setObservable()
        viewButtonsListeners()
        viewTextChangeListeners()



        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelAuthentication()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelAuthentication()
        return true
    }

    private fun viewButtonsListeners() {

        //SIGN IN BUTTON
        binding?.buttonLoginSignIn?.setOnClickListener {
            val userName = binding?.inputLoginUsername?.text.toString()
            val passWord = binding?.inputLoginPassword?.text.toString()
            viewModel.authentication(userName, passWord)
        }

        //SIGN UP BUTTON
        binding?.buttonLoginSignUp?.setOnClickListener {
            findNavController().navigateWithAnimastions(R.id.action_loginFragment_to_navigation2)
        }
    }

    private fun viewTextChangeListeners() {

        binding?.inputLoginUsername?.addTextChangedListener {
            binding?.inputLayoutLoginUsername?.disMissError()
        }

        binding?.inputLoginPassword?.addTextChangedListener {
            binding?.inputLayoutLoginPassword?.disMissError()
        }
    }

    private fun cancelAuthentication() {
        viewModel.refuseAuthentication()
        findNavController().popBackStack(R.id.startFragment, false)
    }

    private fun setObservable() {
        viewModel.authenticationStateEvent.observe(viewLifecycleOwner, { authenticationState ->
            when (authenticationState) {
                is LoginViewModel.AuthenticationState.Authenticated -> {
                    findNavController().popBackStack()
                }
                is LoginViewModel.AuthenticationState.InvalidAuthentication -> {
                    val validationsFields: Map<String, TextInputLayout?> = initValidationFields()
                    authenticationState.fields.forEach { fieldsError ->
                        validationsFields[fieldsError.first]?.error = getString(fieldsError.second)
                    }
                }
                else -> return@observe
            }
        })
    }

    private fun initValidationFields() = mapOf(
            LoginViewModel.INPUT_USERNAME.first to binding?.inputLayoutLoginUsername,
            LoginViewModel.INPUT_PASSWORD.first to binding?.inputLayoutLoginPassword
    )

}