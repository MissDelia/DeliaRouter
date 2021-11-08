/*
 * 2016-2021 ©MissDelia 版权所有
 */
package cool.delia.router.api

/**
 *
 * @author xiong'MissDelia'zhengkun
 *
 * 2021/11/8 14:15
 */
data class RouterResult(
    val resultCode: Int = -1,
    val resultMsg: String = "",
) {
    companion object {
        const val RESULT_CODE_SUCCESS = 0x00000001
        const val RESULT_CODE_FAILURE = 0x00000002
    }
}