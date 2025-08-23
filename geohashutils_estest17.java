package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * Tests that {@link GeohashUtils#lookupHashLenForWidthHeight(double, double)}
     * returns the maximum possible precision when the requested dimensions (error tolerance)
     * are extremely small. This ensures the method correctly handles edge cases where the
     * required precision exceeds the smallest possible geohash cell size.
     */
    @Test
    public void lookupHashLenForWidthHeight_shouldReturnMaxPrecision_forExtremelySmallDimensions() {
        // GIVEN: Extremely small width and height values, representing a demand for
        // very high precision that is smaller than the smallest geohash cell.
        final double extremelySmallDimension = 1.0e-15;

        // WHEN: We look up the required geohash length for these dimensions.
        int actualHashLength = GeohashUtils.lookupHashLenForWidthHeight(
                extremelySmallDimension,
                extremelySmallDimension
        );

        // THEN: The result should be the maximum supported precision level, as defined
        // by the GeohashUtils class.
        assertEquals(GeohashUtils.MAX_PRECISION, actualHashLength);
    }
}