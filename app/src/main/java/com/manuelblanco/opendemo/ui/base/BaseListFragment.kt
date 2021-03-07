package com.manuelblanco.opendemo.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.manuelblanco.opendemo.R
import com.manuelblanco.opendemo.common.viewLifecycle
import com.manuelblanco.opendemo.databinding.FragmentListBinding


abstract class BaseListFragment : BaseFragment() {

    protected var binding: FragmentListBinding by viewLifecycle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        setUpToolbar(binding.toolbar)
        val view = binding.root
        return view
    }

    override fun setUpToolbar(toolbar: Toolbar) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.apply {
            title = getString(R.string.app_name)
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.mipmap.ic_launcher)
        }
    }

    fun dismissSwipeRefresh() {
        binding.swipeRefresh.isRefreshing = false
    }

    open fun showProgress() {
        dismissSwipeRefresh()
        binding.emptyList.visibility = View.GONE
        binding.swipeRefresh.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    open fun hideProgress() {
        dismissSwipeRefresh()
        binding.emptyList.visibility = View.GONE
        binding.swipeRefresh.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    open fun emptyView() {
        dismissSwipeRefresh()
        binding.emptyList.visibility = View.VISIBLE
        binding.swipeRefresh.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }
}