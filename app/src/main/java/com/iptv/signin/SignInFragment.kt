package com.iptv.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.iptv.R
import com.iptv.base.BaseFragment
import com.iptv.databinding.FragmentSigninBinding

class SignInFragment : BaseFragment<FragmentSigninBinding, SignInFragmentViewModel>() {
    override val viewModelClass = SignInFragmentViewModel::class.java

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSigninBinding.inflate(inflater, container, false)

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

        viewModel.signedInState bind signedInObserver
        viewModel.loadingState bind loadingObserver
    }

    private val signedInObserver: (Boolean) -> Unit = {
        if (it) {
            navigateTo(R.id.action_fragmentSignIn_to_fragmentLife)
        }
    }

    private val loadingObserver: (Boolean) -> Unit = {
        binding.progress.isVisible = it
    }
}