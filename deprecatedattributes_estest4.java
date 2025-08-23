package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DeprecatedAttributes} class.
 */
public class DeprecatedAttributesTest {

    /**
     * Verifies that the toString() method produces the correct output format
     * when only the 'since' attribute has been set.
     */
    @Test
    public void toStringShouldFormatCorrectlyWithSinceAttribute() {
        // Arrange
        final String sinceVersion = "1.5";
        final DeprecatedAttributes attributes = DeprecatedAttributes.builder()
                .setSince(sinceVersion)
                .get();
        final String expectedOutput = "Deprecated since " + sinceVersion;

        // Act
        final String actualOutput = attributes.toString();

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
}