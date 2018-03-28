package com.github.ivmikhail;

import com.github.ivmikhail.app.LaunchOptions;
import com.github.ivmikhail.app.Settings;
import com.github.ivmikhail.reward.Calculator;
import com.github.ivmikhail.reward.ExportAs;
import com.github.ivmikhail.reward.RewardSummary;
import com.github.ivmikhail.statement.CSVLoader;

import java.io.IOException;
import java.util.logging.Logger;

public final class Main {
    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private Main() {/* static class with Main method, no need to initialize */}

    public static void main(String[] args) {
        tryToMakeWorldBetter();

        Settings settings = getSettingsOrExit(new LaunchOptions(args));

        Calculator calculator = new Calculator(settings);
        RewardSummary reward = calculator.process(CSVLoader.load(settings));

        if (!settings.getExportPath().isEmpty()) {
            ExportAs.csv(reward, settings.getExportPath());
        }

        String txt = ExportAs.txt(reward);
        System.out.println(txt);
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
            if (exitCode != 0) LOG.warning("Failed to change code page, exit code " + exitCode);
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Settings getSettingsOrExit(LaunchOptions opts) {
        Settings settings = null;
        int exitCode = -1;
        try {
            settings = opts.createSettings();
        } catch (IllegalArgumentException e) {
            LOG.warning(e.getMessage());
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