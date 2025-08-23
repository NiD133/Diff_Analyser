package org.jfree.chart.labels;

import org.junit.Test;
import java.text.NumberFormat;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the equals() method in the {@link IntervalCategoryToolTipGenerator} class.
 */
public class IntervalCategoryToolTipGeneratorTest {

    /**
     * Verifies that the equals() method returns false when the generator is
     * compared with an object of an incompatible type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentType() {
        // Arrange: Create an instance of the tooltip generator.
        String labelFormat = "({0}, {1}) = {3} - {4}";
        NumberFormat formatter = NumberFormat.getPercentInstance();
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator(labelFormat, formatter);

        // An object of a different type to compare against.
        Object otherObject = "Not a IntervalCategoryToolTipGenerator";

        // Act: Compare the generator with the other object.
        boolean isEqual = generator.equals(otherObject);

        // Assert: The result should be false.
        assertFalse(isEqual);
    }
}