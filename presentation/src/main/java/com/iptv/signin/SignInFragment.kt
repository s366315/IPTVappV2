package com.iptv.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.iptv.base.BaseFragment
import com.iptv.R
import com.iptv.SignInFragmentViewModel
import com.iptv.databinding.FragmentSigninBinding

class SignInFragment : BaseFragment<FragmentSigninBinding, SignInFragmentViewModel>() {
    override val viewModelClass: Class<SignInFragmentViewModel>
        get() = SignInFragmentViewModel::class.java

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSigninBinding =
        FragmentSigninBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            inputPhone.addTextChangedListener {

            }

            inputCode.addTextChangedListener {

            }

            btnSignIn.setOnClickListener {
                viewModel.doSignIn(
                    login = inputPhone.text.toString(),
                    password = inputCode.text.toString()
                )
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.signedInState.collect(signedInObserver)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.loadingState.collect {
                binding.progress.isVisible = it
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.errorData.collect {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private val signedInObserver: (Boolean) -> Unit = {
        if (it) {
            Navigation.findNavController(requireView()).navigate(R.id.action_fragmentSignIn_to_fragmentLife)
        }
    }
}