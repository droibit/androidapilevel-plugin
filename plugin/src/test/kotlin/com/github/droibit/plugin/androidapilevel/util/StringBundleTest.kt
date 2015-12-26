package com.github.droibit.plugin.androidapilevel.util

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Test

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