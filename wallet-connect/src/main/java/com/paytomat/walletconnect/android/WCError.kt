package com.paytomat.walletconnect.android

/**
 * created by Alex Ivanov on 2019-06-12.
 */
sealed class WCError(message: String) : RuntimeException(message) {
    object BadServerResponse : WCError("Bad Response")
    object BadJSON : WCError("JSON malformed")
    object InvalidSession : WCError("Invalid session")
    object Unknown : WCError("Unknown")
}