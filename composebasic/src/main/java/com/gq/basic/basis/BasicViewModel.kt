package com.gq.basic.basis

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData

open class BasicViewModel(application: Application):
    AndroidViewModel(application), LifecycleObserver, DefaultLifecycleObserver {

        protected fun <T> getMutableLiveData() = MutableLiveData<T>()

}