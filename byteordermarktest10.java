package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link ByteOrderMark#toString()}.
 */
@DisplayName("ByteOrderMark")
class ByteOrderMarkTest {

    /**
     * Provides test cases for the toString() method.
     * Each argument consists of a ByteOrderMark instance and its expected string representation.
     *
     * @return a stream of arguments for the parameterized test.
     */
    private static Stream<Arguments> toStringTestCases() {
        return Stream.of(
            Arguments.of(new ByteOrderMark("SINGLE_BYTE", 0x01), "ByteOrderMark[SINGLE_BYTE: 0x1]"),
            Arguments.of(new ByteOrderMark("TWO_BYTES", 0x01, 0x02), "ByteOrderMark[TWO_BYTES: 0x1,0x2]"),
            Arguments.of(new ByteOrderMark("THREE_BYTES", 0x01, 0x02, 0x03), "ByteOrderMark[THREE_BYTES: 0x1,0x2,0x3]")
        );
    }

    @DisplayName("toString() should return a correctly formatted string representation")
    @ParameterizedTest(name = "for a BOM with {0}")
    @MethodSource("toStringTestCases")
    void testToString(final ByteOrderMark bom, final String expectedString) {
        // Act
        final String actualString = bom.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }
}