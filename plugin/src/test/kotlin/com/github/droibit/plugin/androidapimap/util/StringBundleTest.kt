package com.github.droibit.plugin.androidapimap.util

import org.junit.Assert.*
import org.junit.Test
import org.hamcrest.CoreMatchers.*

/**
 * @author kumagai
 */
class StringBundleTest {

    @Test
    fun readStringsProperties() {
        val strings = StringBundle(bundleAsMap("strings"))

        assertThat(strings, `is`(notNullValue()))
        assertThat(strings.size, `is`(2))

        assertThat(strings.errorJsonParse, `is`("Failed to read Android API data."))
        assertThat(strings.errorLaunchBrowser, `is`("Failed to launch browser."))
    }
}