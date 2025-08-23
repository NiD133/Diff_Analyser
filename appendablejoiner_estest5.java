package org.apache.commons.lang3;

import org.apache.commons.lang3.function.FailableBiConsumer;
import org.junit.Test;

import java.io.IOException;

/**
 * Unit tests for the static helper methods in {@link AppendableJoiner}.
 */
public class AppendableJoinerTest {

    /**
     * Tests that {@link AppendableJoiner#joinSB(StringBuilder, CharSequence, CharSequence, CharSequence, FailableBiConsumer, Object...)}
     * throws a NullPointerException when the target StringBuilder is null.
     */
    @Test(expected = NullPointerException.class)
    public void joinSBShouldThrowNullPointerExceptionWhenTargetStringBuilderIsNull() {
        // Arrange: Prepare arguments for the method call. The specific values of the
        // prefix, suffix, etc., are not relevant for this test.
        final FailableBiConsumer<Appendable, String, IOException> noOpAppender = FailableBiConsumer.nop();
        final String[] emptyElements = {};

        // Act: Call the method with a null StringBuilder as the target.
        // This is expected to throw a NullPointerException.
        AppendableJoiner.joinSB(null, "", "", "", noOpAppender, emptyElements);

        // Assert: The test succeeds if a NullPointerException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}