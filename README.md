# Solana + RxSolana

This is a open source library on kotlin for Solana protocol.

The objective is to create a cross platform, fully functional, highly tested and less depencies as posible.

# Features
- [x] Sign and send transactions.
- [x] Key pair generation
- [x] RPC configuration.
- [x] Few libraries requirement (Moshi, okhttp3, bitcoinj, eddsa). RxKotlin is optional.
- [x] Fully tested (53%)
- [x] Sockets

# Usage

### Initialization

Set the NetworkingRouter and setup your enviroment. Use it to Initialize your solana object.
```kotlin
val network = NetworkingRouter(RPCEndpoint.devnetSolana)
val solana = Solana(network)
```
### SolanaAccountStorage

SolanaAccountStorage interface is used to return the generated accounts. The actual storage of the accout is handled by the client. Please make sure this account is stored correctly (you can encrypt it on the keychain). The retrived accout is Serializable. Inside Account you will find the phrase, publicKey and secretKey.

Example using Memory (NOT RECOMEMDED).
```kotlin
class InMemoryAccountStorage: SolanaAccountStorage {
    private var _account: Account? = null
    override fun save(account: Account) : Result<Unit> {
        _account = account
        return Result.success(Unit)
    }

    override fun account(): Result<Account> {
        if (_account != null){
            return Result.success(_account!!)
        }
        return Result.failure(Exception("unauthorized"))
    }

    override fun clear(): Result<Unit>{
        _account = null
        return Result.success(Unit)
    }
}
```
### RPC api calls

We support [45](https://github.com/ajamaica/SolanaKT/tree/master/solana/src/main/java/com/solana/api "Check the Api folder") rpc api calls.

Example using callback

Gets Accounts info.

```kotlin
solana.api.getAccountInfo(PublicKey("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV"), AccountInfo::class.java) { result in
// process result
}
```

Gets kotlin

```swift
 solana.api.getBalance(PublicKey("AaXs7cLGcSVAsEt8QxstVrqhLhYN2iGhFNRemwYnHitV")){ result in
 // process result
 }
```

### Actions

Actions are predifined program interfaces that construct the required inputs for the most common tasks in Solana ecosystems. You can see them as bunch of code that implements solana task using rpc calls.

We support 9.
- closeTokenAccount: Closes token account
- getTokenWallets: get token accounts
- createAssociatedTokenAccount: Opens associated token account
- sendSOL : Sends SOL native token
- createTokenAccount: Opens token account
- sendSPLTokens: Sends tokens
- findSPLTokenDestinationAddress : Finds address of a token of a address
- **serializeAndSendWithFee**: Serializes and signs the transaction. Then it it send to the blockchain.
- getMintData: Get mint data for token

#### Example

Create an account token

```swift
solana.action.createTokenAccount(sender, PublicKey("6AUM4fSvCAxCugrbJPFxTqYFp9r3axYx973yoSyzDYVH")) { result in
// process
}
```
Sending sol
```swift
let toPublicKey = "3h1zGmCwsRJnVk5BuRNMLsPaQu1y2aqXqXDWYCgrp5UG"
let transactionId = try! solana.action.sendSOL(
            sender,
            PublicKey("3h1zGmCwsRJnVk5BuRNMLsPaQu1y2aqXqXDWYCgrp5UG"),
            1
){ result in
 // process
}
```

## Requirements

- Android 21+

## Installation

I recomend using the github recomended way to load Artifacts. First get a Github Token from your [account settings](https://github.com/settings/tokens).


Inside settings.gradle add a maven repository:

```
repositories {
	...
	maven {
       name = "GitHubPackages"
       url = "https://maven.pkg.github.com/ajamaica/SolanaKT"
       credentials {
		   username = "<YOUR_GITHUB_USERNAME>"
		   password = "<YOUR_GITHUB_TOKENS>"
       }
	}
}
 
```

Then at your build.gradle:

```
dependencies {
	...
	implementation 'com.solana:solana:+' // Set version
}
```

After that gradle sync.

