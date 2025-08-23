package org.apache.commons.lang3;

import org.junit.Test;

/**
 * Unit tests for {@link AppendableJoiner}.
 */
public class AppendableJoinerTest {

    /**
     * Tests that calling join() with a null Appendable throws a NullPointerException,
     * as the destination for the joined string is a required parameter.
     */
    @Test(expected = NullPointerException.class)
    public void testJoinWithNullAppendableShouldThrowNullPointerException() {
        // Arrange: Create a default joiner. The generic type is not relevant for this test.
        final AppendableJoiner<Object> joiner = AppendableJoiner.builder().get();

        // Act: Call the join method with a null StringBuilder as the appendable.
        // The exception is expected because the appendable argument cannot be null.
        // We also pass a null array, mirroring the original test case.
        joiner.join(null, (Object[]) null);
    }
}