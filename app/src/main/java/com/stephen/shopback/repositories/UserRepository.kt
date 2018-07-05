package com.stephen.shopback.repositories

import com.stephen.shopback.api.ApiRoutes
import com.stephen.shopback.api.ApiService
import com.stephen.shopback.api.UserDataModel
import com.stephen.shopback.api.UserListDataModel
import com.stephen.shopback.utils.PageLinks
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject



class UserRepository (private val apiService: ApiService){
    fun getUserList(url: String?): Single<UserListDataModel> {
        return apiService.getUsers(url ?: ApiRoutes.USER_LIST_ROUTES + "?per_page=20")
                .map {
                    if(it.code() < 200 || it.code() >= 300){
                        var message: String = "Error " + it.code()
                        try {
                            val json = JSONObject(it.errorBody()?.string())
                            message = json.getString("message")
                        } catch (e: Exception){
                        }
                        throw Throwable(message)
                    }
                    val nextUrl = PageLinks(it.headers()["Link"]?: "").next
                    UserListDataModel(it.body() ?: ArrayList(), nextUrl)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    fun getUserDetail(id: String): Single<UserDataModel> {
        return apiService.getUserDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}