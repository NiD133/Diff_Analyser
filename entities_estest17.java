package org.jsoup.nodes;

import org.junit.Test;

/**
 * Tests for the {@link Entities#codepointsForName(String, int[])} method.
 */
public class Entities_ESTestTest17 extends Entities_ESTest_scaffolding {

    /**
     * Verifies that codepointsForName throws an ArrayIndexOutOfBoundsException
     * when the provided output array is too small to hold the resulting codepoints.
     */
    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void codepointsForNameThrowsExceptionWhenOutputArrayIsTooSmall() {
        // Arrange: The entity "amp" has one codepoint, but the output array has a size of zero.
        String entityName = "amp";
        int[] codepointsOutput = new int[0];

        // Act: Attempt to write the codepoint for "amp" into the undersized array.
        // The method is expected to throw an ArrayIndexOutOfBoundsException.
        Entities.codepointsForName(entityName, codepointsOutput);
    }
}