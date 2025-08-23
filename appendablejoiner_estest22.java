package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Collections;

/**
 * Tests for {@link AppendableJoiner}.
 */
public class AppendableJoinerTest {

    /**
     * Tests that joining an empty iterable does not modify the Appendable
     * and returns the same instance.
     */
    @Test
    public void testJoinWithEmptyIterableDoesNotChangeAppendable() throws IOException {
        // Arrange
        // Create a joiner with default settings (empty prefix, suffix, delimiter)
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().get();
        final StringBuilder stringBuilder = new StringBuilder();
        final Iterable<Object> emptyCollection = Collections.emptyList();

        // Act
        final StringBuilder result = joiner.joinA(stringBuilder, emptyCollection);

        // Assert
        // The method should return the same StringBuilder instance it was given.
        assertSame("The returned instance should be the same as the input.", stringBuilder, result);
        // The StringBuilder's content should be unchanged.
        assertEquals("The StringBuilder should still be empty.", "", result.toString());
    }
}