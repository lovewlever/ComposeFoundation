package com.gq.basic.basis

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BasicViewModel @Inject constructor(application: Application): AndroidViewModel(application), LifecycleObserver, DefaultLifecycleObserver {

}