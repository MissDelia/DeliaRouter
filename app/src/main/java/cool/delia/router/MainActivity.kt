/*
 * 2016-2021 ©MissDelia 版权所有
 */
package cool.delia.router

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import cool.delia.router.annotation.Router
import cool.delia.router.api.DeliaRouter

@Router(path = "/main")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.tv_hello)
            .setOnClickListener {
                val turn = DeliaRouter.withPath("/java/main")
                    .withInteger("integer", 114514)
                    .turn(this)
                Log.i("MainActivity", turn.resultMsg)
            }
    }
}