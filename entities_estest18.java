package org.jsoup.nodes;

import org.junit.Test;

/**
 * Tests for the {@link Entities} class, focusing on its exception-handling behavior.
 */
public class EntitiesTest {

    /**
     * Verifies that {@link Entities#codepointsForName(String, int[])} throws a
     * {@link NullPointerException} when the provided codepoints array is null.
     * This ensures the method correctly handles invalid input arguments.
     */
    @Test(expected = NullPointerException.class)
    public void codepointsForNameShouldThrowNullPointerExceptionWhenCodepointsArrayIsNull() {
        // Call the method with a valid entity name but a null array for the output.
        Entities.codepointsForName("nbsp", null);
    }
}