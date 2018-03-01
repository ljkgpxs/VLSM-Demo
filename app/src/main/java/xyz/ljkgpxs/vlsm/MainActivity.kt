package xyz.ljkgpxs.vlsm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import xyz.ljkgpxs.vlsm.core.Vlsm
import xyz.ljkgpxs.vlsm.core.ipData

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        divideButton.setOnClickListener({ handleData() })

        for (i in 0..31) {
            Log.d("MainActivity", "test " + 255.shr(i))
        }

        val bin =  0B1010
        Log.d("MainActivity", "Binary $bin")

        val ip = ipData(192,168,3,1)
        Log.d("MainActivity", ip.toString())
        Log.d("MainActivity", (ip - 1000).toString())
        Log.d("MainActivity and Mask", ip.andMask(8).toString())
    }

    private fun handleData() {
        lateinit var ip: List<String>
        if (ipInput.text.toString() == "") {
            outputView.text = getString(R.string.emptyNot)
            return
        }
        try {
            if (!ipInput.text.matches("((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\/([1-9]|[1-2][0-9]|3[0-2])".toRegex())) {
                outputView.text = getString(R.string.ip_illegal)
                return
            }

            if (!divideInput.text.matches("([0-9]{1,}\\,){0,}[0-9]{1,}".toRegex())) {
                outputView.text = getString(R.string.divide_number_illegal)
                return
            }
            ip = ipInput.text.split("/")
            val vlsm: Vlsm = Vlsm(ip[0], ip[1].toInt())
            val numsStr = divideInput.text.split(",")
            val nums: Array<Int> = Array(numsStr.size, { i -> numsStr[i].toInt() })
            //Log.d("Test Array", nums[2].toString())

            vlsm.compute(nums)
            outputView.text = vlsm.getMessage()

        } catch (error: Exception) {
            Log.d("MainActivity", "Catch a error " + error.message)
            error.printStackTrace()
        }
    }
}
