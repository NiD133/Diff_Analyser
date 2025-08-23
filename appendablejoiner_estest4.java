package org.apache.commons.lang3;

import org.apache.commons.lang3.function.FailableBiConsumer;
import org.junit.Test;

import java.io.IOException;

// The original test extended a scaffolding class. This is retained for structural consistency.
public class AppendableJoiner_ESTestTest4 extends AppendableJoiner_ESTest_scaffolding {

    /**
     * Tests that the static helper method {@code joinA} throws a {@link NullPointerException}
     * when provided with a null appender function.
     * <p>
     * The method under test requires at least one element in the input array to attempt
     * to use the appender, which is when the exception is expected.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void testJoinAThrowsNullPointerExceptionForNullAppender() throws IOException {
        // Arrange: Set up the arguments for the joinA method.
        // The key part of this test is the null appender.
        final StringBuilder target = new StringBuilder();
        final String[] elements = {"a"}; // A non-empty array is required to trigger the appender call.
        final FailableBiConsumer<Appendable, Object, IOException> nullAppender = null;

        // Act & Assert: Call the static helper method with the null appender.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        AppendableJoiner.joinA(target, "prefix", "suffix", "delimiter", nullAppender, elements);
    }
}