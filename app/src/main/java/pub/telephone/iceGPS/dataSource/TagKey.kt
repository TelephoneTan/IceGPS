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
    DataNode(R.id.tagKey_DataNode, R.id.tagInitKey_DataNode),
    DataSource(R.id.tagKey_DataSource, R.id.tagInitKey_DataSource);
}