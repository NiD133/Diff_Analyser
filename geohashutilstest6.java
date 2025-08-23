package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link GeohashUtils}.
 * The test values are based on the table of geohash properties from
 * http://en.wikipedia.org/wiki/Geohash
 */
public class GeohashUtilsTest {

    // The lookupHashLenForWidthHeight method finds the smallest precision level (geohash length)
    // such that the corresponding geohash cell's dimensions are less than or equal to the
    // provided width and height.
    //
    // Geohash cell dimensions at various precision levels:
    // Precision | Lon Width (deg) | Lat Height (deg)
    // -----------------------------------------------
    // 1         | 45.0            | 45.0
    // 2         | 11.25           | 5.625
    // 3         | 1.40625         | 1.40625
    // ...

    @Test
    public void lookupHashLenForWidthHeight_shouldBeDeterminedByLatitude() {
        // For these tests, the longitude width is set to a large value (999) so that
        // the precision is determined solely by the latitude height requirement.

        // A requested height of 46.0 degrees can be satisfied by precision 1 (cell height 45.0),
        // as 45.0 <= 46.0.
        assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 46.0));

        // A requested height of 44.0 degrees cannot be satisfied by precision 1 (cell height 45.0),
        // as 45.0 > 44.0. We need a smaller cell.
        // Precision 2 (cell height 5.625) is the next level, and 5.625 <= 44.0.
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 44.0));

        // A requested height of 5.7 degrees cannot be satisfied by precision 1 (45.0).
        // It can be satisfied by precision 2 (cell height 5.625), as 5.625 <= 5.7.
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(999, 5.7));

        // A requested height of 5.5 degrees cannot be satisfied by precision 2 (cell height 5.625).
        // We need a smaller cell.
        // Precision 3 (cell height 1.40625) is the next level, and 1.40625 <= 5.5.
        assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(999, 5.5));
    }

    @Test
    public void lookupHashLenForWidthHeight_shouldBeDeterminedByLongitude() {
        // For these tests, the latitude height is set to a large value (999) so that
        // the precision is determined solely by the longitude width requirement.

        // A requested width of 46.0 degrees can be satisfied by precision 1 (cell width 45.0),
        // as 45.0 <= 46.0.
        assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(46.0, 999));

        // A requested width of 44.0 degrees cannot be satisfied by precision 1 (cell width 45.0).
        // We need a smaller cell.
        // Precision 2 (cell width 11.25) is the next level, and 11.25 <= 44.0.
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(44.0, 999));

        // A requested width of 11.3 degrees cannot be satisfied by precision 1 (cell width 45.0).
        // It can be satisfied by precision 2 (cell width 11.25), as 11.25 <= 11.3.
        assertEquals(2, GeohashUtils.lookupHashLenForWidthHeight(11.3, 999));

        // A requested width of 11.1 degrees cannot be satisfied by precision 2 (cell width 11.25).
        // We need a smaller cell.
        // Precision 3 (cell width 1.40625) is the next level, and 1.40625 <= 11.1.
        assertEquals(3, GeohashUtils.lookupHashLenForWidthHeight(11.1, 999));
    }

    @Test
    public void lookupHashLenForWidthHeight_shouldReturnMaxPrecision_forTinyDimensions() {
        // When the requested dimensions are smaller than the smallest possible geohash cell,
        // the method should cap at the maximum supported precision.
        assertEquals(GeohashUtils.MAX_PRECISION, GeohashUtils.lookupHashLenForWidthHeight(1e-20, 1e-20));
    }

    @Test
    public void lookupHashLenForWidthHeight_shouldReturnMinPrecision_forLargeDimensions() {
        // When the requested dimensions are very large, the lowest precision (1) will always fit,
        // as its cell (45x45 deg) is smaller than or equal to the requested dimensions.
        assertEquals(1, GeohashUtils.lookupHashLenForWidthHeight(999, 999));
    }
}