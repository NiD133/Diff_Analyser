package com.google.common.escape;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableMap;
import com.google.common.escape.testing.EscaperAsserts;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link ArrayBasedUnicodeEscaper} focusing on safe range behavior.
 */
@RunWith(JUnit4.class)
public class ArrayBasedUnicodeEscaperTest {

    /**
     * This test verifies that characters falling outside the specified 'safe range'
     * are correctly passed to the `escapeUnsafe` method for custom escaping.
     */
    @Test
    public void escape_charsOutsideSafeRange_arePassedToEscapeUnsafe() throws IOException {
        // ARRANGE: Create an escaper where only uppercase letters 'A' through 'Z' are considered
        // safe. Any character outside this range will be "wrapped" in curly braces by our
        // custom implementation of escapeUnsafe().
        UnicodeEscaper wrappingEscaper =
            new ArrayBasedUnicodeEscaper(ImmutableMap.of(), 'A', 'Z', null) {
                @Override
                protected char[] escapeUnsafe(int c) {
                    // Wrap unsafe characters in '{' and '}' for easy identification.
                    return ("{" + (char) c + "}").toCharArray();
                }
            };

        // This helper validates general, common escaper properties.
        EscaperAsserts.assertBasic(wrappingEscaper);

        // ACT: Escape a string containing characters both inside and outside the safe range.
        // The characters '[', '@', and ']' are adjacent to but outside the A-Z range.
        String input = "[FOO@BAR]";
        String actualOutput = wrappingEscaper.escape(input);

        // ASSERT: Verify that only the unsafe characters were wrapped, while the safe
        // characters (F, O, B, A, R) were left untouched.
        String expectedOutput = "{[}FOO{@}BAR{]}";
        assertThat(actualOutput).isEqualTo(expectedOutput);
    }
}