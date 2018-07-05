package com.stephen.shopback.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.stephen.shopback.Injection
import com.stephen.shopback.R
import com.stephen.shopback.api.UserDataModel
import com.stephen.shopback.databinding.ActivityUserDetailBinding
import com.stephen.shopback.viewmodels.UserDetailViewModel
import kotlinx.android.synthetic.main.activity_user_detail.*


class UserDetailActivity : AppCompatActivity() {
    companion object {
        const val PARAM_USER_ID = "PARAM_USER_ID"

        fun launchUserDetail(context: Context, id: String): Intent {
            val intent = Intent(context, UserDetailActivity::class.java)
            intent.putExtra(PARAM_USER_ID, id)
            return intent
        }
    }

    private lateinit var viewModel: UserDetailViewModel

    private lateinit var userId: String
    private val requestOptions = RequestOptions()

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)

        userId = intent.getStringExtra(PARAM_USER_ID)

        setUpAttribute()

        setUpViewState()

        setUpListener()

        refreshUser()
    }

    private fun setUpAttribute() {
        requestOptions.placeholder(R.drawable.ic_account_circle)
        requestOptions.error(R.drawable.ic_account_circle)

        val viewModelFactory = Injection.provideViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailViewModel::class.java)

        lifecycle.addObserver(viewModel)
    }

    private fun setUpListener() {
        binding.swipeLayoutDetail.setOnRefreshListener {
            refreshUser()
        }
        binding.buttonClose.setOnClickListener {
            finish()
        }
    }

    private fun setUpViewState() {
        val observer = Observer<UserDataModel> { data ->
            swipe_layout_detail.isRefreshing = false
            if(data == null){
                return@Observer
            }
            binding.user = data
            binding.executePendingBindings()

            if(data.site_admin){
                text_view_staff_detail.visibility = VISIBLE
            } else {
                text_view_staff_detail.visibility = GONE
            }
            Glide.with(this)
                    .load(data.avatar_url)
                    .apply(requestOptions)
                    .into(image_view_detail)
        }

        viewModel.userDetail.observe(this, observer)
        viewModel.errorMessage.observe(this, Observer {
            val snackbar = Snackbar.make(frame_detail, it ?: "", Snackbar.LENGTH_LONG)
            snackbar.setAction("Refresh") {
                refreshUser()
            }
            snackbar.show()
            swipe_layout_detail.isRefreshing = false
        })
    }


    private fun refreshUser() {
        viewModel.getUserDetail(userId)
        swipe_layout_detail.isRefreshing = true
    }
}
