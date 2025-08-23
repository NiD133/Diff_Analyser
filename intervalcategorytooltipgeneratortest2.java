package org.jfree.chart.labels;

import org.junit.jupiter.api.Test;
import java.text.NumberFormat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Tests for the {@link IntervalCategoryToolTipGenerator} class, focusing on its
 * interaction with its superclass.
 */
class IntervalCategoryToolTipGeneratorTest {

    /**
     * Verifies that an instance of IntervalCategoryToolTipGenerator is not considered
     * equal to an instance of its superclass, StandardCategoryToolTipGenerator.
     * This is a standard check for a well-implemented equals() method.
     */
    @Test
    void equals_shouldReturnFalse_whenComparedWithSuperclassInstance() {
        // Arrange: Create an instance of the class under test.
        var intervalGenerator = new IntervalCategoryToolTipGenerator();

        // Arrange: Create an instance of the superclass with the same configuration
        // as the default IntervalCategoryToolTipGenerator.
        var standardGenerator = new StandardCategoryToolTipGenerator(
                IntervalCategoryToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT_STRING,
                NumberFormat.getInstance()
        );

        // Act & Assert: The equals method should perform a class check, so these
        // two objects, being of different classes, must not be equal.
        assertNotEquals(intervalGenerator, standardGenerator,
                "An IntervalCategoryToolTipGenerator should not be equal to a StandardCategoryToolTipGenerator.");
    }
}