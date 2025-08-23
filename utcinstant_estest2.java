package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Provides clear and understandable tests for the {@link UtcInstant} class.
 */
public class UtcInstantTest {

    /**
     * Tests that the factory method {@code ofModifiedJulianDay} correctly creates an instance
     * with the specified Modified Julian Day value.
     */
    @Test
    public void ofModifiedJulianDay_createsInstanceWithCorrectDay() {
        // Arrange: Define the input values for the UtcInstant.
        long expectedModifiedJulianDay = -2985L;
        long nanoOfDay = 0L;

        // Act: Create a UtcInstant instance using the factory method.
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(expectedModifiedJulianDay, nanoOfDay);

        // Assert: Verify that the getter returns the same value used for creation.
        assertEquals(expectedModifiedJulianDay, instant.getModifiedJulianDay());
    }

    /**
     * Tests the hashCode() method's contract, ensuring that two equal objects
     * produce the same hash code. This provides a meaningful test for the
     * hashCode() method, clarifying the intent of the original test's call.
     */
    @Test
    public void hashCode_isConsistentWithEquals() {
        // Arrange: Create two separate but logically equal UtcInstant objects.
        UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(-2985L, 0L);
        UtcInstant instant2 = UtcInstant.ofModifiedJulianDay(-2985L, 0L);

        // Assert: Verify that the objects are considered equal and, per the contract,
        // have the same hash code.
        assertEquals("Instances created with the same values should be equal.", instant1, instant2);
        assertEquals("Equal objects must have equal hash codes.", instant1.hashCode(), instant2.hashCode());
    }
}