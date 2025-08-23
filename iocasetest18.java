package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the scratch buffer functionality in {@link IOUtils}.
 *
 * <p>Note: This test class was refactored from the original {@code IOCaseTestTest18}
 * for clarity, as it exclusively tests methods within {@link IOUtils}, not {@link IOCase}.</p>
 */
public class IOUtilsScratchBufferTest {

    /**
     * Asserts that all elements in the given character array are zero ('\0').
     *
     * @param array the array to check.
     */
    private void assertIsZeroed(final char[] array) {
        final char[] expected = new char[array.length];
        // A new char array is initialized with '\0' by default, so no need to fill it.
        assertArrayEquals(expected, array, "The scratch array should be zeroed out.");
    }

    @Test
    @DisplayName("getScratchCharArrayWriteOnly() returns a zeroed array and is independent of getScratchCharArray()")
    void getScratchCharArrayWriteOnly_verifiesBehavior() {
        // This test verifies two key behaviors of IOUtils scratch buffers:
        // 1. getScratchCharArrayWriteOnly() consistently returns a zeroed-out array.
        // 2. The buffer from getScratchCharArrayWriteOnly() is independent of the one
        //    from getScratchCharArray(). Modifying one does not affect the other.

        // --- Part 1: Verify getScratchCharArrayWriteOnly() returns a zeroed array ---

        // Arrange: Get the "write-only" scratch array.
        final char[] writeOnlyArray = IOUtils.getScratchCharArrayWriteOnly();
        assertNotNull(writeOnlyArray, "The write-only scratch array should not be null.");

        // Assert: The initial array must be zeroed.
        assertIsZeroed(writeOnlyArray);

        // --- Part 2: Verify independence from the regular scratch array ---

        // Act: Modify ("dirty") the write-only array to ensure it doesn't affect other buffers.
        Arrays.fill(writeOnlyArray, 'X');

        // Arrange: Get the "regular" scratch array.
        final char[] regularScratchArray = IOUtils.getScratchCharArray();
        assertNotNull(regularScratchArray, "The regular scratch array should not be null.");

        // Assert: The regular scratch array should also be zeroed, proving it's
        // unaffected by the modifications to the write-only array.
        assertIsZeroed(regularScratchArray);
    }
}