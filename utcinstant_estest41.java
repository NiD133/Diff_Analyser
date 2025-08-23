package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Instant;

/**
 * Tests for {@link UtcInstant}.
 * This class focuses on specific behaviors not covered by the original test suite.
 */
public class UtcInstantTest {

    /**
     * Tests that isBefore() throws a NullPointerException when the argument is null,
     * as specified by the method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void isBefore_whenArgumentIsNull_throwsNullPointerException() {
        // Arrange: Create any valid UtcInstant instance. Its specific value is not important for this test.
        UtcInstant anyInstant = UtcInstant.of(Instant.EPOCH);

        // Act: Call the method under test with a null argument.
        // Assert: The @Test(expected) annotation handles the exception check.
        anyInstant.isBefore(null);
    }
}