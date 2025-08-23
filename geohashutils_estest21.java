package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * Tests that the calculated latitude/longitude dimensions for a geohash of a
     * given length are correct, especially for high-precision lengths that result
     * in very small degree dimensions.
     */
    @Test
    public void lookupDegreesSizeForHashLen_withHighPrecision_returnsCorrectDimensions() {
        // GIVEN a high-precision geohash length
        int hashLength = 16;

        // EXPECTED values are derived from the geohash algorithm.
        // The initial grid is 180° latitude by 360° longitude.
        // For a length of 16, the grid is subdivided 16 times, which is equivalent
        // to dividing the initial dimensions by 2^40.
        // Latitude height = 180.0 / (2^40)
        // Longitude width = 360.0 / (2^40)
        double expectedLatHeight = 1.6370904631912708E-10;
        double expectedLonWidth = 3.2741809263825417E-10;
        double[] expectedDimensions = {expectedLatHeight, expectedLonWidth};

        // WHEN the dimensions are looked up
        double[] actualDimensions = GeohashUtils.lookupDegreesSizeForHashLen(hashLength);

        // THEN the result should match the expected dimensions
        // A very small delta is used to account for potential floating-point inaccuracies.
        double delta = 1e-25;
        assertArrayEquals(
                "Geohash dimensions for length 16 should be correctly calculated.",
                expectedDimensions,
                actualDimensions,
                delta
        );
    }
}