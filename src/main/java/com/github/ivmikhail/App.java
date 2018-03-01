package com.github.ivmikhail;

import com.github.ivmikhail.reward.ExportAs;
import com.github.ivmikhail.reward.MilesRewardRule;
import com.github.ivmikhail.reward.RewardResult;

import java.io.*;
import java.util.logging.Logger;

public final class App {
    private static final Logger LOG = Logger.getLogger(App.class.getName());

    private App() {/* static class with Main method, no need to initialize */}

    public static void main(String[] args) throws IOException {
        tryToMakeWorldBetter();

        Settings settings = tryGetSettings(new LaunchOptions(args));

        MilesRewardRule rule = new MilesRewardRule(settings);
        RewardResult reward = rule.process();

        if (settings.getExportPath().isEmpty()) {
            String txt = ExportAs.txt(reward);
            System.out.println(txt);
        } else {
            ExportAs.csv(settings.getExportPath(), reward);
        }
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

    private static Settings tryGetSettings(LaunchOptions opts) {
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