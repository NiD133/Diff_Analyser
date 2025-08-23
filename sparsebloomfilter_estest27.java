package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Unit tests for {@link SparseBloomFilter}.
 */
public class SparseBloomFilterTest {

    /**
     * Tests that the contains(BitMapExtractor) method throws a NullPointerException
     * when the provided extractor is null. This is a critical contract for methods
     * accepting object arguments.
     */
    @Test
    public void testContainsWithNullBitMapExtractorThrowsNullPointerException() {
        // Arrange: Create a filter instance. The shape details are not important for this test.
        Shape shape = Shape.fromKM(10, 100);
        SparseBloomFilter filter = new SparseBloomFilter(shape);

        // Act & Assert: Verify that calling contains() with a null argument throws the expected exception.
        try {
            filter.contains((BitMapExtractor) null);
            fail("Expected a NullPointerException to be thrown.");
        } catch (NullPointerException e) {
            // This assertion is more specific and checks that the exception is thrown
            // for the correct reason, which is a common practice for null checks.
            assertEquals("bitMapExtractor", e.getMessage());
        }
    }
}