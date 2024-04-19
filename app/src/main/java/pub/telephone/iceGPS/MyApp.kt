package pub.telephone.iceGPS

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import pub.telephone.iceGPS.dataSource.ColorManager
import java.util.concurrent.atomic.AtomicReference

class MyApp : Application() {
    private val uiHandler: Handler = Handler(Looper.getMainLooper())

    companion object {
        private val appR = AtomicReference<MyApp>()
        private val app: MyApp get() = appR.get()
        private val ui get() = app.uiHandler
        val context: Context get() = app.applicationContext
        fun post(r: Runnable) = ui.post(r)
        private fun isNight(config: Configuration): Boolean {
            return config.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //
        ColorManager.commit(isNight(newConfig))
    }

    override fun onCreate() {
        super.onCreate()
        appR.set(this)
        onConfigurationChanged(resources.configuration)
    }
}