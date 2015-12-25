package com.github.droibit.plugin.androidapimap.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.intellij.openapi.diagnostic.Logger
import java.io.File
import java.net.URL

private const val PREFIX_ANDROID = "Android "

/**
 * @author kumagai
 */
data class AndroidApis(@SerializedName("apis") val raw: Array<AndroidApi>)
{
    // Sorted in descending order by Android version.
    val sortedNameMap: Map<String, List<AndroidApi>>
        get() = raw.groupBy { it.name }

    val size: Int
        get() = raw.size

    final operator fun get(index: Int) = raw[index]
    final operator fun iterator() = raw.iterator()
}

/**
 * @property apiLevel Api Level.
 * @property name Version name.
 * @property link Link to the developer page.
 * @property platformVersions Android versions of API level.
 * @property versionCode Version code name that is defined in Android SDK.
 *
 * @author kumagai
 */
data class AndroidApi(
        val apiLevel: Int,
        val name: String,
        val link: String? = null,
        val platformVersions: Array<String>,
        val versionCode: String) {

    // e.g. "Android 4.0, 4.0.1, 4.0.2"
    val platformVersion: String
        get() = buildString {
            append(PREFIX_ANDROID)
            platformVersions.joinTo(this)
        }
}

/**
 * @author kumagai
 */
object AndroidApiReader {

    private val gson = Gson()

    var logger: Logger? = null

    internal fun jsonFile(fileName: String): URL?
            = AndroidApiReader.javaClass.classLoader.getResource(fileName)

    fun readFromJson(jsonUrl: URL?) = try {
        val json = File(jsonUrl?.toURI()).readText()
        gson.fromJson(json, AndroidApis::class.java)
    } catch (e: Exception) {
        logger?.error("Json Parse Error", e)
        null
    }
}