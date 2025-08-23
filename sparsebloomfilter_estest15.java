package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

// Note: The original class name "SparseBloomFilter_ESTestTest15" is auto-generated.
// A more conventional name would be "SparseBloomFilterTest".
public class SparseBloomFilter_ESTestTest15 extends SparseBloomFilter_ESTest_scaffolding {

    /**
     * Tests that calling merge() with a null IndexExtractor throws a NullPointerException.
     * This ensures the method correctly handles invalid input.
     */
    @Test
    public void mergeWithNullIndexExtractorShouldThrowNullPointerException() {
        // Arrange: Create a filter instance to test against.
        Shape shape = Shape.fromNM(1014, 1014);
        SparseBloomFilter filter = new SparseBloomFilter(shape);

        // Act & Assert: Verify that a NullPointerException is thrown when the method is called
        // with a null argument. The assertThrows method is a clean and standard way to test
        // for exceptions.
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            filter.merge((IndexExtractor) null);
        });

        // Further Assert: Check for a helpful exception message, which is good API practice.
        // The original test's "verifyException" hinted at this, but this is more explicit.
        assertEquals("indexExtractor", exception.getMessage());
    }
}