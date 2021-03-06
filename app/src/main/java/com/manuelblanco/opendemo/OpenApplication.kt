package com.manuelblanco.opendemo

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.manuelblanco.core.coreModules
import com.manuelblanco.opendemo.viewmodel.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class OpenApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@OpenApplication)
            androidLogger(Level.NONE)
            modules(coreModules + viewModelModule)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}