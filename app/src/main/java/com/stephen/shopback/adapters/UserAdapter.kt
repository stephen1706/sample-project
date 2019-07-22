package com.stephen.shopback.adapters

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.makeramen.roundedimageview.RoundedImageView
import com.stephen.shopback.R
import com.stephen.shopback.api.UserDataModel
import com.stephen.shopback.databinding.RowUserListBinding


class UserAdapter(var dataList: ArrayList<UserDataModel>, var listener: UserAdapter.OnItemClickListener) : RecyclerView.Adapter<UserAdapter.BindingHolder>() {
    val requestOptions = RequestOptions()

    init {
        requestOptions.placeholder(R.drawable.ic_account_circle)
        requestOptions.error(R.drawable.ic_account_circle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowUserListBinding.inflate(layoutInflater, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val data = dataList.get(position)

        holder.binding.user = data
        holder.binding.textViewStaff.visibility = if(data.site_admin) View.VISIBLE else View.INVISIBLE
        holder.binding.frameContent.setOnClickListener {
            listener.onClick(it, data)
        }
        setImageUrl(holder.binding.imageViewUser, data.avatar_url)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addAll(data: List<UserDataModel>) {
//        val newList: ArrayList<UserDataModel> = this.dataList.clone() as ArrayList<UserDataModel>
//        newList.addAll(data)
//
//        val diffCallback = UserDiffCallback(this.dataList, newList)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
        val position = dataList.size
        dataList.addAll(data)

        notifyItemInserted(position)
//        diffResult.dispatchUpdatesTo(this)
//        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(view: View, data: UserDataModel)
    }

    fun setImageUrl(imageView: RoundedImageView, url: String?) {
        if (url == null) {
            imageView.setImageDrawable(null)
        } else {
            Glide.with(imageView).load(url).apply(requestOptions).into(imageView)
        }
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }

    class UserDiffCallback(private val oldList: List<UserDataModel>, private val newList: List<UserDataModel>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].login == newList[newItemPosition].login
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            // Implement method if you're going to use ItemAnimator
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
    class BindingHolder(var binding: RowUserListBinding) : RecyclerView.ViewHolder(binding.root)
}
