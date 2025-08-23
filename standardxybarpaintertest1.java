package org.jfree.chart.renderer.xy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link StandardXYBarPainter} class.
 */
class StandardXYBarPainterTest {

    /**
     * Verifies that the equals() method adheres to its contract. Since
     * StandardXYBarPainter is stateless, any two instances should be equal.
     */
    @Test
    void testEquals() {
        // Arrange
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();

        // Assert - Test for symmetry and basic equality
        assertEquals(painter1, painter2, "Two new instances should be equal.");
        assertEquals(painter2, painter1, "Equality should be symmetric.");

        // Assert - Test for reflexivity
        assertEquals(painter1, painter1, "An instance should be equal to itself.");

        // Assert - Test against null and different types
        assertNotEquals(null, painter1, "An instance should not be equal to null.");
        assertNotEquals("Some String", painter1, "An instance should not be equal to an object of a different type.");
    }

    /**
     * Verifies that the hashCode() method is consistent with the equals() method.
     * Equal objects must have the same hash code.
     */
    @Test
    void testHashCode() {
        // Arrange
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();

        // Assert
        assertEquals(painter1.hashCode(), painter2.hashCode(), "Hash codes of equal objects must be the same.");
    }
}