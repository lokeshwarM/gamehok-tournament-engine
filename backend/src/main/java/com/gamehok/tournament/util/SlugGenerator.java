package com.gamehok.tournament.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utility class for generating URL-safe slugs from tournament names.
 */
public final class SlugGenerator {

    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern MULTIPLE_DASHES = Pattern.compile("-+");

    private SlugGenerator() {}

    /**
     * Generates a URL-safe slug from the given input string.
     *
     * @param input the raw string (e.g., tournament name)
     * @return lowercase, hyphenated slug suitable for URLs
     */
    public static String generate(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String slug = normalized
                .toLowerCase(Locale.ENGLISH)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        slug = WHITESPACE.matcher(slug).replaceAll("-");
        slug = NON_LATIN.matcher(slug).replaceAll("");
        slug = MULTIPLE_DASHES.matcher(slug).replaceAll("-");
        return slug.strip().replace("-", "-");
    }
}
