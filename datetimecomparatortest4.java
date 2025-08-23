package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.ObjectCodec;
import org.junit.Test;

import java.io.CharConversionException;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Provides tests for the {@link DataFormatMatcher} class, focusing on its
 * behavior when handling different data formats and potential errors.
 */
public class DataFormatMatcherImprovedTest {

    /**
     * Verifies that calling {@link DataFormatMatcher#createParserWithMatch()} throws a
     * {@link CharConversionException} when the input data appears to be in an
     * unsupported UCS-4 encoding.
     *
     * <p><b>Test Rationale:</b>
     * The JSON parser's bootstrapping process attempts to auto-detect character encoding
     * by looking for a Byte Order Mark (BOM). [19, 23] This test simulates a scenario where the
     * input bytes trigger the detection logic for a UCS-4 encoding variant that the
     * library does not support. The expected behavior is a graceful failure with a
     * specific exception, preventing the creation of a misconfigured parser. [16, 20]
     * </p>
     */
    @Test
    public void createParser_whenInputIsUnsupportedUcs4_shouldThrowCharConversionException() {
        // ARRANGE: Create input data that resembles an unsupported UCS-4 byte stream.
        // The byte sequence 0x00, 0x00, 0xAD (where -83 is the signed byte for 0xAD)
        // is specifically used to trigger the "Unsupported UCS-4 endianness" check
        // within Jackson's ByteSourceJsonBootstrapper.
        byte[] unsupportedUcs4Bytes = new byte[9];
        unsupportedUcs4Bytes[2] = (byte) -83;

        InputAccessor.Std inputAccessor = new InputAccessor.Std(unsupportedUcs4Bytes);
        JsonFactory jsonFactory = new JsonFactory(mock(ObjectCodec.class));
        DataFormatMatcher dataFormatMatcher = inputAccessor.createMatcher(jsonFactory, MatchStrength.WEAK_MATCH);

        // ACT & ASSERT: Attempt to create the parser and verify that the correct exception is thrown.
        // The assertThrows method is a modern approach from JUnit for verifying exceptions,
        // making the test's intent clear. [1, 7, 8]
        CharConversionException thrownException = assertThrows(
                "Expected a CharConversionException for unsupported UCS-4 encoding",
                CharConversionException.class,
                dataFormatMatcher::createParserWithMatch
        );

        // ASSERT: Further verify the content of the exception message for correctness.
        String expectedMessageFragment = "Unsupported UCS-4 endianness";
        assertTrue(
                "Exception message should detail the unsupported encoding issue.",
                thrownException.getMessage().contains(expectedMessageFragment)
        );
    }
}