package com.stephen.shopback.api

data class UserDataModel(val avatar_url: String, val login: String, val site_admin: Boolean,
                            val name: String?, val bio: String?, val location: String?, val blog: String?)