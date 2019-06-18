# Wallet Connect Android

Wallet Connect Android is a library writen in Kotlin which implements WalletConnect protocol(only binance actions). Library is highly inspired by [swift-walletconnect-lib](https://github.com/WalletConnect/swift-walletconnect-lib)

## Download
Using Gradle: 

```groovy
repositories {
	...
	maven {url "https://jitpack.io"}
}

dependencies {
	implementation 'com.github.paytomat:wallet-connect-android:0.9.2'
}
```

## Proguard
Add to you proguard file following lines:

```
-keep class com.paytomat.walletconnect.android.model.* { *; }
```

## Usage
To **start WC session** you must specify SessionInfo and Client metadata:

```kotlin
val wcString: String = "wc:eb07cd88-45b5-4ced-891d-6475ff1fc548@1?bridge=https%3A%2F%2Fwallet-bridge.binance.org&key=88dabccbf7b006c74ed80543108c26ada67fd893ce3b6997c18459d5f6aa4c2b"//scanned wallet connect QR-code
val clientMeta = WCPeerMeta("WalletConnect SDK", "https://github.com/Paytomat/wallet-connect-android")
val uuid: String = "specify UUID here"
val interactor = WCInteractor(session, clientMeta, uuid)
```

Add callbacks by implementing `WCCallbacks` inteface:

```kotlin
override fun onSessionRequest(id: Long, peer: WCPeerMeta) {
	//Handle request for session start here
}

override fun onStatusUpdate(status: Status) {
	//Handle connection status here
}

override fun onBnbSign(id: Long, order: WCBinanceOrder<*>) {
	//Handle bnb order here
}
```

Set callbacks of interactor:

```kotlin
interactor.callbacks = this
```

**NOTE:** callbacks are called from thread where message were received, so if you do some UI action make sure to move action to UI thread 

**Approve session**:

```kotlin
interactor.approveSession(arrayOf(yourBnbAddress), yourChainIdInt)
```

**Reject session**

```kotlin
interactor.rejectSession()
interactor.killSession()//drop socket connection
```

**Approve BNB order**

```kotlin
override fun onBnbSign(id: Long, order: WCBinanceOrder<*>) {
	//Serialize JSON to string with nulls
	val orderString: String = GsonBuilder().serializeNulls().create().toJson(order)
	//Sign message
	val signature: ByteArray = Signature.signMessage(orderJson.toByteArray(), privateKey)
	//Form order signature message
	val signed = WCBinanceOrderSignature(Hex.toHexString(signature), Hex.toHexString(privateKey.publicKey.bytes))
    	//Send message
   	interactor.approveBnbOrder(id, signed)
}
```

**Reject BNB order**

```kotlin
override fun onBnbSign(id: Long, order: WCBinanceOrder<*>) {
	interactor.rejectRequest(id, "Rejected")
}
```

## Tested with:
* [Binance DEX](https://www.binance.org/en/)
