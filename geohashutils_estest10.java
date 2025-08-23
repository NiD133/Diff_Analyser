package org.locationtech.spatial4j.io;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for {@link GeohashUtils}, focusing on edge cases and invalid inputs.
 */
public class GeohashUtilsTest {

    /**
     * The maximum precision for a geohash, as defined in GeohashUtils.
     * Used here to test behavior for values exceeding this limit.
     */
    private static final int MAX_PRECISION = 24;

    /**
     * Verifies that calling encodeLatLon with a precision value greater than the
     * allowed maximum (24) results in an IllegalArgumentException.
     * <p>
     * The original auto-generated test used a very high precision (1970) and relied
     * on a timeout to pass. This is a brittle approach that masks the root cause:
     * the method enters a long-running loop instead of validating its input.
     * This improved test explicitly checks for proper input validation, which is
     * faster, more reliable, and encourages a more robust implementation.
     */
    @Test(expected = IllegalArgumentException.class)
    public void encodeLatLon_shouldThrowException_whenPrecisionExceedsMax() {
        // Arrange
        final double validLatitude = 45.0;
        final double validLongitude = 90.0;
        final int excessivePrecision = MAX_PRECISION + 1;

        // Act
        GeohashUtils.encodeLatLon(validLatitude, validLongitude, excessivePrecision);

        // Assert: The 'expected' attribute of the @Test annotation handles the assertion.
        // Note: If the code under test does not currently throw this exception,
        // this test will fail, correctly indicating that input validation is missing.
    }

    /**
     * Verifies that encodeLatLon can handle longitude values outside the standard
     * [-180, 180] range without throwing an unexpected runtime exception.
     * <p>
     * The original test included calls with out-of-range coordinates but had no
     * assertions, making the intended behavior unclear. This test clarifies that
     * the expectation is for the method to complete successfully, even if the
     * resulting geohash for such coordinates is not standard.
     */
    @Test
    public void encodeLatLon_shouldProcessOutOfRangeCoordinates_withoutCrashing() {
        // Arrange
        final double validLatitude = -1.0;
        final double outOfRangeLongitude = -1258.54;

        // Act
        // The primary goal is to ensure this call completes without an error.
        String geohash = GeohashUtils.encodeLatLon(validLatitude, outOfRangeLongitude);

        // Assert
        // The key assertion is that the method completes and returns a result.
        // The specific value is not tested, as the behavior for out-of-range
        // inputs is not formally defined.
        assertNotNull("Geohash should not be null for an out-of-range longitude", geohash);
    }
}