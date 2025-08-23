package org.apache.commons.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link IOCase} enum.
 */
public class IOCaseTest {

    /**
     * Verifies that IOCase.forName() throws an IllegalArgumentException
     * when provided with a name that does not correspond to any defined IOCase constant.
     */
    @Test
    public void forName_whenGivenInvalidName_throwsIllegalArgumentException() {
        // Arrange: Define an input string that is not a valid IOCase name.
        final String invalidName = "InvalidCaseName";

        try {
            // Act: Attempt to retrieve an IOCase with the invalid name.
            IOCase.forName(invalidName);
            fail("Expected an IllegalArgumentException to be thrown for an invalid name.");
        } catch (final IllegalArgumentException e) {
            // Assert: Check that the exception message is correct and informative.
            final String expectedMessage = "Illegal IOCase name: " + invalidName;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}