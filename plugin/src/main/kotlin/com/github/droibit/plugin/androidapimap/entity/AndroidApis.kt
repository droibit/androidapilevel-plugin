package com.github.droibit.plugin.androidapimap.entity

/**
 * @author kumagai
 */
data class AndroidApis(val list: Array<AndroidApi>) {
   @Transient val nameMap: Map<String, List<AndroidApi>> by lazy { list.groupBy { it.name } }
}

private const val ANDROID = "Android "

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

    // e.g. Android 4.0, 4.0.1, 4.0.2
    val platformVersion: String
        get() = buildString {
            append(ANDROID)
            platformVersions.joinTo(this)
        }
}