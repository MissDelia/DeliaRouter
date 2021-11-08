/*
 * 2016-2021 ©MissDelia 版权所有
 */
package cool.delia.router.annotation

/**
 * ## 路由页面的注解
 *
 * @author xiong'MissDelia'zhengkun
 *
 * 2021/11/8 13:37
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Router (
    val path: String,
    val resultMsg: String = ""
)