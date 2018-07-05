package com.stephen.shopback

import com.stephen.shopback.api.ApiService
import com.stephen.shopback.repositories.UserRepository
import com.stephen.shopback.viewmodels.ViewModelFactory


object Injection {

    fun provideViewModelFactory(): ViewModelFactory {
        return ViewModelFactory(UserRepository(ApiService.create()))
    }
}
