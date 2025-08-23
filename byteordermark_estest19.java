package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the equals() method in {@link ByteOrderMark}.
 */
public class ByteOrderMarkEqualsTest {

    @Test
    public void equals_whenComparedWithDifferentObjectType_shouldReturnFalse() {
        // Arrange
        final ByteOrderMark byteOrderMark = new ByteOrderMark("Test-BOM", 0x01, 0x02, 0x03);
        final Object otherObject = new Object();

        // Act
        final boolean isEqual = byteOrderMark.equals(otherObject);

        // Assert
        // A ByteOrderMark instance should never be equal to an object of a different type.
        assertFalse("ByteOrderMark should not be equal to a generic Object", isEqual);

        // An alternative, more semantic assertion:
        assertNotEquals(byteOrderMark, otherObject);
    }

    @Test
    public void equals_whenComparedWithNull_shouldReturnFalse() {
        // Arrange
        final ByteOrderMark byteOrderMark = new ByteOrderMark("Test-BOM", 0x01, 0x02, 0x03);

        // Act
        final boolean isEqual = byteOrderMark.equals(null);

        // Assert
        assertFalse("ByteOrderMark should not be equal to null", isEqual);
    }
}