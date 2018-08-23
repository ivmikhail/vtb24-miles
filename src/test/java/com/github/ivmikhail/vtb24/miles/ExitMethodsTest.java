package com.github.ivmikhail.vtb24.miles;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.Permission;

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

    @Test(expected = SecurityException.class)
    public void testPrintHelpAndExit() throws UnsupportedEncodingException {
        App.main("-h");
    }

    @Test(expected = SecurityException.class)
    public void testExitInCaseOfWrongArgs() throws UnsupportedEncodingException {
        App.main("-ddsf");
    }
}