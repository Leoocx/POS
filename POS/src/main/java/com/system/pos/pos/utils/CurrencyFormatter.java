package com.system.pos.pos.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utilitário para formatação de valores monetários
 */
public class CurrencyFormatter {
    private static final NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public static String format(BigDecimal value) {
        if (value == null) return format.format(0);
        return format.format(value.doubleValue());
    }

    public BigDecimal parse(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return BigDecimal.ZERO;
            }
            String cleanValue = value.replaceAll("[^\\d,.]", "")
                    .replace(".", "")
                    .replace(",", ".");
            return new BigDecimal(cleanValue);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}