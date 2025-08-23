package org.jfree.chart.labels;

import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link IntervalCategoryItemLabelGenerator} class.
 */
class IntervalCategoryItemLabelGeneratorTest {

    /**
     * Verifies that IntervalCategoryItemLabelGenerator implements PublicCloneable,
     * which is essential for the chart's cloning mechanism.
     */
    @Test
    @DisplayName("Should implement the PublicCloneable interface")
    void shouldImplementPublicCloneable() {
        // Arrange
        IntervalCategoryItemLabelGenerator generator = new IntervalCategoryItemLabelGenerator();

        // Act & Assert
        assertTrue(generator instanceof PublicCloneable,
                "An instance of IntervalCategoryItemLabelGenerator should also be an instance of PublicCloneable.");
    }
}