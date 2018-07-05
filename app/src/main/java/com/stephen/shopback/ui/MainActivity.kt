package com.stephen.shopback.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.stephen.shopback.Injection
import com.stephen.shopback.R
import com.stephen.shopback.adapters.UserAdapter
import com.stephen.shopback.adapters.UserAdapter.OnItemClickListener
import com.stephen.shopback.api.UserDataModel
import com.stephen.shopback.viewmodels.UserListViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: UserListViewModel

    private val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpAttribute()

        setUpViewState()

        setUpListener()

        setUpAdapter()

        loadData()
    }

    private fun setUpAttribute() {
        isLoading.value = false
        val viewModelFactory = Injection.provideViewModelFactory()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserListViewModel::class.java)
        lifecycle.addObserver(viewModel)
    }

    private fun setUpAdapter() {
        adapter = UserAdapter(ArrayList(), object : OnItemClickListener {
            override fun onClick(view: View, data: UserDataModel) {
                startActivity(UserDetailActivity.launchUserDetail(this@MainActivity, data.login))
            }
        })
        header.attachTo(recycler_view);
        recycler_view.adapter = adapter
    }

    private fun setUpListener() {
        swipe_layout.setOnRefreshListener {
            loadData()
        }

        val layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if(isLoading.value!!){
                    return
                }
                val visibleItemCount = layoutManager.getChildCount()
                val totalItemCount = layoutManager.getItemCount()
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    isLoading.value = true
                    viewModel.nextPage()
                }
            }
        })
    }

    private fun setUpViewState() {
        isLoading.observe(this, Observer {
            swipe_layout.isRefreshing = it!!
        })

        viewModel.userList.observe(this, Observer<List<UserDataModel>> { data ->
            isLoading.value = false
            adapter.addAll(data!!)
        })

        viewModel.errorMessage.observe(this, Observer {
            Snackbar.make(coordinator_layout, it ?: "", Snackbar.LENGTH_LONG).show()
            isLoading.value = false
        })
    }

    private fun loadData() {
        isLoading.value = true
        adapter.clear()
        viewModel.refreshUser()
    }
}
