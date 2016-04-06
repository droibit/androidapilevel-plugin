@file:JvmName("Strings")

package com.github.droibit.plugin.androidapilevel.util

import java.util.*

val stringBundle: StringBundle by lazy { StringBundle(bundleAsMap("strings/strings")) }

/**
 * @author kumagai
 */
class StringBundle(private val stringMap: Map<String, String>) {

    val errorJsonParse by stringMap
    val errorLaunchBrowser by stringMap
    val headerName by stringMap
    val headerLevel by stringMap
    val headerPlatformVersion by stringMap
    val headerVersionCode by stringMap
    val jsonPathAndroidApi by stringMap
    val titleAndroidApiLevelDialog by stringMap
    val notificationErrorGroup by stringMap
    val notificationErrorTitle by stringMap

    val size: Int
        get() = stringMap.size
}

internal fun bundleAsMap(propPath: String): Map<String, String> {
    return ResourceBundle.getBundle(propPath).run {
        keys.asSequence().associate { it to getString(it) }
    }

}
