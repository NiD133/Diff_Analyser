package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the long-to-string conversion methods in {@link NumberOutput}.
 * This test suite verifies the correctness of both {@link NumberOutput#outputLong(long, char[], int)}
 * and {@link NumberOutput#toString(long)}.
 */
@DisplayName("NumberOutput Long to String Conversion")
class NumberOutputLongTest {

    /**
     * Provides a stream of long values representing common edge cases for testing.
     * @return A stream of long values.
     */
    private static Stream<Long> longEdgeCases() {
        return Stream.of(
                0L,
                1L,
                -1L,
                10L,
                -10L,
                Long.MAX_VALUE,
                Long.MIN_VALUE,
                Long.MAX_VALUE - 1,
                Long.MIN_VALUE + 1
        );
    }

    @DisplayName("should correctly convert edge case long values")
    @ParameterizedTest(name = "for value = {0}")
    @MethodSource("longEdgeCases")
    void testEdgeCaseLongConversions(long value) {
        String expected = Long.toString(value);

        // Assert that both conversion methods produce the correct string
        assertEquals(expected, convertWithOutputLong(value));
        assertEquals(expected, NumberOutput.toString(value));
    }

    @DisplayName("should correctly convert a large set of random long values")
    @Test
    void testRandomLongConversions() {
        Random random = new Random(12345L);
        // A large number of iterations helps cover the vast long value space
        for (int i = 0; i < 678000; ++i) {
            // Generate a full 64-bit random long
            long value = ((long) random.nextInt() << 32) | random.nextInt();
            String expected = Long.toString(value);
            String context = "Random test failed at index " + i + " for value: " + value;

            // Assert that both conversion methods produce the correct string
            assertEquals(expected, convertWithOutputLong(value), context);
            assertEquals(expected, NumberOutput.toString(value), context);
        }
    }

    /**
     * Helper method to test {@link NumberOutput#outputLong(long, char[], int)}
     * by converting the result into a String.
     */
    private String convertWithOutputLong(long value) {
        // Max long string length is 20 ("-9223372036854775808").
        // A buffer of 22 is a safe size.
        char[] buffer = new char[22];
        int offset = NumberOutput.outputLong(value, buffer, 0);
        return new String(buffer, 0, offset);
    }
}