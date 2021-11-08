/*
 * 2016-2021 ©MissDelia 版权所有
 */
package cool.delia.router

import android.app.Application
import cool.delia.router.api.DeliaRouter

/**
 * Application实例
 *
 * @author xiong'MissDelia'zhengkun
 *
 * 2021/11/8 15:21
 */
class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化Router
        DeliaRouter.init()
    }
}