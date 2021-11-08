/*
 * 2016-2021 ©MissDelia 版权所有
 */
package cool.delia.router.api

import android.util.Log
import cool.delia.router.api.action.Action
import java.util.*

/**
 * ## 路由功能主要实现类
 * 对于各组件间的数据交互，推荐使用Activity/Fragment Result API或其它Bus类第三方库如EventBus
 *
 * 故在路由功能模块中不再集成
 * @author xiong'MissDelia'zhengkun
 *
 * 2021/11/3 15:27
 */
object DeliaRouter {

    var mActions: HashMap<String, Action> = HashMap()

    fun init() {
        Log.v("DeliaRouter", "DeliaRouter start initialization")
        try {
            val clz = Class.forName("${javaClass.canonicalName?.replace(javaClass.simpleName, "")}DeliaRouterAssist")
            val method = clz.getMethod("initRouter")
            @Suppress("UNCHECKED_CAST")
            mActions = method.invoke(clz.newInstance()) as HashMap<String, Action>
        } catch (e: Exception) {
            Log.v("DeliaRouter", "DeliaRouter initialization failed because ${e.message}")
        }
        Log.v("DeliaRouter", "DeliaRouter initialized")
    }

    @Deprecated(message = "This method should not be called by outside code")
    fun registerAction(action: String, mAction: Action) {
        if (mActions.containsKey(action)) {
            return
        }
        mActions[action] = mAction
    }

    /**
     * 采用类建造者模式
     */
    fun withPath(path: String): RouterObject {
        return RouterObject(path)
    }
}