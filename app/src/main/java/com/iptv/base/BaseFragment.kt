package com.iptv.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.iptv.injection.ViewModelFactory
import com.iptv.utils.OnSwipeTouchListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {
    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding!!

    protected lateinit var viewModel: VM

    protected abstract val viewModelClass: Class<VM>

    protected open var toolbarId: Int? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected abstract fun viewBindingInflate(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun viewModelStoreOwner(): ViewModelStoreOwner = this

    protected fun navigateTo(navigationRes: Int) {
        Navigation.findNavController(requireView()).navigate(navigationRes)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = provideViewModel(viewModelClass)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private fun provideViewModel(
        viewModelClass: Class<VM>,
        activity: FragmentActivity? = null
    ): VM {
        return if (activity != null) {
            ViewModelProvider(activity, viewModelFactory)[viewModelClass]
        } else {
            ViewModelProvider(this, viewModelFactory)[viewModelClass]
        }
    }

    infix fun <T> StateFlow<T>.bind(data: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            collect(data)
        }
    }

    infix fun <T> SharedFlow<T>.bind(data: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            collect(data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = viewBindingInflate(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorData bind errorObserver

        toolbarId?.let {
            NavigationUI.setupWithNavController(view.findViewById<Toolbar>(it), findNavController())
        }
    }

    fun View.clicks(): MutableSharedFlow<Unit> {
        val event = MutableSharedFlow<Unit>()
        this.setOnClickListener {
            lifecycleScope.launch {
                event.emit(Unit)
            }
        }

        return event
    }

    fun View.swipeUp(): MutableSharedFlow<Unit> {
        val event = MutableSharedFlow<Unit>()

        setOnTouchListener(object: OnSwipeTouchListener(context) {
            override fun onSwipeTop() {
                lifecycleScope.launch {
                    event.emit(Unit)
                }
            }
        })

        return event
    }

    fun View.swipeDown(): MutableSharedFlow<Unit> {
        val event = MutableSharedFlow<Unit>()

        setOnTouchListener(object: OnSwipeTouchListener(context) {
            override fun onSwipeBottom() {
                lifecycleScope.launch {
                    event.emit(Unit)
                }
            }
        })

        return event
    }

    infix fun BottomSheetBehavior<*>.stateChanges(target: MutableStateFlow<Int>) {
        this.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                target.value = newState
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val errorObserver: (String) -> Unit = {
        Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
    }
}