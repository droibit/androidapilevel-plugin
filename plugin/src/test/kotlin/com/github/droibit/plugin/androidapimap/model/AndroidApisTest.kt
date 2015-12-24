package com.github.droibit.plugin.androidapimap.model

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.junit.Assert.*
import org.hamcrest.CoreMatchers.*
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

    private val adapter: JsonAdapter<AndroidApis> =
            Moshi.Builder().build().adapter(AndroidApis::class.java)

    @Test
    fun itemSize() {
        val apis = adapter.fromJson(JSON)!!

        assertThat(apis.size, `is`(2))
        assertThat(apis.size, `is`(apis.raw.size))
    }

    @Test
    fun operatorGet() {
        val apis = adapter.fromJson(JSON)!!

        assertThat(apis[0], `is`(sameInstance(apis.raw[0])))
        assertThat(apis[1], `is`(sameInstance(apis.raw[1])))
    }
}