package com.github.droibit.plugin.androidapimap.model

import com.github.droibit.plugin.androidapimap.model.AndroidApiReader.jsonFile
import org.junit.Assert.*
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.empty
import org.junit.Test
import java.io.File

/**
 * @author kumagai
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

        val apis = checkNotNull(androidApis)
        assertThat(apis.items.size, `is`(23))
    }

    @Test
    fun checkAndroidName() {
        val apis = checkNotNull(AndroidApiReader.readFromJson(jsonFile(ANDROID_API_JSON_PATH)))
        val androidNames = arrayOf(
                "Marshmallow",
                "Lollipop",
                "KitKat",
                "Jelly Bean",
                "Ice Cream Sandwich",
                "Honeycomb",
                "Gingerbread",
                "Froyo",
                "Eclair",
                "Donut",
                "Cupcake",
                "-"
        )

        val readAndroidNames = apis.sortedNameMap.keys
        assertThat(readAndroidNames, not(empty()))
        assertThat(readAndroidNames, contains(*androidNames))
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