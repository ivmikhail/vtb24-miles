package com.github.ivmikhail;

import com.github.ivmikhail.util.LoadProperties;
import org.apache.commons.cli.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class LaunchOptions {
    private static final String PROPERTIES_CLASSPATH = "/app.properties";
    private static final DateTimeFormatter DATEFORMAT_ARG = DateTimeFormatter.ofPattern("MMyyyy");

    private static final String OPT_STATEMENT_PATH = "s";
    private static final String OPT_MONTH = "m";
    private static final String OPT_PROPS_PATH = "p";
    private static final String OPT_HELP = "h";
    private static final String OPT_EXPORT_PATH = "export";

    private Options options;
    private String[] args;
    private CommandLine cli;

    public LaunchOptions(String[] args) {
        this.options = createOptions();
        this.args = args;
    }

    public Settings createSettings() {
        try {
            cli = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
        return createSettings(cli);
    }

    private Settings createSettings(CommandLine cli) {
        String[] statementPaths = cli.getOptionValues(OPT_STATEMENT_PATH);
        String userPropertiesPath = cli.getOptionValue(OPT_PROPS_PATH);

        Properties properties;
        if (userPropertiesPath == null) {
            properties = LoadProperties.fromClasspath(PROPERTIES_CLASSPATH);
        } else {
            properties = LoadProperties.fromFile(userPropertiesPath);
        }
        LocalDate minDate;
        LocalDate maxDate;
        String rawYearMonth = cli.getOptionValue(OPT_MONTH, null);
        if (rawYearMonth == null) {
            minDate = LocalDate.MIN;
            maxDate = LocalDate.MAX;
        } else {
            YearMonth yearMonth = YearMonth.parse(rawYearMonth, DATEFORMAT_ARG);
            minDate = yearMonth.atDay(1);
            maxDate = yearMonth.atEndOfMonth();
        }

        if (minDate.isAfter(maxDate)) {
            throw new IllegalArgumentException("<minDate> should be before <maxDate>");
        }

        Settings settings = new Settings();
        settings.setPathsToStatement(statementPaths);
        settings.setMinDate(minDate);
        settings.setMaxDate(maxDate);
        settings.setProperties(properties);
        settings.setPrintHelpAndExit(cli.hasOption(OPT_HELP));
        settings.setExportPath(cli.getOptionValue(OPT_EXPORT_PATH, ""));

        return settings;
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar vtb24-miles.jar -s statement.csv", options);
    }

    private Options createOptions() {
        Options options = new Options();

        options.addOption(Option
                .builder(OPT_STATEMENT_PATH)
                .desc("path to statement.csv downloaded from http://telebank.ru." +
                        " You can specify few paths like -s statement1.csv -s statement.csv. In this case transactions will be merged")
                .numberOfArgs(1)
                .required(true)
                .build());

        options.addOption(Option
                .builder(OPT_PROPS_PATH)
                .desc("path to .properties file")
                .numberOfArgs(1)
                .required(false)
                .build());

        options.addOption(Option
                .builder(OPT_MONTH)
                .desc("month, for example 062017. If specified, transactions will be processed only for this month. Otherwise - all transactions in statement.csv")
                .numberOfArgs(1)
                .required(false)
                .build());

        options.addOption(Option
                .builder(OPT_HELP)
                .desc("print this help message and exit")
                .required(false)
                .build());

        options.addOption(Option
                .builder(OPT_EXPORT_PATH)
                .desc("path to file, where result will be exported as CSV. For example, statement-miles.csv")
                .numberOfArgs(1)
                .required(false)
                .build());

        return options;
    }
}