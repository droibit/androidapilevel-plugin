package com.github.droibit.plugin.androidapilevel.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

private const val ANDROID_API_JSON_PATH = "AndroidAPIs.json"

class AndroidApiReaderTest {

    @Test
    fun readAndroidApisJson() {
        val androidApis = AndroidApiReader.readFromJson(ANDROID_API_JSON_PATH)!!

        assertThat(androidApis.raw).hasLength(26)
    }

    @Test
    fun checkAndroidName() {
        val apis = AndroidApiReader.readFromJson(ANDROID_API_JSON_PATH)!!
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
        val androidApis = AndroidApiReader.readFromJson(ANDROID_API_JSON_PATH)!!

        val reversedApis = androidApis.raw.map { it.apiLevel }.reversed() // e.g. 23 .. 1 => 1 .. 23
        for (i in reversedApis.indices) {
            assertThat(i + 1).isEqualTo(reversedApis[i])
        }
    }
}