package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for the cloning and equality behavior of the {@link SymbolicXYItemLabelGenerator} class.
 *
 * Note: The original test class name 'SymbolicXYItemLabelGenerator_ESTestTest10' 
 * was simplified to 'SymbolicXYItemLabelGeneratorTest' for clarity.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies that the clone() method produces an object that is equal to the
     * original in state, but is a distinct instance in memory. This is a key
     * part of the Java clone contract.
     */
    @Test
    public void clone_shouldProduceEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange: Create an instance of the label generator.
        SymbolicXYItemLabelGenerator original = new SymbolicXYItemLabelGenerator();

        // Act: Create a clone of the original instance.
        SymbolicXYItemLabelGenerator clone = (SymbolicXYItemLabelGenerator) original.clone();

        // Assert: Verify the clone contract.
        // 1. The clone must not be the same object as the original.
        assertNotSame("The cloned object should be a new instance.", original, clone);
        
        // 2. The clone must be equal to the original.
        assertEquals("The cloned object should be equal to the original.", original, clone);
    }
}