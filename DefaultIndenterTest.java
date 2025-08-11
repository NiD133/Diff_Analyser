package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for DefaultIndenter focusing on immutability and idempotency of
 * withLinefeed(...) and withIndent(...).
 */
class DefaultIndenterTest {

    private static final String CUSTOM_LF = "LF_TEST";
    private static final String CUSTOM_INDENT = ">>";

    @Nested
    class WithLinefeed {

        @Test
        void returnsNewInstanceWhenLinefeedChanges() {
            // given
            DefaultIndenter original = new DefaultIndenter();

            // when
            DefaultIndenter updated = original.withLinefeed(CUSTOM_LF);

            // then
            assertAll(
                () -> assertEquals(CUSTOM_LF, updated.getEol(), "EOL should be updated to the custom value"),
                () -> assertNotSame(updated, original, "A new instance should be returned when the value changes")
            );
        }

        @Test
        void returnsSameInstanceWhenLinefeedUnchanged() {
            // given
            DefaultIndenter original = new DefaultIndenter().withLinefeed(CUSTOM_LF);

            // when
            DefaultIndenter same = original.withLinefeed(CUSTOM_LF);

            // then
            assertAll(
                () -> assertSame(original, same, "Same instance should be returned when value is unchanged"),
                () -> assertEquals(CUSTOM_LF, same.getEol(), "EOL should remain the custom value")
            );
        }
    }

    @Nested
    class WithIndent {

        @Test
        void returnsNewInstanceWhenIndentChanges_andKeepsExistingLinefeed() {
            // given
            DefaultIndenter original = new DefaultIndenter();

            // when
            DefaultIndenter updated = original.withIndent(CUSTOM_INDENT);

            // then
            assertAll(
                () -> assertEquals(CUSTOM_INDENT, updated.getIndent(), "Indent should be updated to the custom value"),
                () -> assertEquals(System.lineSeparator(), updated.getEol(), "EOL should remain unchanged"),
                () -> assertNotSame(updated, original, "A new instance should be returned when the value changes")
            );
        }

        @Test
        void returnsSameInstanceWhenIndentUnchanged() {
            // given
            DefaultIndenter original = new DefaultIndenter().withIndent(CUSTOM_INDENT);

            // when
            DefaultIndenter same = original.withIndent(CUSTOM_INDENT);

            // then
            assertAll(
                () -> assertSame(original, same, "Same instance should be returned when value is unchanged"),
                () -> assertEquals(CUSTOM_INDENT, same.getIndent(), "Indent should remain the custom value")
            );
        }
    }
}