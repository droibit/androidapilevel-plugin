package com.github.droibit.plugin.androidapilevel.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.intellij.openapi.diagnostic.Logger

data class AndroidApis(@SerializedName("apis") val raw: Array<AndroidApi>) {

    // Sorted in descending order by Android version.
    val sortedNameMap: Map<String, List<AndroidApi>> get() = raw.groupBy(AndroidApi::name)

    val size: Int get() = raw.size

    operator fun get(index: Int) = raw[index]
    operator fun iterator() = raw.iterator()
}

/**
 * @property apiLevel Api Level.
 * @property name Version name.
 * @property link Link to the developer page.
 * @property platformVersions Android versions of API level.
 * @property versionCode Version code name that is defined in Android SDK.
 */
data class AndroidApi(
        val apiLevel: Int,
        val name: String,
        val link: String? = null,
        val platformVersions: Array<String>,
        val versionCode: String) {

    companion object {
        private const val PREFIX_ANDROID = "Android "
    }

    // e.g. "Android 4.0, 4.0.1, 4.0.2"
    val platformVersion: String
        get() = buildString {
            append(PREFIX_ANDROID)
            platformVersions.joinTo(this)
        }
}

object AndroidApiReader {

    private val gson = Gson()

    var logger: Logger? = null

    fun readFromJson(jsonFilePath: String) = try {
        jsonFilePath.toInputStream().reader().use {
            gson.fromJson(it, AndroidApis::class.java)
        }
    } catch (e: Exception) {
        logger?.error("Json Parse Error", e)
        null
    }

    private fun String.toInputStream() = AndroidApiReader::class.java.classLoader.getResourceAsStream(this)
}