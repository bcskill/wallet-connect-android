package com.paytomat.walletconnect.android

import com.paytomat.walletconnect.android.model.WCBinanceOrder
import com.paytomat.walletconnect.android.model.WCPeerMeta

/**
 * created by Alex Ivanov on 2019-06-14.
 */
interface WCCallbacks {

    /**
     * Notifies socket status update when callback is set, method triggers with current status
     */
    fun onStatusUpdate(status: Status)

    /**
     * Called when session request is asked from socket
     * Warning: Method called from
     */
    fun onSessionRequest(id: Long, peer: WCPeerMeta)

    /**
     * Called when binance order signature required
     */
    fun onBnbSign(id: Long, order: WCBinanceOrder<*>)

}