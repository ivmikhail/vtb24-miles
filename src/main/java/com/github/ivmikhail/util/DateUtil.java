package com.github.ivmikhail.util;

import java.time.LocalDate;

public final class DateUtil {
    private DateUtil() {/* util class */}

    /**
     * Return max date
     * <p>
     * NULL and LocalDate.MAX will be ignored
     * <p>
     * In case of all NULLs and/or LocalDate.MAX, LocalDate.MAX will be returned
     */
    public static LocalDate maxOf(LocalDate... dates) {
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
     * Return min date
     * <p>
     * NULL and LocalDate.MAX will be ignored
     * <p>
     * In case of all NULLs and/or LocalDate.MIN, LocalDate.MIN will be returned
     */
    public static LocalDate minOf(LocalDate... dates) {
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
