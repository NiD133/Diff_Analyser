package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link DeprecatedAttributes} class.
 */
public class DeprecatedAttributesTest {

    /**
     * Verifies that the toString() method returns the correct message
     * when an attribute is marked "for removal".
     */
    @Test
    public void toStringShouldIndicateForRemovalWhenSet() {
        // Arrange: Create a DeprecatedAttributes instance with the 'forRemoval' flag set to true.
        final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
            .setForRemoval(true)
            .get();

        final String expectedMessage = "Deprecated for removal";

        // Act: Get the string representation of the object.
        final String actualMessage = attributes.toString();

        // Assert: Verify that the toString() output matches the expected message.
        assertEquals("The toString() message should reflect the 'for removal' status.",
                     expectedMessage, actualMessage);
    }
}