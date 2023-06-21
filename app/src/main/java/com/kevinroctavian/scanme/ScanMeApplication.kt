package com.kevinroctavian.scanme

import android.app.Application
import com.kevinroctavian.scanme.data.AppContainer
import com.kevinroctavian.scanme.data.AppDataContainer
import com.orhanobut.hawk.Hawk

class ScanMeApplication: Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

        Hawk.init(this)
            .build()
    }
}