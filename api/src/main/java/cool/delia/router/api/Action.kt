/*
 * 2016-2021 ©JMX Consumer Finance 版权所有
 */
package cool.delia.router.api

import android.content.Context
import java.util.*

/**
 * ## Action基类
 * 在Action类中处理各种跳转逻辑
 * @author xiong'MissDelia'zhengkun
 *
 * 2021/11/3 15:48
 */
abstract class Action {

    /**
     * 此方法实现跳转逻辑
     * @param context 上下文实例
     * @param requestData 要传递的参数
     */
    abstract fun startAction(context: Context, requestData: HashMap<String, Any>): String
}