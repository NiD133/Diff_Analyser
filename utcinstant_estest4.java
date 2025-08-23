package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link UtcInstant} class, focusing on equality.
 */
public class UtcInstantTest {

    /**
     * Tests that the equals() method correctly returns false when comparing two
     * UtcInstant objects that represent different points in time.
     */
    @Test
    public void equals_returnsFalseForDifferentInstants() {
        // Arrange: Create two distinct UtcInstant objects using different
        // Modified Julian Day and nano-of-day values.
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(1285L, 2341L);
        UtcInstant differentInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);

        // Act & Assert: Verify that the two instants are not considered equal.
        assertNotEquals(instant, differentInstant);
    }
}