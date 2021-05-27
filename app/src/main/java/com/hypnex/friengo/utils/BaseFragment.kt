package com.hypnex.friengo.utils

import androidx.fragment.app.Fragment
import com.hypnex.friengo.MainActivity

open class BaseFragment : Fragment() {

    private fun setLoading(isLoad: Int) {
        (activity as MainActivity).setLoading(isLoad)
    }

    fun observeMainView(baseViewModel: BaseViewModel) {
        baseViewModel.isLoad.observe(this, {
            setLoading(it)
        })

        baseViewModel.toastMessage.observe(this, {
            toast(it)
        })
    }

}