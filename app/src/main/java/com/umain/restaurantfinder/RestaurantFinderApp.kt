package com.umain.restaurantfinder

import android.app.Application
import com.umain.restaurantfinder.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class RestaurantFinderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@RestaurantFinderApp)
            modules(appModule)
        }
    }
}
