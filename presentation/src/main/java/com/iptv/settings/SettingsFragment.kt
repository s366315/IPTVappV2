package com.iptv.settings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.children
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.DialogFragment
import com.iptv.R
import com.iptv.base.BaseDialogFragment
import com.iptv.databinding.FragmentSettingsBinding
import com.iptv.domain.entities.LoginDetails
import com.iptv.utils.dp
import com.iptv.utils.setOnItemChooseListener
import com.iptv.utils.timestampToDate
import javax.inject.Inject

class SettingsFragment : BaseDialogFragment<FragmentSettingsBinding, SettingsFragmentViewModel>() {

    override val viewModelClass: Class<SettingsFragmentViewModel>
        get() = SettingsFragmentViewModel::class.java

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSettingsBinding.inflate(inflater, container, false)

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            attributes?.windowAnimations = R.style.DialogAnimation
            setBackgroundDrawableResource(R.drawable.bg_dialog)
            setLayout(
                if (resources.getBoolean(R.bool.is_tablet)) 560.dp else ViewGroup.LayoutParams.MATCH_PARENT,
                resources.getDimension(R.dimen.dialog_height).toInt()
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.profileState bind initUIObserver
        viewModel.settingsSaveState bind settingsSaveObserver
        viewModel.loadingState bind loadingObserver

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_save -> {
                    viewModel.saveSettings()
                }
                else -> {}
            }
            true
        }

        binding.toolbar.setNavigationOnClickListener { dismiss() }
    }

    private val initUIObserver: (LoginDetails) -> Unit = {
        binding.apply {
            it.let { loginDetails ->
                toolbar.title = loginDetails.account.packetName
                tvLogin.text = loginDetails.account.login
                tvTariff.text = loginDetails.account.packetExpire.toInt().timestampToDate()
                tvVod.text = if (loginDetails.services.vod) "YES" else "NO"
                tvArchive.text = if (loginDetails.services.archive) "YES" else "NO"

                val list = loginDetails.settings.httpCaching.list
                binding.spinnerBuffer.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    list
                )

                binding.spinnerBuffer.setSelection(list.indexOfFirst { it == loginDetails.settings.httpCaching.value })
                binding.spinnerBuffer.setOnItemChooseListener { position ->
                    viewModel.selectBuffer(list[position])
                }
            }
        }
    }

    private val settingsSaveObserver: (String) -> Unit = {
        dismiss()
    }

    private val loadingObserver: (Boolean) -> Unit = {
        binding.progress.isVisible = it
    }
}