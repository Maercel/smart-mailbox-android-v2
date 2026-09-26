package com.example.smartmailbox.addmailbox.domain

object DeviceVerificationCodeFormat {
    const val LENGTH = 8
    fun isValidChar(c: Char): Boolean = c in 'a'..'z' || c in 'A'..'Z' || c in '0'..'9'
    fun normalize(input: String): String = input.filter(::isValidChar).take(LENGTH)
    fun hasRequiredLength(code: String): Boolean = code.length == LENGTH
}