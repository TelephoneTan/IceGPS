package pub.telephone.iceGPS.dataSource

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import pub.telephone.javapromise.async.promise.Promise
import java.lang.ref.WeakReference

object EmbeddedDataNodeAPI {
    interface ViewHolderCreator<CH : DataViewHolder<*>> {
        fun createChild(inflater: LayoutInflater, container: ViewGroup?): CH
    }

    interface ViewHolder<CH : DataViewHolder<*>> : ViewHolderCreator<CH>

    interface DataNodeCreator<
            CH : DataViewHolder<*>,
            INFO,
            CD : pub.telephone.iceGPS.dataSource.DataNode<CH>
            > {
        fun createChild(lifecycleOwner: WeakReference<LifecycleOwner>?, holder: CH?): CD
        fun load(): Promise<INFO> = Promise.Resolve(null)
    }

    interface DataNode<
            CH : DataViewHolder<*>,
            INFO,
            CD : pub.telephone.iceGPS.dataSource.DataNode<CH>
            > : DataNodeCreator<CH, INFO, CD> {
        @Suppress("FunctionName")
        fun initChild_ui(lifecycleOwner: WeakReference<LifecycleOwner>?, holder: CH) {
        }

        @Suppress("FunctionName")
        fun childLoaded_ui(lifecycleOwner: WeakReference<LifecycleOwner>?, holder: CH, info: INFO) {
        }
    }

    interface All<
            CH : DataViewHolder<*>,
            INFO,
            CD : pub.telephone.iceGPS.dataSource.DataNode<CH>
            >
        : ViewHolder<CH>, DataNode<CH, INFO, CD>
}