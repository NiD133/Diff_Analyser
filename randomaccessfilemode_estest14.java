package org.apache.commons.io;

import static org.junit.Assert.assertThrows;

import java.nio.file.OpenOption;
import org.junit.Test;

/**
 * Tests for {@link RandomAccessFileMode}.
 * This snippet focuses on the valueOf(OpenOption...) method.
 */
// The original class name 'RandomAccessFileMode_ESTestTest14' is kept for context,
// but in a real-world scenario, it would be renamed to 'RandomAccessFileModeTest'.
public class RandomAccessFileMode_ESTestTest14 {

    /**
     * Tests that calling valueOf with a null OpenOption array throws a NullPointerException.
     */
    @Test
    public void valueOfOpenOptionWithNullArrayShouldThrowNullPointerException() {
        // The Arrange-Act-Assert pattern is used here.
        // Arrange: No setup is needed as the input is null.

        // Act & Assert: Verify that calling the method with a null array
        // throws the expected exception.
        // The explicit cast to (OpenOption[]) is necessary to resolve method ambiguity.
        assertThrows(NullPointerException.class, () -> RandomAccessFileMode.valueOf((OpenOption[]) null));
    }
}