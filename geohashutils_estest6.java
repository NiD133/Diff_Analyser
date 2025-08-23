package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.assertNotNull;

/**
 * Test suite for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * Verifies that {@link GeohashUtils#decodeBoundary(String, SpatialContext)} can handle
     * geohash strings that are much longer than the documented {@link GeohashUtils#MAX_PRECISION}.
     * <p>
     * This test ensures the method is robust and does not throw an exception (e.g.,
     * ArrayIndexOutOfBoundsException) when processing inputs with extreme precision.
     */
    @Test
    public void decodeBoundaryWithGeohashExceedingMaxPrecisionShouldSucceed() {
        // Arrange
        // A geohash with a length of 128, which is significantly greater than MAX_PRECISION (24).
        // This tests the robustness of the decoding algorithm with an extreme precision input.
        String veryLongGeohash = "pbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbpbp";
        SpatialContext ctx = SpatialContext.GEO;

        // Act
        // We expect this call to complete without throwing an exception.
        Rectangle result = GeohashUtils.decodeBoundary(veryLongGeohash, ctx);

        // Assert
        // A non-null result confirms that the decoding was successful.
        assertNotNull("The decoded boundary should not be null for a very long geohash", result);
    }
}