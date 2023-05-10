package com.ahmrh.storyapp.di

import android.app.Application

class App: Application() {

    override fun onCreate(){
        super.onCreate()
        Injection.init(this)
    }
}