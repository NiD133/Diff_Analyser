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
class LZMAUtilsTest {

    /**
     * Provides test cases for compressed filename generation.
     * @return A stream of arguments, where each is an original filename and its expected compressed counterpart.
     */
    static Stream<Arguments> filenameTestCases() {
        return Stream.of(
            Arguments.of("", ".lzma"),
            Arguments.of("x", "x.lzma"),
            Arguments.of("x.wmf.y", "x.wmf.y.lzma"),
            // Edge cases with whitespace
            Arguments.of("x.wmf ", "x.wmf .lzma"),
            Arguments.of("x.wmf\n", "x.wmf\n.lzma")
        );
    }

    @DisplayName("getCompressedFileName() should append the .lzma suffix")
    @ParameterizedTest(name = "Input: \"{0}\", Expected: \"{1}\"")
    @MethodSource("filenameTestCases")
    void getCompressedFileNameShouldAddLzmaSuffix(final String originalFilename, final String expectedFilename) {
        assertEquals(expectedFilename, LZMAUtils.getCompressedFileName(originalFilename));
    }

    @DisplayName("Deprecated getCompressedFilename() should behave identically to the new method")
    @ParameterizedTest(name = "Input: \"{0}\", Expected: \"{1}\"")
    @MethodSource("filenameTestCases")
    @SuppressWarnings("deprecation")
    void deprecatedGetCompressedFilenameShouldBehaveIdentically(final String originalFilename, final String expectedFilename) {
        assertEquals(expectedFilename, LZMAUtils.getCompressedFilename(originalFilename));
    }
}