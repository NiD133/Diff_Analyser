package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link SymbolicXYItemLabelGenerator} class.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An object must be equal to itself.
     */
    @Test
    public void equals_onSameInstance_shouldReturnTrue() {
        // Arrange
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();

        // Act & Assert
        // According to the Java contract for equals(), an object must equal itself.
        boolean isEqual = generator.equals(generator);
        assertTrue("An instance should be equal to itself.", isEqual);
    }
}