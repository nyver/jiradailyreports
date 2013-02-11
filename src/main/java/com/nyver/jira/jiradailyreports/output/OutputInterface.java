package com.nyver.jira.jiradailyreports.output;

import com.atlassian.jira.rest.client.domain.Issue;

/**
 * Output adapter interface
 *
 * @author Yuri Novitsky
 */
public interface OutputInterface
{
    /**
     * Write header to output
     * @param str header
     */
    public void writeHeader(String str);

    /**
     * Write information about issue
     * @param issue current issue
     */
    public void writeIssue(Issue issue);
}
