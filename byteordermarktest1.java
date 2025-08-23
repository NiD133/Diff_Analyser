package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.charset.Charset;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link ByteOrderMark}.
 */
class ByteOrderMarkTest {

    /**
     * Provides a stream of the predefined {@link ByteOrderMark} constants for parameterized tests.
     *
     * @return A stream of standard {@link ByteOrderMark} constants.
     */
    private static Stream<ByteOrderMark> predefinedBomsProvider() {
        return Stream.of(
            ByteOrderMark.UTF_8,
            ByteOrderMark.UTF_16BE,
            ByteOrderMark.UTF_16LE,
            ByteOrderMark.UTF_32BE,
            ByteOrderMark.UTF_32LE
        );
    }

    /**
     * Tests that the charset name for each predefined BOM constant is valid and can be loaded
     * as a {@link java.nio.charset.Charset} instance without throwing an exception.
     *
     * @param bom The predefined ByteOrderMark constant to test.
     */
    @ParameterizedTest
    @MethodSource("predefinedBomsProvider")
    void predefinedBomShouldHaveValidCharsetName(final ByteOrderMark bom) {
        // Charset.forName() throws an exception for invalid charset names.
        // This assertion directly verifies that the name from the BOM constant is valid.
        assertDoesNotThrow(() -> Charset.forName(bom.getCharsetName()),
            () -> "Charset.forName() should not throw for " + bom.getCharsetName());
    }
}