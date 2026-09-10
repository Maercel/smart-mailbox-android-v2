package com.example.smartmailbox.util

import com.example.smartmailbox.scanner.domain.MailBoxQRParser
import org.junit.Assert.assertEquals
import org.junit.Test

class MailBoxQrParserTest {

    @Test
    fun extractMailBoxId_returnsIdFromValidQrUrl() {
        val url = "https://b.direct4.me/00/000352/600"

        val result = MailBoxQRParser.extractMailBoxId(url)

        assertEquals(352, result)
    }

    @Test
    fun extractMailBoxId_returnsIdWhenUrlHasTrailingSlash() {
        val url = "https://b.direct4.me/00/000352/600/"

        val result = MailBoxQRParser.extractMailBoxId(url)

        assertEquals(352, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun extractMailBoxId_throwsExceptionWhenUrlIsTooShort() {
        val url = "https://b.direct4.me"

        MailBoxQRParser.extractMailBoxId(url)
    }

    @Test(expected = NumberFormatException::class)
    fun extractMailBoxId_throwsExceptionWhenIdIsNotNumber() {
        val url = "https://b.direct4.me/00/not-a-number/600"

        MailBoxQRParser.extractMailBoxId(url)
    }
}