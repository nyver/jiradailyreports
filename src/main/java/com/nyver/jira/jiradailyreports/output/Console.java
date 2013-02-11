package com.nyver.jira.jiradailyreports.output;

import com.atlassian.jira.rest.client.domain.Issue;

/**
 * Console output adapter
 *
 * @author Yuri Novitsky
 */
public class Console implements OutputInterface {

    public void writeHeader(String str) {
        System.out.println(str);
    }

    public void writeString(String str) {
        System.out.println(str);
    }

    public void writeIssue(Issue issue) {
        System.out.println(
                String.format(
                        "[%s] %s (%s%s)",
                        issue.getKey(),
                        issue.getSummary(),
                        issue.getStatus().getName(),
                        (null != issue.getResolution()) ? ", " + issue.getResolution().getName() : ""
                )
        );
    }
}
