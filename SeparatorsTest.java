package com.fasterxml.jackson.core.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Separators} focusing on immutability and withXxx(...) behavior.
 * The class is expected to:
 * - Return the same instance when a "with" method is called with an unchanged value.
 * - Return a new instance when the value changes, without affecting other separators.
 */
class SeparatorsTest {

    private static final char INITIAL = '5';
    private static final char OTHER = '6';
    private static final char OTHER_ENTRY = '!';

    private static Separators newAll(char ch) {
        // Convenience factory: all three separators start as the same character
        return new Separators(ch, ch, ch);
    }

    private static void assertSeparatorChars(Separators s,
                                             char expectedObjectFieldValue,
                                             char expectedObjectEntry,
                                             char expectedArrayValue) {
        assertEquals(expectedObjectEntry, s.getObjectEntrySeparator(), "objectEntrySeparator");
        assertEquals(expectedObjectFieldValue, s.getObjectFieldValueSeparator(), "objectFieldValueSeparator");
        assertEquals(expectedArrayValue, s.getArrayValueSeparator(), "arrayValueSeparator");
    }

    @Nested
    class WithArrayValueSeparator {

        @Test
        void returnsSameInstanceWhenValueUnchanged() {
            // given
            Separators base = newAll(INITIAL);

            // when
            Separators updated = base.withArrayValueSeparator(INITIAL);

            // then
            assertSame(base, updated, "Should return same instance when value is unchanged");
            assertSeparatorChars(updated, INITIAL, INITIAL, INITIAL);
        }

        @Test
        void returnsNewInstanceWhenValueChanges() {
            // given
            Separators base = newAll(INITIAL);

            // when
            Separators updated = base.withArrayValueSeparator(OTHER);

            // then
            assertNotSame(base, updated, "Should return new instance when value changes");
            assertSeparatorChars(updated, INITIAL, INITIAL, OTHER);
        }
    }

    @Nested
    class WithObjectEntrySeparator {

        @Test
        void returnsSameInstanceWhenValueUnchanged() {
            // given
            Separators base = newAll(INITIAL);

            // when
            Separators updated = base.withObjectEntrySeparator(INITIAL);

            // then
            assertSame(base, updated, "Should return same instance when value is unchanged");
            assertSeparatorChars(updated, INITIAL, INITIAL, INITIAL);
        }

        @Test
        void returnsNewInstanceWhenValueChanges() {
            // given
            Separators base = newAll(INITIAL);

            // when
            Separators updated = base.withObjectEntrySeparator(OTHER_ENTRY);

            // then
            assertNotSame(base, updated, "Should return new instance when value changes");
            assertSeparatorChars(updated, INITIAL, OTHER_ENTRY, INITIAL);
        }
    }

    @Nested
    class WithObjectFieldValueSeparator {

        @Test
        void returnsSameInstanceWhenValueUnchanged() {
            // given
            Separators base = newAll(INITIAL);

            // when
            Separators updated = base.withObjectFieldValueSeparator(INITIAL);

            // then
            assertSame(base, updated, "Should return same instance when value is unchanged");
            assertSeparatorChars(updated, INITIAL, INITIAL, INITIAL);
        }

        @Test
        void returnsNewInstanceWhenValueChanges() {
            // given
            Separators base = newAll(INITIAL);

            // when
            Separators updated = base.withObjectFieldValueSeparator(OTHER);

            // then
            assertNotSame(base, updated, "Should return new instance when value changes");
            assertSeparatorChars(updated, OTHER, INITIAL, INITIAL);
        }
    }
}