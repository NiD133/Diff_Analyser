package org.apache.commons.lang3.stream;

import org.junit.Test;

import java.util.stream.Collector;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link LangCollectors}.
 */
public class LangCollectorsTest {

    /**
     * Tests that {@code LangCollectors.joining(delimiter, prefix, suffix)}
     * successfully creates a collector when the delimiter and prefix are null.
     * This test verifies that the factory method is robust and does not throw
     * a NullPointerException for these specific null inputs.
     */
    @Test
    public void joiningWithNullDelimiterAndPrefixShouldReturnCollector() {
        // Arrange: Define the arguments for the joining collector.
        // The key part of this test is using null for the delimiter and prefix.
        CharSequence delimiter = null;
        CharSequence prefix = null;
        CharSequence suffix = "]";

        // Act: Call the factory method with the specified arguments.
        Collector<Object, ?, String> collector = LangCollectors.joining(delimiter, prefix, suffix);

        // Assert: The factory method should return a non-null Collector instance,
        // confirming it handles null inputs without throwing an exception.
        assertNotNull("The created collector should not be null.", collector);
    }
}