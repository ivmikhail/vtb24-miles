package com.github.ivmikhail;

import com.github.ivmikhail.fx.vtb.VTBFxProvider;
import com.github.ivmikhail.reward.MilesRewardRule;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.cli.*;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class App {

    private static final String PROPERTIES_CLASSPATH = "/app.properties";
    private static final DateTimeFormatter DATEFORMAT_ARG = DateTimeFormatter.ofPattern("MMyyyy");

    private static final String OPT_STATEMENT_PATH = "s";
    private static final String OPT_MONTH = "m";
    private static final String OPT_PROPS_PATH = "p";
    private static final String OPT_HELP = "h";
    private static final Options OPTS = createOptions();

    private static final String TEMPLATES_CLASSPATH_DIR = "/templates";
    private static final String TEMPLATE_REWARD_RESULT = "rewardResult.ftl";

    private App() {/* static class with Main method, no need to initialize */}

    public static void main(String[] args) throws IOException, java.text.ParseException, TemplateException {

        Settings settings = null;
        try {
            settings = parse(args);
        } catch (org.apache.commons.cli.ParseException e) {
            printHelp(OPTS);
            System.exit(1);
        }

        List<Transaction> transactions = CSVLoader.load(settings);
        MilesRewardRule rule = new MilesRewardRule(settings.getProperties());
        rule.setFxProvider(new VTBFxProvider(settings.getProperties()));

        Map model = new HashMap();
        model.put("reward", rule.process(transactions));
        model.put("settings", settings);

        Template template = createTemplateEngine().getTemplate(TEMPLATE_REWARD_RESULT);
        Writer out = new OutputStreamWriter(System.out);
        template.process(model, out);
    }

    private static Configuration createTemplateEngine() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
        cfg.setClassForTemplateLoading(App.class, TEMPLATES_CLASSPATH_DIR);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setAPIBuiltinEnabled(true);
        return cfg;
    }

    private static Settings parse(String[] args) throws ParseException, IOException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cli = parser.parse(OPTS, args);

        String[] paths = cli.getOptionValues(OPT_STATEMENT_PATH);
        String propertiesPath = cli.getOptionValue(OPT_PROPS_PATH, null);
        Properties properties = loadProperties(propertiesPath);
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
        settings.setPathsToStatement(paths);
        settings.setMinDate(minDate);
        settings.setMaxDate(maxDate);
        settings.setProperties(properties);

        return settings;
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar vtb24-miles.jar -s statement.csv", options);
    }

    private static Properties loadProperties(String path) throws IOException {
        //load default properties from classpath
        Properties defaults = new Properties();
        try (InputStream is = App.class.getResourceAsStream(PROPERTIES_CLASSPATH);
             InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"))
        ) {
            defaults.load(isr);
        }

        if (path == null || path.isEmpty()) return defaults;

        Properties properties = new Properties(defaults);

        //load custom properties from given path and merge it tot default properties
        try (FileReader reader = new FileReader(path)) {
            properties.load(reader);
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

        return options;
    }
}