package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

/**
 * Test suite for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * Verifies that decodeBoundary() throws an exception when the geohash string
     * contains a character that is not part of the valid base-32 alphabet.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void decodeBoundary_withInvalidCharacter_throwsException() {
        // Arrange
        // The geohash "J-" is invalid because the hyphen '-' is not a valid character
        // in the geohash base-32 alphabet.
        String invalidGeohash = "J-";
        SpatialContext context = SpatialContext.GEO;

        // Act
        // This call is expected to throw an ArrayIndexOutOfBoundsException. The internal
        // character lookup attempts to access an array with a negative index derived
        // from the invalid character.
        GeohashUtils.decodeBoundary(invalidGeohash, context);

        // Assert: The test passes if the expected exception is thrown.
        // This is handled declaratively by the @Test(expected=...) annotation.
    }
}