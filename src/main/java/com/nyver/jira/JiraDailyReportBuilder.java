package com.nyver.jira;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.NullProgressMonitor;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.SearchResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Builder for Jira's daily report
 *
 * @author Yuri Novitsky
 */
public class JiraDailyReportBuilder
{
    private JiraRestClient client;
    private String user;

    public JiraDailyReportBuilder(JiraRestClient client, String user)
    {
        this.client = client;
        this.user   = user;
    }

    /**
     * Get yesterday date
     * @return yesterday date
     */
    protected Date getYesterdayDate()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * Build report
     */
    public void build()
    {
        final NullProgressMonitor pm = new NullProgressMonitor();


        // What was done
        System.out.println("What was done:");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        executeJql(
                String.format(
                        "updatedDate < \"%s\" AND updatedDate > \"%s\" AND assignee CHANGED FROM \"%s\" AND assignee != \"%s\" ORDER BY priority DESC",
                        dateFormat.format(new Date()),
                        dateFormat.format(getYesterdayDate()),
                        user,
                        user
                ),
                pm
        );

        // What's next
        System.out.println("What's next:");

        executeJql(
                String.format("assignee in (\"%s\") AND status in (Open, \"In Progress\", Reopened) ORDER BY priority DESC", user),
                pm
        );

    }

    /**
     * Execute JQL and output results
     * @param jql
     * @param pm
     */
    protected void executeJql(String jql, final NullProgressMonitor pm)
    {
        SearchResult result = client.getSearchClient().searchJql(
                jql,
                pm
        );

        for(BasicIssue basicIssue: result.getIssues()) {
            Issue issue = client.getIssueClient().getIssue(basicIssue.getKey(), pm);
            System.out.println(
                    String.format("[%s] %s (%s)", issue.getKey(), issue.getSummary(), issue.getStatus().getName())
            );
        }
    }

}
