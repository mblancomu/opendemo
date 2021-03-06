package com.manuelblanco.opendemo.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.manuelblanco.opendemo.network.ConnectionLiveData

open class BaseActivity : AppCompatActivity() {
    protected lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(this)
    }
}