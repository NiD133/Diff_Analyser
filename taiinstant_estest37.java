package org.threeten.extra.scale;

import org.junit.Test;

/**
 * Test class for {@link TaiInstant}.
 * This class focuses on specific behaviors of the TaiInstant methods.
 */
public class TaiInstantTest {

    /**
     * Tests that the isBefore() method correctly throws a NullPointerException
     * when a null argument is provided, as specified by its contract.
     */
    @Test(expected = NullPointerException.class)
    public void isBefore_whenArgumentIsNull_throwsNullPointerException() {
        // Arrange: Create an arbitrary instance of TaiInstant to test against.
        TaiInstant testInstant = TaiInstant.ofTaiSeconds(0L, 0L);

        // Act: Call the isBefore() method with a null argument.
        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
        testInstant.isBefore(null);
    }
}