package com.github.droibit.plugin.androidapilevel.model

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
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
        val api = gson.fromJson(marshmallowJson, AndroidApi::class.java)!!

        api.run {
            assertThat(apiLevel).isEqualTo(23)
            assertThat(link).isNotEmpty()
            assertThat(link).isEqualTo("http://developer.android.com/about/versions/marshmallow/android-6.0.html")
            assertThat(platformVersion).isEqualTo("Android 6.0")
            assertThat(versionCode).isEqualTo("M")
        }
    }

    @Test
    fun formatPlatformVersions() {
        val multiPlatformVersions =
                """
                {
                  "platformVersions": ["4.0", "4.0.1", "4.0.2"]
                }
                """
        val api = gson.fromJson(multiPlatformVersions, AndroidApi::class.java)!!

        assertThat(api.platformVersion).isEqualTo("Android 4.0, 4.0.1, 4.0.2")
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
        val api = gson.fromJson(notHasLink, AndroidApi::class.java)!!

        assertThat(api.link.isNullOrEmpty()).isTrue()
        assertThat(api.link).isNull()
    }
}