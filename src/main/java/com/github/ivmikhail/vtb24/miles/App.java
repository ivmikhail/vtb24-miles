package com.github.ivmikhail.vtb24.miles;

import com.github.ivmikhail.vtb24.miles.app.LaunchOptions;
import com.github.ivmikhail.vtb24.miles.app.Settings;
import com.github.ivmikhail.vtb24.miles.reward.Calculator;
import com.github.ivmikhail.vtb24.miles.reward.ExportAs;
import com.github.ivmikhail.vtb24.miles.reward.RewardSummary;
import com.github.ivmikhail.vtb24.miles.statement.CSVLoader;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public final class App {
    private static final Logger LOG = LoggerFactory.getLogger(App.class);

    private static final String DEFAULT_JAR_NAME = "vtb24-miles.jar";

    private App() {/* static class with App method, no need to initialize */}

    public static void main(String... args) throws UnsupportedEncodingException {
        tryToMakeWorldBetter();

        Settings settings = getSettingsOrExit(args, determineExecName());
        Calculator calculator = new Calculator(settings);
        RewardSummary reward = calculator.process(CSVLoader.load(settings));

        if (!settings.getExportPath().isEmpty()) {
            ExportAs.csv(reward, settings.getExportPath());
        }

        String txt = ExportAs.txt(reward);
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8.name());
        out.println(txt);
    }

    private static void tryToMakeWorldBetter() {
        boolean isWin = System.getProperty("os.name").toLowerCase().contains("win");
        if (isWin) { //running on Windows
            setOwnConsoleCodePage("65001");//unicode
        }
    }

    private static void setOwnConsoleCodePage(String codePage) {
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "chcp", codePage).inheritIO();
        try {
            Process p = pb.start();
            int exitCode = p.waitFor();
            if (exitCode != 0) LOG.warn("Failed to change code page, exit code {}", exitCode);
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String determineExecName() {
        URI uri;
        try {
            uri = App.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }

        File f = new File(uri);
        if (f.isDirectory()) {
            //exec from sources
            return DEFAULT_JAR_NAME;
        } else {
            return f.getName();
        }
    }

    private static Settings getSettingsOrExit(String[] args, String executableName) {
        LaunchOptions opts = new LaunchOptions();
        opts.setArgs(args);
        opts.setExecutableName(executableName);

        Settings settings = null;
        try {
            settings = opts.createSettings();
        } catch (ParseException e) {
            LOG.warn(e.getMessage());
            opts.printHelp();

            System.exit(-1);
        }

        if (settings.isPrintHelpAndExit()) {
            opts.printHelp();
            System.exit(0);
        }

        return settings;
    }
}