package org.apache.commons.lang3.stream;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

/**
 * Unit tests for {@link LangCollectors}.
 * This class provides clear, behavior-driven tests for the collectors.
 */
public class LangCollectorsTest {

    /**
     * Tests that {@link LangCollectors#joining()} correctly concatenates various
     * objects in a stream into a single string without any delimiter.
     *
     * This test verifies the primary functionality of the collector with a stream
     * containing multiple elements of different types.
     */
    @Test
    public void testJoiningWithoutArgumentsShouldConcatenateElements() {
        // Arrange: Create a stream with various object types to demonstrate versatility.
        final Stream<Object> inputStream = Stream.of("Apache", 18, "Commons", 3.13, 'L');

        // Act: Collect the stream elements into a single string using the collector.
        final String result = inputStream.collect(LangCollectors.joining());

        // Assert: Verify that the elements are concatenated in order into the expected string.
        assertEquals("The elements should be concatenated in order without a delimiter.", "Apache18Commons3.13L", result);
    }

    /**
     * Tests that {@link LangCollectors#joining()} returns an empty string
     * when collecting an empty stream. This verifies the collector's behavior
     * for an important edge case.
     */
    @Test
    public void testJoiningWithoutArgumentsOnEmptyStreamShouldReturnEmptyString() {
        // Arrange: Create an empty stream.
        final Stream<Object> emptyStream = Stream.empty();

        // Act: Collect the empty stream.
        final String result = emptyStream.collect(LangCollectors.joining());

        // Assert: The result should be an empty string.
        assertEquals("Collecting an empty stream should result in an empty string.", "", result);
    }
}