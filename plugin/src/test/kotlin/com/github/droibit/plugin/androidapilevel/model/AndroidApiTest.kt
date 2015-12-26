package com.github.droibit.plugin.androidapilevel.model

import com.google.gson.Gson
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author kumagai
 */
class AndroidApiTest {

    private val gson = Gson()

    @Test
    fun parseFromJson() {
        val marshmallowJson =
                """
                {
                  "apiLevel": 23,
                  "name": "Marshmallow",
                  "link": "http://developer.android.com/about/versions/marshmallow/android-6.0.html",
                  "platformVersions": ["6.0"],
                  "versionCode": "M"
                }
                """
        val api = gson.fromJson(marshmallowJson, AndroidApi::class.java)

        assertThat(api, `is`(notNullValue()))
        assertThat(api.apiLevel, `is`(23))
        assertThat(api.link.isNullOrEmpty(), `is`(false))
        assertThat(api.link, `is`("http://developer.android.com/about/versions/marshmallow/android-6.0.html"))
        assertThat(api.platformVersion, `is`(equalTo("Android 6.0")))
        assertThat(api.versionCode, `is`("M"))
    }

    @Test
    fun formatPlatformVersions() {
        val multiPlatformVersions =
                """
                {
                  "platformVersions": ["4.0", "4.0.1", "4.0.2"]
                }
                """
        val api = gson.fromJson(multiPlatformVersions, AndroidApi::class.java)

        assertThat(api, `is`(notNullValue()))
        assertThat(api.platformVersion, `is`(equalTo("Android 4.0, 4.0.1, 4.0.2")))
    }

    @Test
    fun nullableLink() {
        val notHasLink =
                """
                {
                  "apiLevel": 1,
                  "name": "-",
                  "platformVersions": ["1.0"],
                  "versionCode": "BASE"
                }
                """
        val api = gson.fromJson(notHasLink, AndroidApi::class.java)

        assertThat(api, `is`(notNullValue()))
        assertThat(api.link.isNullOrEmpty(), `is`(true))
        assertThat(api.link, `is`(nullValue()))
    }
}