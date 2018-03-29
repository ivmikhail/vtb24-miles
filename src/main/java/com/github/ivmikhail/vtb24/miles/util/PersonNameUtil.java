package com.github.ivmikhail.vtb24.miles.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PersonNameUtil {
    private static final Pattern FIO_PATTERN = Pattern.compile("^[А-Я]{1}\\*+[а-я]{1} [А-Я]{1}[а-я]+ [А-Я]{1}[а-я]+$"); //М*******й Евгений Юрьевич

    private PersonNameUtil() { /* helper class*/}

    /**
     * Return true is value something like that М*******й Евгений Юрьевич
     *
     * Otherwise return false
     */
    public static boolean isMaskedPersonName(String value) {
        Matcher m = FIO_PATTERN.matcher(value);
        return m.matches();
    }
}
