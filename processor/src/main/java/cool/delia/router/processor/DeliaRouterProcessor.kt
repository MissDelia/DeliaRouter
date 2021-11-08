/*
 * 2016-2021 ©MissDelia 版权所有
 */
package cool.delia.router.processor

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import cool.delia.router.annotation.Router
import org.apache.commons.collections4.CollectionUtils
import java.io.File
import java.util.HashMap
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.collections.LinkedHashSet

/**
 * ## 路由注解处理器
 * 处理[Router]注解，生成Activity对应的Action类
 *
 * @author xiong'MissDelia'zhengkun
 *
 * 2021/11/3 15:48
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(value = ["cool.delia.router.annotation.Router"])
class DeliaRouterProcessor: AbstractProcessor() {
    private lateinit var mTypeUtils: Types
    private lateinit var mMessager: Messager
    private lateinit var mFiler: Filer
    private lateinit var mElementUtils: Elements

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        // 获取基本Processor工具
        mTypeUtils = processingEnvironment.typeUtils
        mMessager = processingEnvironment.messager
        mFiler = processingEnvironment.filer
        mElementUtils = processingEnvironment.elementUtils
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val annotations: MutableSet<String> = LinkedHashSet()
        annotations.add(Router::class.java.canonicalName)
        return annotations
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (CollectionUtils.isNotEmpty(annotations)) {
            val kaptKotlinGeneratedDir: String = processingEnv.options["kapt.kotlin.generated"]!!
            val outputFile = File(kaptKotlinGeneratedDir).apply {
                mkdirs()
            }
            // 构造Assist类的init方法体
            val stringClz = ClassName.bestGuess("kotlin.String")
            val actionClz = ClassName.bestGuess("cool.delia.router.api.action.Action")
            val methodBody = CodeBlock.builder()
                .addStatement("val routerMap = %T<%T, %T>()", HashMap::class.java, stringClz, actionClz)

            val elements = roundEnv.getElementsAnnotatedWith(Router::class.java)
            for (element in elements) {
                val file = buildAction(element as TypeElement, actionClz)
                // 输出Action类
                file.writeTo(outputFile.toPath())
                // 将path和Action的对应映射写入Assist
                val annotation = element.getAnnotation(Router::class.java)
                methodBody.addStatement("routerMap.put(%S, %T())", annotation.path, ClassName.bestGuess("${file.packageName}.${file.name}"))
            }

            // 输出Assist类
            buildAssist(methodBody).writeTo(outputFile.toPath())
            return true
        }
        return false
    }

    /**
     * Router主类的辅助配置
     * 将生成用于初始化路由信息的功能类
     * 生成的类为cool.delia.router.api.DeliaRouterAssist
     * 类中包含init一个方法，返回一个HashMap实例，其中包含了路由信息
     */
    private fun buildAssist(methodBody: CodeBlock.Builder): FileSpec {
        val assistPackage = "cool.delia.router.api"
        val assistClass = "DeliaRouterAssist"
        val returnType = HashMap::class.asClassName().parameterizedBy(
            ClassName.bestGuess("kotlin.String"),
            ClassName.bestGuess("cool.delia.router.api.action.Action")
        )
        return FileSpec.builder(assistPackage, assistClass)
            .addType(
                TypeSpec.classBuilder(assistClass)
                    .addFunction(
                        FunSpec.builder("initRouter")
                            .returns(returnType)
                            .addCode(methodBody.build())
                            .addStatement("return routerMap")
                            .build()
                    )
                    .build()
            )
            .build()
    }

    /**
     * 构造继承Action的类
     * 其中包含对应Activity跳转的方法
     */
    private fun buildAction(element: TypeElement, action: ClassName): FileSpec {
        val request = HashMap::class.asClassName().parameterizedBy(
            ClassName.bestGuess("kotlin.String"),
            Any::class.asTypeName()
        )
        val toBundleMember = MemberName("cool.delia.router.api.util.MapUtil.Companion", "requestToBundle")
        val packageName = mElementUtils.getPackageOf(element).qualifiedName.toString()
        val newClassName = "${element.simpleName}\$\$Action"
        val annotation = element.getAnnotation(Router::class.java)
        return FileSpec.builder(packageName, newClassName)
            .addType(
                TypeSpec.classBuilder(newClassName)
                    .superclass(action)
                    .addFunction(
                        FunSpec.builder("startAction")
                            .addModifiers(KModifier.OVERRIDE)
                            .addParameter("context", ClassName.bestGuess("android.content.Context"))
                            .addParameter("requestData", request)
                            .addCode(
                                CodeBlock.builder()
                                    .beginControlFlow(
                                        "if (context is %T)",
                                        ClassName.bestGuess("android.app.Activity")
                                    )
                                    .addStatement(
                                        "val i = %T(context, ${element.simpleName}::class.java)",
                                        ClassName.bestGuess("android.content.Intent")
                                    )
                                    .addStatement("i.putExtras(requestData.%M())", toBundleMember)
                                    .addStatement("context.startActivity(i)")
                                    .nextControlFlow("else")
                                    .addStatement(
                                        "val i = %T(context, ${element.simpleName}::class.java)",
                                        ClassName.bestGuess("android.content.Intent")
                                    )
                                    .addStatement("i.putExtras(requestData.%M())", toBundleMember)
                                    .addStatement("i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)")
                                    .addStatement("context.startActivity(i)")
                                    .endControlFlow()
                                    .build()
                            )
                            .returns(String::class.asTypeName())
                            .addStatement("return %S", annotation.resultMsg)
                            .build()
                    )
                    .build()
            )
            .build()

    }
}