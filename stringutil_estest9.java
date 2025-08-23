package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains an improved version of a test for StringUtil.releaseBuilderVoid.
 * The original test was auto-generated and difficult to understand.
 */
public class StringUtil_ESTestTest9 extends StringUtil_ESTest_scaffolding {

    /**
     * Verifies that releaseBuilderVoid() clears a StringBuilder, even when its capacity
     * is too large to be returned to the internal cache pool.
     *
     * @see StringUtil#releaseBuilderVoid(StringBuilder)
     */
    @Test(timeout = 4000)
    public void releaseBuilderVoidClearsBuilderEvenWhenCapacityIsTooLargeForPooling() {
        // Arrange: StringUtil uses an internal pool for StringBuilders with a capacity
        // up to a max size (8192). This test creates a builder with a capacity at that limit
        // to verify behavior for builders that are not returned to the pool.
        final int maxBuilderPoolSize = 8 * 1024;
        StringBuilder largeBuilder = new StringBuilder(maxBuilderPoolSize);
        for (int i = 0; i < maxBuilderPoolSize; i++) {
            largeBuilder.append('a');
        }

        // Sanity check that the builder is set up as expected.
        assertEquals("Pre-condition: StringBuilder should be full.", maxBuilderPoolSize, largeBuilder.length());
        assertTrue("Pre-condition: StringBuilder capacity should be at least the max size.", largeBuilder.capacity() >= maxBuilderPoolSize);

        // Act: Release the builder. This should clear its contents without throwing an exception.
        StringUtil.releaseBuilderVoid(largeBuilder);

        // Assert: The primary observable effect of releaseBuilderVoid is that the builder's
        // length is reset to 0, regardless of whether it was pooled.
        assertEquals("Post-condition: StringBuilder should be empty after release.", 0, largeBuilder.length());
    }
}