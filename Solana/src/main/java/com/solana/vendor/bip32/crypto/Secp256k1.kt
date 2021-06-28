package com.solana.vendor.bip32.crypto

import org.bouncycastle.crypto.ec.CustomNamedCurves
import org.bouncycastle.math.ec.ECPoint
import java.math.BigInteger

object Secp256k1 {
    val SECP = CustomNamedCurves.getByName("secp256k1")

    /**
     * serP(P): serializes the coordinate pair P = (x,y) as a byte sequence using
     * SEC1's compressed form: (0x02 or 0x03) || ser256(x), where the header byte
     * depends on the parity of the omitted y coordinate.
     *
     * @param p point
     * @return serialized point
     */
    @JvmStatic
    fun serP(p: ECPoint): ByteArray {
        return p.getEncoded(true)
    }

    @JvmStatic
    fun deserP(p: ByteArray): ECPoint {
        return SECP.curve.decodePoint(p)
    }

    /**
     * point(p): returns the coordinate pair resulting from EC point multiplication
     * (repeated application of the EC group operation) of the secp256k1 base point
     * with the integer p.
     *
     * @param p input
     * @return point
     */
    @JvmStatic
    fun point(p: BigInteger): ECPoint {
        return SECP.g.multiply(p)
    }

    /**
     * get curve N
     *
     * @return N
     */
    @JvmStatic
    val n: BigInteger
        get() = SECP.n
}