package pub.telephone.iceGPS.dataSource

import pub.telephone.iceGPS.R

enum class TagKey(
    @Suppress("PropertyName")
    @JvmField
    val Key: Int,
    @Suppress("PropertyName")
    @JvmField
    val InitKey: Int
) {
    MainActivityLoad(R.id.tagKey_MainActivityLoad, R.id.tagInitKey_MainActivityLoad),
    DataNodeColor(R.id.tagKey_DataNodeColor, R.id.tagInitKey_DataNodeColor),
    MyActivityLoad(R.id.tagKey_MyActivityLoad, R.id.tagInitKey_MyActivityLoad),
    BrowserSetCurrentWebView(
        R.id.tagKey_BrowserSetCurrentWebView,
        R.id.tagInitKey_BrowserSetCurrentWebView
    ),
    BrowserSetTitle(R.id.tagKey_BrowserSetTitle, R.id.tagInitKey_BrowserSetTitle),
    DataSource(R.id.tagKey_DataSource, R.id.tagInitKey_DataSource),
    DataNode(R.id.tagKey_DataNode, R.id.tagInitKey_DataNode);
}