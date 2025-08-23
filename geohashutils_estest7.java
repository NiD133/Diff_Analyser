package org.locationtech.spatial4j.io;

import org.junit.Test;

/**
 * Unit tests for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * Verifies that lookupDegreesSizeForHashLen throws an exception for negative input.
     * The method uses the hash length as an array index, so it cannot be negative.
     * This test ensures that such invalid input is handled by throwing an
     * {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void lookupDegreesSizeForHashLen_shouldThrowException_whenHashLengthIsNegative() {
        // Arrange: Define an invalid, negative hash length.
        int invalidNegativeHashLength = -1;

        // Act & Assert: Calling the method with this input should trigger the expected exception.
        GeohashUtils.lookupDegreesSizeForHashLen(invalidNegativeHashLength);
    }
}