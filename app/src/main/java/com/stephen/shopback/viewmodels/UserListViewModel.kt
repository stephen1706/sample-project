package com.stephen.shopback.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.stephen.shopback.api.UserDataModel
import com.stephen.shopback.repositories.UserRepository

class UserListViewModel(private val repository: UserRepository) : ViewModel() {
    val userList = MutableLiveData<List<UserDataModel>>()
    val errorMessage = MutableLiveData<String>()

    private var nextUrl: String? = null

    fun refreshUser() {
        nextUrl = null
        nextPage()
    }

    fun nextPage() {
        repository.getUserList(nextUrl)
                .subscribe({
                    userList.value = it.data
                    nextUrl = it.next
                }, {
                    it.printStackTrace()
                    errorMessage.value = it.message ?: "No internet connection, please try again"
                })
    }
}