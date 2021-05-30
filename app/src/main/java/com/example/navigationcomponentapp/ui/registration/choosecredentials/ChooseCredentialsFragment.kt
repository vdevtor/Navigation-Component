package com.example.navigationcomponentapp.ui.registration.choosecredentials

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.navigationcomponentapp.R
import com.example.navigationcomponentapp.databinding.FragmentChooseCredentialsBinding
import com.example.navigationcomponentapp.extensions.disMissError
import com.example.navigationcomponentapp.ui.login.LoginViewModel
import com.example.navigationcomponentapp.ui.registration.RegistrationViewModel
import com.google.android.material.textfield.TextInputLayout

class ChooseCredentialsFragment : Fragment() {
    private val args: ChooseCredentialsFragmentArgs by navArgs()
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    private var binding: FragmentChooseCredentialsBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentChooseCredentialsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding?.textChooseCredentialsName?.text = getString(
                R.string.choose_credentials_text_name, args.name)
        val validationsfields = initValidationFields()
        listenToRegistrationStateEvent(validationsfields)
        registerDeviceBackStack()
        registerViewListeners()
    }

    private fun initValidationFields() = mapOf(
            RegistrationViewModel.INPUT_USERNAME.first to binding?.inputLayoutChooseCredentialsUsername,
            RegistrationViewModel.INPUT_PASSWORD.first to binding?.inputLayoutChooseCredentialsPassword
    )

    private fun listenToRegistrationStateEvent(validationFields: Map<String, TextInputLayout?>) {
        registrationViewModel.registrationStateEvent.observe(viewLifecycleOwner, Observer { registrationState ->
            when (registrationState) {
                is RegistrationViewModel.RegistrationState.RegistrationCompleted -> {
                    val token = registrationViewModel.authToken
                    val username = binding?.inputChooseCredentialsUsername?.text.toString()

                    loginViewModel.authenticateToken(token, username)
                    findNavController().popBackStack(R.id.profileFragment, false)
                }
                is RegistrationViewModel.RegistrationState.InvalidCredentials -> {
                    registrationState.fields.forEach { fieldError ->
                        validationFields[fieldError.first]?.error = getString(fieldError.second)
                    }
                }
                else -> return@Observer
            }
        })
    }

    private fun registerViewListeners() {
        binding?.buttonChooseCredentialsNext?.setOnClickListener {
            val username = binding?.inputChooseCredentialsUsername?.text.toString()
            val password = binding?.inputChooseCredentialsPassword?.text.toString()

            registrationViewModel.createCredentials(username, password)
        }

        binding?.inputChooseCredentialsUsername?.addTextChangedListener {
            binding?.inputLayoutChooseCredentialsUsername?.disMissError()
        }

        binding?.inputChooseCredentialsPassword?.addTextChangedListener {
            binding?.inputLayoutChooseCredentialsPassword?.disMissError()
        }
    }

    private fun registerDeviceBackStack() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelRegistration()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelRegistration()
        return super.onOptionsItemSelected(item)
    }

    private fun cancelRegistration() {
        registrationViewModel.userCancelledRegistration()
        findNavController().popBackStack(R.id.loginFragment, false)
    }
}