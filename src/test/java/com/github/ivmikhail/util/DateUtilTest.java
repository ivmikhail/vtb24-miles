package com.github.ivmikhail.util;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertTrue;

public class DateUtilTest {

    @Test
    public void testMaxOfWithMixedDates() {
        LocalDate feb07 = LocalDate.of(2018, Month.MARCH, 7);
        LocalDate feb08 = LocalDate.of(2018, Month.MARCH, 8);

        LocalDate date = DateUtil.maxOf(null, feb08, LocalDate.MAX, null, feb07);


        assertTrue(feb08.isEqual(date));
    }

    @Test
    public void testMaxOfWithSpecifiedDates() {
        LocalDate feb07 = LocalDate.of(2018, Month.MARCH, 7);
        LocalDate feb08 = LocalDate.of(2018, Month.MARCH, 8);

        LocalDate date = DateUtil.maxOf(feb07, feb08);

        assertTrue(feb08.isEqual(date));
    }

    @Test
    public void testMaxOfWithoutSpecificDate() {
        LocalDate date = DateUtil.maxOf(null, LocalDate.MAX, null);

        assertTrue(LocalDate.MAX.isEqual(date));
    }

    @Test
    public void testMaxOfWithNulls() {
        LocalDate date = DateUtil.maxOf(null, null);

        assertTrue(LocalDate.MAX.isEqual(date));
    }

    @Test
    public void testMinOfWithMixedDates() {
        LocalDate feb07 = LocalDate.of(2018, Month.MARCH, 7);
        LocalDate feb08 = LocalDate.of(2018, Month.MARCH, 8);

        LocalDate date = DateUtil.minOf(null, feb08, LocalDate.MAX, null, feb07);

        assertTrue(feb07.isEqual(date));
    }

    @Test
    public void testMinOfWithoutSpecificDate() {
        LocalDate date = DateUtil.minOf(null, LocalDate.MIN, null);

        assertTrue(LocalDate.MIN.isEqual(date));
    }

    @Test
    public void testMinOfWithNulls() {
        LocalDate date = DateUtil.minOf(null, null);

        assertTrue(LocalDate.MIN.isEqual(date));
    }
}