package cool.delia.router.submodule.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cool.delia.router.annotation.Router

@Router(path = "/kotlin/main", resultMsg = "hello kotlin")
class KotlinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
    }
}