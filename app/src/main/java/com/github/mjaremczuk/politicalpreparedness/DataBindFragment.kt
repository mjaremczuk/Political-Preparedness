package com.github.mjaremczuk.politicalpreparedness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class DataBindFragment<T: ViewDataBinding>: Fragment() {

    protected var _binding: T? = null

    val binding: T
    get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}