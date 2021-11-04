package cool.delia.router

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cool.delia.router.annotation.Router
import cool.delia.router.api.DeliaRouter

@Router(path = "/main")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DeliaRouter.init()
    }
}