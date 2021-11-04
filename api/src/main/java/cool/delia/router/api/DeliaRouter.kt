/*
 * 2016-2021 ©JMX Consumer Finance 版权所有
 */
package cool.delia.router.api

import android.util.Log
import java.util.HashMap

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
        val clz = Class.forName("${javaClass.canonicalName?.replace(javaClass.simpleName, "")}DeliaRouterAssist")
        val method = clz.getMethod("initRouter")
        @Suppress("UNCHECKED_CAST")
        mActions = method.invoke(clz.newInstance()) as HashMap<String, Action>
        Log.v("DeliaRouter", "DeliaRouter initialized")
    }

    fun registerAction(action: String, mAction: Action) {
        if (mActions.containsKey(action)) {
            return
        }
        mActions[action] = mAction
    }

//    fun sendMessage(c: Context?, mSRouterRequest: SRouterRequest): SRouterResponse {
//        val mSRouterResponse = SRouterResponse()
//        val mSAction: SAction? = findRequest(mSRouterRequest)
//        if (mSAction != null) {
//            val mObject: Any = mSAction.startAction(c, mSRouterRequest.getData())
//            mSRouterResponse.setStatus(
//                SRouterResponse.SUCCESS_CODE, SRouterResponse.SUCCESS_DESC, mObject
//            )
//        } else {
//            mSRouterResponse.setStatus(
//                SRouterResponse.Fail_CODE,
//                SRouterResponse.Fail_DESC,
//                "can not find this action,check to see if you have been registered!"
//            )
//        }
//        return mSRouterResponse
//    }
//
//    private fun findRequest(mSRouterRequest: SRouterRequest): SAction? {
//        val action: String = mSRouterRequest.getAction()
//        return if (mActions.containsKey(action)) {
//            mActions[action]
//        } else null
//    }
}