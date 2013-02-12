package com.nyver.jira.jiradailyreports.output;

import com.atlassian.jira.rest.client.domain.Issue;

/**
 * Output adapter interface
 *
 * @author Yuri Novitsky
 */
public interface Output
{
    /**
     * Write header to output
     * @param str header
     */
    public void writeHeader(String str);

    /**
     * Write string to output
     * @param str
     */
    public void writeString(String str);

    /**
     * Write information about issue
     * @param issue current issue
     */
    public void writeIssue(Issue issue);
}
