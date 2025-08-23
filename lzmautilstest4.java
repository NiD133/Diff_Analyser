package org.apache.commons.compress.compressors.lzma;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the filename utility methods in {@link LZMAUtils}.
 */
@DisplayName("LZMAUtils Filename Utility Tests")
class LZMAUtilsFileNameTest {

    /**
     * Provides test cases for {@link #getUncompressedFileNameBehavesAsExpected(String, String, String)}.
     * Each argument consists of the input filename, the expected output, and a description of the test case.
     */
    static Stream<Arguments> uncompressedFileNameProvider() {
        return Stream.of(
            Arguments.of("", "", "An empty filename should remain empty"),
            Arguments.of(".lzma", ".lzma", "A filename that is only the suffix should not be changed"),
            Arguments.of("x.lzma", "x", "The '.lzma' suffix should be removed"),
            Arguments.of("x-lzma", "x", "The '-lzma' suffix should be removed"),
            Arguments.of("x.lzma ", "x.lzma ", "A suffix with a trailing space should not be removed"),
            Arguments.of("x.lzma\n", "x.lzma\n", "A suffix with a trailing newline should not be removed"),
            Arguments.of("x.lzma.y", "x.lzma.y", "A suffix not at the end of the filename should not be removed")
        );
    }

    @DisplayName("getUncompressedFileName should correctly handle various filenames")
    @ParameterizedTest(name = "[{index}] {2}: ''{0}'' -> ''{1}''")
    @MethodSource("uncompressedFileNameProvider")
    @SuppressWarnings("deprecation") // Testing a deprecated method is intentional
    void getUncompressedFileNameBehavesAsExpected(final String input, final String expected, final String description) {
        // Test the current method
        assertEquals(expected, LZMAUtils.getUncompressedFileName(input));

        // Also test the deprecated method to ensure consistent behavior
        assertEquals(expected, LZMAUtils.getUncompressedFilename(input));
    }
}