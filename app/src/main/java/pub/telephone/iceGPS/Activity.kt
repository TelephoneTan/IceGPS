package pub.telephone.iceGPS

import pub.telephone.iceGPS.config.colorManager
import pub.telephone.iceGPS.dataSource.ColorConfig
import pub.telephone.iceGPS.dataSource.DataNode
import pub.telephone.iceGPS.dataSource.DataViewHolder

abstract class Activity<CH : DataViewHolder<*>, CD : DataNode<CH>> : MyActivity<CH, CD>() {
    override fun backgroundColor(colors: ColorConfig<*>): Int {
        return colors.of(colorManager)!!.activity.background
    }

    override fun titleColor(colors: ColorConfig<*>): Int {
        return colors.of(colorManager)!!.activity.text
    }
}