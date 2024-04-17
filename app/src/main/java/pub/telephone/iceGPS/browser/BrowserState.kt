package pub.telephone.iceGPS.browser

import android.content.Context
import android.graphics.Bitmap
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebViewClient
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import pub.telephone.iceGPS.MyApp
import pub.telephone.iceGPS.dataSource.DataNode
import pub.telephone.iceGPS.dataSource.DataViewHolder
import pub.telephone.iceGPS.dataSource.TagKey
import pub.telephone.iceGPS.databinding.BrowserBinding
import java.lang.ref.WeakReference

class BrowserState(
    lifecycleOwner: WeakReference<LifecycleOwner>?,
    holder: ViewHolder?,
    @Suppress("PrivatePropertyName")
    private val setTitle_ui: (title: String) -> Unit,
) : DataNode<BrowserState.ViewHolder>(lifecycleOwner, holder) {
    class ViewHolder(inflater: LayoutInflater, container: ViewGroup?) :
        DataViewHolder<BrowserBinding>(BrowserBinding::class.java, inflater, container)

    inner class WebView(
        context: Context,
        layoutParams: ViewGroup.LayoutParams,
        url: String?,
        referer: String?
    ) :
        android.webkit.WebView(context) {
        init {
            this.layoutParams = layoutParams
        }

        init {
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: android.webkit.WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    setTitle(this@WebView, "跳转到 ${request?.url ?: ""}")
                    return false
                }

                override fun onPageStarted(
                    view: android.webkit.WebView?,
                    url: String?,
                    favicon: Bitmap?
                ) {
                    setTitle(this@WebView, "正在加载 ${url ?: ""}")
                }

                override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
                    setTitle(this@WebView, view?.title ?: "")
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onCreateWindow(
                    view: android.webkit.WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message?
                ): Boolean {
                    setCurrentWebView(SetWebViewPack(
                        saveHistory = true,
                        restoreTitle = false,
                    ) {
                        resultMsg?.run {
                            (obj as? WebViewTransport)?.webView = it
                            sendToTarget()
                        }
                    })
                    return true
                }
            }
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)
                allowFileAccess = false
                allowContentAccess = false
                allowFileAccessFromFileURLs = false
                allowUniversalAccessFromFileURLs = false
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
                domStorageEnabled = true
            }
        }

        init {
            url?.let {
                loadUrl(it, mutableMapOf<String, String>().apply {
                    referer?.let { v -> put("Referer", v) }
                })
            }
        }
    }

    private var currentWebView: WebView? = null
    private val historyWebViews: ArrayDeque<WebView> = ArrayDeque()

    private data class SetTitlePack(val webView: WebView, val title: String)

    private val setTitle: Binding<SetTitlePack> = emptyBinding(TagKey.BrowserSetTitle)

    private data class SetWebViewPack(
        val saveHistory: Boolean,
        val restoreTitle: Boolean,
        val webView: WebView? = null,
        val url: String? = null,
        val referer: String? = null,
        @Suppress("PropertyName")
        val initWebView_ui: ((webView: WebView) -> Unit)? = null
    )

    private val setCurrentWebView: Binding<SetWebViewPack> = emptyBinding(
        TagKey.BrowserSetCurrentWebView
    )

    private fun setTitle(webView: WebView, title: String) {
        MyApp.post {
            EmitChange_ui(
                mutableSetOf(
                    setTitle.SetResult(SetTitlePack(webView, title))
                )
            )
        }
    }

    private fun setCurrentWebView(pack: SetWebViewPack) {
        MyApp.post {
            EmitChange_ui(
                mutableSetOf(
                    setCurrentWebView.SetResult(pack)
                )
            )
        }
    }

    @Suppress("FunctionName")
    fun onBackPressed_ui(
        @Suppress("LocalVariableName")
        super_onBackPressed_ui: () -> Unit
    ) {
        (this.currentWebView?.takeIf { it.canGoBack() }?.run {
            goBack()
            true
        } ?: this.historyWebViews.removeLastOrNull()?.let {
            this@BrowserState.currentWebView?.destroy()
            setCurrentWebView(
                SetWebViewPack(
                    saveHistory = false,
                    restoreTitle = true,
                    webView = it
                )
            )
            true
        }) ?: let {
            this@BrowserState.currentWebView?.destroy()
            super_onBackPressed_ui()
            true
        }
    }

    override fun __Bind__(changedBindingKeys: MutableSet<Int>?) {
        setTitle.Bind(changedBindingKeys) { holder, value ->
            setTitle_ui.takeIf { value.webView == this.currentWebView }?.invoke(value.title)
            null
        }
        setCurrentWebView.Bind(changedBindingKeys) { holder, value ->
            this.currentWebView.takeIf { value.saveHistory }?.let {
                this.historyWebViews.addLast(it)
            }
            //
            val webView = value.webView ?: WebView(
                holder.view.webViewContainer.context,
                ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                ),
                value.url,
                value.referer
            )
            holder.view.webViewContainer.removeAllViews()
            holder.view.webViewContainer.addView(webView)
            this.currentWebView = webView
            //
            value.initWebView_ui?.invoke(webView)
            //
            webView.takeIf { value.restoreTitle }?.title?.let {
                setTitle_ui(it)
            }
            null
        }
    }
}