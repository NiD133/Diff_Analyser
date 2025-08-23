package com.google.common.escape;

import static com.google.common.escape.ReflectionFreeAssertThrows.assertThrows;
import static com.google.common.truth.Truth.assertThat;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableMap;
import com.google.common.escape.testing.EscaperAsserts;
import java.io.IOException;
import junit.framework.TestCase;
import org.jspecify.annotations.NullMarked;

public class ArrayBasedUnicodeEscaperTestTest3 extends TestCase {

    private static final ImmutableMap<Character, String> NO_REPLACEMENTS = ImmutableMap.of();

    private static final ImmutableMap<Character, String> SIMPLE_REPLACEMENTS = ImmutableMap.of('\n', "<newline>", '\t', "<tab>", '&', "<and>");

    private static final char[] NO_CHARS = new char[0];

    public void testDeleteUnsafeChars() throws IOException {
        UnicodeEscaper deletingEscaper = new ArrayBasedUnicodeEscaper(NO_REPLACEMENTS, ' ', '~', null) {

            @Override
            protected char[] escapeUnsafe(int c) {
                return NO_CHARS;
            }
        };
        EscaperAsserts.assertBasic(deletingEscaper);
        assertThat(deletingEscaper.escape("\tEverything\0 outside the\uD800\uDC00 " + "printable ASCII \uFFFFrange is \u007Fdeleted.\n")).isEqualTo("Everything outside the printable ASCII range is deleted.");
    }
}