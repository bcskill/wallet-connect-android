package com.paytomat.walletconnect.android.model

/**
 * created by Alex Ivanov on 2019-06-07.
 */
enum class WCEvent(val eventName: String) {
    SessionRequest("wc_sessionRequest"),
    SessionUpdate("wc_sessionUpdate"),
    ExchangeKey("wc_exchangeKey"),

    BnbSign("bnb_sign"),
    BnbTransactionConfirm("bnb_tx_confirmation");

    companion object {
        fun provideEvent(eventName: String): WCEvent? = values().firstOrNull { it.eventName == eventName }
    }

}