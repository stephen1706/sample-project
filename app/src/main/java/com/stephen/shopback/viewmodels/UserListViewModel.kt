package com.stephen.shopback.viewmodels

import android.arch.lifecycle.*
import com.stephen.shopback.api.UserDataModel
import com.stephen.shopback.repositories.UserRepository
import io.reactivex.disposables.CompositeDisposable

class UserListViewModel(private val repository: UserRepository) : ViewModel(), LifecycleObserver {
    val userList = MutableLiveData<List<UserDataModel>>()
    val errorMessage = MutableLiveData<String>()

    private var nextUrl: String? = null
    private val disposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        disposable.clear()
    }

    fun refreshUser() {
        nextUrl = null
        nextPage()
    }

    fun nextPage() {
        disposable.add(
                repository.getUserList(nextUrl)
                        .subscribe({
                            userList.value = it.data
                            nextUrl = it.next
                        }, {
                            it.printStackTrace()
                            errorMessage.value = it.message ?: "No internet connection, please try again"
                        })
        )
    }
}