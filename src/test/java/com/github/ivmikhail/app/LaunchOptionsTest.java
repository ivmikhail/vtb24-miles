package com.github.ivmikhail.app;

import com.github.ivmikhail.app.LaunchOptions;
import com.github.ivmikhail.app.Settings;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LaunchOptionsTest {

    @Test
    public void testPathToStatements() {
        LaunchOptions opts = new LaunchOptions("-s statement1.csv -s statement2.csv".split(" "));
        Settings s = opts.createSettings();

        assertEquals("statement1.csv", s.getPathsToStatement()[0]);
        assertEquals("statement2.csv", s.getPathsToStatement()[1]);
    }

    @Test
    public void testPeriod() {
        LaunchOptions opts = new LaunchOptions("-s 1.txt -m 032018".split(" "));
        Settings s = opts.createSettings();

        assertEquals(LocalDate.of(2018, Month.MARCH, 1), s.getMinDate());
        assertEquals(LocalDate.of(2018, Month.MARCH, 31), s.getMaxDate());
    }

    @Test
    public void helpFlagShouldBeTrue() {
        LaunchOptions opts = new LaunchOptions("-s 1.txt -h".split(" "));
        Settings s = opts.createSettings();
        assertTrue(s.isPrintHelpAndExit());
    }

    @Test
    public void helpFlagShouldBeFalse() {
        LaunchOptions opts = new LaunchOptions("-s 1.txt".split(" "));
        Settings s = opts.createSettings();
        assertFalse(s.isPrintHelpAndExit());
    }

    @Test
    public void testExportPath() {
        LaunchOptions opts = new LaunchOptions("-s 1.txt -export export.txt".split(" "));
        Settings s = opts.createSettings();

        assertEquals("export.txt", s.getExportPath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongArguments() {
        LaunchOptions opts = new LaunchOptions(new String[]{"-h f g"});
        opts.createSettings();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithoutRequiredArgument() {
        LaunchOptions opts = new LaunchOptions("no minuss arg".split(" "));
        opts.createSettings();
    }
}