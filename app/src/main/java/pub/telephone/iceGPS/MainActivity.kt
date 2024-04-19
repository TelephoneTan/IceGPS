package pub.telephone.iceGPS

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import pub.telephone.iceGPS.browser.BrowserState
import pub.telephone.iceGPS.dataSource.Colors
import pub.telephone.iceGPS.dataSource.EmbeddedDataNode
import pub.telephone.iceGPS.dataSource.EmbeddedDataNodeAPI
import pub.telephone.iceGPS.dataSource.TagKey
import pub.telephone.iceGPS.databinding.ActivityMainBinding
import java.lang.ref.WeakReference

private typealias MainActivityINFO = Any?

class MainActivity : MyActivity<MainActivity.ViewHolder, MainActivity.DataNode>() {
    private val browserCreator =
        object :
            EmbeddedDataNodeAPI.DataNodeCreator<BrowserState.ViewHolder, MainActivityINFO, BrowserState> {
            override fun createChild(
                lifecycleOwner: WeakReference<LifecycleOwner>?,
                holder: BrowserState.ViewHolder?
            ): BrowserState {
                return BrowserState(
                    lifecycleOwner,
                    holder,
                    "https://appassets.androidplatform.net/assets/圣诞树.html"
                ) {
                    title = it
                }
            }
        }

    inner class DataNode(
        lifecycleOwner: WeakReference<LifecycleOwner>?,
        holder: MainActivity.ViewHolder?
    ) :
        EmbeddedDataNode<BrowserState.ViewHolder, ViewHolder, MainActivityINFO, BrowserState>(
            lifecycleOwner, holder, browserCreator
        ),
        EmbeddedDataNodeAPI.DataNodeCreator<BrowserState.ViewHolder, MainActivityINFO, BrowserState> by browserCreator {
        override fun loadKey(): TagKey {
            return TagKey.MainActivityLoad
        }

        init {
            watchColor()
        }

        override fun color_ui(holder: MainActivity.ViewHolder, colors: Colors<Int>) {
            holder.view.toolBar.setTitleTextColor(colors.main.text)
        }
    }

    class ViewHolder(inflater: LayoutInflater, container: ViewGroup?) :
        EmbeddedDataNode.ViewHolder<ActivityMainBinding, BrowserState.ViewHolder>(
            inflater,
            container,
            ActivityMainBinding::class.java,
            Creator
        ), EmbeddedDataNodeAPI.ViewHolderCreator<BrowserState.ViewHolder> by Creator {
        override fun retrieveContainer(): ViewGroup {
            return view.mainContent
        }

        private object Creator :
            EmbeddedDataNodeAPI.ViewHolderCreator<BrowserState.ViewHolder> {
            override fun createChild(
                inflater: LayoutInflater,
                container: ViewGroup?
            ): BrowserState.ViewHolder {
                return BrowserState.ViewHolder(inflater, container)
            }
        }
    }

    override val title_ui: String
        get() = "冰河导航"

    override fun findToolBar_ui(holder: ViewHolder): Toolbar {
        return holder.view.toolBar
    }

    override val useWhiteBarText_ui: Boolean
        get() = true

    override fun createChild(inflater: LayoutInflater, container: ViewGroup?): ViewHolder {
        return ViewHolder(inflater, container)
    }

    override fun createChild(
        lifecycleOwner: WeakReference<LifecycleOwner>?,
        holder: ViewHolder?
    ): DataNode {
        return DataNode(lifecycleOwner, holder)
    }
}