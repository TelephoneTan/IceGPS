package pub.telephone.iceGPS

import pub.telephone.appKit.MyActivity
import pub.telephone.appKit.dataSource.ColorConfig
import pub.telephone.appKit.dataSource.DataNode
import pub.telephone.appKit.dataSource.DataViewHolder
import pub.telephone.iceGPS.config.colorManager

abstract class Activity<CH : DataViewHolder<*>, CD : DataNode<CH>> : MyActivity<CH, CD>() {
    override fun backgroundColor(colors: ColorConfig<*>): Int {
        return colors.of(colorManager)!!.activity.background
    }

    override fun titleColor(colors: ColorConfig<*>): Int {
        return colors.of(colorManager)!!.activity.text
    }
}