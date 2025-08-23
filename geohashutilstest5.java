package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link GeohashUtils#lookupDegreesSizeForHashLen(int)}.
 * The expected values are based on the geohash cell dimension table, which can be found at
 * http://en.wikipedia.org/wiki/Geohash.
 */
public class GeohashUtilsTest {

    // A small delta is used for floating-point comparisons to account for precision differences.
    private static final double DELTA = 0.00001;

    @Test
    public void lookupDegreesSizeForHashLen_withOddLength_returnsCorrectDimensions() {
        // GIVEN a geohash length of 3 (an odd number).
        int hashLength = 3;
        // For a geohash of length 3, the cell dimensions are ~1.40625° x 1.40625°.
        double expectedLonWidth = 1.40625;
        double expectedLatHeight = 1.40625;

        // WHEN we look up the cell dimensions for this length.
        double[] dimensions = GeohashUtils.lookupDegreesSizeForHashLen(hashLength);
        double actualLonWidth = dimensions[0];
        double actualLatHeight = dimensions[1];

        // THEN the returned dimensions should match the expected values.
        assertEquals("Longitude width for hash length 3", expectedLonWidth, actualLonWidth, DELTA);
        assertEquals("Latitude height for hash length 3", expectedLatHeight, actualLatHeight, DELTA);
    }

    @Test
    public void lookupDegreesSizeForHashLen_withEvenLength_returnsCorrectDimensions() {
        // GIVEN a geohash length of 4 (an even number).
        int hashLength = 4;
        // For a geohash of length 4, the cell dimensions are ~0.35156° x 0.17578°.
        // For an even length, the longitude dimension is wider than the latitude dimension.
        double expectedLonWidth = 0.3515625;
        double expectedLatHeight = 0.17578125;

        // WHEN we look up the cell dimensions for this length.
        double[] dimensions = GeohashUtils.lookupDegreesSizeForHashLen(hashLength);
        double actualLonWidth = dimensions[0];
        double actualLatHeight = dimensions[1];

        // THEN the returned dimensions should match the expected values.
        assertEquals("Longitude width for hash length 4", expectedLonWidth, actualLonWidth, DELTA);
        assertEquals("Latitude height for hash length 4", expectedLatHeight, actualLatHeight, DELTA);
    }
}