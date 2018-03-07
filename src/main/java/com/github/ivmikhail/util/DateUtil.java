package com.github.ivmikhail.util;

import java.time.LocalDate;

public final class DateUtil {
    private DateUtil() {/* util class */}

    /**
     * Return MAX date
     * <p>
     * NULL will be ignored
     * <p>
     * In case of all nulls, LocalDate.MAX will be returned
     */
    public static LocalDate coalesceMax(LocalDate... dates) {
        LocalDate max = null;

        for (LocalDate d : dates) {
            if (d == null || d.isEqual(LocalDate.MAX)) continue; //skip null or max date

            if (max == null) {
                max = d;
            } else if (max != null && d.isAfter(max)) {
                max = d;
            }
        }

        return max == null ? LocalDate.MAX : max;
    }

    /**
     * Return MIN date
     * <p>
     * NULL will be ignored
     * <p>
     * In case of all nulls, LocalDate.MIN will be returned
     */
    public static LocalDate coalesceMin(LocalDate... dates) {
        LocalDate min = null;

        for (LocalDate d : dates) {
            if (d == null || d.isEqual(LocalDate.MIN)) continue; //skip null or min date

            if (min == null) {
                min = d;
            } else if (min != null && d.isBefore(min)) {
                min = d;
            }
        }

        return min == null ? LocalDate.MIN : min;
    }
}
