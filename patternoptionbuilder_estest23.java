package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that parsing an empty pattern string results in an empty, but non-null,
     * Options object.
     */
    @Test
    public void parsePattern_WithEmptyString_ShouldReturnEmptyOptions() {
        // Arrange: Define an empty pattern string.
        final String emptyPattern = "";

        // Act: Parse the empty pattern.
        final Options options = PatternOptionBuilder.parsePattern(emptyPattern);

        // Assert: The resulting Options object should be non-null and contain no options.
        assertNotNull("The returned Options object should not be null.", options);
        assertTrue("The returned Options object should be empty.", options.getOptions().isEmpty());
    }
}