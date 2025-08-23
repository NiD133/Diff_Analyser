package org.jfree.chart.labels;

import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Tests for the {@link IntervalCategoryToolTipGenerator} class.
 */
@DisplayName("IntervalCategoryToolTipGenerator")
class IntervalCategoryToolTipGeneratorTest {

    @Test
    @DisplayName("should implement the PublicCloneable interface")
    void shouldBePubliclyCloneable() {
        // Arrange
        IntervalCategoryToolTipGenerator generator = new IntervalCategoryToolTipGenerator();

        // Act & Assert
        // This check is important to ensure that the generator can be safely cloned
        // by JFreeChart's cloning mechanism.
        assertInstanceOf(PublicCloneable.class, generator,
                "IntervalCategoryToolTipGenerator is expected to be publicly cloneable.");
    }
}