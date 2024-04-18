package pub.telephone.iceGPS

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.ColorUtils
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleOwner
import pub.telephone.iceGPS.dataSource.DataNode
import pub.telephone.iceGPS.dataSource.DataViewHolder
import java.lang.ref.WeakReference

abstract class MyActivity<VH : DataViewHolder<*>, State : DataNode<VH>> : AppCompatActivity() {
    @Suppress("FunctionName")
    protected abstract fun createViewHolder_ui(inflater: LayoutInflater, container: ViewGroup?): VH

    @Suppress("FunctionName")
    protected abstract fun createState_ui(
        lifecycleOwner: WeakReference<LifecycleOwner>,
        holder: VH
    ): State

    @Suppress("FunctionName")
    protected abstract fun findToolBar_ui(holder: VH): Toolbar?

    @Suppress("PropertyName")
    protected abstract val title_ui: String
    private var insetsController: WindowInsetsControllerCompat? = null

    @Suppress("FunctionName")
    protected fun applyBackgroundColor_ui(useWhiteText: Boolean) {
        insetsController?.apply {
            isAppearanceLightStatusBars = !useWhiteText
            isAppearanceLightNavigationBars = !useWhiteText
        }
    }

    @Suppress("FunctionName")
    protected fun applyBackgroundColor_ui(@ColorInt color: Int) {
        applyBackgroundColor_ui(isLightColor(color).not())
    }

    @Suppress("PropertyName")
    protected open val useWhiteBarText_ui = false

    companion object {
        private val onApplyWindowInsetsListener = OnApplyWindowInsetsListener { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fun isLightColor(@ColorInt color: Int): Boolean {
            return ColorUtils.calculateLuminance(color) >= 0.5
        }
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.apply {
            navigationBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                navigationBarDividerColor = Color.TRANSPARENT
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                isNavigationBarContrastEnforced = false
            }
        }.apply {
            statusBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                isStatusBarContrastEnforced = false
            }
        }
        val holder = createViewHolder_ui(layoutInflater, null)
        ViewCompat.setOnApplyWindowInsetsListener(holder.itemView, onApplyWindowInsetsListener)
        insetsController = WindowCompat.getInsetsController(window, holder.itemView)
        applyBackgroundColor_ui(useWhiteBarText_ui)
        setSupportActionBar(findToolBar_ui(holder))
        title = title_ui
        setContentView(holder.itemView)
        createState_ui(WeakReference(this), holder).EmitChange_ui(null)
    }

    final override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        onCreate(savedInstanceState)
    }

    @SuppressLint("MissingSuperCall")
    final override fun onSaveInstanceState(outState: Bundle) {
    }

    final override fun onSaveInstanceState(
        outState: Bundle,
        outPersistentState: PersistableBundle
    ) {
        onSaveInstanceState(outState)
    }
}