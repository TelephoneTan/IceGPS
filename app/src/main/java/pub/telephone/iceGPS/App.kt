package pub.telephone.iceGPS

import pub.telephone.iceGPS.dataSource.ColorManager

class App : MyApp() {
    override val colorManager_ui: ColorManager<*, *>
        get() = pub.telephone.iceGPS.config.colorManager
}