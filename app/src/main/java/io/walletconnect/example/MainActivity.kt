package io.walletconnect.example

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.paytomat.binance.Address
import com.paytomat.binance.Signature
import com.paytomat.btc.PrivateKey
import com.paytomat.walletconnect.android.Status
import com.paytomat.walletconnect.android.WCCallbacks
import com.paytomat.walletconnect.android.WCInteractor
import com.paytomat.walletconnect.android.model.WCBinanceOrder
import com.paytomat.walletconnect.android.model.WCBinanceOrderSignature
import com.paytomat.walletconnect.android.model.WCPeerMeta
import com.paytomat.walletconnect.android.model.WCSession
import kotlinx.android.synthetic.main.screen_main.*
import org.bouncycastle.util.encoders.Hex


class MainActivity : Activity(), WCCallbacks {

    private var interactor: WCInteractor? = null

    private val handler: Handler = Handler(Looper.getMainLooper())

    private val privateKey =
        PrivateKey("YOUR KEY HERE", false)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_main)

        editURI.setText("wc:eb07cd88-45b5-4ced-891d-6475ff1fc548@1?bridge=https%3A%2F%2Fwallet-bridge.binance.org&key=88dabccbf7b006c74ed80543108c26ada67fd893ce3b6997c18459d5f6aa4c2b")
        editAddress.setText(Address(privateKey).toString())
        editChainId.setText("1")

        screen_main_connect_button.setOnClickListener {
            if (interactor != null) {
                interactor?.killSession()
                interactor = null
            } else {
                val sessionStr: String = editURI.text.toString()
                val clientMeta = WCPeerMeta("WalletConnect SDK", "https://github.com/TrustWallet/wallet-connect-swift")
                val session: WCSession = WCSession.fromURI(sessionStr) ?: return@setOnClickListener

                //Use Prefs instead
                interactor = WCInteractor(
                    session, clientMeta, Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                )
                interactor?.callbacks = this
                interactor?.connect()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        interactor?.connect()
    }

    override fun onStop() {
        super.onStop()
        interactor?.disconnect()
    }

    override fun onSessionRequest(id: Long, peer: WCPeerMeta) {
        Log.d("<<SS", "Show alert id: $id peer: $peer")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            .setMessage("Confirm session with ${peer.url}")
            .setPositiveButton("Confirm") { _, _ ->
                interactor?.approveSession(
                    arrayOf(editAddress.text.toString()),
                    editChainId.text.toString().toInt()
                )
            }.setNegativeButton("Reject") { _, _ ->
                interactor?.rejectSession()
                interactor?.killSession()
            }
        handler.post { builder.show() }
    }

    override fun onStatusUpdate(status: Status) {
        handler.post {
            screen_main_status.text = when (status) {
                Status.DISCONNECTED -> "Disconnected"
                Status.FAILED_CONNECT -> "Failed to connect"
                Status.CONNECTING -> "Connecting"
                Status.CONNECTED -> "Connected"
            }

            screen_main_connect_button.isEnabled = status != Status.CONNECTING
            screen_main_connect_button.text = if (status == Status.CONNECTED) "Disconnect" else "Connect"
        }
    }

    override fun onBnbSign(id: Long, order: WCBinanceOrder<*>) {
        handler.post {
            AlertDialog.Builder(this)
                .setMessage(Gson().toJson(order))
                .setPositiveButton("ok") { _, _ ->
                    val orderJson: String = GsonBuilder().serializeNulls().create().toJson(order)
                    val signature: ByteArray = Signature.signMessage(orderJson.toByteArray(), privateKey)
                    val signed = WCBinanceOrderSignature(
                        Hex.toHexString(signature),
                        Hex.toHexString(privateKey.publicKey.bytes)
                    )
                    interactor?.approveBnbOrder(id, signed)
                }.setNegativeButton("no") { _, _ -> interactor?.rejectRequest(id, "Rejected") }
                .show()
        }
    }
}
