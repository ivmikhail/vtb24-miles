package com.github.ivmikhail.vtb24.miles;

import com.github.ivmikhail.vtb24.miles.app.LaunchOptions;
import com.github.ivmikhail.vtb24.miles.app.Settings;
import com.github.ivmikhail.vtb24.miles.reward.Calculator;
import com.github.ivmikhail.vtb24.miles.reward.ExportAs;
import com.github.ivmikhail.vtb24.miles.reward.RewardSummary;
import com.github.ivmikhail.vtb24.miles.statement.CSVLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public final class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final String DEFAULT_JAR_NAME = "vtb24-miles.jar";

    private Main() {/* static class with Main method, no need to initialize */}

    public static void main(String[] args) throws UnsupportedEncodingException, URISyntaxException {
        tryToMakeWorldBetter();

        LaunchOptions opts = new LaunchOptions(args);
        opts.setExecutableName(determineExecName());

        Settings settings = getSettingsOrExit(opts);

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
            setOwnConsoleCodePage(65001);//unicode
        }
    }

    private static void setOwnConsoleCodePage(int codePage) {
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "chcp", String.valueOf(codePage)).inheritIO();
        try {
            Process p = pb.start();
            int exitCode = p.waitFor();
            if (exitCode != 0) LOG.warn("Failed to change code page, exit code {}", exitCode);
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String determineExecName() throws URISyntaxException {
        URI uri = Main.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .toURI();

        File f = new File(uri);
        if (f.isDirectory()) {
            //exec from sources
            return DEFAULT_JAR_NAME;
        } else {
            return f.getName();
        }
    }

    private static Settings getSettingsOrExit(LaunchOptions opts) {
        Settings settings = null;
        int exitCode = -1;
        try {
            settings = opts.createSettings();
        } catch (IllegalArgumentException e) {
            LOG.warn("Failed to parse arguments", e);
            exitCode = 1;
        }
        if (settings != null && settings.isPrintHelpAndExit()) exitCode = 0;

        if (exitCode >= 0) {
            opts.printHelp();
            System.exit(exitCode);
        }
        return settings;
    }
}