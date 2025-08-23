package org.threeten.extra.scale;

import org.junit.Test;

/**
 * Unit tests for the TaiInstant class, focusing on the isAfter() method.
 */
public class TaiInstantTest {

    /**
     * Verifies that the isAfter() method correctly throws a NullPointerException
     * when passed a null argument, as per its contract.
     */
    @Test(expected = NullPointerException.class)
    public void isAfter_shouldThrowNullPointerException_whenArgumentIsNull() {
        // Arrange: Create an arbitrary instance of TaiInstant.
        // The specific value of the instant is not relevant to this test.
        TaiInstant anInstant = TaiInstant.ofTaiSeconds(1000L, 0);

        // Act & Assert: Call isAfter() with a null argument.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        anInstant.isAfter(null);
    }
}