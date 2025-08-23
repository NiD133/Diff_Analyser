package org.apache.commons.compress.harmony.pack200;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link CodecEncoding} class.
 */
public class CodecEncodingTest {

    /**
     * Tests that getSpecifierForDefaultCodec returns 0 when the input codec is null.
     * The Pack200 specification defines the first canonical codec (at index 0)
     * as null, which represents the default encoding. This test verifies that
     * passing null correctly returns this specifier.
     */
    @Test
    public void getSpecifierForDefaultCodec_whenCodecIsNull_shouldReturnZero() {
        // Arrange
        final BHSDCodec nullCodec = null;
        final int expectedSpecifier = 0;

        // Act
        final int actualSpecifier = CodecEncoding.getSpecifierForDefaultCodec(nullCodec);

        // Assert
        assertEquals("The specifier for a null default codec should be 0.", expectedSpecifier, actualSpecifier);
    }
}