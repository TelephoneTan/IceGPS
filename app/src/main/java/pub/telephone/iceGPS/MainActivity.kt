package pub.telephone.iceGPS

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import pub.telephone.iceGPS.dataSource.DataViewHolder
import pub.telephone.iceGPS.databinding.ActivityMainBinding
import java.lang.ref.WeakReference

class MainActivity : MyActivity<MainActivity.DataNode.ViewHolder, MainActivity.DataNode>() {
    class DataNode(lifecycleOwner: WeakReference<LifecycleOwner>?, holder: ViewHolder?) :
        pub.telephone.iceGPS.dataSource.DataNode<DataNode.ViewHolder>(
            lifecycleOwner, holder
        ) {
        class ViewHolder(inflater: LayoutInflater, container: ViewGroup?) :
            DataViewHolder<ActivityMainBinding>(
                ActivityMainBinding::class.java,
                inflater,
                container
            )

        override fun __Bind__(changedBindingKeys: MutableSet<Int>?) {
        }
    }

    override fun createViewHolder_ui(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DataNode.ViewHolder {
        return DataNode.ViewHolder(inflater, container)
    }

    override fun createState_ui(
        lifecycleOwner: WeakReference<LifecycleOwner>,
        holder: DataNode.ViewHolder
    ): DataNode {
        return DataNode(lifecycleOwner, holder)
    }

    override val title_ui: String
        get() = "冰河导航"

    override fun findToolBar_ui(holder: DataNode.ViewHolder): Toolbar {
        return holder.view.toolBar
    }

    override val useWhiteBarText_ui: Boolean
        get() = true
}