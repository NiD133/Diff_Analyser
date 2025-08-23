package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An instance of LabelBlock should always be equal to itself.
     */
    @Test
    public void equals_shouldReturnTrue_whenComparedToItself() {
        // Arrange
        LabelBlock labelBlock = new LabelBlock("");

        // Act & Assert
        // The equals() method should return true when an object is compared with itself.
        assertTrue(labelBlock.equals(labelBlock));
    }
}