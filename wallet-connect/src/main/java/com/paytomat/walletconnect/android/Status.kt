package com.paytomat.walletconnect.android

/**
 * created by Alex Ivanov on 2019-06-06.
 */
sealed class Status {
    object DISCONNECTED : Status()
    object CONNECTED : Status()
    object CONNECTING : Status()
    object FAILED_CONNECT : Status()
}