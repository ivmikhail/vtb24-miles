package com.github.ivmikhail.util;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class LoadPropertiesTest {
    private Properties properties;

    @Before
    public void setUp(){
        properties = LoadProperties.fromClasspath("/util/app.properties");
    }

    @Test
    public void testLoadFromClasspath() {
        assertEquals(1, properties.size());
        assertEquals("значение1", properties.getProperty("property1"));
    }

    @Test
    public void testLoadFromFile() throws IOException {
        File temp = File.createTempFile("junit.test.app.properties.", ".tmp");
        properties.store(new FileWriter(temp), "");

        Properties p = LoadProperties.fromFile(temp.getAbsolutePath());
        assertEquals(1, p.size());
        assertEquals("значение1", p.getProperty("property1"));
    }
}
