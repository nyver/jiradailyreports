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
java -jar jar_file -url jira_url -login jira_user_login -password jira_user_password [-user_jira_user_for_which_the_report] [-output console|wiki]