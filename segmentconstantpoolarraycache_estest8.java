package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;

/**
 * Test suite for {@link SegmentConstantPoolArrayCache}.
 */
public class SegmentConstantPoolArrayCacheTest {

    /**
     * Tests that a NullPointerException is thrown when arrayIsCached() is called
     * on an instance with a null internal map. This verifies the method's behavior
     * under an unexpected, corrupt internal state.
     */
    @Test(expected = NullPointerException.class)
    public void arrayIsCachedShouldThrowNPEWhenInternalMapIsNull() {
        // Arrange: Create an instance and a dummy array to test with.
        SegmentConstantPoolArrayCache cache = new SegmentConstantPoolArrayCache();
        String[] testArray = new String[4];

        // To simulate a corrupt or unexpected internal state, we directly set the
        // internal 'knownArrays' map to null. This is a white-box testing approach.
        cache.knownArrays = null;

        // Act & Assert: Calling arrayIsCached should now throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        cache.arrayIsCached(testArray);
    }
}