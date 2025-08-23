package org.apache.commons.io;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.io.ByteOrderMark}.
 */
public class ByteOrderMarkTest {

    /**
     * Tests that a ByteOrderMark instance is equal to itself,
     * which is a fundamental requirement (reflexivity) of the Object.equals() contract.
     */
    @Test
    public void equals_shouldReturnTrue_whenComparingAnInstanceToItself() {
        // Arrange
        final ByteOrderMark bom = ByteOrderMark.UTF_32LE;

        // Act & Assert
        // An object must always be equal to itself.
        assertEquals(bom, bom);
    }
}