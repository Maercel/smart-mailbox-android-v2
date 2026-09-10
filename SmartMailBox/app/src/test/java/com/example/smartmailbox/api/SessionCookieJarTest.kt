package com.example.smartmailbox.api

import com.example.smartmailbox.api.data.SessionCookieJar
import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SessionCookieJarTest {

    @Test
    fun loadForRequest_returnsEmptyListWhenNoCookiesSaved() {
        val cookieJar = SessionCookieJar()
        val url = "http://192.168.2.51:3001/users/mobile-login".toHttpUrl()

        val cookies = cookieJar.loadForRequest(url)

        assertTrue(cookies.isEmpty())
    }

    @Test
    fun saveFromResponse_thenLoadForRequest_returnsSavedCookieForSameHost() {
        val cookieJar = SessionCookieJar()
        val url = "http://192.168.2.51:3001/users/mobile-login".toHttpUrl()

        val cookie = Cookie.Builder()
            .name("connect.sid")
            .value("test-session")
            .domain("192.168.2.51")
            .path("/") // send this cookie to every path on this domain
            .build()

        cookieJar.saveFromResponse(url, listOf(cookie))

        val loadedCookies = cookieJar.loadForRequest(url)

        assertEquals(1, loadedCookies.size)
        assertEquals("connect.sid", loadedCookies[0].name)
        assertEquals("test-session", loadedCookies[0].value)
    }
}