package org.threeten.extra.scale;

import org.junit.Test;

/**
 * Unit tests for {@link UtcInstant}.
 */
public class UtcInstantTest {

    /**
     * Tests that durationUntil() throws a NullPointerException when the end instant is null.
     * The method contract does not specify behavior for null arguments, so throwing a
     * NullPointerException is the expected behavior.
     */
    @Test(expected = NullPointerException.class)
    public void durationUntil_whenEndInstantIsNull_throwsNullPointerException() {
        // Arrange: Create an arbitrary UtcInstant instance to call the method on.
        // The specific value of this instant does not affect the outcome of a null check.
        UtcInstant startInstant = UtcInstant.ofModifiedJulianDay(0, 0);

        // Act & Assert: Calling durationUntil with a null argument should throw a NullPointerException.
        // The assertion is handled by the `expected` attribute of the @Test annotation.
        startInstant.durationUntil(null);
    }
}