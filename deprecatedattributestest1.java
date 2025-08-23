package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * This test suite verifies the functionality of the {@link DeprecatedAttributes} class,
 * focusing on the correct creation of attributes using its builder.
 */
public class DeprecatedAttributesTest {

    /**
     * Tests that the isForRemoval() method returns true when the attribute
     * is configured with setForRemoval(true) via the builder.
     */
    @Test
    public void testIsForRemovalReturnsTrueWhenSetByBuilder() {
        // Arrange: Create a builder and configure the 'forRemoval' attribute.
        DeprecatedAttributes.Builder builder = DeprecatedAttributes.builder();
        builder.setForRemoval(true);

        // Act: Build the DeprecatedAttributes object and get the 'forRemoval' status.
        DeprecatedAttributes attributes = builder.get();
        boolean isForRemoval = attributes.isForRemoval();

        // Assert: Verify that the 'forRemoval' status is true as expected.
        assertTrue("The isForRemoval flag should be true when set by the builder.", isForRemoval);
    }
}