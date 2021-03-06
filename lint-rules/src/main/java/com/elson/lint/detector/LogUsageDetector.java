package com.elson.lint.detector;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiMethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;

import java.util.Arrays;
import java.util.List;

/**
 * @author elson
 * @date 2020/11/28
 * @Desc
 */
public class LogUsageDetector extends Detector implements Detector.UastScanner {

    public static final Issue ISSUE = Issue.create("LogUsage",
            "避免调用 android.util.Log",
            "请勿直接调用 android.util.Log, 应该使用统一工具类.",
            Category.SECURITY,
            6,
            Severity.WARNING,
            new Implementation(LogUsageDetector.class, Scope.JAVA_FILE_SCOPE));

    @Nullable
    @Override
    public List<String> getApplicableMethodNames() {
        return Arrays.asList("v", "d", "i", "w", "e", "wtf");
    }

    @Override
    public void visitMethodCall(@NotNull JavaContext context, @NotNull UCallExpression node, @NotNull PsiMethod method) {
        if (context.getEvaluator().isMemberInClass(method, "android.util.Log")) {
            context.report(ISSUE, context.getLocation(node), "避免调用 android.util.Log");
        }
    }

}
