package org.threeten.extra.scale;

import org.junit.Test;

/**
 * Unit tests for {@link TaiInstant}.
 */
public class TaiInstantTest {

    /**
     * Tests that compareTo() throws a NullPointerException when the other instant is null,
     * as required by the Comparable contract.
     */
    @Test(expected = NullPointerException.class)
    public void compareTo_withNullArgument_shouldThrowNullPointerException() {
        // Arrange: Create any valid TaiInstant instance.
        // The specific value is irrelevant for testing null handling.
        TaiInstant instant = TaiInstant.ofTaiSeconds(100L, 0L);

        // Act: Call compareTo with a null argument.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        instant.compareTo(null);
    }
}