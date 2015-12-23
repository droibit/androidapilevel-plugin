package com.github.droibit.plugin.androidapimap.model

import com.intellij.openapi.diagnostic.Logger
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.io.File
import java.net.URL

public const val ANDROID_API_JSON_PATH = "AndroidAPIs.json"

private const val PREFIX_ANDROID = "Android "

/**
 * @author kumagai
 */
data class AndroidApis(@Json(name = "apis") val raw: Array<AndroidApi>)
{
    // Sorted in descending order by Android version.
    val sortedNameMap: Map<String, List<AndroidApi>>
        get() = raw.groupBy { it.name }

    val size: Int = raw.size

    public final operator fun get(index: Int) = raw[index]
    public final operator fun iterator(): Iterator<AndroidApi> = raw.iterator()
}

/**
 * @author kumagai
 */
object AndroidApiReader {

    private val adapter: JsonAdapter<AndroidApis> by lazy {
        Moshi.Builder().build().adapter(AndroidApis::class.java)
    }
    var logger: Logger? = null

    fun jsonFile(fileName: String): URL?
        = AndroidApiReader.javaClass.classLoader.getResource(fileName)

    fun readFromJson(jsonUrl: URL?) = try {
        val json = File(jsonUrl?.toURI()).readText()
        adapter.fromJson(json)
    } catch (e: Exception) {
        logger?.error("Json Parse Error", e)
        null
    }
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