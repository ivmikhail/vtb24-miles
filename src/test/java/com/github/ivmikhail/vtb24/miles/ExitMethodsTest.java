package com.github.ivmikhail.vtb24.miles;

import com.github.ivmikhail.vtb24.miles.app.Settings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.Permission;

import static org.junit.Assert.assertNotNull;

public class ExitMethodsTest {
    private SecurityManager systemSecurityManager;

    @Before
    public void setUp() {
        systemSecurityManager = System.getSecurityManager();

        SecurityManager nonExitSecurityManager = new SecurityManager() {
            @Override
            public void checkPermission(Permission permission) {
                if (permission.getName().startsWith("exitVM")) {
                    //exitVM.{exitCode}
                    throw new SecurityException("System exit not allowed");
                }
            }

            @Override
            public void checkPermission(Permission perm, Object context) {
                //allow anything, to shut up log4j2 initialization error/warning in test
            }
        };
        System.setSecurityManager(nonExitSecurityManager);
    }

    @After
    public void tearDown() {
        System.setSecurityManager(systemSecurityManager);
    }

    @Test
    public void testGetSettings() {
        Settings s = Main.getSettingsOrExit("-s statement.csv -m 062018".split(" "), "");
        assertNotNull(s);
    }

    @Test(expected = SecurityException.class)
    public void testPrintHelp() {
        Main.getSettingsOrExit("-h".split(" "), "");
    }

    @Test(expected = SecurityException.class)
    public void testExitInCaseOfWrongArgs() {
        Main.getSettingsOrExit("-ddsf".split(" "), "");
    }
}