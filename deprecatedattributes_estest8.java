package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link DeprecatedAttributes} class.
 */
public class DeprecatedAttributesTest {

    /**
     * Verifies that the 'since' attribute of the default DeprecatedAttributes instance
     * is an empty string, confirming its correct default initialization.
     */
    @Test
    public void getSinceShouldReturnEmptyStringForDefaultInstance() {
        // Arrange: The DeprecatedAttributes.DEFAULT constant is the subject under test.
        final DeprecatedAttributes defaultAttributes = DeprecatedAttributes.DEFAULT;
        final String expectedSince = "";

        // Act: Retrieve the 'since' value from the default instance.
        final String actualSince = defaultAttributes.getSince();

        // Assert: The retrieved value should match the expected empty string.
        assertEquals("The 'since' attribute for the default instance should be an empty string.",
                     expectedSince, actualSince);
    }
}