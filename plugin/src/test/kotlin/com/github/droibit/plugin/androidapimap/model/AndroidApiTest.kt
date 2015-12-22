package com.github.droibit.plugin.androidapimap.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * @author kumagai
 */
class AndroidApiTest {

    private lateinit var adapter: JsonAdapter<AndroidApi>

    @Before
    fun setup() {
        adapter = Moshi.Builder().build()
                    .adapter(AndroidApi::class.java)
    }

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
        val api = adapter.fromJson(marshmallowJson)

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
        val api = adapter.fromJson(multiPlatformVersions)

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
        val api = adapter.fromJson(notHasLink)

        assertThat(api, `is`(notNullValue()))
        assertThat(api.link.isNullOrEmpty(), `is`(true))
        assertThat(api.link, `is`(nullValue()))
    }
}