package org.apache.commons.collections4.bloomfilter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the static factory methods in the {@link IndexExtractor} interface.
 */
public class IndexExtractorTest {

    /**
     * Tests that the {@code fromBitMapExtractor} factory method throws a
     * NullPointerException when its argument is null. This ensures the method
     * correctly handles invalid input.
     */
    @Test
    public void fromBitMapExtractorShouldThrowNullPointerExceptionForNullInput() {
        try {
            // Act: Attempt to create an IndexExtractor from a null BitMapExtractor.
            IndexExtractor.fromBitMapExtractor(null);
            fail("A NullPointerException was expected but not thrown.");
        } catch (final NullPointerException e) {
            // Assert: Verify that the exception message is as expected.
            // This confirms that the null check is specific to the 'bitMapExtractor' parameter,
            // which is a common practice when using java.util.Objects.requireNonNull.
            assertEquals("bitMapExtractor", e.getMessage());
        }
    }
}