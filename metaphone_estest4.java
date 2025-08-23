package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    @Test
    public void shouldAllowChangingMaxCodeLength() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        // The default max code length is 4, as specified by the algorithm.
        final int expectedDefaultLength = 4;

        // Assert: Verify the initial state
        assertEquals("The default max code length should be " + expectedDefaultLength,
                expectedDefaultLength, metaphone.getMaxCodeLen());

        // Act: Set a new max code length
        final int newLength = 0;
        metaphone.setMaxCodeLen(newLength);

        // Assert: Verify the value has been updated
        assertEquals("The max code length should be updated to the new value",
                newLength, metaphone.getMaxCodeLen());
    }
}