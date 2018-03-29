package com.github.ivmikhail.vtb24.miles.app;

import com.github.ivmikhail.vtb24.miles.reward.rule.RulesFactory;
import com.github.ivmikhail.vtb24.miles.reward.rule.RulesFactory.RuleId;
import org.apache.commons.cli.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Properties;

import static com.github.ivmikhail.vtb24.miles.util.LoadProperties.fromClasspath;
import static com.github.ivmikhail.vtb24.miles.util.LoadProperties.fromFile;

public class LaunchOptions {
    private static final String PROPERTIES_CLASSPATH = "/app.properties";
    private static final DateTimeFormatter DATEFORMAT_ARG = DateTimeFormatter.ofPattern("MMyyyy");

    private static final String STATEMENT_PATH = "s";
    private static final String MONTH = "m";
    private static final String PROPS_PATH = "p";
    private static final String HELP = "h";
    private static final String EXPORT_PATH = "export";
    private static final String RULE_ID = "rule";

    private Options options;
    private String[] args;

    public LaunchOptions(String[] args) {
        this.options = createOptions();
        this.args = args;
    }

    public Settings createSettings() {
        CommandLine cli;
        try {
            cli = new DefaultParser().parse(options, args);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
        return createSettings(cli);
    }

    private Settings createSettings(CommandLine cli) {
        String[] statementPaths = cli.getOptionValues(STATEMENT_PATH);
        String userPropertiesPath = cli.getOptionValue(PROPS_PATH);

        Properties properties;
        if (userPropertiesPath == null) {
            properties = fromClasspath(PROPERTIES_CLASSPATH);
        } else {
            properties = fromFile(userPropertiesPath);
        }
        LocalDate minDate;
        LocalDate maxDate;
        String rawYearMonth = cli.getOptionValue(MONTH, null);
        if (rawYearMonth == null) {
            minDate = LocalDate.MIN;
            maxDate = LocalDate.MAX;
        } else {
            YearMonth yearMonth = YearMonth.parse(rawYearMonth, DATEFORMAT_ARG);
            minDate = yearMonth.atDay(1);
            maxDate = yearMonth.atEndOfMonth();
        }

        String rule = cli.getOptionValue(RULE_ID, RuleId.KM_PLATINUM.name());

        Settings settings = new Settings();
        settings.setPathsToStatement(statementPaths);
        settings.setMinDate(minDate);
        settings.setMaxDate(maxDate);
        settings.setProperties(properties);
        settings.setPrintHelpAndExit(cli.hasOption(HELP));
        settings.setExportPath(cli.getOptionValue(EXPORT_PATH, ""));
        settings.setRuleId(RuleId.valueOf(rule));

        return settings;
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar vtb24-miles.jar -s statement.csv", options);
    }

    private Options createOptions() {
        Options options = new Options();

        options.addOption(Option
                .builder(STATEMENT_PATH)
                .desc("path to statement.csv downloaded from http://telebank.ru." +
                        " You can specify few paths like -s statement1.csv -s statement.csv. In this case transactions will be merged")
                .numberOfArgs(1)
                .required(true)
                .build());

        options.addOption(Option
                .builder(PROPS_PATH)
                .desc("path to .properties file")
                .numberOfArgs(1)
                .required(false)
                .build());

        options.addOption(Option
                .builder(MONTH)
                .desc("month, for example 062017. If specified, transactions will be processed only for this month. Otherwise - all transactions in statement.csv")
                .numberOfArgs(1)
                .required(false)
                .build());

        options.addOption(Option
                .builder(HELP)
                .desc("print this help message and exit")
                .required(false)
                .build());

        options.addOption(Option
                .builder(EXPORT_PATH)
                .desc("path to file, where result will be exported as CSV. For example, statement-miles.csv")
                .numberOfArgs(1)
                .required(false)
                .build());

        options.addOption(Option
                .builder(RULE_ID)
                .desc("calculate rule, by default - KM_PLATINUM. Available values: " + Arrays.toString(RulesFactory.RuleId.values()))
                .numberOfArgs(1)
                .required(false)
                .build());

        return options;
    }
}