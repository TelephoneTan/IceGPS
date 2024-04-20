package pub.telephone.iceGPS

import pub.telephone.appKit.MyApp
import pub.telephone.appKit.dataSource.ColorManager

class App : MyApp() {
    override val colorManager_ui: ColorManager<*, *>
        get() = pub.telephone.iceGPS.config.colorManager
}