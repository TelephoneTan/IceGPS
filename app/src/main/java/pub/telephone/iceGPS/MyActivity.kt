package pub.telephone.iceGPS

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LifecycleOwner
import pub.telephone.iceGPS.dataSource.Colors
import pub.telephone.iceGPS.dataSource.DataNode
import pub.telephone.iceGPS.dataSource.DataViewHolder
import pub.telephone.iceGPS.dataSource.EmbeddedDataNode
import pub.telephone.iceGPS.dataSource.EmbeddedDataNodeAPI
import pub.telephone.iceGPS.dataSource.TagKey
import pub.telephone.iceGPS.databinding.MyActivityBinding
import java.lang.ref.WeakReference

private typealias MyActivityINFO = Any?

abstract class MyActivity<CH : DataViewHolder<*>, CD : DataNode<CH>>
    : AppCompatActivity(), EmbeddedDataNodeAPI.All<CH, MyActivityINFO, CD> {
    inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup?) :
        EmbeddedDataNode.ViewHolder<MyActivityBinding, CH>(
            inflater, parent, MyActivityBinding::class.java, this
        ),
        EmbeddedDataNodeAPI.ViewHolder<CH> by this {
        override fun retrieveContainer(): ViewGroup {
            return view.myActivityContent
        }
    }

    inner class DataNode(
        lifecycleOwner: WeakReference<LifecycleOwner>?,
        holder: MyActivity<CH, CD>.ViewHolder?
    ) : EmbeddedDataNode<CH, ViewHolder, MyActivityINFO, CD>(
        lifecycleOwner, holder, this
    ), EmbeddedDataNodeAPI.DataNode<CH, MyActivityINFO, CD> by this {
        init {
            watchColor()
        }
        override fun loadKey(): TagKey {
            return TagKey.MyActivityLoad
        }

        override fun color_ui(holder: MyActivity<CH, CD>.ViewHolder, colors: Colors<Int>) {
            backgroundColor(colors).also {
                holder.view.myActivityBackground.setBackgroundColor(it)
                applyBackgroundColor_ui(it)
            }
            titleColor(colors).also {
                holder.view.toolBar.apply {
                    setTitleTextColor(it)
                    setNavigationIconTint(it)
                }
            }
        }
    }

    protected open fun noTitle() = false
    protected open fun noHome() = false

    protected open fun backgroundColor(colors: Colors<Int>) = colors.myActivity.background
    protected open fun titleColor(colors: Colors<Int>) = colors.myActivity.text

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

    protected open fun handleAndroidHome() {
        handleOnBackPressed()
    }

    protected open fun handleOptionsItemSelected(itemID: Int) {}

    final override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (val id = item.itemId) {
            android.R.id.home -> handleAndroidHome()
            else -> handleOptionsItemSelected(id)
        }
        return true
    }

    @Suppress("OVERRIDE_DEPRECATION")
    final override fun onBackPressed() {
        @Suppress("DEPRECATION")
        super.onBackPressed()
    }

    @Suppress("FunctionName")
    protected fun super_onBackPressed() {
        onBackPressedCallback.isEnabled = false
        onBackPressedDispatcher.onBackPressed()
    }

    @Suppress("FunctionName")
    protected open fun onBackPressed_ui() {
        super_onBackPressed()
    }

    private fun handleOnBackPressed() {
        onBackPressed_ui()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            this@MyActivity.handleOnBackPressed()
        }
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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
        val holder = ViewHolder(layoutInflater, null)
        ViewCompat.setOnApplyWindowInsetsListener(holder.itemView, onApplyWindowInsetsListener)
        insetsController = WindowCompat.getInsetsController(window, holder.itemView)
        applyBackgroundColor_ui(useWhiteBarText_ui)
        holder.view.toolBar.takeUnless { noTitle() }?.let {
            it.takeIf { noHome() }?.navigationIcon = null
            setSupportActionBar(it)
        } ?: let {
            holder.view.toolBar.visibility = View.GONE
        }
        title = title_ui
        setContentView(holder.itemView)
        DataNode(WeakReference(this), holder).EmitChange_ui(null)
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