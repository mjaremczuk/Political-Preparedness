package com.github.mjaremczuk.politicalpreparedness.election

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mjaremczuk.politicalpreparedness.R
import com.github.mjaremczuk.politicalpreparedness.election.adapter.ElectionListAdapter
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ElectionModel>?) {
        val adapter = recyclerView.adapter as ElectionListAdapter
        adapter.submitList(data)
}

@BindingAdapter("setVisible")
fun setVisible(view: TextView, count: Int?) {
        if(count != null) {
            view.visibility = if (count > 0) View.VISIBLE else View.GONE
        } else {
            view.visibility = View.GONE
        }
}

@BindingAdapter("loadingStatus")
fun bindStatus(statusImageView: ImageView, status: ElectionsViewModel.Status?) {
    when (status) {
        ElectionsViewModel.Status.LOADING -> {
            statusImageView.visibility = View.GONE
//            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        ElectionsViewModel.Status.FAILURE -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        ElectionsViewModel.Status.SUCCESS -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("refreshing")
fun bindRefreshing(refreshView: SwipeRefreshLayout, status: ElectionsViewModel.Status?) {
    println("IS REFRESHING: $status")
    when (status) {
        ElectionsViewModel.Status.LOADING -> {
            refreshView.isRefreshing = true
        }
        ElectionsViewModel.Status.FAILURE,
        ElectionsViewModel.Status.SUCCESS -> {
            refreshView.isRefreshing = false
        }
    }
}