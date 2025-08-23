package org.apache.commons.lang3;

import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

/**
 * Test suite for {@link AppendableJoiner}.
 * This test focuses on verifying the behavior of the joinA method.
 */
public class AppendableJoinerTest {

    /**
     * Tests that calling joinA with a null Appendable throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void joinAShouldThrowNullPointerExceptionWhenAppendableIsNull() throws IOException {
        // Arrange: Create a default joiner and an empty list of elements.
        // The generic type <StringBuilder> matches the original test's intent.
        final AppendableJoiner<StringBuilder> joiner = AppendableJoiner.<StringBuilder>builder().get();
        final Iterable<StringBuilder> elements = Collections.emptyList();

        // Act: Call joinA with a null appendable.
        // This call is expected to throw a NullPointerException.
        joiner.joinA(null, elements);

        // Assert: The test passes if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}