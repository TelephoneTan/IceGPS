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
    BrowserSetCurrentWebView(
        R.id.tagKey_BrowserSetCurrentWebView,
        R.id.tagInitKey_BrowserSetCurrentWebView
    ),
    BrowserSetTitle(R.id.tagKey_BrowserSetTitle, R.id.tagInitKey_BrowserSetTitle),
    DataSource(R.id.tagKey_DataSource, R.id.tagInitKey_DataSource),
    DataNode(R.id.tagKey_DataNode, R.id.tagInitKey_DataNode);
}