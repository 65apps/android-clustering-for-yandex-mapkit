package com.a65apps.clustering.yandex.view

import com.a65apps.clustering.core.Cluster
import com.a65apps.clustering.core.ClusterItem
import com.a65apps.clustering.core.view.ClusterRenderer
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.runtime.image.AnimatedImageProvider
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.ui_view.ViewProvider

class YandexClusterRenderer<T : ClusterItem>(
        map: Map,
        private val imageProvider: ClusterPinProvider<T>,
        private val minClusterSize: Int)
    : ClusterRenderer<T> {
    private val layer: MapObjectCollection = map.addMapObjectLayer(LAYER_NAME)

    companion object {
        const val LAYER_NAME = "CLUSTER_NAME"
    }

    override fun clusterChanged(clusters: Set<Cluster<T>>) {
        clusters.forEach { cluster ->
            var point: Point
            var image: PinProvider
            val star: PinProvider = imageProvider.getX()
            if (cluster.size() >= minClusterSize) {
                point = Point(cluster.position().latitude,
                        cluster.position().longitude)
                image = imageProvider.get(cluster)
                addPlacemark(layer, point, image.provider(), image.style())
                addPlacemark(layer, point, star.provider(), star.style())
            } else {
                cluster.items().forEach { clusterItem ->
                    point = Point(clusterItem.position().latitude,
                            clusterItem.position().longitude)
                    image = imageProvider.get(clusterItem)
                    addPlacemark(layer, point, image.provider(), image.style())
                    addPlacemark(layer, point, star.provider(), star.style())
                }
            }
        }
    }

    private fun addPlacemark(layer: MapObjectCollection,
                             point: Point,
                             provider: Any,
                             iconStyle: IconStyle?) {
        when (provider) {
            is ImageProvider -> addPlacemark(layer, point, provider, iconStyle)
            is ViewProvider -> addPlacemark(layer, point, provider, iconStyle)
            is AnimatedImageProvider -> addPlacemark(layer, point, provider, iconStyle)
        }
    }

    private fun addPlacemark(layer: MapObjectCollection,
                             point: Point,
                             provider: ImageProvider,
                             iconStyle: IconStyle?) {
        if (iconStyle != null) {
            layer.addPlacemark(point, provider, iconStyle)
        } else {
            layer.addPlacemark(point, provider)
        }
    }

    private fun addPlacemark(layer: MapObjectCollection,
                             point: Point,
                             provider: ViewProvider,
                             iconStyle: IconStyle?) {
        if (iconStyle != null) {
            layer.addPlacemark(point, provider, iconStyle)
        } else {
            layer.addPlacemark(point, provider)
        }
    }

    private fun addPlacemark(layer: MapObjectCollection,
                             point: Point,
                             provider: AnimatedImageProvider,
                             iconStyle: IconStyle?) {
        iconStyle?.let {
            layer.addPlacemark(point, provider, iconStyle)
        }
    }

    override fun animation(withAnimation: Boolean) {
    }

    override fun onAdd() {
    }

    override fun onRemove() {
    }
}