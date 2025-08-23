package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Provides test cases for the length() method, covering both custom and predefined BOMs.
     *
     * @return A stream of arguments, where each argument contains a ByteOrderMark instance
     *         and its expected length.
     */
    private static Stream<Arguments> bomAndExpectedLengthProvider() {
        return Stream.of(
            // Custom BOMs with varying lengths
            Arguments.of(new ByteOrderMark("BOM_1_BYTE", 0x01), 1),
            Arguments.of(new ByteOrderMark("BOM_2_BYTES", 0x01, 0x02), 2),
            Arguments.of(new ByteOrderMark("BOM_3_BYTES", 0x01, 0x02, 0x03), 3),

            // Predefined BOMs from the ByteOrderMark class
            Arguments.of(ByteOrderMark.UTF_8, 3),
            Arguments.of(ByteOrderMark.UTF_16BE, 2),
            Arguments.of(ByteOrderMark.UTF_16LE, 2),
            Arguments.of(ByteOrderMark.UTF_32BE, 4),
            Arguments.of(ByteOrderMark.UTF_32LE, 4)
        );
    }

    /**
     * Tests that {@link ByteOrderMark#length()} returns the correct number of bytes
     * that the BOM consists of.
     *
     * @param bom The ByteOrderMark instance to test.
     * @param expectedLength The expected length of the BOM.
     */
    @ParameterizedTest(name = "{0} should have length {1}")
    @MethodSource("bomAndExpectedLengthProvider")
    void lengthShouldReturnCorrectNumberOfBytes(final ByteOrderMark bom, final int expectedLength) {
        assertEquals(expectedLength, bom.length());
    }
}