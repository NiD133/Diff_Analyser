package com.google.gson;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link FieldNamingPolicy} enum.
 */
public class FieldNamingPolicyTest {

    /**
     * Tests that the values() method returns all expected enum constants.
     *
     * <p>This test acts as a change detector. If a new naming policy is added or an existing one
     * is removed from the {@link FieldNamingPolicy} enum, this test will fail. This prompts the
     * developer to consciously update the test, confirming the change is intentional.
     */
    @Test
    public void values_shouldReturnAllDefinedPolicies() {
        // Arrange: Define the expected number of policies.
        // This number corresponds to the total count of enum constants in the FieldNamingPolicy class.
        final int expectedPolicyCount = 7;

        // Act: Retrieve all the enum constants.
        FieldNamingPolicy[] actualPolicies = FieldNamingPolicy.values();

        // Assert: Verify that the number of retrieved policies matches the expected count.
        assertEquals(
                "The number of FieldNamingPolicy constants has changed. "
                        + "Please update this test if the change was intentional.",
                expectedPolicyCount,
                actualPolicies.length);
    }
}