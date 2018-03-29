package com.github.ivmikhail.vtb24.miles.util;

import org.junit.Test;

import static com.github.ivmikhail.vtb24.miles.util.PersonNameUtil.isMaskedPersonName;
import static org.junit.Assert.*;

public class PersonNameUtilTest {

    @Test
    public void testMaskedName() {
        assertTrue(isMaskedPersonName("М*******й Евгений Юрьевич"));
    }

    @Test
    public void testNotMasked() {
        assertFalse(isMaskedPersonName("Иванов Иван Иванович"));
    }

    @Test
    public void testMaskedNameLowercase() {
        assertFalse(isMaskedPersonName("и**в иван иванович"));
    }
}