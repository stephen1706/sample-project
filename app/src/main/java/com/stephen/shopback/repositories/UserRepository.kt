package com.stephen.shopback.repositories

import com.stephen.shopback.api.UserDataModel
import com.stephen.shopback.api.UserListDataModel
import io.reactivex.Single

interface UserRepository {
    fun getUserList(url: String?): Single<UserListDataModel>

    fun getUserDetail(id: String): Single<UserDataModel>
}