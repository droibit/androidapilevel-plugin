package com.github.droibit.plugin.androidapilevel.model

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import org.junit.Test

private const val JSON =
        """
            {
              "apis": [
                {
                  "apiLevel": 23,
                  "name": "Marshmallow",
                  "link": "http://d.android.com/about/versions/marshmallow/android-6.0.html",
                  "platformVersions": ["6.0"],
                  "versionCode": "M"
                },
                {
                  "apiLevel": 22,
                  "name": "Lollipop",
                  "link": "http://d.android.com/about/versions/android-5.1.html",
                  "platformVersions": ["5.1"],
                  "versionCode": "LOLLIPOP_MR1"
                }
              ]
            }
            """

/**
 * @author kumagai
 */
class AndroidApisTest {

    private val gson = Gson()

    @Test
    fun itemSize() {
        val apis = gson.fromJson(JSON, AndroidApis::class.java)!!
        apis.run {
            assertThat(size).isEqualTo(2)
            assertThat(size).isEqualTo(raw.size)
        }
    }

    @Test
    fun operatorGet() {
        val apis = gson.fromJson(JSON, AndroidApis::class.java)!!
        apis.run {
            assertThat(this[0]).isSameAs(raw[0])
            assertThat(this[1]).isSameAs(raw[1])
        }
    }
}