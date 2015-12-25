package com.github.droibit.plugin.androidapimap.util

import java.util.*

val STRINGS: Strings by lazy { Strings(bundleAsMap("strings/strings")) }

/**
 * @author kumagai
 */
class Strings(private val stringMap: Map<String, String>) {

    val errorJsonParse by stringMap
    val errorLaunchBrowser by stringMap
    val headerName by stringMap
    val headerLevel by stringMap
    val headerPlatformVersion by stringMap
    val headerVersionCode by stringMap
    val jsonPathAndroidApi by stringMap

    val size: Int
        get() = stringMap.size
}

fun bundleAsMap(propPath: String): Map<String, String> {
    return ResourceBundle.getBundle(propPath).run {
        keys.asSequence().toMap { it to getString(it) }
    }

}
