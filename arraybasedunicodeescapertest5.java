package com.google.common.escape;

import static com.google.common.escape.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.truth.Truth.assertThat;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.escape.testing.EscaperAsserts;
import java.io.IOException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;

public class ArrayBasedUnicodeEscaperTestTest5 extends TestCase {

    private static final ImmutableMap<Character, String> NO_REPLACEMENTS = ImmutableMap.of();

    private static final ImmutableMap<Character, String> SIMPLE_REPLACEMENTS = ImmutableMap.of('\n', "<newline>", '\t', "<tab>", '&', "<and>");

    private static final char[] NO_CHARS = new char[0];

    public void testCodePointsFromSurrogatePairs() throws IOException {
        UnicodeEscaper surrogateEscaper = new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 0, 0x20000, null) {

            private final char[] escaped = new char[] { 'X' };

            @Override
            protected char[] escapeUnsafe(int c) {
                return escaped;
            }
        };
        EscaperAsserts.assertBasic(surrogateEscaper);
        // A surrogate pair defining a code point within the safe range.
        // 0x10000
        String safeInput = "\uD800\uDC00";
        assertThat(surrogateEscaper.escape(safeInput)).isEqualTo(safeInput);
        // A surrogate pair defining a code point outside the safe range (but both
        // of the surrogate characters lie within the safe range). It is important
        // not to accidentally treat this as a sequence of safe characters.
        // 0x10FFFF
        String unsafeInput = "\uDBFF\uDFFF";
        assertThat(surrogateEscaper.escape(unsafeInput)).isEqualTo("X");
    }
}
