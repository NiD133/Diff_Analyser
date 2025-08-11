package com.fasterxml.jackson.core.format;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.format.DataFormatMatcher;
import com.fasterxml.jackson.core.format.MatchStrength;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link DataFormatMatcher} class.
 *
 * The tests cover constructor validation, state accessors (hasMatch, getMatch, etc.),
 * and factory methods for creating parsers and data streams.
 */
public class DataFormatMatcherTest {

    // ==========================================================
    // Constructor Tests
    // ==========================================================

    @Test(expected = NullPointerException.class)
    public void constructorWithNullBufferShouldThrowException() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        JsonFactory jsonFactory = new JsonFactory();

        // Act: Attempt to create a matcher with a null buffer
        new DataFormatMatcher(inputStream, null, 0, 0, jsonFactory, MatchStrength.INCONCLUSIVE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithOffsetAndLengthExceedingBufferShouldThrowException() {
        // Arrange
        byte[] buffer = new byte[5];
        InputStream inputStream = new ByteArrayInputStream(buffer);
        JsonFactory jsonFactory = new JsonFactory();
        int offset = 4;
        int length = 2; // offset + length (6) > buffer.length (5)

        // Act: Attempt to create a matcher with invalid bounds
        new DataFormatMatcher(inputStream, buffer, offset, length, jsonFactory, MatchStrength.SOLID_MATCH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNegativeOffsetShouldThrowException() {
        // Arrange
        byte[] buffer = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(buffer);
        JsonFactory jsonFactory = new JsonFactory();
        int negativeOffset = -1;
        int length = 1;

        // Act: Attempt to create a matcher with a negative offset
        new DataFormatMatcher(inputStream, buffer, negativeOffset, length, jsonFactory, MatchStrength.WEAK_MATCH);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNegativeLengthShouldThrowException() {
        // Arrange
        byte[] buffer = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(buffer);
        JsonFactory jsonFactory = new JsonFactory();
        int offset = 0;
        int negativeLength = -1;

        // Act: Attempt to create a matcher with a negative length
        new DataFormatMatcher(inputStream, buffer, offset, negativeLength, jsonFactory, MatchStrength.WEAK_MATCH);
    }

    // ==========================================================
    // Accessor Method Tests
    // ==========================================================

    @Test
    public void hasMatchShouldReturnTrueWhenFactoryIsPresent() {
        // Arrange
        DataFormatMatcher matcher = new DataFormatMatcher(
                new ByteArrayInputStream(new byte[0]), new byte[0], 0, 0,
                new JsonFactory(), // A non-null factory indicates a match
                MatchStrength.WEAK_MATCH);

        // Act & Assert
        assertTrue(matcher.hasMatch());
    }

    @Test
    public void hasMatchShouldReturnFalseWhenFactoryIsNull() {
        // Arrange
        DataFormatMatcher matcher = new DataFormatMatcher(
                new ByteArrayInputStream(new byte[0]), new byte[0], 0, 0,
                null, // A null factory indicates no match
                MatchStrength.NO_MATCH);

        // Act & Assert
        assertFalse(matcher.hasMatch());
    }

    @Test
    public void getMatchShouldReturnTheMatchingFactory() {
        // Arrange
        JsonFactory expectedFactory = new JsonFactory();
        DataFormatMatcher matcher = new DataFormatMatcher(
                new ByteArrayInputStream(new byte[0]), new byte[0], 0, 0,
                expectedFactory, MatchStrength.SOLID_MATCH);

        // Act
        JsonFactory actualFactory = matcher.getMatch();

        // Assert
        assertSame(expectedFactory, actualFactory);
    }

    @Test
    public void getMatchShouldReturnNullWhenNoMatchFound() {
        // Arrange
        DataFormatMatcher matcher = new DataFormatMatcher(
                new ByteArrayInputStream(new byte[0]), new byte[0], 0, 0,
                null, // No matching factory
                MatchStrength.NO_MATCH);

        // Act
        JsonFactory result = matcher.getMatch();

        // Assert
        assertNull(result);
    }

    @Test
    public void getMatchStrengthShouldReturnCorrectStrength() {
        // Arrange
        MatchStrength expectedStrength = MatchStrength.INCONCLUSIVE;
        DataFormatMatcher matcher = new DataFormatMatcher(
                new ByteArrayInputStream(new byte[0]), new byte[0], 0, 0,
                new JsonFactory(), expectedStrength);

        // Act
        MatchStrength actualStrength = matcher.getMatchStrength();

        // Assert
        assertEquals(expectedStrength, actualStrength);
    }

    @Test
    public void getMatchedFormatNameShouldReturnNameWhenMatchExists() {
        // Arrange
        JsonFactory jsonFactory = new JsonFactory(); // Default format name is "JSON"
        DataFormatMatcher matcher = new DataFormatMatcher(
                new ByteArrayInputStream(new byte[0]), new byte[0], 0, 0,
                jsonFactory, MatchStrength.SOLID_MATCH);

        // Act
        String formatName = matcher.getMatchedFormatName();

        // Assert
        assertEquals(jsonFactory.getFormatName(), formatName);
        assertEquals("JSON", formatName);
    }

    @Test
    public void getMatchedFormatNameShouldReturnNullWhenNoMatchExists() {
        // Arrange
        DataFormatMatcher matcher = new DataFormatMatcher(
                new ByteArrayInputStream(new byte[0]), new byte[0], 0, 0,
                null, MatchStrength.NO_MATCH);

        // Act
        String formatName = matcher.getMatchedFormatName();

        // Assert
        assertNull(formatName);
    }

    // ==========================================================
    // Factory Method Tests
    // ==========================================================

    @Test
    public void getDataStreamShouldReturnMergedStreamWithBufferedAndOriginalData() throws IOException {
        // Arrange
        byte[] originalData = " world".getBytes(StandardCharsets.UTF_8);
        InputStream originalStream = new ByteArrayInputStream(originalData);
        byte[] bufferedData = "Hello".getBytes(StandardCharsets.UTF_8);
        DataFormatMatcher matcher = new DataFormatMatcher(
                originalStream, bufferedData, 0, bufferedData.length,
                new JsonFactory(), MatchStrength.SOLID_MATCH);

        // Act
        InputStream resultStream = matcher.getDataStream();

        // Assert
        // The resulting stream should contain the buffered data followed by the original stream data.
        byte[] resultData = new byte[bufferedData.length + originalData.length];
        int bytesRead = resultStream.read(resultData);

        assertEquals("Stream should contain all data", resultData.length, bytesRead);
        assertEquals("Hello world", new String(resultData, StandardCharsets.UTF_8));
        assertEquals("Stream should be at the end", -1, resultStream.read());
    }

    @Test
    public void createParserWithMatchShouldReturnParserWhenMatchExists() throws IOException {
        // Arrange
        byte[] inputData = "{}".getBytes(StandardCharsets.UTF_8);
        DataFormatMatcher matcher = new DataFormatMatcher(
                new ByteArrayInputStream(inputData), inputData, 0, inputData.length,
                new JsonFactory(), MatchStrength.SOLID_MATCH);

        // Act
        JsonParser parser = matcher.createParserWithMatch();

        // Assert
        assertNotNull(parser);
        parser.close();
    }

    @Test
    public void createParserWithMatchShouldReturnNullWhenNoMatchExists() throws IOException {
        // Arrange
        DataFormatMatcher matcher = new DataFormatMatcher(
                new ByteArrayInputStream(new byte[0]), new byte[0], 0, 0,
                null, MatchStrength.NO_MATCH);

        // Act
        JsonParser parser = matcher.createParserWithMatch();

        // Assert
        assertNull(parser);
    }

    @Test(expected = IOException.class)
    public void createParserWithFailingStreamShouldPropagateIOException() throws IOException {
        // Arrange
        // Use a mock InputStream that throws IOException on any read attempt.
        InputStream failingStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Simulated stream failure");
            }
        };
        DataFormatMatcher matcher = new DataFormatMatcher(
                failingStream, new byte[0], 0, 0,
                new JsonFactory(), MatchStrength.FULL_MATCH);

        // Act: This should attempt to read from the failing stream and throw an exception.
        matcher.createParserWithMatch();
    }

    @Test
    public void createParserWithUnsupportedEncodingShouldThrowException() throws IOException {
        // Arrange
        // This byte sequence can be misinterpreted by Jackson's ByteSourceJsonBootstrapper
        // as a BOM for an unsupported encoding, leading to a CharConversionException.
        byte[] inputData = new byte[] { 0, 0, (byte) 0xAD, 0 };
        DataFormatMatcher matcher = new DataFormatMatcher(
                new ByteArrayInputStream(inputData), inputData, 0, inputData.length,
                new JsonFactory(), MatchStrength.WEAK_MATCH);

        // Act & Assert
        try {
            matcher.createParserWithMatch();
            fail("Expected a CharConversionException to be thrown");
        } catch (CharConversionException e) {
            assertTrue("Exception message should indicate an unsupported encoding",
                    e.getMessage().startsWith("Unsupported UCS-4 endianness"));
        }
    }
}