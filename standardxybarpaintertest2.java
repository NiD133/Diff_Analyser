package org.jfree.chart.renderer.xy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link StandardXYBarPainter} class, focusing on its compliance
 * with the equals() and hashCode() contract.
 */
class StandardXYBarPainterTest {

    /**
     * Verifies that two equal StandardXYBarPainter instances produce the same hash code,
     * which is a fundamental requirement of the Object.hashCode() contract.
     */
    @Test
    @DisplayName("Equal instances should have equal hash codes")
    void hashCode_forEqualInstances_shouldBeEqual() {
        // Arrange: Create two separate but identical StandardXYBarPainter instances.
        // Since the class has no state, two default instances should be equal.
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();

        // Assert: First, confirm the two instances are considered equal.
        // This establishes the precondition for the main test.
        assertEquals(painter1, painter2, "The two painter instances should be equal.");

        // Act & Assert: Then, verify that their hash codes are also equal.
        assertEquals(painter1.hashCode(), painter2.hashCode(), "Equal objects must have equal hash codes.");
    }
}