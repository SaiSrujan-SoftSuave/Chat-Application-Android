package dev.open.chat_application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import dev.open.chat_application.di.initKoin

class ChatApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@ChatApplication)
        }
    }
}