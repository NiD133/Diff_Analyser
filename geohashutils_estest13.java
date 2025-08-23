package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertThrows;

/**
 * Test suite for GeohashUtils.
 * Note: The original test class name 'GeohashUtils_ESTestTest13' suggests it was
 * auto-generated. A more conventional name would be 'GeohashUtilsTest'.
 */
public class GeohashUtilsTest {

    /**
     * Verifies that the decode method throws a NullPointerException when the
     * provided SpatialContext is null. A valid geohash requires a context
     * to be decoded into a shape.
     */
    @Test
    public void decode_shouldThrowNullPointerException_whenContextIsNull() {
        // Arrange: Use any valid geohash string. Its specific value is not important for this test.
        String anyValidGeohash = "dpz83d"; // A geohash for San Francisco

        // Act & Assert: Verify that calling decode with a null context throws the expected exception.
        // The cast to (SpatialContext) is necessary to resolve method ambiguity for the null argument.
        assertThrows(NullPointerException.class, () -> {
            GeohashUtils.decode(anyValidGeohash, (SpatialContext) null);
        });
    }
}