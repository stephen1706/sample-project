package com.stephen.shopback.viewmodels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.stephen.shopback.api.UserDataModel
import com.stephen.shopback.repositories.UserRepository


class UserDetailViewModel(private val repository: UserRepository) : ViewModel() {
    val userDetail = MutableLiveData<UserDataModel>()
    val errorMessage = MutableLiveData<String>()

    fun getUserDetail(id: String) {
        repository.getUserDetail(id)
                .subscribe({
                    userDetail.value = it
                }, {
                    it.printStackTrace()
                    errorMessage.value = it.message ?: "No internet connection, please try again"
                })
    }
}