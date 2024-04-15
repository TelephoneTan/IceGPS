package pub.telephone.iceGPS

import android.app.Application
import android.os.Handler
import android.os.Looper
import java.util.concurrent.atomic.AtomicReference

class MyApp : Application() {
    private val uiHandler: Handler = Handler(Looper.getMainLooper())

    companion object {
        private val appR = AtomicReference<MyApp>()
        val app: MyApp get() = appR.get()
        val ui get() = app.uiHandler
        fun post(r: Runnable) = ui.post(r)
    }

    override fun onCreate() {
        super.onCreate()
        appR.set(this)
    }
}