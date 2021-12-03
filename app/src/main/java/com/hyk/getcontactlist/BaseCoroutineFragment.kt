package com.hyk.getcontactlist

import androidx.databinding.ViewDataBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseCoroutineFragment<T : ViewDataBinding> : BaseFragment<T>(), CoroutineScope {

    // 비동기 처리
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    // 비동기 종료
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
