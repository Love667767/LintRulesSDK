package com.elson.lint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.ApiKt;
import com.android.tools.lint.detector.api.Issue;
import com.elson.lint.detector.LogUsageDetector;
import com.elson.lint.detector.NamingConventionDetector;
import com.elson.lint.detector.PngUsageDetector;
import com.elson.lint.detector.PermissionDetector;
import com.elson.lint.detector.ThreadUsageDetector;

import java.util.Arrays;
import java.util.List;

/**
 * @author elson
 * @date 2020/11/28
 * @Desc
 */
public class CustomIssueRegistry extends IssueRegistry {

    @Override
    public int getApi() {
        return ApiKt.CURRENT_API;
    }

    @Override
    public int getMinApi() {
        return 1;
    }

    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(
                LogUsageDetector.ISSUE,
                NamingConventionDetector.ISSUE,
                PngUsageDetector.ISSUE_PNG_IN_CODE,
                PngUsageDetector.ISSUE_PNG_IN_XML,
                PermissionDetector.ISSUE,
                ThreadUsageDetector.ISSUE
        );
    }
}
