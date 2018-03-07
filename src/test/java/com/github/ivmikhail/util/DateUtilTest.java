package com.github.ivmikhail.util;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertTrue;

public class DateUtilTest {

    @Test
    public void testCoalesceMax() {
        LocalDate feb07 = LocalDate.of(2018, Month.MARCH, 7);
        LocalDate feb08 = LocalDate.of(2018, Month.MARCH, 8);

        LocalDate date = DateUtil.coalesceMax(null, feb08, LocalDate.MAX, null, feb07);


        assertTrue(feb08.isEqual(date));
    }

    @Test
    public void testCoalesceMaxSpecifiedDates() {
        LocalDate feb07 = LocalDate.of(2018, Month.MARCH, 7);
        LocalDate feb08 = LocalDate.of(2018, Month.MARCH, 8);

        LocalDate date = DateUtil.coalesceMax(feb07, feb08);

        assertTrue(feb08.isEqual(date));
    }

    @Test
    public void testCoalesceMaxWithoutSpecificDate() {
        LocalDate date = DateUtil.coalesceMax(null, LocalDate.MAX, null);

        assertTrue(LocalDate.MAX.isEqual(date));
    }

    @Test
    public void testCoalesceMaxNulls() {
        LocalDate date = DateUtil.coalesceMax(null, null);

        assertTrue(LocalDate.MAX.isEqual(date));
    }

    @Test
    public void testCoalesceMin() {
        LocalDate feb07 = LocalDate.of(2018, Month.MARCH, 7);
        LocalDate feb08 = LocalDate.of(2018, Month.MARCH, 8);

        LocalDate date = DateUtil.coalesceMin(null, feb08, LocalDate.MAX, null, feb07);

        assertTrue(feb07.isEqual(date));
    }

    @Test
    public void testCoalesceMinWithoutSpecificDate() {
        LocalDate date = DateUtil.coalesceMin(null, LocalDate.MIN, null);

        assertTrue(LocalDate.MIN.isEqual(date));
    }

    @Test
    public void testCoalesceMinNulls() {
        LocalDate date = DateUtil.coalesceMin(null, null);

        assertTrue(LocalDate.MIN.isEqual(date));
    }
}
