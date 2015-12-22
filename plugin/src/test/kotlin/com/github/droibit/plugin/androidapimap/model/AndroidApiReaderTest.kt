package com.github.droibit.plugin.androidapimap.model

import com.github.droibit.plugin.androidapimap.model.AndroidApiReader.jsonFile
import org.junit.Assert.*
import org.hamcrest.CoreMatchers.*
import org.junit.Test
import java.io.File

/**
 * Created by kumagai on 2015/12/22.
 */
class AndroidApiReaderTest {

    @Test
    fun makeJsonFileURL() {
        val url = jsonFile(ANDROID_API_JSON_PATH)
        val jsonFile = File(url?.toURI())

        assertThat(jsonFile.exists(), `is`(true))
    }

    @Test
    fun readAndroidApisJson() {
        val androidApis = AndroidApiReader.readFromJson(jsonFile(ANDROID_API_JSON_PATH))

        assertThat(androidApis, `is`(notNullValue()))

        val apis = checkNotNull(androidApis?.items)
        assertThat(apis.size, `is`(23))
    }

    @Test
    fun checkApiLevels() {
        val androidApis = AndroidApiReader.readFromJson(jsonFile(ANDROID_API_JSON_PATH))

        val apis = checkNotNull(androidApis?.items)
        val reversedApis = apis.map { it.apiLevel }.reversed() // e.g. 23 .. 1 => 1 .. 23
        for (i in reversedApis.indices) {
            assertThat(i+1, `is`(reversedApis[i]))
        }
    }

    @Test
    fun readAndroidApisJsonIfNotExist() {
        val url = jsonFile("NotExist.json")
        assertThat(url, `is`(nullValue()))

        val androidApis = AndroidApiReader.readFromJson(url)
        assertThat(androidApis, `is`(nullValue()))
    }
}