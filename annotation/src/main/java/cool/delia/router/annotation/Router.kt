package cool.delia.router.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Router (
    val path: String,
    val resultMsg: String = ""
)