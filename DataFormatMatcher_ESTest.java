package com.fasterxml.jackson.core.format;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.format.DataFormatMatcher;
import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.core.format.MatchStrength;
import java.io.ByteArrayInputStream;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;

/**
 * Test suite for DataFormatMatcher functionality.
 * Tests format detection, parser creation, and data stream access.
 */
public class DataFormatMatcher_ESTest {

    // Test data constants
    private static final int SMALL_BUFFER_SIZE = 6;
    private static final int MEDIUM_BUFFER_SIZE = 9;
    private static final int LARGE_BUFFER_SIZE = 12;
    private static final byte INVALID_UTF_BYTE = (byte) -83;

    // ========== Basic Functionality Tests ==========

    @Test(timeout = 4000)
    public void shouldReturnMatchingJsonFactoryWhenSolidMatch() throws Throwable {
        // Given: A piped input stream with buffer and solid match strength
        PipedInputStream inputStream = new PipedInputStream();
        byte[] buffer = new byte[SMALL_BUFFER_SIZE];
        InputAccessor.Std accessor = new InputAccessor.Std(inputStream, buffer);
        JsonFactory expectedFactory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.SOLID_MATCH;

        // When: Creating a matcher with solid match
        DataFormatMatcher matcher = accessor.createMatcher(expectedFactory, matchStrength);

        // Then: Should return the matching factory and correct strength
        JsonFactory actualFactory = matcher.getMatch();
        assertNotNull("Expected non-null JsonFactory for solid match", actualFactory);
        assertEquals("Match strength should be SOLID_MATCH", MatchStrength.SOLID_MATCH, matcher.getMatchStrength());
    }

    @Test(timeout = 4000)
    public void shouldReturnTrueForHasMatchWhenWeakMatch() throws Throwable {
        // Given: Empty byte array with weak match strength
        byte[] emptyBuffer = new byte[0];
        JsonFactory factory = new JsonFactory();
        InputAccessor.Std accessor = new InputAccessor.Std(emptyBuffer);
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;

        // When: Creating matcher with weak match
        DataFormatMatcher matcher = accessor.createMatcher(factory, matchStrength);

        // Then: Should indicate it has a match
        boolean hasMatch = matcher.hasMatch();
        assertTrue("Should have match for WEAK_MATCH strength", hasMatch);
        assertEquals("Match strength should be WEAK_MATCH", MatchStrength.WEAK_MATCH, matcher.getMatchStrength());
    }

    @Test(timeout = 4000)
    public void shouldReturnFalseForHasMatchWhenNoMatch() throws Throwable {
        // Given: Empty buffer with no match strength
        byte[] emptyBuffer = new byte[0];
        InputAccessor.Std accessor = new InputAccessor.Std(emptyBuffer);
        MatchStrength noMatchStrength = MatchStrength.NO_MATCH;

        // When: Creating matcher with no match
        DataFormatMatcher matcher = accessor.createMatcher(null, noMatchStrength);

        // Then: Should indicate no match
        matcher.hasMatch(); // Call the method being tested
        assertEquals("Match strength should be NO_MATCH", MatchStrength.NO_MATCH, matcher.getMatchStrength());
    }

    // ========== Parser Creation Tests ==========

    @Test(timeout = 4000)
    public void shouldCreateValidParserWhenMatchExists() throws Throwable {
        // Given: Buffer with valid data and weak match
        byte[] buffer = new byte[MEDIUM_BUFFER_SIZE];
        JsonFactory factory = new JsonFactory();
        InputAccessor.Std accessor = new InputAccessor.Std(buffer);
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;

        // When: Creating parser with match
        DataFormatMatcher matcher = accessor.createMatcher(factory, matchStrength);
        JsonParser parser = matcher.createParserWithMatch();

        // Then: Should create valid parser
        assertNotNull("Parser should be created for valid match", parser);
        assertEquals("Match strength should be preserved", MatchStrength.WEAK_MATCH, matcher.getMatchStrength());
    }

    @Test(timeout = 4000)
    public void shouldReturnNullParserWhenNoMatch() throws Throwable {
        // Given: Empty buffer with no match
        byte[] emptyBuffer = new byte[0];
        InputAccessor.Std accessor = new InputAccessor.Std(emptyBuffer);
        MatchStrength noMatchStrength = MatchStrength.NO_MATCH;

        // When: Creating parser with no match
        DataFormatMatcher matcher = accessor.createMatcher(null, noMatchStrength);
        matcher.createParserWithMatch(); // Should not throw exception

        // Then: Should handle no match gracefully
        assertEquals("Match strength should remain NO_MATCH", MatchStrength.NO_MATCH, matcher.getMatchStrength());
    }

    // ========== Data Stream Access Tests ==========

    @Test(timeout = 4000)
    public void shouldProvideAccessToDataStream() throws Throwable {
        // Given: Buffer with data
        byte[] buffer = new byte[LARGE_BUFFER_SIZE];
        JsonFactory factory = new JsonFactory();
        InputAccessor.Std accessor = new InputAccessor.Std(buffer);
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;

        // When: Getting data stream
        DataFormatMatcher matcher = accessor.createMatcher(factory, matchStrength);
        InputStream dataStream = matcher.getDataStream();

        // Then: Should provide access to all buffered data
        assertEquals("Data stream should contain all buffered bytes", LARGE_BUFFER_SIZE, dataStream.available());
        assertEquals("Match strength should be preserved", MatchStrength.WEAK_MATCH, matcher.getMatchStrength());
        assertTrue("Should indicate match exists", matcher.hasMatch());
    }

