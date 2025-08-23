package com.google.common.escape;

import static com.google.common.escape.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.truth.Truth.assertThat;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.escape.testing.EscaperAsserts;
import java.io.IOException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;

public class ArrayBasedUnicodeEscaperTestTest2 extends TestCase {

    private static final ImmutableMap<Character, String> NO_REPLACEMENTS = ImmutableMap.of();

    private static final ImmutableMap<Character, String> SIMPLE_REPLACEMENTS = ImmutableMap.of('\n', "<newline>", '\t', "<tab>", '&', "<and>");

    private static final char[] NO_CHARS = new char[0];

    public void testSafeRange() throws IOException {
        // Basic escaping of unsafe chars (wrap them in {,}'s)
        UnicodeEscaper wrappingEscaper = new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, 'A', 'Z', null) {

            @Override
            protected char[] escapeUnsafe(int c) {
                return ("{" + (char) c + "}").toCharArray();
            }
        };
        EscaperAsserts.assertBasic(wrappingEscaper);
        // '[' and '@' lie either side of [A-Z].
        assertThat(wrappingEscaper.escape("[FOO@BAR]")).isEqualTo("{[}FOO{@}BAR{]}");
    }
}
