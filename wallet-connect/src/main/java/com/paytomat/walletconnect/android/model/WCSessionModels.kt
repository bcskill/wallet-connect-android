package com.paytomat.walletconnect.android.model

/**
 * created by Alex Ivanov on 2019-06-07.
 */
data class WCExchangeKeyParam(val peerId: String, val peerMeta: WCPeerMeta, val nextKey: String)

data class WCSessionRequestParam(val peerId: String, val peerMeta: WCPeerMeta, val chainId: String?)

data class WCSessionUpdateParam(val approved: Boolean, val chainId: Int?, val accounts: Array<String>?)

data class WCApproveSessionResponse(
    val approved: Boolean,
    val chainId: Int,
    val accounts: Array<String>,
    val peerId: String?,
    val peerMeta: WCPeerMeta?
)