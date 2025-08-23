package org.apache.commons.lang3;

import org.apache.commons.lang3.function.FailableBiConsumer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test suite for the static helper methods in {@link AppendableJoiner}.
 */
public class AppendableJoinerStaticTest {

    /**
     * Tests that the static {@code joinA} method correctly handles a "no-op" appender.
     * It should not modify the target Appendable and should return the same instance.
     */
    @Test
    public void testJoinAWithNopAppenderReturnsSameInstanceWithoutModification() throws IOException {
        // Arrange
        final StringBuilder target = new StringBuilder("initial-");
        final Object[] elements = new Object[] { "A", "B", null, "C" };

        // A "no-operation" appender that does nothing for each element.
        final FailableBiConsumer<Appendable, ?, IOException> nopAppender = FailableBiConsumer.nop();

        // Act
        // Call joinA with a no-op appender and non-empty delimiters to ensure the appender is the reason for no change.
        final StringBuilder result = AppendableJoiner.joinA(target, "[", "]", ",", nopAppender, elements);

        // Assert
        // 1. The method must return the same instance that was passed in.
        assertSame("The returned StringBuilder should be the same instance as the input.", target, result);

        // 2. The content of the StringBuilder should be unchanged because the appender did nothing.
        assertEquals("The StringBuilder content should not be modified by a no-op appender.", "initial-", target.toString());
    }
}