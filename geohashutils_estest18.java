package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Test suite for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * The 32 single-character geohashes that form the base of the encoding system.
     * The source documentation for getSubGeohashes states the result is sorted.
     */
    private static final String[] GEOHASH_BASE_32_STRINGS = {
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "b", "c", "d", "e", "f", "g",
        "h", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
    };

    @Test
    public void getSubGeohashes_withEmptyBase_returnsAllBaseCharacters() {
        // Arrange
        // An empty string represents the root of the geohash hierarchy.
        String emptyBaseGeohash = "";

        // Act
        // Calling getSubGeohashes on the empty base should return all possible
        // first-level (i.e., single-character) geohashes.
        String[] actualSubGeohashes = GeohashUtils.getSubGeohashes(emptyBaseGeohash);

        // Assert
        // The result should be a sorted array containing each of the 32 base geohash characters.
        assertArrayEquals(
            "Expected all 32 base geohash characters in sorted order",
            GEOHASH_BASE_32_STRINGS,
            actualSubGeohashes
        );
    }
}