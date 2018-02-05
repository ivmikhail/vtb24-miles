package com.github.ivmikhail;

import java.io.File;
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

    public String[] getPathsToStatement() {
        return pathsToStatement;
    }

    public void setPathsToStatement(String[] pathsToStatement) {
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
