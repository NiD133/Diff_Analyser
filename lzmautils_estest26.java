package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Verifies that the package-private helper method getCachedLZMAAvailability()
     * runs without error and returns a non-null status. This serves as a basic
     * smoke test for this internal-use method.
     */
    @Test
    public void getCachedLZMAAvailabilityShouldReturnCurrentStatus() {
        // The getCachedLZMAAvailability() method is a simple getter for an internal
        // state field, primarily intended for use in other tests.
        // We are calling it here to ensure it executes without throwing an exception.
        LZMAUtils.CachedAvailability availability = LZMAUtils.getCachedLZMAAvailability();

        // A simple assertion confirms that the method returns a valid enum constant,
        // which should never be null.
        assertNotNull("The cached availability status should not be null", availability);
    }
}