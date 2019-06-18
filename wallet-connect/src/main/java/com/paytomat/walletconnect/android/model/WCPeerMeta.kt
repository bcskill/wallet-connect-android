package com.paytomat.walletconnect.android.model

/**
 * created by Alex Ivanov on 2019-06-06.
 */
data class WCPeerMeta(
    val name: String,
    val url: String,
    val description: String = "",
    val icons: List<String> = emptyList()
)