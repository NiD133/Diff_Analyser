package org.apache.commons.compress.harmony.unpack200;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

/**
 * Tests for the static utility methods in {@link SegmentConstantPool}.
 */
public class SegmentConstantPoolTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * Verifies that toIndex() throws an IOException when given a negative index,
     * as indices must be non-negative.
     */
    @Test
    public void toIndexShouldThrowIOExceptionForNegativeIndex() throws IOException {
        // Arrange: We expect an IOException with a specific message.
        thrown.expect(IOException.class);
        thrown.expectMessage("Cannot have a negative index.");

        // Act: Call the method with a negative value.
        SegmentConstantPool.toIndex(-1975L);

        // Assert: The ExpectedException rule handles the verification.
    }
}