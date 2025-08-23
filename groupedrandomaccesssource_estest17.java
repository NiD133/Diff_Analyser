package com.itextpdf.text.io;

import org.junit.Test;
import java.io.IOException;

/**
 * Tests for {@link GroupedRandomAccessSource}.
 */
public class GroupedRandomAccessSourceTest {

    /**
     * Verifies that the constructor throws a NullPointerException
     * when the input array of sources is null. This is expected behavior
     * as the constructor requires a valid source array to operate.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionForNullSourceArray() throws IOException {
        // Attempt to create an instance with a null array of sources.
        // This is expected to fail with a NullPointerException.
        new GroupedRandomAccessSource(null);
    }
}