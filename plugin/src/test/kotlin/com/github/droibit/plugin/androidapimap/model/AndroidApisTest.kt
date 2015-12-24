package com.github.droibit.plugin.androidapimap.model

import com.google.gson.Gson
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

    private val gson = Gson()

    @Test
    fun itemSize() {
        val apis = gson.fromJson(JSON, AndroidApis::class.java)!!

        assertThat(apis.size, `is`(2))
        assertThat(apis.size, `is`(apis.raw.size))
    }

    @Test
    fun operatorGet() {
        val apis = gson.fromJson(JSON, AndroidApis::class.java)!!

        assertThat(apis[0], `is`(sameInstance(apis.raw[0])))
        assertThat(apis[1], `is`(sameInstance(apis.raw[1])))
    }
}