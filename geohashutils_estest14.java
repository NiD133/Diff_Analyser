package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

/**
 * Test suite for {@link GeohashUtils}.
 */
public class GeohashUtilsTest {

    /**
     * Verifies that decode() throws a NullPointerException when the provided
     * SpatialContext is null, as it is a required argument for creating shapes.
     */
    @Test(expected = NullPointerException.class)
    public void decode_withNullContext_shouldThrowNullPointerException() {
        // Given a valid geohash string
        String geohash = "A0D";

        // When the decode method is called with a null SpatialContext
        // Then a NullPointerException is expected
        GeohashUtils.decode(geohash, (SpatialContext) null);
    }
}