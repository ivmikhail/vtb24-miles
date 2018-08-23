package com.github.ivmikhail.vtb24.miles.app;

import com.github.ivmikhail.vtb24.miles.reward.rule.RulesFactory.RuleId;

import java.time.LocalDate;
import java.util.Properties;

/**
 * Created by ivmikhail on 02/07/2017.
 */
public class Settings {

    private String[] pathsToStatement;
    private LocalDate minDate;
    private LocalDate maxDate;
    private Properties properties;
    private boolean printHelpAndExit;
    private String exportPath;
    private RuleId ruleId;

    public Settings() {
        this.minDate = LocalDate.MIN;
        this.maxDate = LocalDate.MAX;
        this.properties = new Properties();
    }

    public RuleId getRuleId() {
        return ruleId;
    }

    public void setRuleId(RuleId ruleId) {
        this.ruleId = ruleId;
    }

    public boolean isPrintHelpAndExit() {
        return printHelpAndExit;
    }

    public void setPrintHelpAndExit(boolean printHelpAndExit) {
        this.printHelpAndExit = printHelpAndExit;
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath(String exportPath) {
        this.exportPath = exportPath;
    }

    public String[] getPathsToStatement() {
        return pathsToStatement;
    }

    public void setPathsToStatement(String... pathsToStatement) {
        this.pathsToStatement = pathsToStatement;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }
}