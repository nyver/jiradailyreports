package com.nyver.jira.jiradailyreports.output;

/**
 * Console output adapter
 *
 * @author Yuri Novitsky
 */
public class Console implements com.nyver.jira.jiradailyreports.output.OutputInterface {

    @Override
    public void writeLn(String str) {
        System.out.println(str);
    }
}
