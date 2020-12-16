package com.elson.lint.detector

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.elson.lint.category.BizCategory
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNewExpression
import com.sun.org.apache.bcel.internal.generic.NEW
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.getQualifiedName
import org.jetbrains.uast.util.isConstructorCall


/**
 * @author elson
 * @date 2020/12/16
 * @Desc
 */
class ThreadUsageDetector : Detector(), Detector.UastScanner {

    companion object {
        @JvmField
        internal val ISSUE: Issue = Issue.create(
                "ThreadUsage",
                "Thread Usage",
                "Please use ThreadPool,such as AsyncTask.SERIAL_EXECUTOR",
                BizCategory.BIZ_DEPRECATED,
                6,
                Severity.WARNING,
                Implementation(ThreadUsageDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )

        private const val NEW_THREAD = "java.lang.Thread"
        private const val REPORT_MSG = "Avoid call new Thread() directly"
    }

    override fun getApplicableConstructorTypes(): List<String>? {
        return listOf(NEW_THREAD)
    }

    override fun visitConstructor(context: JavaContext, node: UCallExpression, constructor: PsiMethod) {
        if (context.evaluator.isMemberInClass(constructor, NEW_THREAD)) {
            context.report(ISSUE, node, context.getLocation(node), REPORT_MSG)
        }
    }


    // --------- 下面是等价方法 ---------

//    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
//        return listOf(UCallExpression::class.java)
//    }
//
//    override fun createUastHandler(context: JavaContext): UElementHandler? {
//        return object : UElementHandler() {
//            override fun visitCallExpression(node: UCallExpression) {
//                // 判断是否是构造函数被调用
//                if (!node.isConstructorCall()) {
//                    return
//                }
//                // 获取当前类的全路径
//                val classRef = node.classReference
//                if (classRef != null) {
//                    val className = classRef.getQualifiedName()
//                    // 判断类型是否一致
//                    if (NEW_THREAD == className && context.project.isAndroidProject) {
//                        context.report(ISSUE, node, context.getLocation(node), REPORT_MSG)
//                    }
//                }
//            }
//        }
//    }
}