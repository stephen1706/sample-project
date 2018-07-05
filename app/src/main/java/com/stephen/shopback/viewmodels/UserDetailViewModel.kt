package com.stephen.shopback.viewmodels

import android.arch.lifecycle.*
import com.stephen.shopback.api.UserDataModel
import com.stephen.shopback.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


class UserDetailViewModel(private val repository: UserRepository) : ViewModel(), LifecycleObserver {
    val userDetail = MutableLiveData<UserDataModel>()
    val errorMessage = MutableLiveData<String>()
    private val disposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        disposable.clear()
    }

    fun getUserDetail(id: String) {
        disposable.add(
                repository.getUserDetail(id)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            userDetail.value = it
                        }, {
                            it.printStackTrace()
                            errorMessage.value = it.message ?: "No internet connection, please try again"
                        })
        )
    }
}