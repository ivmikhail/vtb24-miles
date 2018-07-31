package com.github.ivmikhail.vtb24.miles.app;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.*;

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

    @Test
    public void testUserProperties() throws IOException {
        File f = File.createTempFile("user.properties.", ".tmp");

        String args = "-s 1.txt -p " + f.getAbsolutePath();
        LaunchOptions opts = new LaunchOptions(args.split(" "));
        Settings s = opts.createSettings();

        assertEquals(0, s.getProperties().size());
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

    @Test
    public void testHelpPrint() {
        LaunchOptions opts = new LaunchOptions(new String[]{});
        opts.setExecutableName("MyFunnyApp");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));//change out

        opts.printHelp();

        System.setOut(System.out);//restore out

        assertTrue(outContent.toString().startsWith("usage: java -jar MyFunnyApp"));
    }
}