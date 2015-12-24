package com.github.droibit.plugin.androidapimap.util

import java.util.*


val STRINGS: Strings by lazy { Strings(bundleAsMap("strings")) }

/**
 * Created by kumagai on 2015/12/24.
 */

class Strings(private val stringRes: Map<String, String>) {

    val errorJsonParse by stringRes
    val errorLaunchBrowser by stringRes

    val size: Int
        get() = stringRes.size
}

fun bundleAsMap(propName: String): Map<String, String> {
    val props = ResourceBundle.getBundle(propName)
    val map = HashMap<String, String>()
    for (key in props.keys) {
        map.put(key, props.getString(key))
    }
    return map
}