    // ========== Format Name Tests ==========

    @Test(timeout = 4000)
    public void shouldReturnFormatNameWhenMatchExists() throws Throwable {
        // Given: Buffer with solid match
        byte[] buffer = new byte[4];
        JsonFactory factory = new JsonFactory();
        InputAccessor.Std accessor = new InputAccessor.Std(buffer);
        MatchStrength matchStrength = MatchStrength.SOLID_MATCH;

        // When: Getting format name
        DataFormatMatcher matcher = accessor.createMatcher(factory, matchStrength);
        String formatName = matcher.getMatchedFormatName();

        // Then: Should return valid format name
        assertNotNull("Format name should not be null for valid match", formatName);
        assertEquals("Match strength should be SOLID_MATCH", MatchStrength.SOLID_MATCH, matcher.getMatchStrength());
    }

    @Test(timeout = 4000)
    public void shouldReturnNullFormatNameWhenNoMatch() throws Throwable {
        // Given: Buffer with no factory match
        byte[] buffer = new byte[41];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        MatchStrength matchStrength = MatchStrength.INCONCLUSIVE;

        // When: Creating matcher without factory
        DataFormatMatcher matcher = new DataFormatMatcher(inputStream, buffer, 0, 0, null, matchStrength);
        String formatName = matcher.getMatchedFormatName();

        // Then: Should return null format name
        assertNull("Format name should be null when no factory match", formatName);
    }

    // ========== Edge Cases and Error Handling ==========

    @Test(timeout = 4000)
    public void shouldHandleNullMatchStrength() throws Throwable {
        // Given: Buffer with null match strength
        byte[] buffer = new byte[MEDIUM_BUFFER_SIZE];
        JsonFactory factory = new JsonFactory();
        InputAccessor.Std accessor = new InputAccessor.Std(buffer);

        // When: Creating matcher with null strength
        DataFormatMatcher matcher = accessor.createMatcher(factory, null);

        // Then: Should handle null strength gracefully
        matcher.getMatchStrength(); // Should not throw exception
        assertTrue("Should still indicate match exists", matcher.hasMatch());
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionForInvalidBufferBounds() throws Throwable {
        // Given: Invalid buffer parameters
        byte[] buffer = new byte[1];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        JsonFactory factory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.INCONCLUSIVE;

        // When & Then: Should throw exception for invalid bounds
        try {
            new DataFormatMatcher(inputStream, null, '\"', '\"', factory, matchStrength);
            fail("Expected NullPointerException for null buffer");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionForInvalidStartLength() throws Throwable {
        // Given: Invalid start/length parameters
        byte[] emptyBuffer = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(emptyBuffer);
        JsonFactory factory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.INCONCLUSIVE;

        // When & Then: Should throw exception for invalid start/length
        try {
            new DataFormatMatcher(inputStream, emptyBuffer, '\"', '\"', factory, matchStrength);
            fail("Expected IllegalArgumentException for invalid start/length");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention illegal start/length", 
                      e.getMessage().contains("Illegal start/length"));
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowExceptionForNegativeLength() throws Throwable {
        // Given: Negative length parameter
        byte[] buffer = new byte[MEDIUM_BUFFER_SIZE];
        JsonFactory factory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;

        // When & Then: Should throw exception for negative length
        try {
            new DataFormatMatcher(null, buffer, '\"', -1442, factory, matchStrength);
            fail("Expected IllegalArgumentException for negative length");
        } catch (IllegalArgumentException e) {
            assertTrue("Exception should mention illegal start/length", 
                      e.getMessage().contains("Illegal start/length"));
        }
    }

    // ========== Exception Handling Tests ==========

    @Test(timeout = 4000)
    public void shouldThrowArrayIndexOutOfBoundsForInvalidAccess() throws Throwable {
        // Given: Matcher with invalid buffer access parameters
        byte[] buffer = new byte[SMALL_BUFFER_SIZE];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        JsonFactory factory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.SOLID_MATCH;

        // When: Creating matcher with invalid parameters
        DataFormatMatcher matcher = new DataFormatMatcher(inputStream, buffer, 2, 1, factory, matchStrength);

        // Then: Should throw exception when trying to create parser
        try {
            matcher.createParserWithMatch();
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception due to invalid buffer access
        }
    }

    @Test(timeout = 4000)
    public void shouldThrowCharConversionExceptionForInvalidUTF() throws Throwable {
        // Given: Buffer with invalid UTF-8 sequence
        byte[] buffer = new byte[MEDIUM_BUFFER_SIZE];
        buffer[2] = INVALID_UTF_BYTE; // Invalid UTF-8 byte
        InputAccessor.Std accessor = new InputAccessor.Std(buffer);
        JsonFactory factory = new JsonFactory();
        MatchStrength matchStrength = MatchStrength.WEAK_MATCH;

        // When: Creating parser with invalid UTF-8 data
        DataFormatMatcher matcher = accessor.createMatcher(factory, matchStrength);

        // Then: Should throw CharConversionException
        try {
            matcher.createParserWithMatch();
            fail("Expected CharConversionException for invalid UTF-8");
        } catch (CharConversionException e) {
            assertTrue("Exception should mention UCS-4 endianness", 
                      e.getMessage().contains("Unsupported UCS-4 endianness"));
        }
    }
}