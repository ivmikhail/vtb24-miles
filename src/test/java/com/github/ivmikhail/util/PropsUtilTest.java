package com.github.ivmikhail.util;

import org.junit.Test;

import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PropsUtilTest {

    @Test
    public void testGetPropertyAsSet() {
        Properties props = new Properties();
        props.setProperty("key1", "1 2 3");

        Set<String> values = PropsUtil.getAsSet(props, "key1", " ");
        assertEquals(3, values.size());
    }
}
