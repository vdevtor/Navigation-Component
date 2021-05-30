package com.example.navigationcomponentapp.ui.registration.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.navigationcomponentapp.R
import com.example.navigationcomponentapp.databinding.FragmentProfileDataBinding
import com.example.navigationcomponentapp.extensions.disMissError
import com.example.navigationcomponentapp.ui.registration.RegistrationViewModel
import com.google.android.material.textfield.TextInputLayout

class ProfileDataFragment : Fragment() {
    private var binding: FragmentProfileDataBinding? = null
    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentProfileDataBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val invalidadteFields = initValidationsFields()

        setObservable(invalidadteFields)
        buttonsListeners()
        viewTextChangeListeners()


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            cancelAuthentication()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        cancelAuthentication()
        return true
    }

    private fun buttonsListeners() {

        binding?.buttonProfileDataNext?.setOnClickListener {
            val name = binding?.inputProfileDataName?.text.toString()
            val bio = binding?.inputProfileDataBio?.text.toString()
            registrationViewModel.collectProfileData(name, bio)
        }
    }

    private fun viewTextChangeListeners() {

        binding?.inputProfileDataName?.addTextChangedListener {
            binding?.inputLayoutProfileDataName?.disMissError()
        }

        binding?.inputProfileDataBio?.addTextChangedListener {
            binding?.inputLayoutProfileDataBio?.disMissError()
        }
    }

    private fun cancelAuthentication() {
        registrationViewModel.userCancelledRegistration()
        findNavController().popBackStack(R.id.loginFragment, false)
    }

    private fun setObservable(validations: Map<String, TextInputLayout?>) {
        registrationViewModel.registrationStateEvent.observe(viewLifecycleOwner, { registrationState ->
            when (registrationState) {
                is RegistrationViewModel.RegistrationState.CollectCredentials -> {
                    val name = binding?.inputProfileDataName?.text.toString()
                    val directions = ProfileDataFragmentDirections.actionProfileDataFragmentToChooseCredentialsFragment(name)

                    findNavController().navigate(directions)
                }
                is RegistrationViewModel.RegistrationState.InvalidProfileData -> {
                    registrationState.fields.forEach { fieldsError ->
                        validations[fieldsError.first]?.error = getString(fieldsError.second)

                    }
                }
            }
        })
    }

    private fun initValidationsFields() = mapOf(
            RegistrationViewModel.INPUT_NAME.first to binding?.inputLayoutProfileDataName,
            RegistrationViewModel.INPUT_BIO.first to binding?.inputLayoutProfileDataBio
    )
}