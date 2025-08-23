package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * Tests that {@link GeohashUtils#lookupHashLenForWidthHeight(double, double)}
     * returns the precision level at which a geohash cell's dimensions become
     * strictly smaller than the provided error dimensions.
     *
     * This test case uses a longitude error that is exactly the width of a geohash
     * cell at precision 10. The method is expected to return the next level (11),
     * where the cell width is guaranteed to be smaller.
     */
    @Test
    public void lookupHashLenForWidthHeight_findsPrecisionForCellSmallerThanError() {
        // GIVEN: A required error tolerance for longitude and a non-constraining
        // tolerance for latitude.

        // The longitude error is set to the exact width of a geohash cell at precision 10.
        final double GEOHASH_WIDTH_AT_PRECISION_10 = 1.0728836059570312E-5;

        // The latitude error is set to a very large value, ensuring it does not
        // influence the outcome, as any geohash cell height is smaller.
        final double NON_CONSTRAINING_LATITUDE_ERROR = 1115.07;

        // The expected precision is 11. At this level, the geohash cell width
        // becomes strictly smaller than the specified longitude error.
        //  - Precision 10 width: ~1.07e-5 (not smaller)
        //  - Precision 11 width: ~1.34e-6 (smaller)
        final int expectedPrecision = 11;

        // WHEN: The geohash length is looked up for the given error dimensions.
        int actualPrecision = GeohashUtils.lookupHashLenForWidthHeight(
                GEOHASH_WIDTH_AT_PRECISION_10,
                NON_CONSTRAINING_LATITUDE_ERROR);

        // THEN: The returned precision should be the expected level.
        assertEquals(expectedPrecision, actualPrecision);
    }
}