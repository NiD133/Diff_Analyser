package org.jfree.chart.labels;

import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Tests for the {@link SymbolicXYItemLabelGenerator} class, focusing on its implementation contracts.
 */
class SymbolicXYItemLabelGeneratorTest {

    /**
     * Verifies that SymbolicXYItemLabelGenerator correctly implements the PublicCloneable interface,
     * signaling that it supports public cloning.
     */
    @Test
    @DisplayName("Should implement the PublicCloneable interface")
    void shouldImplementPublicCloneable() {
        // Arrange
        SymbolicXYItemLabelGenerator generator = new SymbolicXYItemLabelGenerator();

        // Assert
        // This check confirms that the generator can be safely cast and cloned
        // through the PublicCloneable interface, which is a key part of JFreeChart's API.
        assertInstanceOf(PublicCloneable.class, generator,
                "SymbolicXYItemLabelGenerator must implement PublicCloneable.");
    }
}