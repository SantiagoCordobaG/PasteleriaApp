package com.anthonydevs.intento3.pasteleria

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApp : Application() { // <-- aquí hereda de Application
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
