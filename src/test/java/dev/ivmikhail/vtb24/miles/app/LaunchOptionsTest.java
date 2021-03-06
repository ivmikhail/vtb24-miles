package dev.ivmikhail.vtb24.miles.app;

import org.apache.commons.cli.ParseException;
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
    public void testPathToStatements() throws ParseException {
        LaunchOptions opts = new LaunchOptions();
        opts.setArgs("-s", "statement1.csv", "-s", "statement2.csv");
        Settings s = opts.createSettings();

        assertEquals("statement1.csv", s.getPathsToStatement()[0]);
        assertEquals("statement2.csv", s.getPathsToStatement()[1]);
    }

    @Test
    public void testPeriod() throws ParseException {
        LaunchOptions opts = new LaunchOptions();
        opts.setArgs("-s", "1.txt", "-m", "032018");
        Settings s = opts.createSettings();

        assertEquals(LocalDate.of(2018, Month.MARCH, 1), s.getMinDate());
        assertEquals(LocalDate.of(2018, Month.MARCH, 31), s.getMaxDate());
    }

    @Test
    public void helpFlagShouldBeTrue() throws ParseException {
        LaunchOptions opts = new LaunchOptions();
        opts.setArgs("-s", "1.txt", "-h");
        Settings s = opts.createSettings();
        assertTrue(s.isPrintHelpAndExit());
    }

    @Test
    public void helpFlagShouldBeFalse() throws ParseException {
        LaunchOptions opts = new LaunchOptions();
        opts.setArgs("-s", "1.txt");
        Settings s = opts.createSettings();
        assertFalse(s.isPrintHelpAndExit());
    }

    @Test
    public void testExportPath() throws ParseException {
        LaunchOptions opts = new LaunchOptions();
        opts.setArgs("-s", "1.txt", "-export", "export.txt");
        Settings s = opts.createSettings();

        assertEquals("export.txt", s.getExportPath());
    }

    @Test
    public void testUserProperties() throws IOException, ParseException {
        File f = File.createTempFile("user.properties.", ".tmp");

        LaunchOptions opts = new LaunchOptions();
        opts.setArgs("-s", "1.txt", "-p", f.getAbsolutePath());
        Settings s = opts.createSettings();

        assertEquals(0, s.getProperties().size());
    }

    @Test
    public void testHelpPrint() {
        LaunchOptions opts = new LaunchOptions();
        opts.setExecutableName("MyFunnyApp");

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));//change out

        opts.printHelp();

        System.setOut(System.out);//restore out

        assertTrue(outContent.toString().startsWith("usage: java -jar MyFunnyApp"));
    }
}