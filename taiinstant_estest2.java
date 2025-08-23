package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the TaiInstant class, focusing on the equals() and hashCode() contract.
 */
public class TaiInstantTest {

    /**
     * Tests that two TaiInstant objects are not equal if their nanosecond components differ,
     * even when their second components are the same.
     * This also verifies that their hash codes are different, upholding the equals/hashCode contract.
     */
    @Test
    public void equals_returnsFalse_whenNanosDiffer() {
        // Arrange: Create two instants with the same seconds but different nanos.
        TaiInstant instant1 = TaiInstant.ofTaiSeconds(37L, 37L);
        TaiInstant instant2 = TaiInstant.ofTaiSeconds(37L, 3503L);

        // Act & Assert: The two instants should not be considered equal.
        assertNotEquals(instant1, instant2);

        // Assert: Per the Java contract, unequal objects should ideally have different hash codes.
        assertNotEquals(instant1.hashCode(), instant2.hashCode());
    }
}