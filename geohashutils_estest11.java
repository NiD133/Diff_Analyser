package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.Collections;

/**
 * This test class contains tests for the {@link GeohashUtils} class.
 */
public class GeohashUtilsTest {

    /**
     * Tests that {@link GeohashUtils#decodeBoundary(String, SpatialContext)} is robust against
     * extremely long geohash strings.
     * <p>
     * The original auto-generated test suggested that providing a geohash generated with an
     * extremely high precision (far beyond the documented MAX_PRECISION) would cause an
     * undeclared exception (i.e., a RuntimeException or Error). This test verifies that the
     * method fails under such conditions rather than executing indefinitely or causing
     * unpredictable behavior.
     * <p>
     * Note: We expect a {@code RuntimeException} as it's the most common type for "undeclared
     * exceptions". If a more specific exception (like {@code OutOfMemoryError} or
     * {@code ArrayIndexOutOfBoundsException}) is the consistent outcome, the test should be
     * updated to expect that specific type.
     */
    @Test(expected = RuntimeException.class, timeout = 4000)
    public void decodeBoundary_whenGeohashIsExtremelyLong_throwsException() {
        // ARRANGE
        // Define an extremely high precision value, far exceeding the practical limit (MAX_PRECISION = 24).
        // This is intended to test the robustness of the decoding algorithm against invalidly long inputs.
        final int extremePrecision = 1701;
        final SpatialContext ctx = SpatialContext.GEO;

        // The original test used out-of-bounds coordinates, which consistently produces a geohash
        // of all 'z's. We can construct this string directly to make the test faster and
        // more focused on the decodeBoundary method's handling of long strings.
        String extremelyLongGeohash = String.join("", Collections.nCopies(extremePrecision, "z"));

        // ACT
        // The call to decodeBoundary is expected to fail due to the extremely long geohash string.
        GeohashUtils.decodeBoundary(extremelyLongGeohash, ctx);

        // ASSERT
        // The assertion is handled by the @Test(expected = RuntimeException.class) annotation.
        // If this line is reached, the test will fail because no exception was thrown.
    }
}