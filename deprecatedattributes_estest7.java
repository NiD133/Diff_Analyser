package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Tests for the {@link DeprecatedAttributes} class.
 */
public class DeprecatedAttributesTest {

    /**
     * Verifies that the default DeprecatedAttributes instance is correctly
     * configured to not be "for removal".
     */
    @Test
    public void isForRemovalShouldReturnFalseForDefaultInstance() {
        // Arrange: The static DEFAULT instance is pre-configured with forRemoval=false.
        DeprecatedAttributes defaultAttributes = DeprecatedAttributes.DEFAULT;

        // Act: Call the method under test.
        boolean forRemoval = defaultAttributes.isForRemoval();

        // Assert: The result should be false as per the default configuration.
        assertFalse("The default instance should not be marked for removal", forRemoval);
    }
}