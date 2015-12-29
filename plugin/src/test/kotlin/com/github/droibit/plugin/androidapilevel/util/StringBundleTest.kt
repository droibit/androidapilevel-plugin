package com.github.droibit.plugin.androidapilevel.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * @author kumagai
 */
class StringBundleTest {

    @Test
    fun readStringsProperties() {
        val strings = StringBundle(bundleAsMap("strings"))

        strings.run {
            assertThat(size).isEqualTo(2)
            assertThat(errorJsonParse).isEqualTo("Failed to read Android API data.")
            assertThat(errorLaunchBrowser).isEqualTo("Failed to launch browser.")
        }
    }
}