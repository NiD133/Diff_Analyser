package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;

/**
 * Tests for the UtcInstant class, focusing on its comparison behavior.
 */
public class UtcInstantTest {

    /**
     * Verifies that compareTo() throws a NullPointerException when given a null argument.
     * This is a standard contract of the Comparable interface.
     */
    @Test(expected = NullPointerException.class)
    public void compareTo_withNullArgument_throwsNullPointerException() {
        // Arrange: Create a standard, non-null instance of UtcInstant.
        // The specific value (EPOCH) is arbitrary and chosen for clarity.
        UtcInstant instant = UtcInstant.of(Instant.EPOCH);

        // Act & Assert: Calling compareTo with null should throw the declared exception.
        instant.compareTo(null);
    }
}