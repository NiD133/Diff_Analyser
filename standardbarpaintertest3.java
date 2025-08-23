package org.jfree.chart.renderer.category;

import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests for the {@link StandardBarPainter} class.
 */
@DisplayName("StandardBarPainter")
class StandardBarPainterTest {

    /**
     * Verifies that StandardBarPainter does not support cloning. This is the
     * expected behavior because instances of the class are immutable, making
     * cloning unnecessary.
     */
    @Test
    @DisplayName("should not be cloneable")
    void shouldNotBeCloneable() {
        // Arrange
        StandardBarPainter painter = new StandardBarPainter();

        // Act & Assert
        // The class is immutable, so it should not implement Cloneable or PublicCloneable.
        assertFalse(painter instanceof Cloneable, "StandardBarPainter should not implement Cloneable.");
        assertFalse(painter instanceof PublicCloneable, "StandardBarPainter should not implement PublicCloneable.");
    }
}