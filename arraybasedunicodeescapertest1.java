package com.google.common.escape;

import static com.google.common.escape.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.truth.Truth.assertThat;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.escape.testing.EscaperAsserts;
import java.io.IOException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;

public class ArrayBasedUnicodeEscaperTestTest1 extends TestCase {

    private static final ImmutableMap<Character, String> NO_REPLACEMENTS = ImmutableMap.of();

    private static final ImmutableMap<Character, String> SIMPLE_REPLACEMENTS = ImmutableMap.of('\n', "<newline>", '\t', "<tab>", '&', "<and>");

    private static final char[] NO_CHARS = new char[0];

    public void testReplacements() throws IOException {
        // In reality this is not a very sensible escaper to have (if you are only
        // escaping elements from a map you would use a ArrayBasedCharEscaper).
        UnicodeEscaper escaper = new ArrayBasedUnicodeEscaper(SIMPLE_REPLACEMENTS, Character.MIN_VALUE, Character.MAX_CODE_POINT, null) {

            @Override
            protected char[] escapeUnsafe(int c) {
                return NO_CHARS;
            }
        };
        EscaperAsserts.assertBasic(escaper);
        assertThat(escaper.escape("\tFish & Chips\n")).isEqualTo("<tab>Fish <and> Chips<newline>");
        // Verify that everything else is left unescaped.
        String safeChars = "\0\u0100\uD800\uDC00\uFFFF";
        assertThat(escaper.escape(safeChars)).isEqualTo(safeChars);
        // Ensure that Unicode escapers behave correctly wrt badly formed input.
        String badUnicode = "\uDC00\uD800";
        assertThrows(IllegalArgumentException.class, () -> escaper.escape(badUnicode));
    }
}
