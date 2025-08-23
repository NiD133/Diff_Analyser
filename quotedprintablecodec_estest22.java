package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test case verifies the behavior of the QuotedPrintableCodec's decode method
 * when provided with an empty string.
 *
 * Note: The original test class name 'QuotedPrintableCodec_ESTestTest22'
 * suggests it was auto-generated. This refactoring focuses on improving the
 * clarity of the test method itself.
 */
public class QuotedPrintableCodec_ESTestTest22 {

    /**
     * Tests that decoding an empty string correctly results in an empty string.
     */
    @Test
    public void decodeEmptyStringShouldReturnEmptyString() throws Exception {
        // Arrange
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "";
        String expected = "";

        // Act
        // The charset is specified as "UTF-8", but it is irrelevant for an empty string.
        String actual = codec.decode(input, "UTF-8");

        // Assert
        assertEquals("Decoding an empty string should result in an empty string.",
                expected, actual);
    }
}