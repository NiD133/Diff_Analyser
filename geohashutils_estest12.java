package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

/**
 * Test suite for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * Verifies that calling decodeBoundary with a null SpatialContext
     * results in a NullPointerException.
     *
     * This test ensures the method correctly validates its input parameters,
     * preventing potential runtime errors from propagating through the system.
     */
    @Test(expected = NullPointerException.class)
    public void decodeBoundary_withNullContext_throwsNullPointerException() {
        // Given an empty geohash string
        String geohash = "";

        // When calling decodeBoundary with a null SpatialContext
        // Then a NullPointerException is expected
        GeohashUtils.decodeBoundary(geohash, null);
    }
}