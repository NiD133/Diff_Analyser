package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JavaVersion}.
 */
public class JavaVersionTest {

    /**
     * Tests that {@code parseMajorJavaVersion} correctly handles a string
     * representing a negative number.
     *
     * <p>While Java versions are not expected to be negative, this test verifies
     * the current parsing behavior for such an edge case. The method is expected
     * to simply parse and return the negative integer value.
     */
    @Test
    public void parseMajorJavaVersion_withNegativeString_returnsNegativeInteger() {
        // Arrange
        String negativeVersionString = "-8";
        int expectedVersion = -8;

        // Act
        int actualVersion = JavaVersion.parseMajorJavaVersion(negativeVersionString);

        // Assert
        assertEquals(expectedVersion, actualVersion);
    }
}