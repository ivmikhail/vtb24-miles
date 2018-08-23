package com.github.ivmikhail.vtb24.miles;

import org.junit.Test;

import static com.github.ivmikhail.vtb24.miles.Main.determineExecName;
import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void testDetermineExecName() {
        String name = determineExecName();
        assertEquals("vtb24-miles.jar", name);
    }
}
