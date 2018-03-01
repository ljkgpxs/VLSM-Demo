package xyz.ljkgpxs.vlsm.core

import android.util.Log

data class ipData(private var a : Int, private var b : Int, private var c : Int, private var d : Int) {
    constructor(ip: ipData) : this(ip.a, ip.b, ip.c, ip.d)
    constructor(ipArray: IntArray) : this(ipArray[0], ipArray[1], ipArray[2], ipArray[3])

    private fun hBitToNum8(bit: Int): Long {
        var result: Long = 0

        if (bit > 8)
            return 0

        for (i in 0 until bit) {
            result += Math.pow(2.0, 7.0 - i.toDouble()).toLong()
        }

        return result
    }

    override fun toString(): String {
        return a.toString() + '.' + b + '.' + c + '.' + d
    }

    operator fun minus(n: Long): ipData {
        val newIpArray: IntArray = intArrayOf(a, b, c, d)
        var res = n

        for (i in 3 downTo 0) {
            if (newIpArray[i] < res) {
                newIpArray[i] = (255 - res % newIpArray[i]).toInt()
                res = (res - this[i]) / 255 + 1
            } else {
                newIpArray[i] -= res.toInt()
                break
            }
        }
        return ipData(newIpArray)
    }

    operator fun get(i: Int) = when (i) {
        0 -> a
        1 -> b
        2 -> c
        3 -> d
        else -> 0
    }

    operator fun plus(n: Long): ipData {
        val newIpArray: IntArray = intArrayOf(a, b, c, d)
        var res = n

        for (i in 3 downTo 0) {
            if (newIpArray[i] + res > 255) {
                newIpArray[i] = ((res + newIpArray[i]) % 255).toInt()
                res = (res + this[i]) / 255
            } else {
                newIpArray[i] += res.toInt()
                break
            }
        }
        return ipData(newIpArray)
    }

    fun andMask(bit : Int) : ipData {
        val arr = intArrayOf(0, 0, 0, 0)
        var nbit = bit
        for (i in 0..3) {
            if (nbit == 0) {
                arr[i] = 0
                continue
            }
            if (nbit - 8 < 0) {
                arr[i] = (hBitToNum8(nbit) and this[i].toLong()).toInt()
                nbit = 0
            } else  {
                arr[i] = 255 and this[i]
                nbit -= 8
            }
        }
        return ipData(arr)
    }
}