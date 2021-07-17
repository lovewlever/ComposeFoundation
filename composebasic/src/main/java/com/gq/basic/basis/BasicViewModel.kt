package com.gq.basic.basis

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BasicViewModel @Inject constructor(application: Application): AndroidViewModel(application), LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun lifecycleEventOnStop() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun lifecycleEventOnCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun lifecycleEventOnDestroy() {

    }

}