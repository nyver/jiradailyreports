package com.nyver.jira.jiradailyreports.output;

import com.atlassian.jira.rest.client.domain.Issue;

/**
 * Description
 *
 * @author Yuri Novitsky
 */
public class Wiki implements OutputInterface {
    public void writeHeader(String str) {
        System.out.println("==" + str + "==");
    }

    public void writeIssue(Issue issue) {
        System.out.println(
                String.format(
                        "* [%s] %s (%s%s)",
                        issue.getKey(),
                        issue.getSummary(),
                        issue.getStatus().getName(),
                        (null != issue.getResolution()) ? ", " + issue.getResolution().getName() : ""
                )
        );
    }
}
