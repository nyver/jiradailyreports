package com.nyver.jira;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import org.apache.commons.cli.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Main class
 * @author Yuri Novitsky
 */
public class JiraDailyReports
{
    private static String VERSION = "1.0";

    private static String OPTION_NAME_URL      = "url";
    private static String OPTION_NAME_LOGIN    = "login";
    private static String OPTION_NAME_PASSWORD = "password";

    public static void main(String[] args)
    {
        String login;
        CommandLine line;
        Options options;

        System.out.println("Jira Daily Reports " + VERSION);

        try {
            options = getOptions();
            line = parseArguments(args, options);

            login = line.getOptionValue(OPTION_NAME_LOGIN);

            if (isOptionsValid(line)) {
                JiraDailyReportBuilder builder = new JiraDailyReportBuilder(
                        getJiraRestClient(
                                line.getOptionValue(OPTION_NAME_URL),
                                login,
                                line.getOptionValue(OPTION_NAME_PASSWORD)
                        ),
                        login
                );
                builder.build();
            } else {
                // Automatically generate the help statement
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("jiradailyreports", options);
            }

        } catch (ParseException e) {
            System.out.println("Parsing of command line arguments failed: " + e.getMessage());
        } catch (URISyntaxException e) {
            System.out.println("URL syntax exception: " + e.getMessage());
        }
    }

    /**
     * Get command line options
     * @return command line options
     */
    private static Options getOptions()
    {
        Options options = new Options();

        options.addOption(
                OptionBuilder.withArgName("URL")
                        .hasArg()
                        .withDescription("Jira server url")
                        .create(OPTION_NAME_URL)
        );
        options.addOption(
                OptionBuilder.withArgName("LOGIN")
                        .hasArg()
                        .withDescription("Jira user login")
                        .create(OPTION_NAME_LOGIN)
        );
        options.addOption(
                OptionBuilder.withArgName("PASSWORD")
                        .hasArg()
                        .withDescription("Jira user password")
                        .create(OPTION_NAME_PASSWORD)
        );

        return options;
    }

    /**
     * Parsing command line arguments
     * @param args command line arguments
     */
    private static CommandLine parseArguments(String[] args, Options options) throws ParseException
    {
        CommandLineParser parser = new PosixParser();
        CommandLine line = parser.parse(options, args);

        return line;
    }

    /**
     * Validate command line options
     * @param line
     * @return true if options is valid
     */
    private static boolean isOptionsValid(CommandLine line)
    {
        if (!line.hasOption(OPTION_NAME_URL)
                || !line.hasOption(OPTION_NAME_LOGIN)
                || !line.hasOption(OPTION_NAME_PASSWORD)) {
            return false;
        }

        return true;
    }

    /**
     * Get Jira Rest API Client
     * @param url
     * @param login
     * @param password
     * @return
     * @throws URISyntaxException
     */
    private static JiraRestClient getJiraRestClient(String url, String login, String password) throws URISyntaxException {
        final JerseyJiraRestClientFactory factory = new JerseyJiraRestClientFactory();
        return factory.createWithBasicHttpAuthentication(new URI(url), login, password);
    }
}
