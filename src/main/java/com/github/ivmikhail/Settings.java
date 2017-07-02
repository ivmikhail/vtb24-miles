package com.github.ivmikhail;

import java.io.File;
import java.time.LocalDate;
import java.util.Properties;

/**
 * Created by ivmikhail on 02/07/2017.
 */
public class Settings {
    private File statementFile;
    private LocalDate minDate;
    private LocalDate maxDate;
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public File getStatementFile() {
        return statementFile;
    }

    public void setStatementFile(File statementFile) {
        this.statementFile = statementFile;
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

    @Override
    public String toString() {
        return "Settings{" +
                "statementFile=" + statementFile +
                ", minDate=" + minDate +
                ", maxDate=" + maxDate +
                ", properties=" + properties +
                '}';
    }
}
