package com.a65apps.clustering.core

import com.a65apps.clustering.core.algorithm.ClusterProvider

/**
 * Default implementation of ClusterProvider
 */
open class DefaultClusterProvider : ClusterProvider {
    override fun get(cluster: Cluster): Cluster =
            DefaultCluster(cluster.geoCoor(), cluster.payload())
}
