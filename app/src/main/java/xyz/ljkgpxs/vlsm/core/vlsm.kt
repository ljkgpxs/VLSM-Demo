package xyz.ljkgpxs.vlsm.core

import android.util.Log

class Vlsm(private var ip: String, private var submask: Int) {
    private lateinit var ipArray : IntArray

    init {
        val t = ip.split(".")
        ipArray = intArrayOf(t[0].toInt(), t[1].toInt(), t[2].toInt(), t[3].toInt())
    }

    private var message: String? = null

    // 从低位转换, 参数为位数
    private fun lBitToNum(bit: Int): Long {
        var result: Long = 0

        for (i in 0 until bit) {
            result += Math.pow(2.0, i.toDouble()).toLong()
        }
        return result + 1
    }

    // 从高位转换, 参数为位数, 一共8位
    private fun hBitToNum8(bit: Int): Long {
        var result: Long = 0

        if (bit > 8)
            return 0

        for (i in 0 until bit) {
            result += Math.pow(2.0, 7.0 - i.toDouble()).toLong()
        }

        return result
    }

    private fun bitToIp(bit: Int): String? {
        var submask: String? = ""
        val a: Int = bit / 8
        val b: Int = bit % 8

        //Log.d("bitToIp", a.toString() + b)

        submask = when (a) {
            0 -> hBitToNum8(b).toString() + ".0.0.0"
            1 -> "255." + hBitToNum8(b) + ".0.0"
            2 -> "255.255." + hBitToNum8(b) + ".0"
            3 -> "255.255.255." + hBitToNum8(b)
            4 -> "255.255.255.255"
            else -> null
        }

        return submask
    }

    private fun findBestBit(num: Int): Int {
        var n = 1
        for (i in 0..31) {
            n *= 2
            Log.d("find", n.toString() + " " + i)
            if (n > num) {
                return i + 1
            }
        }
        return 0
    }

    fun compute(nums: Array<Int>) {
        var tmpIp = ipData(ipArray).andMask(submask)
        val maxClient = lBitToNum(32 - submask)
        message = "Max Client numbers: " + maxClient.toString() + '\n'

        if (nums.sum() > maxClient) {
            message += "Divide too much" + '\n'
        }

        for (n in nums) {
            Log.d("find Best Bit", findBestBit(n).toString())
            val bestBitNum = findBestBit(n)
            val tmpIp2 = tmpIp + lBitToNum(bestBitNum)
            message += bitToIp(32 - bestBitNum) + ", " + tmpIp + "/" + (32 - bestBitNum) + ", " + tmpIp + "~" + (tmpIp2 - 1) + "\n\n"
            tmpIp = tmpIp2
        }
    }

    fun getMessage(): String? = message

}