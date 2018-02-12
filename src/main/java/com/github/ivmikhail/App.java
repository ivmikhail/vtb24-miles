package com.github.ivmikhail;

import com.github.ivmikhail.reward.MilesRewardRule;
import com.github.ivmikhail.reward.RewardResult;
import org.apache.commons.cli.*;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

public final class App {

    private static final Logger LOG = Logger.getLogger(App.class.getName());

    private static final String PROPERTIES_CLASSPATH = "/app.properties";
    private static final DateTimeFormatter DATEFORMAT_ARG = DateTimeFormatter.ofPattern("MMyyyy");

    private static final String OPT_STATEMENT_PATH = "s";
    private static final String OPT_MONTH = "m";
    private static final String OPT_PROPS_PATH = "p";
    private static final String OPT_HELP = "h";
    private static final String OPT_EXPORT_PATH = "export";

    private static final Options OPTS = createOptions();

    private App() {/* static class with Main method, no need to initialize */}

    public static void main(String[] args) throws IOException, java.text.ParseException {
        tryToMakeWorldBetter();

        CommandLine cli = null;
        try {
            cli = parse(args);
        } catch (org.apache.commons.cli.ParseException e) {
            printHelp(OPTS);
            System.exit(1);
        }

        Settings settings = get(cli);
        MilesRewardRule rule = new MilesRewardRule(settings);
        RewardResult reward = rule.process();

        if(cli.hasOption(OPT_EXPORT_PATH)) {
            ExportAs.csv(cli.getOptionValue(OPT_EXPORT_PATH), reward);
        } else {
            String txt = ExportAs.txt(reward);
            System.out.println(txt);
        }
    }

    private static CommandLine parse(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return parser.parse(OPTS, args);
    }

    private static Settings get(CommandLine cli) {
        String[] statementPaths = cli.getOptionValues(OPT_STATEMENT_PATH);
        String propertiesPath = cli.getOptionValue(OPT_PROPS_PATH);

        Properties properties;
        if (propertiesPath == null) {
            properties = loadPropertiesFromClasspath();
        } else {
            properties = loadProperties(propertiesPath);
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

        return settings;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar vtb24-miles.jar -s statement.csv", options);
    }

    private static Properties loadPropertiesFromClasspath() {
        Properties properties = new Properties();
        try (InputStream is = App.class.getResourceAsStream(PROPERTIES_CLASSPATH);
             InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"))
        ) {
            properties.load(isr);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return properties;
    }

    private static Properties loadProperties(String path) {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader(path)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        return properties;
    }

    private static Options createOptions() {
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

    private static void tryToMakeWorldBetter() {
        if (isRunningOnWindows()) {
            setOwnConsoleCodePage(65001);//unicode
        }
    }

    private static boolean isRunningOnWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private static void setOwnConsoleCodePage(int codePage) {
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "chcp", String.valueOf(codePage)).inheritIO();
        try {
            Process p = pb.start();
            int exitCode = p.waitFor();
            if (exitCode != 0) LOG.warning("Failed to change code page, exit code " + exitCode);
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}