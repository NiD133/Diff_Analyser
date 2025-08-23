package org.apache.commons.lang3;

import org.junit.Test;

/**
 * Unit tests for {@link AppendableJoiner}.
 */
public class AppendableJoinerTest {

    /**
     * Tests that calling joinA with a null Appendable throws a NullPointerException,
     * as the target for appending cannot be null.
     */
    @Test(expected = NullPointerException.class)
    public void joinAWithNullAppendableShouldThrowNullPointerException() {
        // Arrange: Create a default joiner and an empty array of elements.
        final AppendableJoiner<StringBuilder> joiner = AppendableJoiner.builder().get();
        final StringBuilder[] emptyArray = {};

        // Act: Attempt to join into a null Appendable.
        // This call is expected to throw the NullPointerException.
        joiner.joinA(null, emptyArray);

        // Assert: The test passes if the expected exception is thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}