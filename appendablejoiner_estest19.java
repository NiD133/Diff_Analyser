package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;

/**
 * Unit tests for {@link AppendableJoiner}.
 */
public class AppendableJoinerTest {

    /**
     * Tests that calling joinA with a null array of elements does not modify the
     * Appendable and returns the same instance.
     */
    @Test
    public void testJoinWithNullArrayShouldNotChangeAppendable() throws IOException {
        // Arrange
        // Create a default joiner with no prefix, suffix, or delimiter.
        final AppendableJoiner<String> joiner = AppendableJoiner.<String>builder().get();
        final StringBuilder stringBuilder = new StringBuilder();
        final String[] elements = null;

        // Act
        // The joinA method appends to the provided Appendable.
        final StringBuilder result = joiner.joinA(stringBuilder, elements);

        // Assert
        // The method should not add any characters for a null array.
        assertEquals("The StringBuilder should remain empty.", "", result.toString());
        
        // The method's contract is to return the same instance it was given.
        assertSame("The method should return the same StringBuilder instance.", stringBuilder, result);
    }
}