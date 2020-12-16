package com.elson.lint.detector

import com.android.tools.lint.detector.api.*
import com.android.tools.lint.detector.api.Detector.UastScanner
import com.android.tools.lint.detector.api.Scope.Companion.JAVA_FILE_SCOPE
import com.elson.lint.category.BizCategory
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

/**
 * @author elson
 * @date 2020/11/28
 * @Desc
 */
class PermissionDetector : Detector(), UastScanner {

    companion object {
        @JvmField
        internal val ISSUE = Issue.create("RxPermissionUsage",
                "Deprecated rxPermission",
                "Deprecated rxPermission, replace with Permissions SDK.",
                BizCategory.BIZ_DEPRECATED,
                6, Severity.WARNING,
                Implementation(PermissionDetector::class.java, JAVA_FILE_SCOPE))

        private const val RXPERMISSION_CLASS = "com.tbruyelle.rxpermissions3.RxPermissions"
    }

    override fun getApplicableConstructorTypes(): List<String>? {
        return listOf(RXPERMISSION_CLASS)
    }

    override fun visitConstructor(context: JavaContext, node: UCallExpression, constructor: PsiMethod) {
        if (context.evaluator.isMemberInClass(constructor, RXPERMISSION_CLASS)) {
            context.report(ISSUE, context.getLocation(node), "避免直接调用 ${RXPERMISSION_CLASS}, 使用同一的权限管理类")
        }
    }


}