package ar.edu.unlam.appsdeterceros

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        repeat(20) {
            Log.i("service", "el extra es ${intent?.getStringExtra("key")}")
            Log.i("service", "estoy en un servicio")
        }
        return 0
    }
}