package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the {@link SegmentUtils} utility class.
 */
public class SegmentUtilsTest {

    /**
     * Verifies that the SegmentUtils utility class can be instantiated without errors.
     * <p>
     * This test is primarily for achieving full code coverage, as utility classes
     * with private or default constructors are often instantiated this way in tests.
     * </p>
     */
    @Test
    public void shouldInstantiateSuccessfully() {
        // Act: Attempt to create an instance of the utility class.
        final SegmentUtils segmentUtils = new SegmentUtils();

        // Assert: Ensure the instance is not null.
        assertNotNull("The SegmentUtils instance should not be null.", segmentUtils);
    }
}