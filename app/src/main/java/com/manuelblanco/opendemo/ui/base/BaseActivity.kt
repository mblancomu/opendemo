package com.manuelblanco.opendemo.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.manuelblanco.opendemo.network.ConnectionLiveData
import com.manuelblanco.opendemo.network.isConnected
import com.manuelblanco.opendemo.viewmodel.CharactersViewModel
import com.manuelblanco.opendemo.viewmodel.DetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class BaseActivity : AppCompatActivity() {
    protected lateinit var connectionLiveData: ConnectionLiveData
    val charactersViewModel by viewModel<CharactersViewModel>()
    val detailViewModel by viewModel<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Internet connection checker, it extends to all the activities of the app.
        connectionLiveData = ConnectionLiveData(this)

        //The connection value is updated.
        connectionLiveData.observe(this, Observer {
            charactersViewModel.isNetworkAvailable.value = it
            detailViewModel.isNetworkAvailable.value = it
        })

        charactersViewModel.isNetworkAvailable.value = isConnected
        detailViewModel.isNetworkAvailable.value = isConnected
    }
}