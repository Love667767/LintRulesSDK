package com.elson.lint.detector

import com.android.SdkConstants.ATTR_SRC
import com.android.resources.ResourceFolderType
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UElement
import org.jetbrains.uast.util.isMethodCall
import org.w3c.dom.Attr
import java.io.File


/**
 * @author elson
 * @date 2020/12/16
 * @Desc PNG文件扫描
 */
class PngUsageDetector : Detector(), XmlScanner, Detector.UastScanner {

    companion object {
        private const val LINT_EXPLANATION = "WebP can provide better compression than PNG. "
        private const val LINT_MSG = "Please use webp instead of png."

        private const val CLASS_IMAGE_VIEW = "android.widget.ImageView"

        @JvmField
        internal val ISSUE_PNG_IN_XML = Issue.create(
                "PngUseInXml",
                "Png Usage",
                LINT_EXPLANATION,
                Category.CORRECTNESS,
                5,
                Severity.WARNING,
                Implementation(PngUsageDetector::class.java, Scope.ALL_RESOURCES_SCOPE)
        )

        @JvmField
        internal val ISSUE_PNG_IN_CODE: Issue = Issue.create(
                "PngUseInCode",
                "Png Usage",
                LINT_EXPLANATION,
                Category.CORRECTNESS,
                5,
                Severity.WARNING,
                Implementation(PngUsageDetector::class.java, Scope.JAVA_FILE_SCOPE)
        )
    }

    override fun getApplicableAttributes(): Collection<String>? {
        return listOf(ATTR_SRC)
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        val srcValue: String = attribute.getValue()
        if (check(srcValue.substring(10), context.mainProject.resourceFolders)) {
            context.report(ISSUE_PNG_IN_XML, attribute, context.getLocation(attribute), LINT_MSG
            )
        }
    }

    override fun getApplicableUastTypes(): List<Class<out UElement>>? {
        return listOf(UCallExpression::class.java)
    }

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        return object : UElementHandler() {
            override fun visitCallExpression(node: UCallExpression) {
                if (!node.isMethodCall()
                        || "setImageResource" != node.methodName
                        || node.valueArgumentCount != 1) {
                    return
                }
                val psiMethod = node.resolve()
                val b = context.evaluator.isMemberInClass(psiMethod, CLASS_IMAGE_VIEW)
                if (b) {
                    if (check(node.valueArguments[0].toString().substring(11), context.mainProject.resourceFolders)) {
                        context.report(ISSUE_PNG_IN_CODE, node, context.getLocation(node), LINT_MSG)
                    }
                }
            }
        }
    }

    private fun check(imageName: String, resFolders: List<File>): Boolean {
        var resFolder: File? = null
        for (file in resFolders) {
            if ("res" == file.getName()) {
                resFolder = File(file.getPath())
                break
            }
        }
        if (resFolder == null) {
            return false
        }
        val drawableFolder = File(resFolder.getPath(), "drawable")
        if (!drawableFolder.exists() && !drawableFolder.isDirectory()) {
            return false
        }
        // TODO: 2019/7/8 还需处理 drawable 目录下还有目录的情况
        val filesName: Array<String> = drawableFolder.list()
        if (filesName == null || filesName.size == 0) {
            return false
        }
        for (fileName in filesName) {
            if (fileName.substring(0, fileName.indexOf(".")) == imageName) {
                if (fileName.endsWith(".png")) {
                    return true
                }
                break
            }
        }
        return false
    }

    override fun appliesTo(folderType: ResourceFolderType): Boolean {
        return folderType == ResourceFolderType.LAYOUT
    }

    override fun run(context: Context) {
        assert(false)
    }

}