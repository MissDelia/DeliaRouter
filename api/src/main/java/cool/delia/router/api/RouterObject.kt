/*
 * 2016-2021 ©MissDelia 版权所有
 */
package cool.delia.router.api

import android.content.Context
import android.util.Log
import cool.delia.router.api.action.Action
import java.io.Serializable
import java.lang.StringBuilder
import java.util.HashMap

/**
 * 由RouterAPI构建的节点模型，使用turn方法进行跳转
 *
 * @author xiong'MissDelia'zhengkun
 *
 * 2021/11/8 14:41
 */
class RouterObject(private val path: String = "") {

    /**
     * 传递给下一个节点的参数表
     */
    private val requestMap = HashMap<String, Any>()

    fun withInteger(key: String, param: Int): RouterObject {
        requestMap[key] = param
        return this
    }

    fun withLong(key: String, param: Long): RouterObject {
        requestMap[key] = param
        return this
    }

    fun withFloat(key: String, param: Float): RouterObject {
        requestMap[key] = param
        return this
    }

    fun withDouble(key: String, param: Double): RouterObject {
        requestMap[key] = param
        return this
    }

    fun withChar(key: String, param: Char): RouterObject {
        requestMap[key] = param
        return this
    }

    fun withString(key: String, param: String): RouterObject {
        requestMap[key] = param
        return this
    }

    fun withObject(key: String, param: Serializable): RouterObject {
        requestMap[key] = param
        return this
    }

    /**
     * 从哈希表中寻找path对应的Action
     */
    private fun findRequest(): Action? {
        return if (DeliaRouter.mActions.containsKey(path)) {
            DeliaRouter.mActions[path]
        } else {
            val pathGroup = buildPath(path.split("/"))
            val className = "cool.delia.router.api.action.impl.$pathGroup\$\$Action"
            var action: Action? = null
            try {
                val clz = Class.forName(className)
                action = clz.newInstance() as Action
                DeliaRouter.mActions[path] = action
            } catch (e: Exception) {
                Log.v("DeliaRouter", "Action initialization failed because ${e.message}")
            }
            action
        }
    }

    /**
     * 启动路由转换
     *
     * @param c 当前页面的上下文
     */
    fun turn(c: Context): RouterResult {
        val action = findRequest()
        return if (action != null) {
            val resultMsg = action.startAction(c, requestMap)
            RouterResult(RouterResult.RESULT_CODE_SUCCESS, resultMsg)
        } else {
            RouterResult(RouterResult.RESULT_CODE_FAILURE)
        }
    }

    private fun buildPath(pathGroup: List<String>): String {
        val sb = StringBuilder()
        for (s in pathGroup) {
            if (s == "") {
                continue
            }
            val chars = s.toCharArray()
            if (chars[0] in 'a'..'z') {
                chars[0] = chars[0] - 32
            }
            sb.append(String(chars))
        }
        return sb.toString()
    }
}