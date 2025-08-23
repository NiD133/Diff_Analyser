package org.jfree.chart.renderer.category;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link StandardBarPainter} class, focusing on its
 * implementation of the equals() method.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that the equals() method returns false when comparing an instance
     * to null. This is a standard requirement of the Object.equals() contract.
     */
    @Test
    public void equals_whenComparedWithNull_shouldReturnFalse() {
        // Arrange
        StandardBarPainter painter = new StandardBarPainter();

        // Act: Compare the painter instance to null.
        boolean result = painter.equals(null);

        // Assert: The result should be false, as per the equals() contract.
        assertFalse(result);
    }
}