package org.jfree.chart.renderer.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link StandardBarPainter} class, focusing on its compliance
 * with the equals() and hashCode() contract.
 */
class StandardBarPainterTest {

    /**
     * Verifies that two equal StandardBarPainter instances produce the same hash code,
     * as required by the Object.hashCode() contract.
     */
    @Test
    @DisplayName("Hash code should be equal for equal instances")
    void hashCode_shouldBeEqual_forEqualInstances() {
        // Arrange: Create two separate instances of StandardBarPainter.
        // Since StandardBarPainter is stateless, any two instances should be equal.
        StandardBarPainter painter1 = new StandardBarPainter();
        StandardBarPainter painter2 = new StandardBarPainter();

        // Assert: First, confirm the premise that the two instances are equal.
        assertEquals(painter1, painter2, "Two new StandardBarPainter instances should be equal.");

        // Assert: Then, verify that their hash codes are also equal, fulfilling the contract.
        assertEquals(painter1.hashCode(), painter2.hashCode(), "Equal objects must have equal hash codes.");
    }
}