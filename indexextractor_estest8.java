package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Contains tests for the {@link IndexExtractor} interface, focusing on edge cases.
 */
public class IndexExtractorTest {

    /**
     * Tests that processing unique indices containing Integer.MAX_VALUE throws an
     * IndexOutOfBoundsException.
     *
     * <p>The default implementation of {@code uniqueIndices()} often uses a {@code java.util.BitSet}
     * to determine uniqueness. When a subsequent operation like {@code asIndexArray()}
     * processes these unique indices, an internal iterator may attempt to find the next
     * index after {@code Integer.MAX_VALUE}.</p>
     *
     * <p>This involves calculating {@code Integer.MAX_VALUE + 1}, which overflows to
     * {@code Integer.MIN_VALUE}. The underlying {@code BitSet} then throws an
     * {@code IndexOutOfBoundsException} when its methods are called with this
     * negative index. This test verifies this specific boundary condition.</p>
     */
    @Test
    public void asIndexArrayFromUniqueIndicesWithMaxValueThrowsException() {
        // Arrange: Create an IndexExtractor containing Integer.MAX_VALUE.
        // This value is the boundary condition that triggers an integer overflow
        // in the default implementation. The array also includes duplicates to ensure
        // the uniqueIndices() method is properly exercised.
        final int[] sourceIndices = {Integer.MAX_VALUE, 0, 0};
        final IndexExtractor sourceExtractor = IndexExtractor.fromIndexArray(sourceIndices);
        final IndexExtractor uniqueExtractor = sourceExtractor.uniqueIndices();

        // Act & Assert: Attempting to convert the unique indices back to an array
        // should fail with an IndexOutOfBoundsException due to the overflow.
        try {
            uniqueExtractor.asIndexArray();
            fail("Expected an IndexOutOfBoundsException to be thrown");
        } catch (final IndexOutOfBoundsException e) {
            // Verify that the exception message confirms the cause: a negative index
            // resulting from the integer overflow (MAX_VALUE + 1 -> MIN_VALUE).
            final String expectedMessagePart = "fromIndex < 0: " + Integer.MIN_VALUE;
            final String actualMessage = e.getMessage();

            assertTrue(
                String.format("Exception message should contain the overflow result. Expected to contain '%s', but was '%s'",
                    expectedMessagePart, actualMessage),
                actualMessage != null && actualMessage.contains(expectedMessagePart)
            );
        }
    }
}