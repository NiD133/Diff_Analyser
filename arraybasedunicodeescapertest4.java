package com.google.common.escape;

import static com.google.common.escape.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.truth.Truth.assertThat;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.escape.testing.EscaperAsserts;
import java.io.IOException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;

public class ArrayBasedUnicodeEscaperTestTest4 extends TestCase {

    private static final ImmutableMap<Character, String> NO_REPLACEMENTS = ImmutableMap.of();

    private static final ImmutableMap<Character, String> SIMPLE_REPLACEMENTS = ImmutableMap.of('\n', "<newline>", '\t', "<tab>", '&', "<and>");

    private static final char[] NO_CHARS = new char[0];

    public void testReplacementPriority() throws IOException {
        UnicodeEscaper replacingEscaper = new ArrayBasedUnicodeEscaper(SIMPLE_REPLACEMENTS, ' ', '~', null) {

            private final char[] unknown = new char[] { '?' };

            @Override
            protected char[] escapeUnsafe(int c) {
                return unknown;
            }
        };
        EscaperAsserts.assertBasic(replacingEscaper);
        // Replacements are applied first regardless of whether the character is in
        // the safe range or not ('&' is a safe char while '\t' and '\n' are not).
        assertThat(replacingEscaper.escape("\tFish &\0 Chips\r\n")).isEqualTo("<tab>Fish <and>? Chips?<newline>");
    }
}
