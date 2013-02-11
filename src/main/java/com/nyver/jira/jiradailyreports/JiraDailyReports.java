package com.nyver.jira.jiradailyreports;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.RestClientException;
import com.atlassian.jira.rest.client.internal.jersey.JerseyJiraRestClientFactory;
import com.nyver.jira.jiradailyreports.output.Console;
import com.nyver.jira.jiradailyreports.output.OutputInterface;
import com.nyver.jira.jiradailyreports.output.Wiki;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

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
    private static String OPTION_NAME_OUTPUT    = "output";

    private static String OUTPUT_CONSOLE = "console";
    private static String OUTPUT_WIKI    = "wiki";

    public static void main(String[] args)
    {
        String login;
        CommandLine line;
        Options options;

        System.out.println("Jira Daily Reports " + VERSION);

        options = getOptions();

        try {
            line = parseArguments(args, options);

            validateOptions(line);

            login = line.getOptionValue(OPTION_NAME_LOGIN);

            JiraDailyReportBuilder builder = new JiraDailyReportBuilder(
                    getJiraRestClient(
                            line.getOptionValue(OPTION_NAME_URL),
                            login,
                            line.getOptionValue(OPTION_NAME_PASSWORD)
                    ),
                    getOutput(line)
            );
            builder.setUser(login);
            builder.build();
        } catch (ParseException e) {
            System.out.println("Parsing of command line arguments failed: " + e.getMessage());
        } catch (URISyntaxException e) {
            System.out.println("URL syntax exception: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid argument: " + e.getMessage());

            // Automatically generate the help statement
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("jiradailyreports", options);
        } catch (RestClientException e) {
            System.out.println("Jira Rest client exception: " + e.getMessage());
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
        options.addOption(
                OptionBuilder.withArgName("OUTPUT")
                        .hasOptionalArg()
                        .withDescription("Output: console (default), wiki")
                        .create(OPTION_NAME_OUTPUT)
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
    private static boolean validateOptions(CommandLine line)
    {
        if (!line.hasOption(OPTION_NAME_URL)) {
            throw new IllegalArgumentException(String.format("Parameter $s is not exists", OPTION_NAME_URL));
        }
        if (!line.hasOption(OPTION_NAME_LOGIN)) {
            throw new IllegalArgumentException(String.format("Parameter $s is not exists", OPTION_NAME_LOGIN));
        }
        if (!line.hasOption(OPTION_NAME_PASSWORD)) {
            throw new IllegalArgumentException(String.format("Parameter $s is not exists", OPTION_NAME_PASSWORD));
        }

        String url = line.getOptionValue(OPTION_NAME_URL);
        String login = line.getOptionValue(OPTION_NAME_LOGIN);
        String password = line.getOptionValue(OPTION_NAME_PASSWORD);

        if (url.isEmpty()) {
            throw new IllegalArgumentException("Url can not be empty");
        }

        // Check for availability url
        final URLConnection connection;

        try {
            connection = new URL(url).openConnection();
            connection.connect();
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("Can not connect to url \"%s\"", url));
        }

        if (login.isEmpty()) {
            throw new IllegalArgumentException("Login can not be empty");
        }

        if (password.isEmpty()) {
            throw new IllegalArgumentException("Password can not be empty");
        }

        return true;
    }

    /**
     * Get output
     * @param line
     * @return
     */
    public static OutputInterface getOutput(CommandLine line)
    {
        OutputInterface output = new Console();

        if (line.hasOption(OPTION_NAME_OUTPUT)) {
            String outputFormat = line.getOptionValue(OPTION_NAME_OUTPUT);
            if (outputFormat.toLowerCase().equals(OUTPUT_WIKI)) {
                output = new Wiki();
            }
        }

        return output;
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
