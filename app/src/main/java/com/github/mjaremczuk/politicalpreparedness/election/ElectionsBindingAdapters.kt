package com.github.mjaremczuk.politicalpreparedness.election

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mjaremczuk.politicalpreparedness.election.adapter.ElectionListAdapter
import com.github.mjaremczuk.politicalpreparedness.election.model.ElectionModel

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ElectionModel>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("setVisible")
fun setVisible(view: View, count: Int?) {
    if (count != null) {
        view.visibility = if (count > 0) View.VISIBLE else View.GONE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("showError")
fun showError(view: View, count: Int?) {
    if (count != null) {
        view.visibility = if (count > 0) View.GONE else View.VISIBLE
    } else {
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("refreshing")
fun bindRefreshing(refreshView: SwipeRefreshLayout, loading: Boolean?) {
    refreshView.isRefreshing = loading == true
}