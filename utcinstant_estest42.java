package org.threeten.extra.scale;

import org.junit.Test;

/**
 * Unit tests for the UtcInstant class, focusing on its comparison methods.
 */
public class UtcInstantTest {

    /**
     * Tests that isAfter() correctly throws a NullPointerException when its argument is null,
     * as this is a violation of the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void isAfter_whenArgumentIsNull_throwsNullPointerException() {
        // Arrange: Create any valid UtcInstant instance. The specific value is irrelevant
        // for this test, as we are only verifying the null-handling behavior.
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(0, 0);

        // Act & Assert: Call isAfter() with a null argument.
        // The @Test(expected) annotation handles the assertion, failing the test
        // if a NullPointerException is not thrown.
        instant.isAfter(null);
    }
}