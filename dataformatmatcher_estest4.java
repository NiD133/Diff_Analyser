package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import org.junit.Test;

import java.io.CharConversionException;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Contains tests for {@link DataFormatMatcher}, focusing on edge cases related to parser creation.
 */
public class DataFormatMatcher_ESTestTest4 {

    /**
     * Verifies that createParserWithMatch() throws a CharConversionException when the input
     * byte sequence resembles an unsupported UCS-4 encoding.
     * <p>
     * Jackson's internal ByteSourceJsonBootstrapper attempts to detect character encoding.
     * A byte pattern like {0x00, 0xXX, 0x00, 0x00} is heuristically identified as a
     * potential "2143" ordered UCS-4 stream, which is not supported, leading to an exception.
     */
    @Test
    public void createParserWithMatch_whenInputResemblesUnsupportedUcs4_throwsCharConversionException() throws IOException {
        // Arrange: Create a byte sequence that mimics an unsupported UCS-4 encoding.
        // The pattern {0, -83, 0, 0} is specifically crafted to trigger the "2143"
        // endianness check within Jackson's bootstrapper.
        byte[] unsupportedUcs4Data = new byte[9];
        unsupportedUcs4Data[1] = (byte) -83; // This creates the 0x00AD0000 pattern

        InputAccessor.Std inputAccessor = new InputAccessor.Std(unsupportedUcs4Data);
        JsonFactory jsonFactory = new JsonFactory();
        DataFormatMatcher matcher = inputAccessor.createMatcher(jsonFactory, MatchStrength.WEAK_MATCH);

        // Act & Assert
        try {
            matcher.createParserWithMatch();
            fail("Expected a CharConversionException due to unsupported encoding, but none was thrown.");
        } catch (CharConversionException e) {
            // Assert that the correct exception with the expected message is thrown.
            String expectedMessage = "Unsupported UCS-4 endianness (2143) detected";
            assertTrue(
                "The exception message should indicate the specific unsupported encoding. Got: " + e.getMessage(),
                e.getMessage().contains(expectedMessage)
            );
        }
    }
}