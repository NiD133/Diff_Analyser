package org.jfree.chart.labels;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link SymbolicXYItemLabelGenerator} class.
 */
public class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies that the equals() method returns false when comparing an instance
     * to an object of a different, incompatible type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange: Create an instance of the class under test and an object of a different type.
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();
        Object otherObject = new Object();

        // Act: Call the equals method with the other object.
        boolean isEqual = generator.equals(otherObject);

        // Assert: The result should be false, as the types are incompatible.
        assertFalse(isEqual);
    }
}