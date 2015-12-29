package com.github.droibit.plugin.androidapilevel.model

import com.github.droibit.plugin.androidapilevel.model.AndroidApiReader.jsonFile
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.File

private const val ANDROID_API_JSON_PATH = "AndroidAPIs.json"

/**
 * @author kumagai
 */
class AndroidApiReaderTest {

    @Test
    fun makeJsonFileURL() {
        val url = jsonFile(ANDROID_API_JSON_PATH)
        val jsonFile = File(url?.toURI())

        assertThat(jsonFile.exists()).isTrue()
    }

    @Test
    fun readAndroidApisJson() {
        val androidApis = AndroidApiReader.readFromJson(jsonFile(ANDROID_API_JSON_PATH))!!

        assertThat(androidApis!!.raw.size).isEqualTo(23)
    }

    @Test
    fun checkAndroidName() {
        val apis = AndroidApiReader.readFromJson(jsonFile(ANDROID_API_JSON_PATH))!!
        val androidNames = listOf(
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

        apis.run {
            assertThat(sortedNameMap.keys).isNotEmpty()
            assertThat(sortedNameMap.keys).containsAllIn(androidNames)
        }
    }

    @Test
    fun checkApiLevels() {
        val androidApis = AndroidApiReader.readFromJson(jsonFile(ANDROID_API_JSON_PATH))!!

        val reversedApis = androidApis.raw.map { it.apiLevel }.reversed() // e.g. 23 .. 1 => 1 .. 23
        for (i in reversedApis.indices) {
            assertThat(i+1).isEqualTo(reversedApis[i])
        }
    }

    @Test
    fun readAndroidApisJsonIfNotExist() {
        val url = jsonFile("NotExist.json")
        assertThat(url).isNull()

        val androidApis = AndroidApiReader.readFromJson(url)
        assertThat(androidApis).isNull()
    }
}