jiradailyreports
================

Jira Daily Reports

Tool for creating developer's daily reports from Jira.

BUILD
=================
mvn assembly:assembly or mvn package

use only Maven 2, not Maven 3

USAGE
=================
java -jar <jar_file> -url <jira url> -login <jira user login> -password <jira user password> [-user <jira user for which the report>] [-output <console|wiki>]