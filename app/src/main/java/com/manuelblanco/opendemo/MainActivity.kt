package com.manuelblanco.opendemo

import android.os.Bundle
import com.manuelblanco.opendemo.ui.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}