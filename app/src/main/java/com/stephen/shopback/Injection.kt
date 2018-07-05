package com.stephen.shopback

import com.stephen.shopback.api.ApiService
import com.stephen.shopback.repositories.UserRepositoryImpl
import com.stephen.shopback.viewmodels.ViewModelFactory


object Injection {

    fun provideViewModelFactory(): ViewModelFactory {
        return ViewModelFactory(UserRepositoryImpl(ApiService.create()))
    }
}
